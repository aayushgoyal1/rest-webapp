package com.sit.sso.web.application.domain;

import java.util.ArrayList;
import java.util.List;

import com.sit.sso.common.application.domain.Application;

/**
 * Conversion factory for converting Application to ApplicationExternal and vice versa.
 *
 */
public class ApplicationDomainConversionFactory {

		public static Application fromApplicationExternalToApplication (ApplicationExternal appExternal) {
			Application app = new Application();
			app.setAppId(appExternal.getAppId());
			app.setName(appExternal.getName());
			app.setDescription(appExternal.getDescription());
			app.setPlannedDeploymentDate(appExternal.getPlannedDeploymentDate());
			app.setNumberUsers(appExternal.getNumberUsers());
			app.setMaxLoginsPerHour(appExternal.getMaxLoginsPerHour());
			app.setHomePage(appExternal.getHomePage());
			app.setTechnicalOwner(appExternal.getTechnicalOwner());
			app.setBusinessOwner(appExternal.getBusinessOwner());
			app.setSupportContact(appExternal.getSupportContact());
			app.setOtherContact(appExternal.getOtherContact());
			app.setRegisteredBy(appExternal.getRegisteredBy());
			return app;
	    }
	
		public static Application updateInternalInstanceWithExternalData (Application appInternal, ApplicationExternal appExternal) {
			appInternal.setName(appExternal.getName());
			appInternal.setDescription(appExternal.getDescription());
			appInternal.setPlannedDeploymentDate(appExternal.getPlannedDeploymentDate());
			appInternal.setNumberUsers(appExternal.getNumberUsers());
			appInternal.setMaxLoginsPerHour(appExternal.getMaxLoginsPerHour());
			appInternal.setHomePage(appExternal.getHomePage());
			appInternal.setTechnicalOwner(appExternal.getTechnicalOwner());
			appInternal.setBusinessOwner(appExternal.getBusinessOwner());
			appInternal.setSupportContact(appExternal.getSupportContact());
			appInternal.setOtherContact(appExternal.getOtherContact());
			return appInternal;
	    }
		public static List<Application> fromApplicationExternalListToApplicationList(List<ApplicationExternal> appExternalList) {
			List<Application> appList = new ArrayList<Application>();
			for (int i=0; i<appExternalList.size(); i++) {
				ApplicationExternal currentExternalApp = appExternalList.get(i);
				Application currentApp = fromApplicationExternalToApplication(currentExternalApp);
				appList.add(currentApp);
			}
			return appList;
		}
		
		public static ApplicationExternal fromApplicationToApplicationExternal (Application app) {
			ApplicationExternal appExternal = new ApplicationExternal();
			appExternal.setAppId(app.getAppId());
			appExternal.setName(app.getName());
			appExternal.setDescription(app.getDescription());
			appExternal.setPlannedDeploymentDate(app.getPlannedDeploymentDate());
			appExternal.setNumberUsers(app.getNumberUsers());
			appExternal.setMaxLoginsPerHour(app.getMaxLoginsPerHour());
			appExternal.setHomePage(app.getHomePage());
			appExternal.setTechnicalOwner(app.getTechnicalOwner());
			appExternal.setBusinessOwner(app.getBusinessOwner());
			appExternal.setSupportContact(app.getSupportContact());
			appExternal.setOtherContact(app.getOtherContact());
			appExternal.setRegisteredBy(app.getRegisteredBy());
			return appExternal;
	    }
		
		public static List<ApplicationExternal> fromApplicationListToApplicationExternalList(List<Application> appList) {
			List<ApplicationExternal> appExternalList = new ArrayList<ApplicationExternal>();
			for (int i=0; i<appList.size(); i++) {
				Application currentApp = appList.get(i);
				ApplicationExternal currentExternalApp = fromApplicationToApplicationExternal(currentApp);
				appExternalList.add(currentExternalApp);
			}
			return appExternalList;
			
		}
		
}
