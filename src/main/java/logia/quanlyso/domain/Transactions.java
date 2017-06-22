package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Transactions.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transactions extends AbstractAuditingEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The chosen number. */
	@Column(name = "chosen_number")
	private Integer chosenNumber;

	/** The net value. */
	@Column(name = "net_value")
	private Float netValue;

	/** The transaction details. */
	@OneToMany(mappedBy = "transactions")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TransactionDetails> transactionDetails = new HashSet<>();

	/** The users. */
	@ManyToOne(cascade=CascadeType.ALL)
	private User users;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * Gets the chosen number.
	 *
	 * @return the chosen number
	 */
	public Integer getChosenNumber() {
		return chosenNumber;
	}

	/**
	 * Chosen number.
	 *
	 * @param chosenNumber the chosen number
	 * @return the transactions
	 */
	public Transactions chosenNumber(Integer chosenNumber) {
		this.chosenNumber = chosenNumber;
		return this;
	}

	/**
	 * Sets the chosen number.
	 *
	 * @param chosenNumber the new chosen number
	 */
	public void setChosenNumber(Integer chosenNumber) {
		this.chosenNumber = chosenNumber;
	}

	/**
	 * Gets the net value.
	 *
	 * @return the net value
	 */
	public Float getNetValue() {
		return netValue;
	}

	/**
	 * Net value.
	 *
	 * @param netValue the net value
	 * @return the transactions
	 */
	public Transactions netValue(Float netValue) {
		this.netValue = netValue;
		return this;
	}

	/**
	 * Sets the net value.
	 *
	 * @param netValue the new net value
	 */
	public void setNetValue(Float netValue) {
		this.netValue = netValue;
	}

	/**
	 * Gets the transaction details.
	 *
	 * @return the transaction details
	 */
	public Set<TransactionDetails> getTransactionDetails() {
		return transactionDetails;
	}

	/**
	 * Transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @return the transactions
	 */
	public Transactions transactionDetails(Set<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
		return this;
	}

	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @return the transactions
	 */
	public Transactions addTransactionDetails(TransactionDetails transactionDetails) {
		return this.addTransactionDetails(transactionDetails, true);
	}
	
	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @param isSetToTarget the is set to target detail
	 * @return the transactions
	 */
	Transactions addTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.add(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setTransactions(this, false);	
		}		
		return this;
	}

	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @return the transactions
	 */
	public Transactions removeTransactionDetails(TransactionDetails transactionDetails) {
		return this.removeTransactionDetails(transactionDetails, true);
	}
	
	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @param isSetToTarget the is set to target detail
	 * @return the transactions
	 */
	Transactions removeTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.remove(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setTransactions(null, false);	
		}		
		return this;
	}

	/**
	 * Sets the transaction details.
	 *
	 * @param transactionDetails the new transaction details
	 */
	public void setTransactionDetails(Set<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public User getUsers() {
		return this.users;
	}

	/**
	 * Users.
	 *
	 * @param user the user
	 * @return the transactions
	 */
	public Transactions users(User user) {
		this.setUsers(user);;
		return this;
	}

	/**
	 * Sets the users.
	 *
	 * @param user the new users
	 */
	public void setUsers(User user) {
		this.setUsers(user, true);
	}
	
	/**
	 * Sets the users.
	 *
	 * @param user the user
	 * @param isSetToTarget the is set to target user
	 */
	void setUsers(User user, boolean isSetToTarget) {
		this.users = user;
		if (user != null && isSetToTarget) {
			user.addTransactions(this, false);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Transactions transactions = (Transactions) o;
		if (transactions.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), transactions.getId());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Transactions{" +
				"id=" + getId() +
				", chosenNumber='" + getChosenNumber() + "'" +
				", netValue='" + getNetValue() + "'" +
				"}";
	}
}
