package logia.quanlyso.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TransactionDetails.
 *
 * @author Dai Mai
 */
@Entity
@Table(name = "transaction_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionDetails implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long				id;

	/** The amount. */
	@Column(name = "amount")
	private Float				amount;

	/** The profit. */
	@Column(name = "profit")
	private Float				profit;

	/** The costs. */
	@Column(name = "costs")
	private Float				costs;

	/** The transactions. */
	@ManyToOne
	private Transactions		transactions;

	/** The channels. */
	@ManyToOne
	private Channel				channels;

	/** The styles. */
	@ManyToOne
	private Style				styles;

	/** The types. */
	@ManyToOne
	private Types				types;

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
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public Float getAmount() {
		return this.amount;
	}

	/**
	 * Amount.
	 *
	 * @param amount the amount
	 * @return the transaction details
	 */
	public TransactionDetails amount(Float amount) {
		this.amount = amount;
		return this;
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
		return this.profit;
	}

	/**
	 * Profit.
	 *
	 * @param profit the profit
	 * @return the transaction details
	 */
	public TransactionDetails profit(Float profit) {
		this.profit = profit;
		return this;
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
		return this.costs;
	}

	/**
	 * Costs.
	 *
	 * @param costs the costs
	 * @return the transaction details
	 */
	public TransactionDetails costs(Float costs) {
		this.costs = costs;
		return this;
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
	 * Gets the transactions.
	 *
	 * @return the transactions
	 */
	public Transactions getTransactions() {
		return this.transactions;
	}

	/**
	 * Transactions.
	 *
	 * @param transactions the transactions
	 * @return the transaction details
	 */
	public TransactionDetails transactions(Transactions transactions) {
		this.setTransactions(transactions);
		return this;
	}

	/**
	 * Sets the transactions.
	 *
	 * @param transactions the new transactions
	 */
	public void setTransactions(Transactions transactions) {
		this.setTransactions(transactions, true);
	}

	/**
	 * Sets the transactions.
	 *
	 * @param transactions the transactions
	 * @param isSetToTarget the is set to target transaction
	 */
	void setTransactions(Transactions transactions, boolean isSetToTarget) {
		this.transactions = transactions;
		if (transactions != null && isSetToTarget) {
			transactions.addTransactionDetails(this, false);
		}
	}

	/**
	 * Gets the channels.
	 *
	 * @return the channels
	 */
	public Channel getChannels() {
		return this.channels;
	}

	/**
	 * Channels.
	 *
	 * @param channel the channel
	 * @return the transaction details
	 */
	public TransactionDetails channels(Channel channel) {
		this.setChannels(channel);
		return this;
	}

	/**
	 * Sets the channels.
	 *
	 * @param channel the new channels
	 */
	public void setChannels(Channel channel) {
		this.setChannels(channel, true);
	}

	/**
	 * Sets the channels.
	 *
	 * @param channel the channel
	 * @param isSetToTarget the is set to target channel
	 */
	void setChannels(Channel channel, boolean isSetToTarget) {
		this.channels = channel;
		if (channel != null && isSetToTarget) {
			channel.addTransactionDetails(this, false);
		}
	}

	/**
	 * Gets the styles.
	 *
	 * @return the styles
	 */
	public Style getStyles() {
		return this.styles;
	}

	/**
	 * Styles.
	 *
	 * @param style the style
	 * @return the transaction details
	 */
	public TransactionDetails styles(Style style) {
		this.setStyles(style);
		return this;
	}

	/**
	 * Sets the styles.
	 *
	 * @param style the new styles
	 */
	public void setStyles(Style style) {
		this.setStyles(style, true);
	}

	/**
	 * Sets the styles.
	 *
	 * @param style the style
	 * @param isSetToTarget the is set to target style
	 */
	void setStyles(Style style, boolean isSetToTarget) {
		this.styles = style;
		if (style != null && isSetToTarget) {
			style.addTransactionDetails(this, false);
		}
	}

	/**
	 * Gets the types.
	 *
	 * @return the types
	 */
	public Types getTypes() {
		return this.types;
	}

	/**
	 * Types.
	 *
	 * @param types the types
	 * @return the transaction details
	 */
	public TransactionDetails types(Types types) {
		this.setTypes(types);
		return this;
	}

	/**
	 * Sets the types.
	 *
	 * @param types the new types
	 */
	public void setTypes(Types types) {
		this.setTypes(types, true);
	}

	/**
	 * Sets the types.
	 *
	 * @param types the types
	 * @param isSetToTarget the is set to target type
	 */
	void setTypes(Types types, boolean isSetToTarget) {
		this.types = types;
		if (types != null && isSetToTarget) {
			types.addTransactionDetails(this, false);
		}
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
		TransactionDetails transactionDetails = (TransactionDetails) o;
		if (transactionDetails.getId() == null || this.getId() == null) {
			return false;
		}
		return Objects.equals(this.getId(), transactionDetails.getId());
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
		return "TransactionDetails{" + "id=" + this.getId() + ", amount='" + this.getAmount() + "'"
				+ ", profit='" + this.getProfit() + "'" + ", costs='" + this.getCosts() + "'" + "}";
	}
}
