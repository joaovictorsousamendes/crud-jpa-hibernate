package repository;

import entity.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public class EmployeeRepository extends JpaRepository<Employee>{

    public EmployeeRepository(EntityManager entityManager){
        super(Employee.class, entityManager);
    }

    /**
     * returns an Employee entity with its projects collection loaded.
     * @param id employee id.
     * @return the Employee with the Project collection loaded.
     */
    public Optional<Employee> findByIdWithProjects(long id){
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.projects " +
                        "WHERE e.id = :id", Employee.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResultOrNull());
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

    /**
     * returns all the employees with the corresponding department id and with a salary between the specified.
     * @param idDepartment department id.
     * @param minSalary minimum salary range.
     * @param maxSalary maximum salary range.
     * @return a collection of employees with the corresponding department id and with a salary between the specified.
     */
    public Collection<Employee> findByDepartmentIdAndSalaryBetween(long idDepartment, BigDecimal minSalary, BigDecimal maxSalary){
        TypedQuery<Employee> query = entityManager.createQuery(
            "SELECT e FROM Employee e WHERE e.department.id = :idDepartment AND " +
               "(e.salary BETWEEN :minSalary AND :maxSalary)", Employee.class
        );
        query.setParameter("idDepartment", idDepartment);
        query.setParameter("minSalary", minSalary);
        query.setParameter("maxSalary", maxSalary);
        return query.getResultList();
    }

    public Optional<Employee> findMaxSalaryEmployee(){
        return Optional.ofNullable(entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary = " +
                   "(SELECT MAX(e2.salary) FROM Employee e2)", Employee.class)
                .getSingleResultOrNull());
    }

    public Optional<Employee> findMinSalaryEmployee(){
        return Optional.ofNullable(entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.salary = " +
                   "(SELECT MIN(e2.salary) FROM Employee e2)", Employee.class
        ).getSingleResultOrNull());
    }
}
