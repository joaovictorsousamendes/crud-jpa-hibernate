package entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project implements EntityInterface{
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate finishDate;

    // Setter ued on entity.Department.addProject() and entity.Department.removeProject()
    @Setter(AccessLevel.PROTECTED)
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees;

    public Project(){
        employees = new HashSet<>();
    }

    /**
     * Compare Projects objects based on id or reference.
     * @param obj  the reference object with which to compare.
     * @return true if the objects are the same. false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;

        if(obj == null) return false;

        if(!(obj instanceof Project)) return false;

        Project project = (Project) obj;

        return project.getId() != null && project.getId().equals(id);
    }

    /**
     * Returns a constant hash code for all instances of Project.
     * @return the hash code of the Project class.
     */
    @Override
    public int hashCode(){
        return getClass().hashCode();
    }

    // Setter manually written with standardization of string.
    public void setName(String name){
        this.name = standarizeString(name);
    }

    // enity.Employee has all the logic behind the relation between Employee and Project.
    public boolean addEmployee(Employee employee){
        if(employee == null) throw new EntityNotFoundException("Employee not found.");

        if(employee.getDepartment() == null) {
            throw new IllegalArgumentException("Employee must be on the same department as project.");
        }
        return employee.addProject(this);
    }

    public boolean removeEmployee(Employee employee){
        if(employee == null) return false;

        return employee.removeProject(this);
    }

    public void removeAllEmployees(){
        for(Employee e : employees){
            e.removeProject(this);
        }
    }

}
