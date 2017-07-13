package logia.quanlyso.listener;

import org.springframework.stereotype.Component;

/**
 * The listener interface for receiving processing events.
 * The class that is interested in processing a processing
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addProcessingListener<code> method. When
 * the processing event occurs, that object's appropriate
 * method is invoked.
 *
 * @author Dai Mai
 * @see ProcessingEvent
 */
@Component
public class ProcessingListener {
	
	/** The processing. */
	private int processing = 0;
	
	/** The total. */
	private int total;

	/**
	 * Gets the processing.
	 *
	 * @return the processing
	 */
	public int getProcessing() {
		return processing;
	}

	/**
	 * Next processing.
	 */
	public void nextProcessing() {
		this.processing++;
	}
	
	/**
	 * Reset processing.
	 */
	public void resetProcessing() {
		this.processing = 0;
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
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}	
	
}
