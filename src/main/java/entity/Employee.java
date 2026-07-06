package entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee implements EntityInterface {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    @Column(nullable = false, precision = 12, scale = 2) // max 12 digits, 2 decimal digits.
    private BigDecimal salary;

    // Setter used on entity.Department.addEmployee() and entity.Department.removeEmployee()
    @Setter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
            name = "employee_projects",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;

    public Employee(){
        projects = new HashSet<>();
    }

    /**
     * Compare Employee objects based on id or reference.
     * @param obj  the reference object with which to compare.
     * @return true if the objects are the same. false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;

        if(obj == null) return false;

        if(!(obj instanceof Employee)) return false;

        Employee employee = (Employee) obj;

        return employee.getId() != null && employee.getId().equals(id);
    }

    /**
     * Returns a constant hash code for all instances of Employee.
     * @return the hash code of the Employee class.
     */
    @Override
    public int hashCode(){
        return getClass().hashCode();
    }

    // Setter manually written with standardization of string.
    public void setFirstName(String firstName){
        this.firstName = standarizeString(firstName);
    }

    // Setter manually written with standardization of string.
    public void setLastName(String lastName){
        this.lastName = standarizeString(lastName);
    }


    // enity.Employee has all the logic behind the relation between Employee and Project.
    public boolean addProject(Project project){
        if(project == null) throw new IllegalArgumentException("Project cannot be null.");

        if(!project.getDepartment().equals(department)){
            throw new IllegalArgumentException("Project and Employee must be on the same department.");
        }

        if(projects.add(project)){
            project.getEmployees().add(this);
            return true;
        }
        return false;
    }

    public void addProjects(Collection<Project> projectCollection){
        for(Project p : projectCollection){
            addProject(p);
        }
    }

    public boolean removeProject(Project project){
        if(project == null) return false;

        if(projects.remove(project)){
            project.getEmployees().remove(this);
            return true;
        }
        return false;
    }

    public void removeAllProjects(){
        Iterator<Project> iterator = projects.iterator();
        while (iterator.hasNext()){
            Project p = iterator.next();
            p.getEmployees().remove(this); // Removing this Employee from the current Project.
            iterator.remove(); // Removing the current Project from the Employee Set.
        }
    }
}
