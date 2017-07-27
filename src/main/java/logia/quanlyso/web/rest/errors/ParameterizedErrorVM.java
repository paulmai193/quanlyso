package logia.quanlyso.web.rest.errors;

import java.io.Serializable;
import java.util.Map;

/**
 * View Model for sending a parameterized error message.
 *
 * @author Dai Mai
 */
public class ParameterizedErrorVM implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long			serialVersionUID	= 1L;

	/** The message. */
	private final String				message;

	/** The param map. */
	private final Map<String, String>	paramMap;

	/**
	 * Instantiates a new parameterized error VM.
	 *
	 * @param message the message
	 * @param paramMap the param map
	 */
	public ParameterizedErrorVM(String message, Map<String, String> paramMap) {
		this.message = message;
		this.paramMap = paramMap;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Gets the params.
	 *
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return this.paramMap;
	}

}
