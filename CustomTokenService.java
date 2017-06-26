import java.util.Arrays;
import java.util.HashSet;

import org.mitre.oauth2.introspectingfilter.IntrospectingTokenService;
import org.mitre.oauth2.introspectingfilter.service.impl.StaticIntrospectionConfigurationService;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.oauth2.model.ClientDetailsEntity.AppType;
import org.mitre.oauth2.model.ClientDetailsEntity.AuthMethod;
import org.mitre.oauth2.model.ClientDetailsEntity.SubjectType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/*************************************************************************************************
 *   
 *   <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:security="http://www.springframework.org/schema/security"
   xmlns:oauth2="http://www.springframework.org/schema/security/oauth2"
   xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
    http://www.springframework.org/schema/security
    http://www.springframework.org/schema/security/spring-security-4.2.xsd
    http://www.springframework.org/schema/security/oauth2
    http://www.springframework.org/schema/security/spring-security-oauth2.xsd">
<security:authentication-manager>
	<security:authentication-provider ref='RestAuthenticationProvider'/>
</security:authentication-manager>
<oauth2:expression-handler id="oauthExpressionHandler" />
<oauth2:web-expression-handler id="oauthWebExpressionHandler" />
<security:global-method-security pre-post-annotations="enabled" proxy-target-class="true">
    <security:expression-handler ref="oauthExpressionHandler" />
</security:global-method-security>
<oauth2:resource-server id="oidcResourceServer" resource-id="oidcResourceServerId" token-services-ref="tokenServices" />
<security:http use-expressions="true" pattern="/**" create-session="never"
    entry-point-ref="authenticationEntryPoint">
    <security:anonymous enabled="false" />
	<security:intercept-url pattern="/**" requires-channel="https" access="hasRole('ROLE_USER')" />
    <security:custom-filter ref="oidcResourceServer" before="PRE_AUTH_FILTER" />
    <security:http-basic entry-point-ref="authenticationEntryPoint" />
    <security:access-denied-handler ref="oauthAccessDeniedHandler" />
    <security:expression-handler ref="oauthWebExpressionHandler" />
    <security:csrf disabled="true" />
</security:http>
</beans>
 *   
 *************************************************************************************************/

//@Component
public class CustomTokenService implements ResourceServerTokenServices{

	private String clientId=Constants.getString("oidc.client.client-id");
	private String clientSecret=Constants.getString("oidc.client.client-secret");
	private String clientName=Constants.getString("oidc.client.client-name");
	private String introspectUrl=Constants.getString("oidc.client.introspect-url");
	
	IntrospectingTokenService its = new IntrospectingTokenService();

	public CustomTokenService () {
		// limited client configuration for introspect only
		RegisteredClient client = new RegisteredClient();
		client.setClientId(clientId);
		client.setClientSecret(clientSecret);
		client.setAllowIntrospection(Boolean.TRUE);
		client.setApplicationType(AppType.WEB);
		client.setClientName(clientName);
		client.setScope(new HashSet<String>(Arrays.asList("openid")));
		client.setSubjectType(SubjectType.PUBLIC);
		client.setTokenEndpointAuthMethod(AuthMethod.SECRET_POST);

		StaticIntrospectionConfigurationService introspectConfigurationService = new StaticIntrospectionConfigurationService();
		introspectConfigurationService.setClientConfiguration(client);
		introspectConfigurationService.setIntrospectionUrl(introspectUrl);
		
		its.setIntrospectionConfigurationService(introspectConfigurationService);
	}
	
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		// TODO Auto-generated method stub
		return its.loadAuthentication(accessToken);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		// TODO Auto-generated method stub
		return its.readAccessToken(accessToken);
	}

}
