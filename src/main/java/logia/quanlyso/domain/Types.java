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
 * A Types.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "types")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Types implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long					id;

	/** The name. */
	@Column(name = "name")
	private String					name;

	/** The transaction details. */
	@OneToMany(mappedBy = "types")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<TransactionDetails>	transactionDetails	= new HashSet<>();

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
	 * @param name the name
	 * @return the types
	 */
	public Types name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the types
	 */
	public Types transactionDetails(Set<TransactionDetails> transactionDetails) {
		this.transactionDetails = transactionDetails;
		return this;
	}

	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @return the types
	 */
	public Types addTransactionDetails(TransactionDetails transactionDetails) {
		return this.addTransactionDetails(transactionDetails, true);
	}

	/**
	 * Adds the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @param isSetToTarget the is set to target detail
	 * @return the types
	 */
	Types addTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.add(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setTypes(this, false);
		}
		return this;
	}

	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @return the types
	 */
	public Types removeTransactionDetails(TransactionDetails transactionDetails) {
		return this.removeTransactionDetails(transactionDetails, true);
	}

	/**
	 * Removes the transaction details.
	 *
	 * @param transactionDetails the transaction details
	 * @param isSetToTarget the is set to target detail
	 * @return the types
	 */
	Types removeTransactionDetails(TransactionDetails transactionDetails, boolean isSetToTarget) {
		this.transactionDetails.remove(transactionDetails);
		if (isSetToTarget) {
			transactionDetails.setTypes(null, false);
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
		Types types = (Types) o;
		if (types.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), types.getId());
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
		return "Types{" + "id=" + this.getId() + ", name='" + this.getName() + "'" + "}";
	}
}
