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
    private long id;
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate finishDate;

    @Setter(AccessLevel.PROTECTED) // Used on Department.addProject() and Department.removeProject()
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Setter(AccessLevel.PROTECTED)
    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees;

    public Project(){
        employees = new HashSet<>();
    }

    // Setter manually written with standardization of string.
    public void setName(String name){
        this.name = standarizeString(name);
    }

    public boolean addEmployee(Employee employee){
        if(employee == null) throw new EntityNotFoundException("Employee not found.");

        if(employee.getDepartment() == null) {
            throw new IllegalArgumentException("Employee must be on the same department as project.");
        }
        return employee.addProject(this);
    }

    public boolean removeEmployee(Employee employee){
        if(employee == null) throw new EntityNotFoundException("Employee not found.");

        if(employee.getDepartment() == null) {
            throw new IllegalArgumentException("Employee must be on the same department as project.");
        }
        return employee.removeProject(this);
    }

    public void removeAllEmployees(){
        for(Employee e : employees){
            e.removeProject(this);
        }
    }

}
