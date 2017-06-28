import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LdapUser extends User {

public LdapUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
}

private String email="anonymous";
private String uid;
private String firstName;
private String lastName;
private String phoneNumber;

public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getFirstName() {
	return firstName;
}
public void setFirstName(String firstName) {
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
public void setLastName(String lastName) {
	this.lastName = lastName;
}
public String getPhoneNumber() {
	return phoneNumber;
}
public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}


}
