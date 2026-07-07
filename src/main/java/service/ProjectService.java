package service;

import jakarta.persistence.EntityNotFoundException;
import util.JPAUtil;

import entity.Department;
import entity.Employee;
import entity.Project;
import repository.ProjectRepository;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class ProjectService implements ServiceInterface<Project>{

    private void validateDates(LocalDate startDate, LocalDate finishDate){
        if(startDate == null) throw new IllegalArgumentException("Start date can not be null.");

        if(finishDate != null && finishDate.isBefore(startDate)){
            throw new IllegalArgumentException("Finish date must be equal or after start date.");
        }
    }

    @Override
    public void validateEntity(Project project){
        if(project == null) throw new IllegalArgumentException("Project not found.");

        // Project must be assigned to a department
        if(project.getDepartment() == null) throw new IllegalStateException(
                "Project is not assigned to a department.");

        validateStringAttribute(project.getName(), "project name");
        validateDates(project.getStartDate(), project.getFinishDate());

    }

    @Override
    public void save(Project project){
        validateEntity(project);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();

            entityManager.persist(project);

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
    public void update(Project project){
        validateEntity(project);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            entityManager.merge(project);

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
    public Project getById(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            Project project = entityManager.find(Project.class, id);

            if(project == null){
                throw new EntityNotFoundException("Project with id " + id + " not found.");
            }
            return project;
        }
    }

    @Override
    public List<Project> getAll(){
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new ProjectRepository(entityManager).findAll().stream().toList();
        }
    }

    @Override
    public List<Project> getAll(int startIndex, int maxResults){
        if(startIndex < 0 || maxResults < 1) throw new IllegalArgumentException("Invalid argument");

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new ProjectRepository(entityManager).findAll(startIndex, maxResults).stream().toList();
        }
    }

    @Override
    public void delete(long id){
        validateId(id);

        EntityManager entityManager = JPAUtil.getEntityManager();
        try{
            entityManager.getTransaction().begin();

            ProjectRepository repository = new ProjectRepository(entityManager);
            Project project = entityManager.find(Project.class, id);

            if(project == null) return;

            // Removing dependencies.
            Department department = project.getDepartment();

            project.removeAllEmployees();
            if(department != null) department.removeProject(project);
            // Deleting entity.
            entityManager.remove(project);

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
     * returns a Project with its employee collection loaded.
     * @param id: project id.
     * @return the Project with the corresponding id with its employee collection loaded.
     */
    public Project getByIdWithEmployees(long id){
        validateId(id);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new ProjectRepository(entityManager).findByIdWithEmployees(id)
                    .orElseThrow(() -> new EntityNotFoundException("Project with id " + id + " not found."));
        }
    }

    public List<Project> getByDepartmentId(long departmentId){
        validateId(departmentId);

        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            ProjectRepository repository = new ProjectRepository(entityManager);
            Department department = entityManager.find(Department.class, departmentId);

            if(department == null) {
                throw new IllegalArgumentException("Department with id " + departmentId + " not found.");
            }
            return repository.findByDepartmentId(departmentId).stream().toList();
        }
    }

    public List<Project> getByEmployeeId(long employeeId){
        validateId(employeeId);

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            ProjectRepository repository = new ProjectRepository(entityManager);
            Employee employee = entityManager.find(Employee.class, employeeId);

            if(employee == null){
            throw new IllegalArgumentException("Employee with id: " + employeeId + " does not exist.");
            }
            return repository.findByEmployeeId(employeeId).stream().toList();
        }
    }

    public List<Project> getByName(String name){
        validateStringAttribute(name, "project name");

        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new ProjectRepository(entityManager).findByName(standarizeString(name))
                    .stream().toList();
        }
    }

    public List<Project> getByStartDate(LocalDate date){
        try(EntityManager entityManager = JPAUtil.getEntityManager()){
            return new ProjectRepository(entityManager).findByStartDate(date).stream().toList();
        }
    }

    public List<Project> getByFinishDate(LocalDate date){
        try(EntityManager entityManager = JPAUtil.getEntityManager()) {
            return new ProjectRepository(entityManager).findByFinishDate(date).stream().toList();
        }
    }

}
