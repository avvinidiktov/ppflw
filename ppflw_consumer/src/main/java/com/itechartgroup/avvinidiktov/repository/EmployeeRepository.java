package com.itechartgroup.avvinidiktov.repository;

import com.itechartgroup.avvinidiktov.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
