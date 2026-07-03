package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Collection;


public abstract class JpaRepository<T> {
    private final Class<T> entityClass;
    protected final EntityManager entityManager;

    protected JpaRepository(Class<T> entityClass, EntityManager entityManager){
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    public Collection<T> findAll(){
        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return entityManager.createQuery(query, entityClass).getResultList();
    }

    public Collection<T> findAll(int startIndex, int maxResults){
        TypedQuery<T> query = entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass
        );
        query.setFirstResult(startIndex);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

}
