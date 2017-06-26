import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class LdapAttributesMapper implements AttributesMapper<Object> {
      public Object mapFromAttributes(Attributes attrs) throws NamingException {
         System.out.println("attrs: "+ attrs.toString());
         
         String preferredFirstName = null;
         Attribute preferredFirstNameAttr = (Attribute)attrs.get("preferredFirstName");
         if (preferredFirstNameAttr != null) preferredFirstName = (String)preferredFirstNameAttr.get();
        	 
         String hrFirstName = null;
         Attribute hrFirstNameAttr = (Attribute)attrs.get("hrFirstName");
         if (hrFirstNameAttr != null) hrFirstName = (String)hrFirstNameAttr.get();

         String givenName = null;
         Attribute givenNameAttr = (Attribute)attrs.get("givenName");
         if (givenNameAttr != null) givenName = (String)givenNameAttr.get(0);
         
         String email = null;
         Attribute emailAttr = (Attribute)attrs.get("preferredIdentity");
         if (emailAttr != null) email = (String)emailAttr.get();
         SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
         LdapUser user = new LdapUser(email, email, Arrays.asList(new GrantedAuthority[] {authority}));
         user.setEmail(email);

         String firstName = null;
         if (preferredFirstName != null) firstName = preferredFirstName;
         else if (hrFirstName != null) firstName = hrFirstName;
         else if (givenName != null) firstName = givenName;
         else firstName = "Not found";
         user.setFirstName(firstName);
         
         String preferredLastName = null;
         Attribute preferredLastNameAttr = (Attribute)attrs.get("preferredLastName");
         if (preferredLastNameAttr != null) preferredLastName = (String)preferredLastNameAttr.get();
        	 
         String hrLastName = null;
         Attribute hrLastNameAttr = (Attribute)attrs.get("hrLastName");
         if (hrLastNameAttr != null) hrLastName = (String)hrLastNameAttr.get();

         String surName = null;
         Attribute surNameAttr = (Attribute)attrs.get("sn");
         if (surNameAttr != null) surName = (String)surNameAttr.get(0);
         
         String lastName = null;
         if (preferredLastName != null) lastName = preferredLastName;
         else if (hrLastName != null) lastName = hrLastName;
         else if (surName != null) lastName = surName;
         else lastName = "Not found";
         user.setLastName(lastName);

         String uid = null;
         Attribute uidAttr = (Attribute)attrs.get("uid");
         if (uidAttr != null) uid = (String)uidAttr.get();
         user.setUid(uid);

         String phone = null;
         Attribute phoneAttr = (Attribute)attrs.get("telephoneNumber");
         if (phoneAttr != null) phone = (String)phoneAttr.get();
         user.setPhoneNumber(phone);
        

      }
}
