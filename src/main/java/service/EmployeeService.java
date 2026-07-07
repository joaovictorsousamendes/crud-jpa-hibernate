package service;

import util.JPAUtil;
import repository.*;
import entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService implements ServiceInterface<Employee>{

    private void validateEmail(String email){
       if(email == null || email.isBlank() || !email.contains("@")){
           throw new IllegalArgumentException("Email is not valid.");
       }
    }

    private void validateSalary(BigDecimal salary){
        if(salary == null || salary.compareTo(BigDecimal.valueOf(0)) < 1){
            throw new IllegalArgumentException("Salary not valid.");
        }
    }

    private void validateSalaryRange(BigDecimal minSalary, BigDecimal maxSalary){
        if(minSalary.compareTo(maxSalary) > 0){
            throw new IllegalArgumentException("Minimum salary must be smaller than maximum salary.");
        }
    }

    @Override
    public void validateEntity(Employee employee){
        if(employee == null) throw new EntityNotFoundException("Employee not found.");
        validateStringAttribute(employee.getFirstName(), "employee first name");
        validateStringAttribute(employee.getLastName(), "employee last name");
        validateStringAttribute(employee.getPassword(), "employee password");
        validateEmail(employee.getEmail());
        validateSalary(employee.getSalary());
    }

    @Override
    public void save(Employee employee){
        validateEntity(employee);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            entityManager.persist(employee);

            entityManager.getTransaction().commit();
        } catch (Exception e){
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Employee employee){
        validateEntity(employee);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.merge(employee);

            entityManager.getTransaction().commit();
        } catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Employee getById(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            Employee employee = entityManager.find(Employee.class, id);

            if(employee == null){
                throw new EntityNotFoundException("Employee with id " + id + " not found.");
            }
            return employee;
        }
    }

    @Override
    public List<Employee> getAll(){
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findAll().stream().toList();
        }
    }

    @Override
    public List<Employee> getAll(int startIndex, int maxResults){
        if (startIndex < 0 || maxResults < 1) return new ArrayList<>();

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new EmployeeRepository(entityManager).findAll(startIndex, maxResults).stream().toList();
        }
    }

    @Override
    public void delete(long id){
        validateId(id);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            EmployeeRepository repository = new EmployeeRepository(entityManager);

            repository.findByIdWithProjects(id).ifPresent(
                    employee -> {
                        // removing employee from department.
                        if(employee.getDepartment() != null){
                            employee.getDepartment().removeEmployee(employee);
                        }
                        // removing dependencies.
                        employee.removeAllProjects();
                        // deleting entity.
                        entityManager.remove(employee);
                    }
            );

            entityManager.getTransaction().commit();
        } catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Returns an Employee with its project collection loaded.
     * @param id: employee id.
     * @return the Employee with corresponding id with its project collection loaded.
     */
    public Employee getByIdWithProjects(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findByIdWithProjects(id)
                    .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found."));
        }
    }

    /**
     * Assign an employee to a project.
     * @param employeeId: id of the employee.
     * @param projectId: id of the project.
     * @return true if the employee has been assigned to the project; false otherwise.
     */
    public boolean assignEmployeeToProject(long employeeId, long projectId){
        validateId(employeeId);
        validateId(projectId);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Employee employee = entityManager.find(Employee.class, employeeId);
            Project project = entityManager.find(Project.class, projectId);

            if(project == null) throw new EntityNotFoundException("Project with id " +
                    projectId + " not found.");

            if(employee == null) throw new EntityNotFoundException("Employee with id " +
                    employeeId + " not found.");

            boolean operation = employee.addProject(project);
            entityManager.merge(employee);
            entityManager.merge(project);

            entityManager.getTransaction().commit();
            return operation;
        } catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Removes an employee of a project.
     * @param employeeId: id of the employee.
     * @param projectId: id of the project.
     * @return true if the remotion was successful; false otherwise.
     */
    public boolean removeEmployeeFromProject(long employeeId, long projectId){
        validateId(employeeId);
        validateId(projectId);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            Employee employee = entityManager.find(Employee.class, employeeId);
            Project project = entityManager.find(Project.class, projectId);

            if(project == null) throw new EntityNotFoundException("Project with id " +
                    projectId + " not found.");

            if(employee == null) throw new EntityNotFoundException("Employee with id + " +
                    employeeId + " not found.");

            boolean operation = employee.removeProject(project);
            entityManager.merge(employee);
            entityManager.merge(project);

            entityManager.getTransaction().commit();
            return operation;
        } catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }


    public List<Employee> getByFirstName(String firstName){
        validateStringAttribute(firstName, "first name");

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return  new EmployeeRepository(entityManager).findByFirstName(standarizeString(firstName))
                    .stream().toList();
        }
    }

    public List<Employee> getByLastName(String lastName){
        validateStringAttribute(lastName, "last name");

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return  new EmployeeRepository(entityManager).findByLastName(standarizeString(lastName))
                    .stream().toList();
        }
    }

    public List<Employee> getByFullName(String firstName, String lastName){
        boolean firstIsValid = firstName != null && !firstName.isBlank();
        boolean lastIsValid = lastName != null && !lastName.isBlank();

        if(!firstIsValid && !lastIsValid){
            throw new IllegalArgumentException("first and last name are invalid.");
        }

        if(!firstIsValid){
            return getByLastName(lastName);
        }

        if(!lastIsValid){
            return getByFirstName(firstName);
        }

        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            return  new EmployeeRepository(entityManager).findByFullName(
                    standarizeString(firstName), standarizeString(lastName))
                    .stream().toList();
        }
    }

    public List<Employee> getBySalaryLessThenEqual(BigDecimal salary){
        validateSalary(salary);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findBySalaryLessThanEqual(salary).stream().toList();
        }
    }

    public List<Employee> getBySalaryMoreThenEqual(BigDecimal salary){
        validateSalary(salary);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return  new EmployeeRepository(entityManager).findBySalaryMoreThanEqual(salary).stream().toList();
        }
    }

    public List<Employee> getBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary){
        validateSalary(maxSalary);
        validateSalary(minSalary);
        validateSalaryRange(minSalary, maxSalary);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findBySalaryBetween(minSalary, maxSalary).stream().toList();
        }
    }

    public Employee getMaxSalaryEmployee(){
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findMaxSalaryEmployee()
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        }
    }

    public Employee getMinSalaryEmployee(){
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new EmployeeRepository(entityManager).findMinSalaryEmployee()
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        }
    }

    public List<Employee> getByDepartmentAndSalaryBetween(long departmentId, BigDecimal minSalary, BigDecimal maxSalary){
        validateId(departmentId);
        validateSalary(minSalary);
        validateSalary(maxSalary);
        validateSalaryRange(minSalary, maxSalary);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            EmployeeRepository repository = new EmployeeRepository(entityManager);
            Department department = entityManager.find(Department.class, departmentId);
            if(department == null){
                throw new IllegalArgumentException("Department with id " + departmentId + " does not exist.");
            }
            return repository.findByDepartmentIdAndSalaryBetween(departmentId, minSalary, maxSalary).stream().toList();
        }
    }

    /**
     * Moves an employee to a different department
     * @param employeeId: employee id.
     * @param newDepartmentId: id of the new department.
     */
    public void changeDepartment(long employeeId, long newDepartmentId){
        validateId(employeeId);
        validateId(newDepartmentId);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            Employee employee = entityManager.find(Employee.class, employeeId);
            Department newDepartment = entityManager.find(Department.class, newDepartmentId);

            if(employee == null) {
                throw new IllegalArgumentException("Employee with id " + employeeId + " does not exist.");
            }
            if(newDepartment == null) {
                throw new IllegalArgumentException(("Department with id " + newDepartmentId + " does not exist."));
            }

            // Employee has no current department.
            if(employee.getDepartment() == null){
                newDepartment.addEmployee(employee);
            }
            else if(!employee.getDepartment().equals(newDepartment)){
                // removing employee from all its current projects
                employee.removeAllProjects();
                // removing employee from current department
                employee.getDepartment().removeEmployee(employee);
                // Assigning employee to new department
                newDepartment.addEmployee(employee);
            }
            entityManager.merge(employee);

            entityManager.getTransaction().commit();

        } catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw e;
        }finally {
            entityManager.close();
        }
    }

}
