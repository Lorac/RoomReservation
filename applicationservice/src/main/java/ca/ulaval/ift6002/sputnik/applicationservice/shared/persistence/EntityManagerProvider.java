package ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence;

import javax.persistence.EntityManager;

public class EntityManagerProvider {

    private static ThreadLocal<EntityManager> localEntityManager = new ThreadLocal<>();

    public static void clearEntityManager() {
        localEntityManager.set(null);
    }

    public EntityManager getEntityManager() {
        return localEntityManager.get();
    }

    public static void setEntityManager(EntityManager entityManager) {
        localEntityManager.set(entityManager);
    }

}
