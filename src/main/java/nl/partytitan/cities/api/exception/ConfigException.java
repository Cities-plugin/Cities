package nl.partytitan.cities.api.exception;

import javax.annotation.Nonnull;

/**
 * Exception that is thrown by configuration problems
 */
public class ConfigException extends BaseException {
    /**
     * @param message The reason for the exception
     * @param e       The root exception
     */
    public ConfigException(@Nonnull String message, @Nonnull Exception e) {
        super(message, e);
    }
}
