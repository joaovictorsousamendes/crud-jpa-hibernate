package service;

import util.JPAUtil;
import entity.Department;
import repository.DepartmentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class DepartmentService implements ServiceInterface<Department>{
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

            if(department == null){
                throw new EntityNotFoundException("Department with id " + id + " not found.");
            }
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

            repository.findByIdComplete(id).ifPresent(
                    department -> {
                        // Removing dependencies
                        department.removeAllEmployees();
                        department.removeAllProjects();
                        // Deleting entity
                        entityManager.remove(department);
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
     * returns a Department with its employee collection loaded.
     * @param id: the department id.
     * @return the Department with the corresponding id with its employee collection loaded.
     */
    public Department getByIdWithEmployees(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new DepartmentRepository(entityManager).findByIdWithEmployees(id)
                    .orElseThrow(() -> new EntityNotFoundException("Department with id " + id +
                            " not found."));
        }
    }

    /**
     * returns a Department with its project collection loaded.
     * @param id: the department id.
     * @return the Department with the corresponding id with its project collection loaded.
     */
    public Department getByIdWithProjects(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new DepartmentRepository(entityManager).findByIdWithProjects(id)
                    .orElseThrow(() -> new EntityNotFoundException("Department with id " + id +
                            " not found."));
        }
    }

    /**
     * returns a Department with all its collections loaded.
     * @param id: the department id.
     * @return the Department with the corresponding id with its employee and project collection loaded.
     */
    public Department getByIdComplete(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new DepartmentRepository(entityManager).findByIdComplete(id)
                    .orElseThrow(() -> new EntityNotFoundException("Department with id " + id +
                            " not found."));
        }
    }


    public Department getByName(String departmentName){
        validateStringAttribute(departmentName, "department name");

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            DepartmentRepository repository = new DepartmentRepository(entityManager);

            return repository.findByName(standarizeString(departmentName))
                    .orElseThrow(() -> new EntityNotFoundException("Department '" + departmentName + " not found."));
        }
    }
}
