package nl.partytitan.cities.api.exception;

import javax.annotation.Nonnull;

/**
 * A Base Exception class
 */
public class BaseException extends RuntimeException {


    /**
     * @param message The reason for the exception
     */
    public BaseException(@Nonnull String message) {
        super(message);
    }

    /**
     * @param message The reason for the exception
     * @param e       The root exception
     */
    public BaseException(@Nonnull String message, @Nonnull Exception e) {
        super(message, e);
    }
}
