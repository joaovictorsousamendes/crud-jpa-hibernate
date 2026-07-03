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
     * Initialize the employees list of the specified department.
     * @param department
     */
    public void initializeEmployees(Department department){
        Objects.requireNonNull(department, "Department object is null.");
        Hibernate.initialize(department.getEmployees());
    }


    /**
     * Initialize the projects list of the specified department.
     * @param department
     */
    public void initializeProjects(Department department){
        Objects.requireNonNull(department, "Department object is null.");
        Hibernate.initialize(department.getProjects());
    }

    public Optional<Department> findByName(String name){
        TypedQuery<Department> query = entityManager.createQuery(
          "SELECT d FROM Department d WHERE d.name LIKE :name", Department.class
        );
        query.setParameter("name", "%"+name+"%");
        return Optional.ofNullable(query.getSingleResultOrNull());
    }

}
