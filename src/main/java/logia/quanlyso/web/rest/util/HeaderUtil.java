package logia.quanlyso.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 *
 * @author Dai Mai
 */
public final class HeaderUtil {

	/** The Constant log. */
	private static final Logger	log					= LoggerFactory.getLogger(HeaderUtil.class);

	/** The Constant APPLICATION_NAME. */
	private static final String	APPLICATION_NAME	= "quanlysoApp";

	/**
	 * Instantiates a new header util.
	 */
	private HeaderUtil() {
	}

	/**
	 * Creates the alert.
	 *
	 * @param message the message
	 * @param param the param
	 * @return the http headers
	 */
	public static HttpHeaders createAlert(String message, String param) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-quanlysoApp-alert", message);
		headers.add("X-quanlysoApp-params", param);
		return headers;
	}

	/**
	 * Creates the entity creation alert.
	 *
	 * @param entityName the entity name
	 * @param param the param
	 * @return the http headers
	 */
	public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
		return HeaderUtil.createAlert(HeaderUtil.APPLICATION_NAME + "." + entityName + ".created", param);
	}

	/**
	 * Creates the entity update alert.
	 *
	 * @param entityName the entity name
	 * @param param the param
	 * @return the http headers
	 */
	public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
		return HeaderUtil.createAlert(HeaderUtil.APPLICATION_NAME + "." + entityName + ".updated", param);
	}

	/**
	 * Creates the entity deletion alert.
	 *
	 * @param entityName the entity name
	 * @param param the param
	 * @return the http headers
	 */
	public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
		return HeaderUtil.createAlert(HeaderUtil.APPLICATION_NAME + "." + entityName + ".deleted", param);
	}

	/**
	 * Creates the failure alert.
	 *
	 * @param entityName the entity name
	 * @param errorKey the error key
	 * @param defaultMessage the default message
	 * @return the http headers
	 */
	public static HttpHeaders createFailureAlert(String entityName, String errorKey,
			String defaultMessage) {
		HeaderUtil.log.error("Entity processing failed, {}", defaultMessage);
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-quanlysoApp-error", "error." + errorKey);
		headers.add("X-quanlysoApp-params", entityName);
		return headers;
	}
}
