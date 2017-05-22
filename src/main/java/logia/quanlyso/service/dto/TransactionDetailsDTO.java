package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TransactionDetails entity.
 */
public class TransactionDetailsDTO implements Serializable {

    private Long id;

    private Float amount;

    private Float profit;

    private Float costs;

    private Long transactionsId;

    private Long channelsId;

    private Long factorsId;

    private Long stylesId;

    private Long typesId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getProfit() {
        return profit;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }

    public Float getCosts() {
        return costs;
    }

    public void setCosts(Float costs) {
        this.costs = costs;
    }

    public Long getTransactionsId() {
        return transactionsId;
    }

    public void setTransactionsId(Long transactionsId) {
        this.transactionsId = transactionsId;
    }

    public Long getChannelsId() {
        return channelsId;
    }

    public void setChannelsId(Long channelId) {
        this.channelsId = channelId;
    }

    public Long getFactorsId() {
        return factorsId;
    }

    public void setFactorsId(Long factorId) {
        this.factorsId = factorId;
    }

    public Long getStylesId() {
        return stylesId;
    }

    public void setStylesId(Long styleId) {
        this.stylesId = styleId;
    }

    public Long getTypesId() {
        return typesId;
    }

    public void setTypesId(Long typesId) {
        this.typesId = typesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDetailsDTO transactionDetailsDTO = (TransactionDetailsDTO) o;
        if(transactionDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDetailsDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", profit='" + getProfit() + "'" +
            ", costs='" + getCosts() + "'" +
            "}";
    }
}
