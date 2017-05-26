package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Transactions entity.
 *
 * @author Dai Mai
 */
public class TransactionsDTO implements Serializable {

    /** The id. */
    private Long id;

    /** The chosen number. */
    private Integer chosenNumber;

    /** The net value. */
    private Float netValue;

    /** The clients id. */
    private Long clientsId;

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
        return clientsId;
    }

    /**
     * Sets the clients id.
     *
     * @param clientId the new clients id
     */
    public void setClientsId(Long clientId) {
        this.clientsId = clientId;
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

        TransactionsDTO transactionsDTO = (TransactionsDTO) o;
        if(transactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionsDTO.getId());
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
        return "TransactionsDTO{" +
            "id=" + getId() +
            ", chosenNumber='" + getChosenNumber() + "'" +
            ", netValue='" + getNetValue() + "'" +
            "}";
    }
}
