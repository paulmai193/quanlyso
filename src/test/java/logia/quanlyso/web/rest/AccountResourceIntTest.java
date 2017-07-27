package logia.quanlyso.web.rest;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.QuanlysoApp;
import logia.quanlyso.domain.Authority;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.AuthorityRepository;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.security.AuthoritiesConstants;
import logia.quanlyso.service.MailService;
import logia.quanlyso.service.UserService;
import logia.quanlyso.service.dto.UserDTO;
import logia.quanlyso.web.rest.vm.KeyAndPasswordVM;
import logia.quanlyso.web.rest.vm.ManagedUserVM;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuanlysoApp.class)
public class AccountResourceIntTest {

	/** The user repository. */
	@Autowired
	private UserRepository			userRepository;

	/** The authority repository. */
	@Autowired
	private AuthorityRepository		authorityRepository;

	/** The user service. */
	@Autowired
	private UserService				userService;

	/** The password encoder. */
	@Autowired
	private PasswordEncoder			passwordEncoder;

	/** The http message converters. */
	@Autowired
	private HttpMessageConverter[]	httpMessageConverters;

	/** The mock user service. */
	@Mock
	private UserService				mockUserService;

	/** The mock mail service. */
	@Mock
	private MailService				mockMailService;

	/** The rest user mock mvc. */
	private MockMvc					restUserMockMvc;

	/** The rest mvc. */
	private MockMvc					restMvc;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(this.mockMailService).sendActivationEmail(Matchers.anyObject());

		AccountResource accountResource = new AccountResource(this.userRepository, this.userService,
				this.mockMailService);

		AccountResource accountUserMockResource = new AccountResource(this.userRepository,
				this.mockUserService, this.mockMailService);

		this.restMvc = MockMvcBuilders.standaloneSetup(accountResource)
				.setMessageConverters(this.httpMessageConverters).build();
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
	}

	/**
	 * Test non authenticated user.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testNonAuthenticatedUser() throws Exception {
		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(""));
	}

	/**
	 * Test authenticated user.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testAuthenticatedUser() throws Exception {
		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/authenticate").with(request -> {
			request.setRemoteUser("test");
			return request;
		}).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string("test"));
	}

	/**
	 * Test get existing account.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetExistingAccount() throws Exception {
		Set<Authority> authorities = new HashSet<>();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.ADMIN);
		authorities.add(authority);

		User user = new User();
		user.setLogin("test");
		user.setFirstName("john");
		user.setLastName("doe");
		user.setEmail("john.doe@jhipster.com");
		user.setImageUrl("http://placehold.it/50x50");
		user.setLangKey("en");
		user.setAuthorities(authorities);
		Mockito.when(this.mockUserService.getUserWithAuthorities()).thenReturn(user);

		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/account").accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(MockMvcResultMatchers.jsonPath("$.login").value("test"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("john"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("doe"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@jhipster.com"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").value("http://placehold.it/50x50"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.langKey").value("en"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
	}

	/**
	 * Test get unknown account.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetUnknownAccount() throws Exception {
		Mockito.when(this.mockUserService.getUserWithAuthorities()).thenReturn(null);

		this.restUserMockMvc.perform(MockMvcRequestBuilders.get("/api/account").accept(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	/**
	 * Test register valid.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterValid() throws Exception {
		ManagedUserVM validUser = new ManagedUserVM(null, // id
				"joe", // login
				"password", // password
				"Joe", // firstName
				"Shmoe", // lastName
				"joe@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(validUser)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		Optional<User> user = this.userRepository.findOneByLogin("joe");
		Assertions.assertThat(user.isPresent()).isTrue();
	}

	/**
	 * Test register invalid login.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterInvalidLogin() throws Exception {
		ManagedUserVM invalidUser = new ManagedUserVM(null, // id
				"funky-log!n", // login <-- invalid
				"password", // password
				"Funky", // firstName
				"One", // lastName
				"funky@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
		.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(invalidUser)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		Optional<User> user = this.userRepository.findOneByEmail("funky@example.com");
		Assertions.assertThat(user.isPresent()).isFalse();
	}

	/**
	 * Test register invalid email.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterInvalidEmail() throws Exception {
		ManagedUserVM invalidUser = new ManagedUserVM(null, // id
				"bob", // login
				"password", // password
				"Bob", // firstName
				"Green", // lastName
				"invalid", // email <-- invalid
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
		.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(invalidUser)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		Optional<User> user = this.userRepository.findOneByLogin("bob");
		Assertions.assertThat(user.isPresent()).isFalse();
	}

	/**
	 * Test register invalid password.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterInvalidPassword() throws Exception {
		ManagedUserVM invalidUser = new ManagedUserVM(null, // id
				"bob", // login
				"123", // password with only 3 digits
				"Bob", // firstName
				"Green", // lastName
				"bob@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restUserMockMvc
		.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(invalidUser)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		Optional<User> user = this.userRepository.findOneByLogin("bob");
		Assertions.assertThat(user.isPresent()).isFalse();
	}

	/**
	 * Test register duplicate login.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterDuplicateLogin() throws Exception {
		// Good
		ManagedUserVM validUser = new ManagedUserVM(null, // id
				"alice", // login
				"password", // password
				"Alice", // firstName
				"Something", // lastName
				"alice@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		// Duplicate login, different email
		ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), validUser.getLogin(),
				validUser.getPassword(), validUser.getFirstName(), validUser.getLastName(),
				"alicejr@example.com", true, validUser.getImageUrl(), validUser.getLangKey(),
				validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(),
				validUser.getLastModifiedDate(), validUser.getAuthorities(), ZonedDateTime.now(),
				ZonedDateTime.now());

		// Good user
		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(validUser)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Duplicate login
		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());

		Optional<User> userDup = this.userRepository.findOneByEmail("alicejr@example.com");
		Assertions.assertThat(userDup.isPresent()).isFalse();
	}

	/**
	 * Test register duplicate email.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterDuplicateEmail() throws Exception {
		// Good
		ManagedUserVM validUser = new ManagedUserVM(null, // id
				"john", // login
				"password", // password
				"John", // firstName
				"Doe", // lastName
				"john@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER)),
				ZonedDateTime.now(), ZonedDateTime.now());

		// Duplicate email, different login
		ManagedUserVM duplicatedUser = new ManagedUserVM(validUser.getId(), "johnjr",
				validUser.getPassword(), validUser.getLogin(), validUser.getLastName(),
				validUser.getEmail(), true, validUser.getImageUrl(), validUser.getLangKey(),
				validUser.getCreatedBy(), validUser.getCreatedDate(), validUser.getLastModifiedBy(),
				validUser.getLastModifiedDate(), validUser.getAuthorities(), ZonedDateTime.now(),
				ZonedDateTime.now());

		// Good user
		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(validUser)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		// Duplicate email
		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(duplicatedUser)))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());

		Optional<User> userDup = this.userRepository.findOneByLogin("johnjr");
		Assertions.assertThat(userDup.isPresent()).isFalse();
	}

	/**
	 * Test register admin is ignored.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testRegisterAdminIsIgnored() throws Exception {
		ManagedUserVM validUser = new ManagedUserVM(null, // id
				"badguy", // login
				"password", // password
				"Bad", // firstName
				"Guy", // lastName
				"badguy@example.com", // email
				true, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(validUser)))
		.andExpect(MockMvcResultMatchers.status().isCreated());

		Optional<User> userDup = this.userRepository.findOneByLogin("badguy");
		Assertions.assertThat(userDup.isPresent()).isTrue();
		Assertions.assertThat(userDup.get().getAuthorities()).hasSize(1)
		.containsExactly(this.authorityRepository.findOne(AuthoritiesConstants.USER));
	}

	/**
	 * Test activate account.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testActivateAccount() throws Exception {
		final String activationKey = "some activation key";
		User user = new User();
		user.setLogin("activate-account");
		user.setEmail("activate-account@example.com");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(false);
		user.setActivationKey(activationKey);

		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(MockMvcRequestBuilders.get("/api/activate?key={activationKey}", activationKey))
		.andExpect(MockMvcResultMatchers.status().isOk());

		user = this.userRepository.findOneByLogin(user.getLogin()).orElse(null);
		Assertions.assertThat(user.getActivated()).isTrue();
	}

	/**
	 * Test activate account with wrong key.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@Ignore
	public void testActivateAccountWithWrongKey() throws Exception {
		this.restMvc.perform(MockMvcRequestBuilders.get("/api/activate?key=wrongActivationKey"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	/**
	 * Test save account.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("save-account")
	@Ignore
	public void testSaveAccount() throws Exception {
		User user = new User();
		user.setLogin("save-account");
		user.setEmail("save-account@example.com");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);

		this.userRepository.saveAndFlush(user);

		UserDTO userDTO = new UserDTO(null, // id
				"not-used", // login
				"firstname", // firstName
				"lastname", // lastName
				"save-account@example.com", // email
				false, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(MockMvcResultMatchers.status().isOk());

		User updatedUser = this.userRepository.findOneByLogin(user.getLogin()).orElse(null);
		Assertions.assertThat(updatedUser.getFirstName()).isEqualTo(userDTO.getFirstName());
		Assertions.assertThat(updatedUser.getLastName()).isEqualTo(userDTO.getLastName());
		Assertions.assertThat(updatedUser.getEmail()).isEqualTo(userDTO.getEmail());
		Assertions.assertThat(updatedUser.getLangKey()).isEqualTo(userDTO.getLangKey());
		Assertions.assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
		Assertions.assertThat(updatedUser.getImageUrl()).isEqualTo(userDTO.getImageUrl());
		Assertions.assertThat(updatedUser.getActivated()).isEqualTo(true);
		Assertions.assertThat(updatedUser.getAuthorities()).isEmpty();
	}

	/**
	 * Test save invalid email.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("save-invalid-email")
	@Ignore
	public void testSaveInvalidEmail() throws Exception {
		User user = new User();
		user.setLogin("save-invalid-email");
		user.setEmail("save-invalid-email@example.com");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);

		this.userRepository.saveAndFlush(user);

		UserDTO userDTO = new UserDTO(null, // id
				"not-used", // login
				"firstname", // firstName
				"lastname", // lastName
				"invalid email", // email
				false, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		Assertions.assertThat(this.userRepository.findOneByEmail("invalid email")).isNotPresent();
	}

	/**
	 * Test save existing email.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("save-existing-email")
	@Ignore
	public void testSaveExistingEmail() throws Exception {
		User user = new User();
		user.setLogin("save-existing-email");
		user.setEmail("save-existing-email@example.com");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);

		this.userRepository.saveAndFlush(user);

		User anotherUser = new User();
		anotherUser.setLogin("save-existing-email2");
		anotherUser.setEmail("save-existing-email2@example.com");
		anotherUser.setPassword(RandomStringUtils.random(60));
		anotherUser.setActivated(true);

		this.userRepository.saveAndFlush(anotherUser);

		UserDTO userDTO = new UserDTO(null, // id
				"not-used", // login
				"firstname", // firstName
				"lastname", // lastName
				"save-existing-email2@example.com", // email
				false, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		User updatedUser = this.userRepository.findOneByLogin("save-existing-email").orElse(null);
		Assertions.assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email@example.com");
	}

	/**
	 * Test save existing email and login.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("save-existing-email-and-login")
	@Ignore
	public void testSaveExistingEmailAndLogin() throws Exception {
		User user = new User();
		user.setLogin("save-existing-email-and-login");
		user.setEmail("save-existing-email-and-login@example.com");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);

		this.userRepository.saveAndFlush(user);

		User anotherUser = new User();
		anotherUser.setLogin("save-existing-email-and-login");
		anotherUser.setEmail("save-existing-email-and-login@example.com");
		anotherUser.setPassword(RandomStringUtils.random(60));
		anotherUser.setActivated(true);

		UserDTO userDTO = new UserDTO(null, // id
				"not-used", // login
				"firstname", // firstName
				"lastname", // lastName
				"save-existing-email-and-login@example.com", // email
				false, // activated
				"http://placehold.it/50x50", // imageUrl
				"vi", // langKey
				null, // createdBy
				null, // createdDate
				null, // lastModifiedBy
				null, // lastModifiedDate
				new HashSet<>(Collections.singletonList(AuthoritiesConstants.ADMIN)),
				ZonedDateTime.now(), ZonedDateTime.now());

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(userDTO))).andExpect(MockMvcResultMatchers.status().isOk());

		User updatedUser = this.userRepository.findOneByLogin("save-existing-email-and-login")
				.orElse(null);
		Assertions.assertThat(updatedUser.getEmail()).isEqualTo("save-existing-email-and-login@example.com");
	}

	/**
	 * Test change password.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("change-password")
	public void testChangePassword() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("change-password");
		user.setEmail("change-password@example.com");
		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/change_password").content("new password"))
		.andExpect(MockMvcResultMatchers.status().isOk());

		User updatedUser = this.userRepository.findOneByLogin("change-password").orElse(null);
		Assertions.assertThat(this.passwordEncoder.matches("new password", updatedUser.getPassword())).isTrue();
	}

	/**
	 * Test change password too small.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("change-password-too-small")
	public void testChangePasswordTooSmall() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("change-password-too-small");
		user.setEmail("change-password-too-small@example.com");
		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/change_password").content("new"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		User updatedUser = this.userRepository.findOneByLogin("change-password-too-small").orElse(null);
		Assertions.assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
	}

	/**
	 * Test change password too long.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("change-password-too-long")
	public void testChangePasswordTooLong() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("change-password-too-long");
		user.setEmail("change-password-too-long@example.com");
		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/change_password").content(RandomStringUtils.random(101)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		User updatedUser = this.userRepository.findOneByLogin("change-password-too-long").orElse(null);
		Assertions.assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
	}

	/**
	 * Test change password empty.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	@WithMockUser("change-password-empty")
	public void testChangePasswordEmpty() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("change-password-empty");
		user.setEmail("change-password-empty@example.com");
		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/change_password").content(RandomStringUtils.random(0)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		User updatedUser = this.userRepository.findOneByLogin("change-password-empty").orElse(null);
		Assertions.assertThat(updatedUser.getPassword()).isEqualTo(user.getPassword());
	}

	/**
	 * Test request password reset.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testRequestPasswordReset() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);
		user.setLogin("password-reset");
		user.setEmail("password-reset@example.com");
		this.userRepository.saveAndFlush(user);

		this.restMvc.perform(
				MockMvcRequestBuilders.post("/api/account/reset_password/init").content("password-reset@example.com"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Test request password reset wrong email.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testRequestPasswordResetWrongEmail() throws Exception {
		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/reset_password/init")
				.content("password-reset-wrong-email@example.com"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	/**
	 * Test finish password reset.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testFinishPasswordReset() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("finish-password-reset");
		user.setEmail("finish-password-reset@example.com");
		user.setResetDate(Instant.now().plusSeconds(60));
		user.setResetKey("reset key");
		this.userRepository.saveAndFlush(user);

		KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
		keyAndPassword.setKey(user.getResetKey());
		keyAndPassword.setNewPassword("new password");

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/reset_password/finish")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
		.andExpect(MockMvcResultMatchers.status().isOk());

		User updatedUser = this.userRepository.findOneByLogin(user.getLogin()).orElse(null);
		Assertions.assertThat(
				this.passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword()))
		.isTrue();
	}

	/**
	 * Test finish password reset too small.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testFinishPasswordResetTooSmall() throws Exception {
		User user = new User();
		user.setPassword(RandomStringUtils.random(60));
		user.setLogin("finish-password-reset-too-small");
		user.setEmail("finish-password-reset-too-small@example.com");
		user.setResetDate(Instant.now().plusSeconds(60));
		user.setResetKey("reset key too small");
		this.userRepository.saveAndFlush(user);

		KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
		keyAndPassword.setKey(user.getResetKey());
		keyAndPassword.setNewPassword("foo");

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/reset_password/finish")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

		User updatedUser = this.userRepository.findOneByLogin(user.getLogin()).orElse(null);
		Assertions.assertThat(
				this.passwordEncoder.matches(keyAndPassword.getNewPassword(), updatedUser.getPassword()))
		.isFalse();
	}

	/**
	 * Test finish password reset wrong key.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testFinishPasswordResetWrongKey() throws Exception {
		KeyAndPasswordVM keyAndPassword = new KeyAndPasswordVM();
		keyAndPassword.setKey("wrong reset key");
		keyAndPassword.setNewPassword("new password");

		this.restMvc.perform(MockMvcRequestBuilders.post("/api/account/reset_password/finish")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(keyAndPassword)))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
}
