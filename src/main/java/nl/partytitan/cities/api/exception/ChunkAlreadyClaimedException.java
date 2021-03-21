package nl.partytitan.cities.api.exception;

import javax.annotation.Nonnull;

public class ChunkAlreadyClaimedException extends BaseException {
    public ChunkAlreadyClaimedException(@Nonnull String message) {
        super(message);
    }
}
