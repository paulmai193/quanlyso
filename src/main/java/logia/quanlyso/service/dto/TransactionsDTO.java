package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Transactions entity.
 */
public class TransactionsDTO implements Serializable {

    private Long id;

    private Integer chosenNumber;

    private Float netValue;

    private Long clientsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getChosenNumber() {
        return chosenNumber;
    }

    public void setChosenNumber(Integer chosenNumber) {
        this.chosenNumber = chosenNumber;
    }

    public Float getNetValue() {
        return netValue;
    }

    public void setNetValue(Float netValue) {
        this.netValue = netValue;
    }

    public Long getClientsId() {
        return clientsId;
    }

    public void setClientsId(Long clientId) {
        this.clientsId = clientId;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionsDTO{" +
            "id=" + getId() +
            ", chosenNumber='" + getChosenNumber() + "'" +
            ", netValue='" + getNetValue() + "'" +
            "}";
    }
}
