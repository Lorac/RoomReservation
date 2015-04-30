package ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider {
    private static EntityManagerFactory instance;

    private EntityManagerFactoryProvider() {
    }

    public static EntityManagerFactory getFactory() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("persistence");
        }
        return instance;
    }

}
