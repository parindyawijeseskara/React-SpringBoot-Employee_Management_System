package lk.ijse.spring.Controller;

import lk.ijse.spring.entity.Employee;
import lk.ijse.spring.repo.EmployeeRepo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin()
@RestController
public class EmployeeController {
    @Autowired
    EmployeeRepo employeeRepo;

    /*Get all the employees*/
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        try {
            return new ResponseEntity<>(employeeRepo.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Get the employee by id*/
    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) {
        try {
            //check if employee exist in database
            val empObj = getEmpRecord(id);

            if (empObj != null) {
                return new ResponseEntity<>(empObj, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Create new employee*/
    @PostMapping("/employee")
    public ResponseEntity<Employee> newEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeRepo
                .save(Employee.builder()
                        .name(employee.getName())
                        .role(employee.getRole())
                        .build());
        return new ResponseEntity<>(newEmployee, HttpStatus.OK);
    }

    /*Update Employee record by using it's id*/
    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") int id, @RequestBody Employee employee) {

        //check if employee exist in database
        Employee empObj = getEmpRecord(id);

        if (empObj != null) {
            empObj.setName(employee.getName());
            empObj.setRole(employee.getRole());
            return new ResponseEntity<>(employeeRepo.save(empObj), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*Delete Employee by Id*/
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<HttpStatus> deleteEmployeeById(@PathVariable("id") int id) {
        try {
            //check if employee exist in database
            Employee emp = getEmpRecord(id);

            if (emp != null) {
                employeeRepo.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Delete all employees*/
    @DeleteMapping("/employees")
    public ResponseEntity<HttpStatus> deleteAllEmployees() {
        try {
            employeeRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*get the employee record by id*/
    private Employee getEmpRecord(int id) {
        Optional<Employee> empObj = employeeRepo.findById(id);

        if (empObj.isPresent()) {
            return empObj.get();
        }
        return null;
    }


}
