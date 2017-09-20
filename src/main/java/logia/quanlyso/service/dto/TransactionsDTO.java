package logia.quanlyso.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Transactions entity.
 *
 * @author Dai Mai
 */
public class TransactionsDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long						id;

	/** The chosen number. */
	private String						chosenNumber;

	/** The net value. */
	private Float						netValue;

	private Float cost;

	/** The open date. */
	private ZonedDateTime	openDate;

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
     * @param openDate the openDate to set
     */
    public void setOpenDate(ZonedDateTime openDate) {
		this.openDate = openDate;
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
     * Gets the transaction details DT os.
     *
     * @return the transactionDetailsDTOs
     */
    public Set<TransactionDetailsDTO> getTransactionDetailsDTOs() {
		return this.transactionDetailsDTOs;
	}

    /**
     * Sets the transaction details DT os.
     *
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

    @Override
    public String toString() {
        return "TransactionsDTO{" +
            "id=" + id +
            ", chosenNumber='" + chosenNumber + '\'' +
            ", netValue=" + netValue +
            ", cost=" + cost +
            ", openDate=" + openDate +
            ", clientsId=" + clientsId +
            ", transactionDetailsDTOs=" + transactionDetailsDTOs +
            '}';
    }

}
