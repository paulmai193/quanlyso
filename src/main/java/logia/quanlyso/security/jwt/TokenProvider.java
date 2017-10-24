/*
 * 
 */
package logia.quanlyso.security.jwt;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * The Class TokenProvider.
 *
 * @author Dai Mai
 */
@Component
public class TokenProvider {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

	/** The Constant AUTHORITIES_KEY. */
	private static final String AUTHORITIES_KEY = "auth";

	/** The secret key. */
	private String secretKey;

	/** The token validity in milliseconds. */
	private long tokenValidityInMilliseconds;

	/** The token validity in milliseconds for remember me. */
	private long tokenValidityInMillisecondsForRememberMe;

	/** The j hipster properties. */
	private final JHipsterProperties jHipsterProperties;

	/**
	 * Instantiates a new token provider.
	 *
	 * @param jHipsterProperties
	 *            the j hipster properties
	 */
	public TokenProvider(JHipsterProperties jHipsterProperties) {
		this.jHipsterProperties = jHipsterProperties;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		this.secretKey = this.jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();

		this.tokenValidityInMilliseconds = 1000
				* this.jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
		this.tokenValidityInMillisecondsForRememberMe = 1000 * this.jHipsterProperties.getSecurity().getAuthentication()
				.getJwt().getTokenValidityInSecondsForRememberMe();
	}

	/**
	 * Creates the token.
	 *
	 * @param authentication
	 *            the authentication
	 * @param rememberMe
	 *            the remember me
	 * @return the string
	 */
	public String createToken(Authentication authentication, Boolean rememberMe) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity;
		if (rememberMe) {
			validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
		} else {
			validity = new Date(now + this.tokenValidityInMilliseconds);
		}

		return Jwts.builder().setSubject(authentication.getName()).claim(TokenProvider.AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS512, this.secretKey).setExpiration(validity).compact();
	}

	/**
	 * Gets the authentication.
	 *
	 * @param token
	 *            the token
	 * @return the authentication
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(TokenProvider.AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	/**
	 * Validate token.
	 *
	 * @param authToken
	 *            the auth token
	 * @return true, if successful
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			this.log.info("Invalid JWT signature.");
			this.log.trace("Invalid JWT signature trace: {}", e);
		} catch (MalformedJwtException e) {
			this.log.info("Invalid JWT token.");
			this.log.trace("Invalid JWT token trace: {}", e);
		} catch (ExpiredJwtException e) {
			this.log.info("Expired JWT token.");
			this.log.trace("Expired JWT token trace: {}", e);
		} catch (UnsupportedJwtException e) {
			this.log.info("Unsupported JWT token.");
			this.log.trace("Unsupported JWT token trace: {}", e);
		} catch (IllegalArgumentException e) {
			this.log.info("JWT token compact of handler are invalid.");
			this.log.trace("JWT token compact of handler are invalid trace: {}", e);
		}
		return false;
	}
}
