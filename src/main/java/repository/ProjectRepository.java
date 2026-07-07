package repository;

import entity.Department;
import entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


public class ProjectRepository extends JpaRepository<Project> {
    public ProjectRepository(EntityManager entityManager){
        super(Project.class, entityManager);
    }

    /**
     * returns a Projects entity with its employees collection loaded.
     * @param id project id.
     * @return the Project with the Employee collection loaded.
     */
    public Optional<Project> findByIdWithEmployees(long id){
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.employees " +
                        "WHERE p.id = :id", Project.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

    public Collection<Project> findByDepartmentId(long departmentId){
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p WHERE p.department.id = :departmentId", Project.class
        );
        query.setParameter("departmentId", departmentId);
        return query.getResultList();
    }

    public Collection<Project> findByEmployeeId(long employeeId){
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p JOIN p.employees e WHERE " +
                   "e.id = :employeeId", Project.class
        );
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }


    public Collection<Project> findByName(String name){
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p WHERE p.name LIKE :name", Project.class
        );
        query.setParameter("name", "%"+name+"%");
        return query.getResultList();
    }

    public Collection<Project> findByStartDate(LocalDate startDate){
        TypedQuery<Project> query = entityManager.createQuery(
          "SELECT p FROM Project p WHERE p.startDate = :startDate", Project.class
        );
        query.setParameter("startDate", startDate);
        return query.getResultList();
    }

    public Collection<Project> findByFinishDate(LocalDate finishDate){
        TypedQuery<Project> query = entityManager.createQuery(
                "SELECT p FROM Project p WHERE p.finishDate = :finishDate", Project.class
        );
        query.setParameter("finishDate", finishDate);
        return query.getResultList();
    }

}
