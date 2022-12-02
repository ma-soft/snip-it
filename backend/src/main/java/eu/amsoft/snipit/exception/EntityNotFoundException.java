package eu.amsoft.snipit.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(final Class<?> klass, final String id) {
        super(format("%s {id: %s} not found.", klass.getName(), id));
    }
}
