package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Transactions entity.
 *
 * @author Dai Mai
 */
public class TransactionsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long						id;

	/** The chosen number. */
	private String						chosenNumber;

	/** The net value. */
	private Float						netValue;

	/** The clients id. */
	private Long						clientsId;

	/** The details DTOs. */
	private Set<TransactionDetailsDTO>	transactionDetailsDTOs	= new HashSet<>();

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
	 * Sets the net value.
	 *
	 * @param netValue the new net value
	 */
	public void setNetValue(Float netValue) {
		this.netValue = netValue;
	}

	/**
	 * Gets the clients id.
	 *
	 * @return the clients id
	 */
	public Long getClientsId() {
		return this.clientsId;
	}

	/**
	 * Sets the clients id.
	 *
	 * @param clientId the new clients id
	 */
	public void setClientsId(Long clientId) {
		this.clientsId = clientId;
	}

	/**
	 * @return the transactionDetailsDTOs
	 */
	public Set<TransactionDetailsDTO> getTransactionDetailsDTOs() {
		return this.transactionDetailsDTOs;
	}

	/**
	 * @param transactionDetailsDTOs the transactionDetailsDTOs to set
	 */
	public void setTransactionDetailsDTOs(Set<TransactionDetailsDTO> transactionDetailsDTOs) {
		this.transactionDetailsDTOs = transactionDetailsDTOs;
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

		TransactionsDTO transactionsDTO = (TransactionsDTO) o;
		if (transactionsDTO.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), transactionsDTO.getId());
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
		return "TransactionsDTO{" + "id=" + this.getId() + ", chosenNumber='" + this.getChosenNumber() + "'"
				+ ", netValue='" + this.getNetValue() + "'" + ", details='" + this.getTransactionDetailsDTOs()
				+ "'" + "}";
	}
}
