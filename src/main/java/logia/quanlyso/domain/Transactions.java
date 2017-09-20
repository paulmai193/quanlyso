package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
	private static final long		serialVersionUID	= 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long					id;

	/** The chosen number. */
	@Column(name = "chosen_number")
	private String					chosenNumber;

	/** The net value. */
	@Column(name = "net_value")
	private Float					netValue;

	/** The open date. */
	@Column(name = "open_date")
	private ZonedDateTime openDate;

	/** The transaction details. */
	@OneToMany(mappedBy = "transactions")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TransactionDetails>	transactionDetails	= new HashSet<>();

	/** The users. */
	@ManyToOne()
	private User					users;

	private transient Float cost;

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
     * Gets the chosen number.
     *
     * @return the chosen number
     */
    public String getChosenNumber() {
		return this.chosenNumber;
	}

    /**
     * Chosen number.
     *
     * @param chosenNumber the chosen number
     * @return the transactions
     */
    public Transactions chosenNumber(String chosenNumber) {
		this.setChosenNumber(chosenNumber);
		return this;
	}

    /**
     * Sets the chosen number.
     *
     * @param chosenNumber the new chosen number
     */
    public void setChosenNumber(String chosenNumber) {
		this.chosenNumber = chosenNumber;
	}

    /**
     * Gets the net value.
     *
     * @return the net value
     */
    public Float getNetValue() {
		return this.netValue;
	}

    /**
     * Net value.
     *
     * @param netValue the net value
     * @return the transactions
     */
    public Transactions netValue(Float netValue) {
		this.setNetValue(netValue);;
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
     * Gets the open date.
     *
     * @return the openDate
     */
    public ZonedDateTime getOpenDate() {
		return openDate;
	}

    /**
     * Sets the open date.
     *
     * @param __openDate the openDate to set
     */
    public void setOpenDate(ZonedDateTime __openDate) {
		this.openDate = __openDate;
	}

    /**
     * Open date.
     *
     * @param __openDay the open day
     * @return the transactions
     */
    public Transactions openDate(ZonedDateTime __openDay) {
		this.setOpenDate(__openDay);
		return this;
	}

    /**
     * Gets the transaction details.
     *
     * @return the transaction details
     */
    public Set<TransactionDetails> getTransactionDetails() {
		return this.transactionDetails;
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
     * @param isSetToTarget      the is set to target detail
     * @return the transactions
     */
    Transactions addTransactionDetails(TransactionDetails transactionDetails,
			boolean isSetToTarget) {
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
     * @param isSetToTarget      the is set to target detail
     * @return the transactions
     */
    Transactions removeTransactionDetails(TransactionDetails transactionDetails,
			boolean isSetToTarget) {
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
		this.setUsers(user);
		;
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
     * @param user          the user
     * @param isSetToTarget the is set to target user
     */
    void setUsers(User user, boolean isSetToTarget) {
		this.users = user;
		if (user != null && isSetToTarget) {
			user.addTransactions(this, false);
		}
	}

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public Float getCost() {
        return cost;
    }

    /**
     * Sets cost.
     *
     * @param cost the cost
     */
    public void setCost(Float cost) {
        this.cost = cost;
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
		Transactions transactions = (Transactions) o;
		if (transactions.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), transactions.getId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.getId());
	}

    @Override
    public String toString() {
        return "Transactions{" +
            "id=" + id +
            ", chosenNumber='" + chosenNumber + '\'' +
            ", netValue=" + netValue +
            ", openDate=" + openDate +
            ", transactionDetails=" + transactionDetails +
            ", users=" + users +
            ", cost=" + cost +
            '}';
    }
}
