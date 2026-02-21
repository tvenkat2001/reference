package com.alacriti.reference.converter;

import com.alacriti.reference.entity.Employee;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter implements Converter<Employee, com.alacriti.reference.model.response.Employee>{
    @Override
    public com.alacriti.reference.model.response.Employee convert(Employee source) {
        return com.alacriti.reference.model.response.Employee.builder()
                .salary(source.getSalary())
                .email(source.getEmail())
                .id(source.getId())
                .department(source.getDepartment())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .build();
    }
}
