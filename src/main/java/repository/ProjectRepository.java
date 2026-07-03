package repository;

import entity.Department;
import entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;


public class ProjectRepository extends JpaRepository<Project> {
    public ProjectRepository(EntityManager entityManager){
        super(Project.class, entityManager);
    }

    /**
     * Initialize the employees list of the specified project.
     *
     */
    public void initializeEmployees(Project project){
        Objects.requireNonNull(project, "Department object is null.");
        Hibernate.initialize(project.getEmployees());
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
