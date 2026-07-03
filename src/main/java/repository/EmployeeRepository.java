package repository;

import entity.Department;
import entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class EmployeeRepository extends JpaRepository<Employee>{

    public EmployeeRepository(EntityManager entityManager){
        super(Employee.class, entityManager);
    }

    /**
     * Initialize the projects list of the specified employee.
     */
    public void initializeProjects(Employee employee){
        Objects.requireNonNull(employee, "Employee can not be null.");
        Hibernate.initialize(employee.getProjects());
    }

    public Collection<Employee> findByFirstName(String firstName){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.firstName LIKE :firstName", Employee.class
        );
        query.setParameter("firstName", "%"+firstName+"%");
        return query.getResultList();
    }

    public Collection<Employee> findByLastName(String lastName){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.lastName LIKE :lastName", Employee.class
        );
        query.setParameter("lastName", "%"+lastName+"%");
        return query.getResultList();
    }

    public Collection<Employee> findByFullName(String firstName, String lastName){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.firstName LIKE :firstName AND e.lastName LIKE :lastName",
                Employee.class
        );
        query.setParameter("firstName", "%"+firstName+"%");
        query.setParameter("lastName", "%"+lastName+"%");
        return query.getResultList();
    }

    public Collection<Employee> findBySalaryBetween(BigDecimal min, BigDecimal max){
        TypedQuery<Employee> query =  entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max", Employee.class
        );
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }

    public Collection<Employee> findBySalaryLessThanEqual(BigDecimal maxSalary){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary <= :maxSalary", Employee.class
        );
        query.setParameter("maxSalary", maxSalary);
        return query.getResultList();
    }

    public Collection<Employee> findBySalaryMoreThanEqual(BigDecimal minSalary){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary >= :minSalary", Employee.class
        );
        query.setParameter("minSalary", minSalary);
        return query.getResultList();
    }

    public Collection<Employee> findByDepartmentIdAndSalary(long idDepartment, BigDecimal minSalary, BigDecimal maxSalary){
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e WHERE e.department.id = :idDepartment AND " +
               "(e.salary BETWEEN :minSalary AND :maxSalary)", Employee.class
        );
        query.setParameter("idDepartment", idDepartment);
        query.setParameter("minSalary", minSalary);
        query.setParameter("maxSalary", maxSalary);
        return query.getResultList();
    }

    public Optional<Employee> findMaxSalary(){
        return Optional.ofNullable(entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary = " +
                   "(SELECT MAX(e2.salary) FROM Employee e2)", Employee.class)
                .getSingleResultOrNull());
    }

    public Optional<Employee> findMinSalary(){
        return Optional.ofNullable(entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary = " +
                   "(SELECT MIN(e2.salary) FROM Employee e2)", Employee.class
        ).getSingleResultOrNull());
    }
}
