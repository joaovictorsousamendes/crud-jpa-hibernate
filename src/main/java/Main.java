
import entity.*;
import jakarta.persistence.EntityNotFoundException;
import service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args){

       ProjectService projectService = new ProjectService();
       // DepartmentService departmentService = new DepartmentService();
        //EmployeeService employeeService = new EmployeeService();

        Project p = projectService.getById(3);
    }
}
