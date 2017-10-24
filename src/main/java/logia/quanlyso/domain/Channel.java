/*
 * 
 */
package logia.quanlyso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Channel.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "channel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Channel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The name. */
	@Column(name = "name")
	private String name;

	/** The code. */
	@Column(name = "code")
	private String code;

	/** The sunday. */
	@Column(name = "sunday")
	private Boolean sunday;

	/** The monday. */
	@Column(name = "monday")
	private Boolean monday;

	/** The tuesday. */
	@Column(name = "tuesday")
	private Boolean tuesday;

	/** The wednesday. */
	@Column(name = "wednesday")
	private Boolean wednesday;

	/** The thursday. */
	@Column(name = "thursday")
	private Boolean thursday;

	/** The friday. */
	@Column(name = "friday")
	private Boolean friday;

	/** The saturday. */
	@Column(name = "saturday")
	private Boolean saturday;

	/** The transaction details. */
	@OneToMany(mappedBy = "channels")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TransactionDetails> transactionDetails = new HashSet<>();

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
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Name.
	 *
	 * @param name
	 *            the name
	 * @return the channel
	 */
	public Channel name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Code.
	 *
	 * @param code
	 *            the code
	 * @return the channel
	 */
	public Channel code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Checks if is sunday.
	 *
	 * @return the boolean
	 */
	public Boolean isSunday() {
		return this.sunday;
	}

	/**
	 * Sunday.
	 *
	 * @param sunday
	 *            the sunday
	 * @return the channel
	 */
	public Channel sunday(Boolean sunday) {
		this.sunday = sunday;
		return this;
	}

	/**
	 * Sets the sunday.
	 *
	 * @param sunday
	 *            the new sunday
	 */
	public void setSunday(Boolean sunday) {
		this.sunday = sunday;
	}

	/**
	 * Checks if is monday.
	 *
	 * @return the boolean
	 */
	public Boolean isMonday() {
		return this.monday;
	}

	/**
	 * Monday.
	 *
	 * @param monday
	 *            the monday
	 * @return the channel
	 */
	public Channel monday(Boolean monday) {
		this.monday = monday;
		return this;
	}

	/**
	 * Sets the monday.
	 *
	 * @param monday
	 *            the new monday
	 */
	public void setMonday(Boolean monday) {
		this.monday = monday;
	}

	/**
	 * Checks if is tuesday.
	 *
	 * @return the boolean
	 */
	public Boolean isTuesday() {
		return this.tuesday;
	}

	/**
	 * Tuesday.
	 *
	 * @param tuesday
	 *            the tuesday
	 * @return the channel
	 */
	public Channel tuesday(Boolean tuesday) {
		this.tuesday = tuesday;
		return this;
	}

	/**
	 * Sets the tuesday.
	 *
	 * @param tuesday
	 *            the new tuesday
	 */
	public void setTuesday(Boolean tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * Checks if is wednesday.
	 *
	 * @return the boolean
	 */
	public Boolean isWednesday() {
		return this.wednesday;
	}

	/**
	 * Wednesday.
	 *
	 * @param wednesday
	 *            the wednesday
	 * @return the channel
	 */
	public Channel wednesday(Boolean wednesday) {
		this.wednesday = wednesday;
		return this;
	}

	/**
	 * Sets the wednesday.
	 *
	 * @param wednesday
	 *            the new wednesday
	 */
	public void setWednesday(Boolean wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * Checks if is thursday.
	 *
	 * @return the boolean
	 */
	public Boolean isThursday() {
		return this.thursday;
	}

	/**
	 * Thursday.
	 *
	 * @param thursday
	 *            the thursday
	 * @return the channel
	 */
	public Channel thursday(Boolean thursday) {
		this.thursday = thursday;
		return this;
	}

	/**
	 * Sets the thursday.
	 *
	 * @param thursday
	 *            the new thursday
	 */
	public void setThursday(Boolean thursday) {
		this.thursday = thursday;
	}

	/**
	 * Checks if is friday.
	 *
	 * @return the boolean
	 */
	public Boolean isFriday() {
		return this.friday;
	}

	/**
	 * Friday.
	 *
	 * @param friday
	 *            the friday
	 * @return the channel
	 */
	public Channel friday(Boolean friday) {
		this.friday = friday;
		return this;
	}

	/**
	 * Sets the friday.
	 *
	 * @param friday
	 *            the new friday
	 */
	public void setFriday(Boolean friday) {
		this.friday = friday;
	}

	/**
	 * Checks if is saturday.
	 *
	 * @return the boolean
	 */
	public Boolean isSaturday() {
		return this.saturday;
	}

	/**
	 * Saturday.
	 *
	 * @param saturday
	 *            the saturday
	 * @return the channel
	 */
	public Channel saturday(Boolean saturday) {
		this.saturday = saturday;
		return this;
	}

	/**
	 * Sets the saturday.
	 *
	 * @param saturday
	 *            the new saturday
	 */
	public void setSaturday(Boolean saturday) {
		this.saturday = saturday;
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
	 * @param transactionDetails
	 *            the transaction details
	 * @return the channel
	 */
	public Channel transactionDetails(Set<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
		return this;
	}

	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails
	 *            the transaction details
	 * @return the channel
	 */
	public Channel addTransactionDetails(TransactionDetails transactionDetails) {
		return this.addTransactionDetails(transactionDetails, true);
	}

	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails
	 *            the transaction details
	 * @param isSetToTarget
	 *            the is set to target detail
	 * @return the channel
	 */
	Channel addTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.add(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setChannels(this, false);
		}
		return this;
	}

	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails
	 *            the transaction details
	 * @return the channel
	 */
	public Channel removeTransactionDetails(TransactionDetails transactionDetails) {
		return this.removeTransactionDetails(transactionDetails, true);
	}

	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails
	 *            the transaction details
	 * @param isSetToTarget
	 *            the is set to target detail
	 * @return the channel
	 */
	Channel removeTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.remove(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setChannels(null, false);
		}
		return this;
	}

	/**
	 * Sets the transaction details.
	 *
	 * @param transactionDetails
	 *            the new transaction details
	 */
	public void setTransactionDetails(Set<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
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
		Channel channel = (Channel) o;
		if (channel.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), channel.getId());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Channel{" + "id=" + this.getId() + ", name='" + this.getName() + "'" + ", code='" + this.getCode() + "'"
				+ ", sunday='" + this.isSunday() + "'" + ", monday='" + this.isMonday() + "'" + ", tuesday='"
				+ this.isTuesday() + "'" + ", wednesday='" + this.isWednesday() + "'" + ", thursday='"
				+ this.isThursday() + "'" + ", friday='" + this.isFriday() + "'" + ", saturday='" + this.isSaturday()
				+ "'" + "}";
	}
}
