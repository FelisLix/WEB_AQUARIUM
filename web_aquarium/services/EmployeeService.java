package web_aquarium.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web_aquarium.data.Employee;
import web_aquarium.repos.EmployeeRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Employee getEmployeeById(int id) {
        return  employeeRepository.findEmployeesById(id);
    }
}
