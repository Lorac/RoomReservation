package ca.ulaval.ift6002.sputnik.applicationservice.shared.locator;

class DoubleServiceRegistrationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DoubleServiceRegistrationException(Class<?> service) {
        super("A implementation for the service '" + service.getCanonicalName() + "' is already present.");
    }
}
