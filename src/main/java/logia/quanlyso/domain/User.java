package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import logia.quanlyso.config.Constants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long				id;

	/** The login. */
	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String				login;

	/** The password. */
	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60)
	private String				password;

	/** The first name. */
	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String				firstName;

	/** The last name. */
	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String				lastName;

	/** The email. */
	@Email
	@Size(min = 5, max = 100)
	@Column(length = 100, unique = true)
	private String				email;

	/** The activated. */
	@NotNull
	@Column(nullable = false)
	private boolean				activated			= false;

	/** The lang key. */
	@Size(min = 2, max = 5)
	@Column(name = "lang_key", length = 5)
	private String				langKey;

	/** The image url. */
	@Size(max = 256)
	@Column(name = "image_url", length = 256)
	private String				imageUrl;

	/** The activation key. */
	@Size(max = 20)
	@Column(name = "activation_key", length = 20)
	@JsonIgnore
	private String				activationKey;

	/** The reset key. */
	@Size(max = 20)
	@Column(name = "reset_key", length = 20)
	@JsonIgnore
	private String				resetKey;

	/** The reset date. */
	@Column(name = "reset_date")
	private Instant				resetDate			= null;

	/** The authorities. */
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "jhi_user_authority", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_name", referencedColumnName = "name") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	@BatchSize(size = 20)
	private Set<Authority>		authorities			= new HashSet<>();

	/** The grant access date. */
	@Column(name = "grant_access_date")
	private ZonedDateTime		grantAccessDate		= ZonedDateTime.now();

	/** The revoke access date. */
	@Column(name = "revoke_access_date")
	private ZonedDateTime		revokeAccessDate	= ZonedDateTime.now();

	/** The transactionsses. */
	@OneToMany(mappedBy = "users")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Transactions>	transactionsses		= new HashSet<>();

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Sets the login.
	 *
	 * @param login the new login
	 */
	// Lowercase the login before saving it in database
	public void setLogin(String login) {
		this.login = login.toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Append login.
	 *
	 * @param login the login
	 * @return the user
	 */
	public User appendLogin(String login) {
		this.setLogin(login);
		return this;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Append password.
	 *
	 * @param password the password
	 * @return the user
	 */
	public User appendPassword(String password) {
		this.setPassword(password);
		return this;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Append first name.
	 *
	 * @param firstName the first name
	 * @return the user
	 */
	public User appendFirstName(String firstName) {
		this.setFirstName(firstName);
		return this;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Append last name.
	 *
	 * @param lastName the last name
	 * @return the user
	 */
	public User appendLastName(String lastName) {
		this.setLastName(lastName);
		return this;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Append email.
	 *
	 * @param email the email
	 * @return the user
	 */
	public User appendEmail(String email) {
		this.setEmail(email);
		return this;
	}

	/**
	 * Gets the image url.
	 *
	 * @return the image url
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}

	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the new image url
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Append image url.
	 *
	 * @param imageUrl the image url
	 * @return the user
	 */
	public User appendImageUrl(String imageUrl) {
		this.setImageUrl(imageUrl);
		return this;
	}

	/**
	 * Gets the activated.
	 *
	 * @return the activated
	 */
	public boolean getActivated() {
		return this.activated;
	}

	/**
	 * Sets the activated.
	 *
	 * @param activated the new activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * Gets the activation key.
	 *
	 * @return the activation key
	 */
	public String getActivationKey() {
		return this.activationKey;
	}

	/**
	 * Sets the activation key.
	 *
	 * @param activationKey the new activation key
	 */
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	/**
	 * Gets the reset key.
	 *
	 * @return the reset key
	 */
	public String getResetKey() {
		return this.resetKey;
	}

	/**
	 * Sets the reset key.
	 *
	 * @param resetKey the new reset key
	 */
	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	/**
	 * Gets the reset date.
	 *
	 * @return the reset date
	 */
	public Instant getResetDate() {
		return this.resetDate;
	}

	/**
	 * Sets the reset date.
	 *
	 * @param resetDate the new reset date
	 */
	public void setResetDate(Instant resetDate) {
		this.resetDate = resetDate;
	}

	/**
	 * Gets the lang key.
	 *
	 * @return the lang key
	 */
	public String getLangKey() {
		return this.langKey;
	}

	/**
	 * Sets the lang key.
	 *
	 * @param langKey the new lang key
	 */
	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	/**
	 * Append lang key.
	 *
	 * @param langKey the lang key
	 * @return the user
	 */
	public User appendLangKey(String langKey) {
		this.setLangKey(langKey);
		return this;
	}

	/**
	 * Gets the authorities.
	 *
	 * @return the authorities
	 */
	public Set<Authority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * Sets the authorities.
	 *
	 * @param authorities the new authorities
	 */
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * Gets the transactionsses.
	 *
	 * @return the transactionsses
	 */
	public Set<Transactions> getTransactionsses() {
		return this.transactionsses;
	}

	/**
	 * Transactionsses.
	 *
	 * @param transactions the transactions
	 * @return the client
	 */
	public User transactionsses(Set<Transactions> transactions) {
		this.setTransactionsses(transactions);
		return this;
	}

	/**
	 * Adds the transactionss.
	 *
	 * @param transactions the transactions
	 * @return the client
	 */
	public User addTransactionss(Transactions transactions) {
		return this.addTransactions(transactions, true);
	}

	/**
	 * Adds the transactions.
	 *
	 * @param transactions the transactions
	 * @param isSetToTarget the is set to target transaction
	 * @return the user
	 */
	User addTransactions(Transactions transactions, boolean isSetToTarget) {
		this.transactionsses.add(transactions);
		if (isSetToTarget) {
			transactions.setUsers(this, false);
		}
		return this;
	}

	/**
	 * Removes the transactionss.
	 *
	 * @param transactions the transactions
	 * @return the client
	 */
	public User removeTransactionss(Transactions transactions) {
		return this.removeTransactionss(transactions, true);
	}

	/**
	 * Removes the transactionss.
	 *
	 * @param transactions the transactions
	 * @param isSetToTarget the is set to target transaction
	 * @return the user
	 */
	User removeTransactionss(Transactions transactions, boolean isSetToTarget) {
		Set<Transactions> wrapper = new HashSet<>(this.transactionsses);
		boolean isSuccess = wrapper.remove(transactions);
		if (isSuccess) {
			this.setTransactionsses(wrapper);
			if (isSetToTarget) {
				transactions.setUsers(null, false);
			}
		}
		return this;
	}

	/**
	 * Sets the transactionsses.
	 *
	 * @param transactions the new transactionsses
	 */
	public void setTransactionsses(Set<Transactions> transactions) {
		this.transactionsses = transactions;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		return this.login.equals(user.login);
	}

	public ZonedDateTime getGrantAccessDate() {
		return this.grantAccessDate;
	}

	public void setGrantAccessDate(ZonedDateTime grantAccessDate) {
		this.grantAccessDate = grantAccessDate;
	}

	public ZonedDateTime getRevokeAccessDate() {
		return this.revokeAccessDate;
	}

	public void setRevokeAccessDate(ZonedDateTime revokeAccessDate) {
		this.revokeAccessDate = revokeAccessDate;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.login.hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User{" + "login='" + this.login + '\'' + ", firstName='" + this.firstName + '\''
				+ ", lastName='" + this.lastName + '\'' + ", email='" + this.email + '\'' + ", imageUrl='"
				+ this.imageUrl + '\'' + ", activated='" + this.activated + '\'' + ", langKey='" + this.langKey
				+ '\'' + ", activationKey='" + this.activationKey + '\'' + ", grantAccessDate='"
				+ this.grantAccessDate + '\'' + ", revokeAccessDate='" + this.revokeAccessDate + '\'' + "}";
	}
}
