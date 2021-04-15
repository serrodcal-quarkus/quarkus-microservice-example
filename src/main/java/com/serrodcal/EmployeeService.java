package com.serrodcal;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EmployeeService {

    @Inject
    Mutiny.Session session;

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<List<Employee>> getAll() {
        return session.createNamedQuery(Employee.FIND_ALL, Employee.class).getResultList();
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Employee> getSingle(Integer id) {
        return session.find(Employee.class, id);
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Employee> create(Employee employee) {
        return session.persist(employee)
                .chain(session::flush)
                .onItem().transform(ignore -> {
                    return employee;
                });
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Employee> update(EmployeeDTO employeeDTO, Integer id) {
        return session.find(Employee.class, id)
                // If entity exists then update
                .onItem().ifNotNull().transformToUni(entity -> {
                    entity.setName(employeeDTO.getName());
                    entity.setSurname(employeeDTO.getSurname());
                    entity.setEmail(employeeDTO.getEmail());
                    entity.setBirth(employeeDTO.getBirth());
                    return session.flush()
                            .onItem().transform(ignore -> entity);
                })
                // else
                .onItem().ifNull().fail();
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Uni<Employee> delete(Integer id) {
        return session.find(Employee.class, id)
                // If entity exists then delete
                .onItem().ifNotNull().transformToUni(entity -> session.remove(entity)
                        .chain(session::flush)
                        .map(ignore -> {
                            return entity;
                        }))
                // else
                .onItem().ifNull().fail();
    }

}
