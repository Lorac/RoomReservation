package ca.ulaval.ift6002.sputnik.applicationservice.shared.locator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceLocator {

    private static final ReentrantLock lock = new ReentrantLock();
    private static ServiceLocator instance;
    private Map<Class<?>, Object> services;

    private ServiceLocator() {
        services = new HashMap<>();
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ServiceLocator();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public static void reset() {
        lock.lock();
        try {
            instance = null;
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> service) {
        if (!services.containsKey(service)) {
            throw new UnregisteredServiceException(service);
        }

        return (T) services.get(service);

    }

    public <T> void register(Class<T> service, T implementation) {
        if (services.containsKey(service)) {
            throw new DoubleServiceRegistrationException(service);
        }
        services.put(service, implementation);
    }

    public void unregister(Class<?> service) {
        services.remove(service);
    }
}
