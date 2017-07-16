package logia.quanlyso.service.dto;

import java.io.Serializable;

/**
 * The Class ProcessingDTO.
 * 
 * @author Dai Mai
 */
public class ProcessingDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long	serialVersionUID	= 1L;

	/** The processing. */
	private int	processing	= 0;

	/** The total. */
	private int	total		= 0;

	/**
	 * Instantiates a new processing DTO.
	 *
	 * @param processing the processing
	 * @param total the total
	 */
	public ProcessingDTO(int processing, int total) {
		super();
		this.processing = processing;
		this.total = total;
	}

	/**
	 * Gets the processing.
	 *
	 * @return the processing
	 */
	public int getProcessing() {
		return processing;
	}

	/**
	 * Sets the processing.
	 *
	 * @param processing the new processing
	 */
	public void setProcessing(int processing) {
		this.processing = processing;
	}

	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Sets the total.
	 *
	 * @param total the new total
	 */
	public void setTotal(int total) {
		this.total = total;
	}

}
