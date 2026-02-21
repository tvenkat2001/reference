package com.alacriti.reference.repository;

import com.alacriti.reference.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface EmployeeRepository extends JpaRepository<Employee,Long> {
}
