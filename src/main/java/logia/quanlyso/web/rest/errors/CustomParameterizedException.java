package logia.quanlyso.web.rest.errors;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom, parameterized exception, which can be translated on the client side.
 * For example:
 * 
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 * 
 * Can be translated with:
 * 
 * <pre>
 * "error.myCustomError" :  "The server says {{param0}} to {{param1}}"
 * </pre>
 *
 * @author Dai Mai
 */
public class CustomParameterizedException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant PARAM. */
    private static final String PARAM = "param";

    /** The message. */
    private final String message;

    /** The param map. */
    private final Map<String, String> paramMap = new HashMap<>();

    /**
     * Instantiates a new custom parameterized exception.
     *
     * @param message the message
     * @param params the params
     */
    public CustomParameterizedException(String message, String... params) {
        super(message);
        this.message = message;
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                paramMap.put(PARAM + i, params[i]);
            }
        }
    }

    /**
     * Instantiates a new custom parameterized exception.
     *
     * @param message the message
     * @param paramMap the param map
     */
    public CustomParameterizedException(String message, Map<String, String> paramMap) {
        super(message);
        this.message = message;
        this.paramMap.putAll(paramMap);
    }

    /**
     * Gets the error VM.
     *
     * @return the error VM
     */
    public ParameterizedErrorVM getErrorVM() {
        return new ParameterizedErrorVM(message, paramMap);
    }
}
