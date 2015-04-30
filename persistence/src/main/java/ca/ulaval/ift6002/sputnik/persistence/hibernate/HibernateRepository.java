package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;

import javax.persistence.EntityManager;

public abstract class HibernateRepository {

    protected EntityManager entityManager = null;

    public HibernateRepository() {
        entityManager = new EntityManagerProvider().getEntityManager();
    }

    public HibernateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
