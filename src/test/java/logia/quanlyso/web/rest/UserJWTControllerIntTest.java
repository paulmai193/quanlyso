package logia.quanlyso.web.rest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.security.jwt.TokenProvider;
import logia.quanlyso.service.UserService;
import logia.quanlyso.web.rest.vm.LoginVM;

/**
 * Test class for the UserJWTController REST controller.
 *
 * @see UserJWTController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class UserJWTControllerIntTest {

	/** The token provider. */
	@Autowired
	private TokenProvider			tokenProvider;

	/** The authentication manager. */
	@Autowired
	private AuthenticationManager	authenticationManager;

	/** The user repository. */
	@Autowired
	private UserRepository			userRepository;

	/** The password encoder. */
	@Autowired
	private PasswordEncoder			passwordEncoder;

	/** The user service. */
	@Autowired
	private UserService				userService;

	/** The mock mvc. */
	private MockMvc					mockMvc;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		UserJWTController userJWTController = new UserJWTController(this.tokenProvider,
				this.authenticationManager, this.userService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userJWTController).build();
	}

	/**
	 * Test authorize.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testAuthorize() throws Exception {
		User user = new User();
		user.setLogin("user-jwt-controller");
		user.setEmail("user-jwt-controller@example.com");
		user.setActivated(true);
		user.setPassword(this.passwordEncoder.encode("test"));
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		user.setGrantAccessDate(now.minusDays(1));
		user.setRevokeAccessDate(now.plusDays(1));

		this.userRepository.saveAndFlush(user);

		LoginVM login = new LoginVM();
		login.setUsername("user-jwt-controller");
		login.setPassword("test");
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(login))).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").isString())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").isNotEmpty());
	}

	/**
	 * Test authorize with remember me.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testAuthorizeWithRememberMe() throws Exception {
		User user = new User();
		user.setLogin("user-jwt-controller-remember-me");
		user.setEmail("user-jwt-controller-remember-me@example.com");
		user.setActivated(true);
		user.setPassword(this.passwordEncoder.encode("test"));
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		user.setGrantAccessDate(now.minusDays(1));
		user.setRevokeAccessDate(now.plusDays(1));

		this.userRepository.saveAndFlush(user);

		LoginVM login = new LoginVM();
		login.setUsername("user-jwt-controller-remember-me");
		login.setPassword("test");
		login.setRememberMe(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(login))).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").isString())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").isNotEmpty());
	}

	/**
	 * Test authorize fails because wrong credential.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testAuthorizeFailsBecauseWrongCredential() throws Exception {
		LoginVM login = new LoginVM();
		login.setUsername("wrong-user");
		login.setPassword("wrong password");
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(login)))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").doesNotExist());
	}

	/**
	 * Test authorize fails because be revoked.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testAuthorizeFailsBecauseBeRevoked() throws Exception {
		User user = new User();
		user.setLogin("user-jwt-controller");
		user.setEmail("user-jwt-controller@example.com");
		user.setActivated(true);
		user.setPassword(this.passwordEncoder.encode("test"));
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		user.setGrantAccessDate(now.minusDays(2));
		user.setRevokeAccessDate(now.minusDays(1));

		this.userRepository.saveAndFlush(user);

		LoginVM login = new LoginVM();
		login.setUsername("user-jwt-controller");
		login.setPassword("test");
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(login)))
		.andExpect(MockMvcResultMatchers.status().isPaymentRequired())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id_token").doesNotExist());
	}
}
