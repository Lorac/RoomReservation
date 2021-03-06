package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;

import javax.persistence.EntityManager;

abstract class HibernateRepository<T> {

    EntityManager entityManager = null;

    HibernateRepository() {
        entityManager = new EntityManagerProvider().getEntityManager();
    }

    HibernateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract void update(T t);
}
