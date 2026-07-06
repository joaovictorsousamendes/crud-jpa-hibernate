package entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department implements EntityInterface {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;
    private String name;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "department")
    private Set<Project> projects;

    public Department(){
        employees = new HashSet<>();
        projects = new HashSet<>();
    }

    /**
     * Compare Department objects based on id or reference.
     * @param obj   the reference object with which to compare.
     * @return true if the objects are the same. false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;

        if(obj == null) return false;

        if(!(obj instanceof  Department)) return false;

        Department department = (Department) obj;

        return department.getId() != null && department.getId().equals(id);
    }

    /**
     * Returns a constant hash code for all instances of Department.
     * @return the hash code of the Department class.
     */
    @Override
    public int hashCode(){
        return getClass().hashCode();
    }

    // Setter manually written with standardization of string.
    public void setName(String name){
        this.name = standarizeString(name);
    }

    public void addEmployee(Employee employee){
        if(employee == null){
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        if(employee.getDepartment() != null && !employee.getDepartment().equals(this)){
            throw new IllegalArgumentException("Employee with id " + employee.getId() +
                    " is already on department with id " + employee.getDepartment().getId());
        }

        if(employees.add(employee)){
            employee.setDepartment(this);
        }
    }

    public void addProject(Project project){
        if(project == null) return;

        if(project.getDepartment() != null && !project.getDepartment().equals(this)){
            throw new IllegalArgumentException("Project with id " + project.getId() +
                    "is already on a department with id " + project.getDepartment().getId());
        }

        if(projects.add(project)) {
            project.setDepartment(this);
        }
    }

    public void addEmployees(Collection<Employee> employeeList){
        for(Employee e : employeeList){
            addEmployee(e);
        }
    }

    public void addProjects(ArrayList<Project> projectList){
        for(Project p : projectList){
            addProject(p);
        }
    }

    public boolean removeEmployee(Employee employee){
        if(employee == null) return false;

        if(employees.remove(employee)){
            employee.setDepartment(null);
            return true;
        }
        return false;
    }

    public boolean removeProject(Project project){
        if(project == null) return false;

        if(projects.remove(project)) {
            project.setDepartment(null);
            return true;
        }
        return false;
    }

    public void removeAllEmployees(){
        for(Employee e : employees){
            e.setDepartment(null);
        }
        employees.clear();
    }

    public void removeAllProjects(){
        for(Project p: projects){
            p.setDepartment(null);
        }
        projects.clear();
    }

}
