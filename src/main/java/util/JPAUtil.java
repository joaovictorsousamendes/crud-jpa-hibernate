package util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public final class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("my-pu");

    private JPAUtil(){
        throw new UnsupportedOperationException("This class should not be instantiated.");
    }

    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
