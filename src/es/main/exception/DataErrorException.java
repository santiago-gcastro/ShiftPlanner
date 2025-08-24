/**
 * 
 */
package es.main.exception;

/**
 * 
 */
public class DataErrorException extends RuntimeException {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 2802447974231930094L;
	
    /** Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public DataErrorException(String message) {
        super("DATA ERROR - " + message);
    }
}
