/*
 * 
 */
package logia.quanlyso.web.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for transferring error message with a list of field errors.
 *
 * @author Dai Mai
 */
public class ErrorVM implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The message. */
	private final String message;

	/** The description. */
	private final String description;

	/** The field errors. */
	private List<FieldErrorVM> fieldErrors;

	/**
	 * Instantiates a new error VM.
	 *
	 * @param message
	 *            the message
	 */
	public ErrorVM(String message) {
		this(message, null);
	}

	/**
	 * Instantiates a new error VM.
	 *
	 * @param message
	 *            the message
	 * @param description
	 *            the description
	 */
	public ErrorVM(String message, String description) {
		this.message = message;
		this.description = description;
	}

	/**
	 * Instantiates a new error VM.
	 *
	 * @param message
	 *            the message
	 * @param description
	 *            the description
	 * @param fieldErrors
	 *            the field errors
	 */
	public ErrorVM(String message, String description, List<FieldErrorVM> fieldErrors) {
		this.message = message;
		this.description = description;
		this.fieldErrors = fieldErrors;
	}

	/**
	 * Adds the.
	 *
	 * @param objectName
	 *            the object name
	 * @param field
	 *            the field
	 * @param message
	 *            the message
	 */
	public void add(String objectName, String field, String message) {
		if (this.fieldErrors == null) {
			this.fieldErrors = new ArrayList<>();
		}
		this.fieldErrors.add(new FieldErrorVM(objectName, field, message));
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the field errors.
	 *
	 * @return the field errors
	 */
	public List<FieldErrorVM> getFieldErrors() {
		return this.fieldErrors;
	}
}
