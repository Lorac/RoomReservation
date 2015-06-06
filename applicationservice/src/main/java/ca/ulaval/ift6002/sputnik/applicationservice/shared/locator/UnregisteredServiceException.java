package ca.ulaval.ift6002.sputnik.applicationservice.shared.locator;

class UnregisteredServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnregisteredServiceException(Class<?> service) {
        super("Cannot find service name '" + service.getCanonicalName() + "'. Did you register it?");
    }
}
