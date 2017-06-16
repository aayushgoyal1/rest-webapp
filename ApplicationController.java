import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This RestController handles the Application methods.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Qualifier("appService")
	private ApplicationService appService;
	
	@Autowired
	public void setApplicationService (ApplicationService appService) {
		this.appService = appService;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(new ApplicationValidator());
	}

	/**
	 * Returns a specific application based on the specified ID.  The authenticated user must have 
	 * authority to see the requested application.  If the appId is not found, BAD_REQUEST is returned.
	 * If the application is successfully returned, OK is returned.  If the user does not have authority
	 * to see the application, UNAUTHORIZED is returned.  If a problem occurs, SERVICE_UNAVAILABLE is 
	 * returned.
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/applications/{id}" , method=RequestMethod.GET, produces="application/json; charset=UTF-8")
	public ResponseEntity<?> getApplicationById(@PathVariable String id) throws IOException{
		Errors errors = new MapBindingResult(new HashMap<String,String>(), id);
		try {
	        // Check security
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String message = "No security context.  Not authorized to access application data.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
			id= URLDecoder.decode(id,"UTF-8");
			logger.info("GetApplicationById - "+id);
			
			Application existingApp = (Application) appService.getApplicationDetailsById(id);
			if (existingApp==null) {
				String message = "Application was not found for specified appId: " + id;
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
						}
		} catch(Exception ex) {
			ex.printStackTrace();
			String message = "An exception occurred processing the request: " + ex.getClass().getName();
			errors.rejectValue("appId", message);  logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ValidationErrorBuilder.fromBindingErrors(errors));
		}
	}
	
	/**
	 * Returns all applications which the authenticated users has authority to access.   The
	 * application list is returned along with OK status.   If a problem occurs, SERVICE_UNAVAILABLE
	 * is returned.
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/applications" , method=RequestMethod.GET, produces="application/json; charset=UTF-8")
	public ResponseEntity<?> getApplicationList() throws IOException {
		Errors errors = new MapBindingResult(new HashMap<String,String>(), "appId");

		try{
	        // Check security
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String message = "No security context.  Not authorized to access application data.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
			logger.info("Get Application List- \n");
			List<ApplicationExternal> appListExternal = ApplicationDomainConversionFactory.
					fromApplicationListToApplicationExternalList(appList);
			return new ResponseEntity<List<ApplicationExternal>>( appListExternal,HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			String message = "An exception occurred processing the request: " + ex.getClass().getName();
			errors.rejectValue("appId", message);  logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ValidationErrorBuilder.fromBindingErrors(errors));
		}
	}
	
	/**
	 * Creates an application.  If the application is created successfully, CREATED is returned.
	 * If a problem occurs, SERVICE_UNAVAILABLE is returned.
	 * 
	 * @param application
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/applications" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json; charset=UTF-8")
	public ResponseEntity<?> registerApplication(@Valid @RequestBody ApplicationExternal application, Errors errors, UriComponentsBuilder ucBuilder) throws IOException{
		try{
	        // Check security
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String message = "No security context.  Not authorized to access application data.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}			
			//If error, just return a 400 bad request, along with the error message
	        if (errors.hasErrors()) {
	        	System.out.println("Validation Errors: " + errors.getAllErrors());
	            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
	        }	
	        
			application.setAppId(UUID.randomUUID().toString());
			logger.info("Register Application - \n"+application.toString());
			Application app = ApplicationDomainConversionFactory.fromApplicationExternalToApplication(application);
			boolean i = appService.registerApplication(app);
			if (i) {
				logger.debug ("The Application was successfully created.");
		        HttpHeaders headers = new HttpHeaders();
		        headers.setLocation(ucBuilder.path("/applications/{appId}").buildAndExpand(app.getAppId()).toUri());
				return new ResponseEntity<ApplicationExternal>(application, headers, HttpStatus.CREATED);
			} else {
				String message = "The application was not successfully created.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String message = "An exception occurred processing the request: " + ex.getClass().getName();
			errors.rejectValue("appId", message);  logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ValidationErrorBuilder.fromBindingErrors(errors));
		}
	}
	
	/**
	 * Updates an existing application.  If the user does not have authority to update the application,
	 * UNAUTHORIZED is returned.  If the application ID does not exist, NOT_FOUND is returned.  If
	 * the application is updated, OK is returned with the application in the response.  If a problem
	 * occurs, SERVICE_UNAVAILABLE is returned.
	 *   
	 * @param application
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/applications/{appId}" , method=RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE, produces="application/json; charset=UTF-8")
	public ResponseEntity<?> updateApplication(@PathVariable("appId") String appId, @Valid @RequestBody ApplicationExternal application, Errors errors) throws IOException{
		try{
	        // Check security
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String message = "No security context.  Not authorized to access application data.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
			//If error, just return a 400 bad request, along with the error message
	        if (errors.hasErrors()) {
	        	System.out.println("Errors: " + errors.getAllErrors());
	            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
	        }			

			// Check if appId already exists and whether the current user is authorized to update it.
			Application existingApp = (Application) appService.getApplicationDetailsById(appId);
			if (existingApp==null) {
				String message = "The specified application ID was not found.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ValidationErrorBuilder.fromBindingErrors(errors));
			} 
							
				boolean i = appService.update(tobeUpdated);
				if (i) {
					ApplicationExternal existingExternal = ApplicationDomainConversionFactory.
							fromApplicationToApplicationExternal(tobeUpdated);
					return new ResponseEntity<ApplicationExternal>(existingExternal, HttpStatus.OK);
				} else {
					String message = "The application was not successfully updated.";
					errors.rejectValue("appId", message);  logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String message = "An exception occurred processing the request: " + ex.getClass().getName();
			errors.rejectValue("appId", message);  logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ValidationErrorBuilder.fromBindingErrors(errors));
		}
	}
	
	
	/**
	 * Deletes an existing application.  If the user does not have authority, UNAUTHORIZED is returned.
	 * If the application is successfully deleted, NO_CONTENT is returned.  If a problem occurs, 
	 * SERVICE_UNAVAILABLE is returned.
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/applications/{appId}" , method=RequestMethod.DELETE)
	public ResponseEntity<?> delete( @PathVariable String appId) throws IOException {
		Errors errors = new MapBindingResult(new HashMap<String,String>(), appId);
		try{
	        // Check security
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String message = "No security context.  Not authorized to access application data.";
				errors.rejectValue("appId", message);  logger.error(message);
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}			
			Application app = (Application)appService.getApplicationById(appId);			
				if (i) {
					return new ResponseEntity<ApplicationExternal>( HttpStatus.NO_CONTENT );
				} else {
					String message = "The application was not successfully deleted.";
					errors.rejectValue("appId", message);  logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ValidationErrorBuilder.fromBindingErrors(errors));
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ValidationErrorBuilder.fromBindingErrors(errors));
			}
		} catch (Exception e) {
			e.printStackTrace();
			String message = "An exception occurred processing the request: " + e.getClass().getName();
			errors.rejectValue("appId", message);  logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ValidationErrorBuilder.fromBindingErrors(errors));
		}
	}	

}
