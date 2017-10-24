/*
 * 
 */
package logia.quanlyso.config;

import io.github.jhipster.security.Http401UnauthorizedEntryPoint;
import logia.quanlyso.security.AuthoritiesConstants;
import logia.quanlyso.security.jwt.JWTConfigurer;
import logia.quanlyso.security.jwt.TokenProvider;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

/**
 * The Class SecurityConfiguration.
 *
 * @author Dai Mai
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/** The authentication manager builder. */
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	/** The user details service. */
	private final UserDetailsService userDetailsService;

	/** The token provider. */
	private final TokenProvider tokenProvider;

	/** The cors filter. */
	private final CorsFilter corsFilter;

	/**
	 * Instantiates a new security configuration.
	 *
	 * @param authenticationManagerBuilder
	 *            the authentication manager builder
	 * @param userDetailsService
	 *            the user details service
	 * @param tokenProvider
	 *            the token provider
	 * @param corsFilter
	 *            the cors filter
	 */
	public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
			UserDetailsService userDetailsService, TokenProvider tokenProvider, CorsFilter corsFilter) {

		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;
		this.tokenProvider = tokenProvider;
		this.corsFilter = corsFilter;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		try {
			this.authenticationManagerBuilder.userDetailsService(this.userDetailsService)
					.passwordEncoder(this.passwordEncoder());
		} catch (Exception e) {
			throw new BeanInitializationException("Security configuration failed", e);
		}
	}

	/**
	 * Http 401 unauthorized entry point.
	 *
	 * @return the http 401 unauthorized entry point
	 */
	@Bean
	public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
		return new Http401UnauthorizedEntryPoint();
	}

	/**
	 * Password encoder.
	 *
	 * @return the password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter
	 * #configure(org.springframework.security.config.annotation.web.builders.
	 * WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/app/**/*.{js,html}")
				.antMatchers("/bower_components/**").antMatchers("/i18n/**").antMatchers("/content/**")
				.antMatchers("/swagger-ui/index.html").antMatchers("/test/**");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter
	 * #configure(org.springframework.security.config.annotation.web.builders.
	 * HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(this.corsFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
				.authenticationEntryPoint(this.http401UnauthorizedEntryPoint()).and().csrf().disable().headers()
				.frameOptions().disable().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/register").permitAll().antMatchers("/api/activate").permitAll()
				.antMatchers("/api/authenticate").permitAll().antMatchers("/api/account/reset_password/init")
				.permitAll().antMatchers("/api/account/reset_password/finish").permitAll()
				.antMatchers("/api/profile-info").permitAll().antMatchers("/api/**").authenticated()
				.antMatchers("/management/health").permitAll().antMatchers("/management/**")
				.hasAuthority(AuthoritiesConstants.ADMIN).antMatchers("/v2/api-docs/**").permitAll()
				.antMatchers("/swagger-resources/configuration/ui").permitAll().antMatchers("/swagger-ui/index.html")
				.hasAuthority(AuthoritiesConstants.ADMIN).and().apply(this.securityConfigurerAdapter());

	}

	/**
	 * Security configurer adapter.
	 *
	 * @return the JWT configurer
	 */
	private JWTConfigurer securityConfigurerAdapter() {
		return new JWTConfigurer(this.tokenProvider);
	}

	/**
	 * Security evaluation context extension.
	 *
	 * @return the security evaluation context extension
	 */
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
