import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ApplicationExternal {

	public ApplicationExternal() {
	}
  
	private String appId;

	@NotNull(message="Name cannot be null")
	@Size(min=1,max=200,message="name is invalid")
	private String name;
	
	@NotNull(message="Description cannot be null")
	@Size(min=1,max=999,message="description is invalid")
	private String description;
	
	private String plannedDeploymentDate;
	
	@NotNull(message="numberUsers cannot be null")
	@Min(value=1, message="numberUsers must be at least 1")
	private Integer numberUsers;
	
	@NotNull(message="maxLoginsPerHour cannot be null")
	@Min(value=1, message="maxLoginsPerHour must be at least 1")
	private Integer maxLoginsPerHour;
	
	@Size(min=0,max=200,message="homePage max size is 200")
	private String homePage;
	
	@NotNull(message="technicalOwner cannot be null")
	@Size(min=1,max=200,message="technicalOwner max size is 200")
	private String technicalOwner;
	
	@NotNull(message="businessOwner cannot be null")
	@Size(min=1,max=200,message="businessOwner max size is 200")
	private String businessOwner;
	
	@Size(min=0,max=200,message="supportContact max size is 200")
	private String supportContact;
	
	@Size(min=0,max=200,message="otherContact max size is 200")
	private String otherContact;
	
	@Size(min=0,max=200,message="registeredBy max size is 200")
	private String registeredBy;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlannedDeploymentDate() {
		return plannedDeploymentDate;
	}

	public void setPlannedDeploymentDate(String plannedDeploymentDate) {
		this.plannedDeploymentDate = plannedDeploymentDate;
	}

	public Integer getNumberUsers() {
		return numberUsers;
	}

	public void setNumberUsers(Integer numberUsers) {
		this.numberUsers = numberUsers;
	}

	public Integer getMaxLoginsPerHour() {
		return maxLoginsPerHour;
	}

	public void setMaxLoginsPerHour(Integer maxLoginsPerHour) {
		this.maxLoginsPerHour = maxLoginsPerHour;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getTechnicalOwner() {
		return technicalOwner;
	}

	public void setTechnicalOwner(String technicalOwner) {
		this.technicalOwner = technicalOwner;
	}

	public String getBusinessOwner() {
		return businessOwner;
	}

	public void setBusinessOwner(String businessOwner) {
		this.businessOwner = businessOwner;
	}

	public String getSupportContact() {
		return supportContact;
	}

	public void setSupportContact(String supportContact) {
		this.supportContact = supportContact;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	public String getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}

	@Override
	public String toString() {
		return "ApplicationExternal [appId=" + appId + ", name=" + name + ", description=" + description
				+ ", plannedDeploymentDate=" + plannedDeploymentDate + ", numberUsers=" + numberUsers
				+ ", maxLoginsPerHour=" + maxLoginsPerHour + ", homePage=" + homePage + ", technicalOwner="
				+ technicalOwner + ", businessOwner=" + businessOwner + ", supportContact=" + supportContact
				+ ", otherContact=" + otherContact + ", registeredBy=" + registeredBy + "]";
	}

}
