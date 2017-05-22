package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Transactions entity.
 */
public class TransactionsDTO implements Serializable {

    private Long id;

    private Long userId;

    private Integer chosenNumber;

    private Float netValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
            ", userId='" + getUserId() + "'" +
            ", chosenNumber='" + getChosenNumber() + "'" +
            ", netValue='" + getNetValue() + "'" +
            "}";
    }
}
