package service;

import entity.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import repository.DepartmentRepository;
import util.JPAUtil;

import java.util.List;
import java.util.Optional;

public class DepartmentService implements ServiceInterface<Department>{

    private void validateId(long id){
        if(id < 1) throw new IllegalArgumentException("ID " + id + " is not valid");
    }

    private void validateStringAttribute(String str, String attributeName){
        if(str == null || str.isBlank()){
            throw new IllegalArgumentException(attributeName + " is blank or null.");
        }
    }

    @Override
    public void validateEntity(Department department){
        if(department == null){
            throw new EntityNotFoundException("Department not found.");
        }
        validateStringAttribute(department.getName(), "department name");
    }

    @Override
    public void save(Department department){
        validateEntity(department);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            entityManager.persist(department);

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
    public void update(Department department){
        validateEntity(department);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            entityManager.merge(department);

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
    public Department getById(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            Department department = entityManager.find(Department.class, id);

            validateEntity(department);
            return department;
        }
    }

    @Override
    public List<Department> getAll(){
        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new DepartmentRepository(entityManager).findAll().stream().toList();
        }
    }

    @Override
    public List<Department> getAll(int startIndex, int maxResults){
        if(startIndex < 0 || maxResults < 1) throw new IllegalArgumentException("Invalid argument");

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new DepartmentRepository(entityManager).findAll(startIndex, maxResults).stream().toList();
        }
    }

    @Override
    public void delete(long id){
        validateId(id);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            DepartmentRepository repository = new DepartmentRepository(entityManager);
            Department department = entityManager.find(Department.class, id);

            if(department == null) return;

            // Loading dependencies
            repository.initializeProjects(department);
            repository.initializeEmployees(department);

            // Removing dependencies
            department.removeAllEmployees();
            department.removeAllProjects();

            entityManager.remove(department);

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


    public Department getByIdWithEmployees(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            DepartmentRepository repository = new DepartmentRepository(entityManager);
            Department department = entityManager.find(Department.class, id);

            validateEntity(department);
            repository.initializeEmployees(department);
            return department;
        }
    }

    public Department getByIdWithProjects(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            DepartmentRepository repository = new DepartmentRepository(entityManager);
            Department department = entityManager.find(Department.class ,id);

            validateEntity(department);
            repository.initializeProjects(department);
            return department;
        }
    }

    public Department getByIdComplete(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            DepartmentRepository repository = new DepartmentRepository(entityManager);
            Department department = entityManager.find(Department.class ,id);

            validateEntity(department);
            repository.initializeEmployees(department);
            repository.initializeProjects(department);
            return department;
        }
    }

    public Department getByName(String departmentName){
        validateStringAttribute(departmentName, "department name");

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            DepartmentRepository repository = new DepartmentRepository(entityManager);

            return repository.findByName(standarizeString(departmentName))
                    .orElseThrow(() -> new EntityNotFoundException("Department not found."));
        }
    }


}
