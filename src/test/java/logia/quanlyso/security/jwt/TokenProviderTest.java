/*
 * 
 */
package logia.quanlyso.security.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import logia.quanlyso.security.AuthoritiesConstants;

/**
 * The Class TokenProviderTest.
 *
 * @author Dai Mai
 */
public class TokenProviderTest {

	/** The secret key. */
	private final String secretKey = "e5c9ee274ae87bc031adda32e27fa98b9290da83";

	/** The one minute. */
	private final long ONE_MINUTE = 60000;

	/** The j hipster properties. */
	private JHipsterProperties jHipsterProperties;

	/** The token provider. */
	private TokenProvider tokenProvider;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		this.jHipsterProperties = Mockito.mock(JHipsterProperties.class);
		this.tokenProvider = new TokenProvider(this.jHipsterProperties);
		ReflectionTestUtils.setField(this.tokenProvider, "secretKey", this.secretKey);
		ReflectionTestUtils.setField(this.tokenProvider, "tokenValidityInMilliseconds", this.ONE_MINUTE);
	}

	/**
	 * Test return false when JW thas invalid signature.
	 */
	@Test
	public void testReturnFalseWhenJWThasInvalidSignature() {
		boolean isTokenValid = this.tokenProvider.validateToken(this.createTokenWithDifferentSignature());

		Assertions.assertThat(isTokenValid).isEqualTo(false);
	}

	/**
	 * Test return false when JW tis malformed.
	 */
	@Test
	public void testReturnFalseWhenJWTisMalformed() {
		Authentication authentication = this.createAuthentication();
		String token = this.tokenProvider.createToken(authentication, false);
		String invalidToken = token.substring(1);
		boolean isTokenValid = this.tokenProvider.validateToken(invalidToken);

		Assertions.assertThat(isTokenValid).isEqualTo(false);
	}

	/**
	 * Test return false when JW tis expired.
	 */
	@Test
	public void testReturnFalseWhenJWTisExpired() {
		ReflectionTestUtils.setField(this.tokenProvider, "tokenValidityInMilliseconds", -this.ONE_MINUTE);

		Authentication authentication = this.createAuthentication();
		String token = this.tokenProvider.createToken(authentication, false);

		boolean isTokenValid = this.tokenProvider.validateToken(token);

		Assertions.assertThat(isTokenValid).isEqualTo(false);
	}

	/**
	 * Test return false when JW tis unsupported.
	 */
	@Test
	public void testReturnFalseWhenJWTisUnsupported() {
		String unsupportedToken = this.createUnsupportedToken();

		boolean isTokenValid = this.tokenProvider.validateToken(unsupportedToken);

		Assertions.assertThat(isTokenValid).isEqualTo(false);
	}

	/**
	 * Test return false when JW tis invalid.
	 */
	@Test
	public void testReturnFalseWhenJWTisInvalid() {
		boolean isTokenValid = this.tokenProvider.validateToken("");

		Assertions.assertThat(isTokenValid).isEqualTo(false);
	}

	/**
	 * Creates the authentication.
	 *
	 * @return the authentication
	 */
	private Authentication createAuthentication() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
		return new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities);
	}

	/**
	 * Creates the unsupported token.
	 *
	 * @return the string
	 */
	private String createUnsupportedToken() {
		return Jwts.builder().setPayload("payload").signWith(SignatureAlgorithm.HS512, this.secretKey).compact();
	}

	/**
	 * Creates the token with different signature.
	 *
	 * @return the string
	 */
	private String createTokenWithDifferentSignature() {
		return Jwts.builder().setSubject("anonymous")
				.signWith(SignatureAlgorithm.HS512, "e5c9ee274ae87bc031adda32e27fa98b9290da90")
				.setExpiration(new Date(new Date().getTime() + this.ONE_MINUTE)).compact();
	}
}
