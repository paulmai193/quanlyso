package logia.quanlyso.security;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test class for the SecurityUtils utility class.
 *
 * @see SecurityUtils
 */
public class SecurityUtilsUnitTest {

	/**
	 * Testget current user login.
	 */
	@Test
	public void testgetCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext
		.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
		SecurityContextHolder.setContext(securityContext);
		String login = SecurityUtils.getCurrentUserLogin();
		Assertions.assertThat(login).isEqualTo("admin");
	}

	/**
	 * Testget current user JWT.
	 */
	@Test
	public void testgetCurrentUserJWT() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext
		.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "token"));
		SecurityContextHolder.setContext(securityContext);
		String jwt = SecurityUtils.getCurrentUserJWT();
		Assertions.assertThat(jwt).isEqualTo("token");
	}

	/**
	 * Test is authenticated.
	 */
	@Test
	public void testIsAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext
		.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
		SecurityContextHolder.setContext(securityContext);
		boolean isAuthenticated = SecurityUtils.isAuthenticated();
		Assertions.assertThat(isAuthenticated).isTrue();
	}

	/**
	 * Test anonymous is not authenticated.
	 */
	@Test
	public void testAnonymousIsNotAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
		securityContext.setAuthentication(
				new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
		SecurityContextHolder.setContext(securityContext);
		boolean isAuthenticated = SecurityUtils.isAuthenticated();
		Assertions.assertThat(isAuthenticated).isFalse();
	}
}
