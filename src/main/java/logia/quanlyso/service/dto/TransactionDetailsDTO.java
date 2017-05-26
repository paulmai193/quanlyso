package logia.quanlyso.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TransactionDetails entity.
 *
 * @author Dai Mai
 */
public class TransactionDetailsDTO implements Serializable {

    /** The id. */
    private Long id;

    /** The amount. */
    private Float amount;

    /** The profit. */
    private Float profit;

    /** The costs. */
    private Float costs;

    /** The transactions id. */
    private Long transactionsId;

    /** The channels id. */
    private Long channelsId;

    /** The factors id. */
    private Long factorsId;

    /** The styles id. */
    private Long stylesId;

    /** The types id. */
    private Long typesId;

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
     * Gets the amount.
     *
     * @return the amount
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * Sets the amount.
     *
     * @param amount the new amount
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    /**
     * Gets the profit.
     *
     * @return the profit
     */
    public Float getProfit() {
        return profit;
    }

    /**
     * Sets the profit.
     *
     * @param profit the new profit
     */
    public void setProfit(Float profit) {
        this.profit = profit;
    }

    /**
     * Gets the costs.
     *
     * @return the costs
     */
    public Float getCosts() {
        return costs;
    }

    /**
     * Sets the costs.
     *
     * @param costs the new costs
     */
    public void setCosts(Float costs) {
        this.costs = costs;
    }

    /**
     * Gets the transactions id.
     *
     * @return the transactions id
     */
    public Long getTransactionsId() {
        return transactionsId;
    }

    /**
     * Sets the transactions id.
     *
     * @param transactionsId the new transactions id
     */
    public void setTransactionsId(Long transactionsId) {
        this.transactionsId = transactionsId;
    }

    /**
     * Gets the channels id.
     *
     * @return the channels id
     */
    public Long getChannelsId() {
        return channelsId;
    }

    /**
     * Sets the channels id.
     *
     * @param channelId the new channels id
     */
    public void setChannelsId(Long channelId) {
        this.channelsId = channelId;
    }

    /**
     * Gets the factors id.
     *
     * @return the factors id
     */
    public Long getFactorsId() {
        return factorsId;
    }

    /**
     * Sets the factors id.
     *
     * @param factorId the new factors id
     */
    public void setFactorsId(Long factorId) {
        this.factorsId = factorId;
    }

    /**
     * Gets the styles id.
     *
     * @return the styles id
     */
    public Long getStylesId() {
        return stylesId;
    }

    /**
     * Sets the styles id.
     *
     * @param styleId the new styles id
     */
    public void setStylesId(Long styleId) {
        this.stylesId = styleId;
    }

    /**
     * Gets the types id.
     *
     * @return the types id
     */
    public Long getTypesId() {
        return typesId;
    }

    /**
     * Sets the types id.
     *
     * @param typesId the new types id
     */
    public void setTypesId(Long typesId) {
        this.typesId = typesId;
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

        TransactionDetailsDTO transactionDetailsDTO = (TransactionDetailsDTO) o;
        if(transactionDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDetailsDTO.getId());
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
        return "TransactionDetailsDTO{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", profit='" + getProfit() + "'" +
            ", costs='" + getCosts() + "'" +
            "}";
    }
}
