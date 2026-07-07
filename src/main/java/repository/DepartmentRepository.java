package repository;

import entity.Department;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.Optional;

public class DepartmentRepository extends JpaRepository<Department> {
    public DepartmentRepository(EntityManager entityManager){
        super(Department.class, entityManager);
    }

    /**
     * returns a Department entity with its employees collection loaded.
     * @param id department id.
     * @return the Department with the Employee collection loaded.
     */
    public Optional<Department> findByIdWithEmployees(long id){
        TypedQuery<Department> query = entityManager.createQuery(
                "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees " +
                        "WHERE d.id = :id", Department.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    /**
     * returns a Department entity with its projects collection loaded.
     * @param id department id.
     * @return the Department with the Project collection loaded.
     */
    public Optional<Department> findByIdWithProjects(long id){
        TypedQuery<Department> query = entityManager.createQuery(
                "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.projects " +
                        "WHERE d.id = :id", Department.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    /**
     * returns a Department entity with all its collections loaded.
     * @param id department id.
     * @return the Department with all its collections loaded.
     */
    public Optional<Department> findByIdComplete(long id){
        TypedQuery<Department> query = entityManager.createQuery(
                "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees " +
                        "LEFT JOIN FETCH d.projects WHERE d.id = :id", Department.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }


    public Optional<Department> findByName(String name){
        TypedQuery<Department> query = entityManager.createQuery(
          "SELECT d FROM Department d WHERE d.name LIKE :name", Department.class
        );
        query.setParameter("name", "%"+name+"%");
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

}
