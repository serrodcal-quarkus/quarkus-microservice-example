package com.serrodcal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Cacheable
@Table(name = "employees")
@NamedQuery(name = Employee.FIND_ALL, query = "SELECT e FROM Employee e ORDER BY e.name")
public class Employee {

    public static final String FIND_ALL = "Employees.findAll";

    @Id
    @SequenceGenerator(name = "employeesSequence", sequenceName = "employees_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "employeesSequence")
    private Integer id;

    private String name;

    private String surname;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birth;

    private String email;

    public Employee() { }

    public Employee(Integer id, String name, String surname, LocalDate birth, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) { this.name = name; }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
