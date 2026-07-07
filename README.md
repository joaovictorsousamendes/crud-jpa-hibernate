

# Index
+ [[#CRUD JPA/Hibernate]]
+ [[#Technologies]]
+ [[#Features]]
+ [[#Prerequisites]]
+ [[#Cloning]]
+ [[#Project Structure]]
+ [[#Executing project]]
+ [[#Contact]]


# CRUD JPA/Hibernate
A Department, Employee and Project CRUD using JPA (Jakarta Persistence API) and the ORM Hibernate.


---
## Technologies
+ Java SE 21
+ [JPA (Jakarta Persistence API)](https://jakarta.ee/specifications/persistence/)
+ [Lombok](https://projectlombok.org/)
+ [MySQL](https://www.mysql.com/)
- [Maven](https://maven.apache.org/)

---
## Features

### Department
+ Create departments with name, projects and employees.
+ Access departments by: ID or name.
+ Update departments.
+ Get a list of departments.
+ Add and remove employees and projects.
+ Delete departments.
### Employee
+ Create employees with:
	+ First and last name;
	+ Email;
	+ Password;
	+ Salary;
	+ Projects.
+ Access employee by id.
+ Update employees.
+ Get employee with maximum or minimum salary.
+ Get a list of employees.
+ Get a list of employees filtered by:
	+  First, last and/or full name.
	+ Salary between two values.
	+ Salary less then or equal a value.
	+ Salary more then or equal a value.
	+ Department ID and salary between two values.
+ Assign an employee to a project.
+ Remove an employee from a project.
+ Delete employees.
### Project
+ Create projects with: 
	+ Name;
	+ Start date;
	+ Finish date;
	+ Employees.
+ Access project by id.
+ Update projects.
+ Get a list of projects.
+ Get a list of projects filtered by:
	+ Department id;
	+ Employee id;
	+ Name;
	+ Start date;
	+ Finish date.
+ Add and remove employees.
+ Delete projects.

---
## Prerequisites
+ Java 21 
+ Maven 4.0 
+ Hibernate 7.2
+ MySQL 9.6.0 
+ Jakarta Persistence API 3.2.0 
+ Lombok 1.18.4
+ Git  2.49.0 
---
## Cloning

```shell
git clone https://github.com/joaovictorsousamendes/crud-jpa-hibernate.git
```

---
## Project Structure

```text
project/
├── src/
	├── main/
		├── java/
			├── entity/
				├── Department
				├── Employee
				├── EntityInterface
				├── Project
			├── repository/
				├── DepartmentRepository
				├── EmployeeRepository
				├── JPARepository
				├── ProjectRepository
			├── service/
				├── DepartmentService
				├── EmployeeService
				├── ProjectService
				├── ServiceInterface
			├── util/
				├── JPAUtil
			├── Main
		├── resources/
			├── META-INF/
				├── persistence.xml
  ├── test/
    ├── java
├── .gitignore
├── pom.xml
```

---
## Executing project
### Database Configuration
1. Create a MySQL database.
2. Update `src/main/resources/META-INF/persistence.xml` with your credentials.
### Running the Application
```shell
mvn clean install
mvn exec:java -Dexec.mainClass="com.yourpackage.Main"
```

---
## Contact
João Victor Sousa Mendes.
Email: sousa.joaovictor30@gmail.com
[LinkedIn](https://www.linkedin.com/in/jo%C3%A3o-victor-213835264/)
