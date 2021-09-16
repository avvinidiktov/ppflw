package com.itechartgroup.avvinidiktov.PeopleFlow.repository;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
