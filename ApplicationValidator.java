import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component(value="applicationValidator")
public class ApplicationValidator implements Validator{

	protected final Logger logger = Logger.getLogger(getClass());
	
	public ApplicationValidator(){
	}

	@Autowired
	LdapConfiguration ldapConfig = new LdapConfiguration();
	
	List<String> validatedUsers = new ArrayList<String>();
	
	/**
     * This Validator validates *just* Applications
     */
    public boolean supports(Class<?> clazz) {
        return ApplicationExternal.class.equals(clazz);
    }

	@Override
	public void validate(Object arg0, Errors arg1) {
		logger.debug("** IN VALIDATE - " + arg0 + ", ERRORS: " + arg1);
		ApplicationExternal application = (ApplicationExternal) arg0;
		Errors errors = (Errors) arg1;
		
		String techOwner = application.getTechnicalOwner();
		String businessOwner = application.getBusinessOwner();
		String otherContact = application.getOtherContact();
		String supportContact = application.getSupportContact();

		if(!StringUtils.hasText(techOwner)){
			logger.debug("Technical owner required." );
			errors.rejectValue("technicalOwner", "Technical owner required.");
			return;
		}
		if(!StringUtils.hasText(businessOwner)){
			logger.debug("Business owner required." );
			errors.rejectValue("businessOwner", "Business owner required.");
			return;
		}
    	
		if(!StringUtils.hasText(application.getName() )){
			logger.debug("Application name required." );
			errors.rejectValue("name", "Application name required.");
			return;
		}
		if(!StringUtils.hasText(application.getDescription() )){
			logger.debug("Application name required." );
			errors.rejectValue("description", "Application description required.");
			return;
		}
		if(application.getPlannedDeploymentDate()!=null && !application.getPlannedDeploymentDate().equals("")){ // validate, this field is not mandatory
			logger.debug("Validating planned production deployment date" );
			boolean isValid = ApplicationUtils.isDateValid(application.getPlannedDeploymentDate());
			logger.debug("Validating planned production deployment date: isvalid "+ isValid );
			if(!isValid){
				logger.debug("Planned Production Deployment Date is invalid." );
				errors.rejectValue("plannedDeploymentDate", "Planned Production Deployment Date is invalid.");
			}
			if(!ApplicationUtils.isFutureDate(application.getPlannedDeploymentDate())){
				logger.debug("Past date is not allowed for Planned Production Deployment." );
				errors.rejectValue("plannedDeploymentDate", "Past date is not allowed for Planned Production Deployment");
				return;
			}
		}
		if(application.getMaxLoginsPerHour() == null){
			logger.debug("Max number logins per hour required." );
			errors.rejectValue("maxLoginsPerHour", "Max number logins per hour required.");
			return;
		}
		if(application.getMaxLoginsPerHour() != null && application.getMaxLoginsPerHour().intValue() <= 0){
			logger.debug("Max number logins must be > 0" );
			errors.rejectValue("maxLoginsPerHour", "Max number logins must be > 0");
			return;
		}
		if(application.getNumberUsers() == null){
			logger.debug("Number users required." );
			errors.rejectValue("numberUsers", "Number users required.");
			return;
		}
		if(application.getNumberUsers() != null && application.getNumberUsers() .intValue() <= 0){
			logger.debug("Number of users must be > 0" );
			errors.rejectValue("numberUsers", "Number users must be > 0");
			return;
		}
	}
	
	public Boolean validUser(String internet){
		try{
			/* If already validated. */
			if (validatedUsers.contains(internet)) return Boolean.TRUE;
			
			logger.debug("Validating user = "+internet);
			LdapAttributesMapper attributeMapper = new LdapAttributesMapper();
			String searchBase=Constants.getString("ldap.client.base");
			String searchFilter=Constants.getString("ldap.client.filter");
			searchFilter = searchFilter.replace((CharSequence)"{0}", (CharSequence)internet);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			controls.setReturningAttributes(new String[] {"preferredIdentity", "uid"});
			List<Object> userList = (List<Object>) ldapConfig.ldapTemplate().search(searchBase, searchFilter, controls, attributeMapper);
			if (userList != null && userList.size() > 0) {
				logger.info("Found valid user = "+internet);
				validatedUsers.add(internet);
				return true;
			}
			else{
				logger.info("Invalid user = "+internet);
				return false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error(ex.getClass().getName());
			return false;
		}
	}
	
	 public Boolean validOtherContacts(String otherContact){
		 try{
			 StringTokenizer lineTok = new StringTokenizer(otherContact,",");
			 while(lineTok.hasMoreTokens()){
				 String email = lineTok.nextToken().trim();
				 if(!validUser(email))return false;
			 }
			 return true;
		 }catch(Exception ex){
			 return false;
		 }
	 }
	

}
