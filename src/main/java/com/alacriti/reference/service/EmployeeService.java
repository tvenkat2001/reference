package com.alacriti.reference.service;

import com.alacriti.reference.model.input.CreateEmployeeInput;
import com.alacriti.reference.model.response.Employee;
import com.alacriti.reference.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService{

    private final EmployeeRepository repository;
    private final ConversionService conversionService;

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return repository.findById(id).map(employee ->conversionService.convert(employee,Employee.class) )
                .orElseThrow(() -> new RuntimeException("Employee not found"));


    }


    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(employee -> conversionService.convert(employee,Employee.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public Employee createEmployee(CreateEmployeeInput input) {
        final var employee = com.alacriti.reference.entity.Employee.builder()
                .firstName(input.firstName())
                .lastName(input.lastName())
                .email(input.email())
                .salary(input.salary())
                .department(input.department())
                .build();

        return conversionService.convert(repository.save(employee),Employee.class);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}