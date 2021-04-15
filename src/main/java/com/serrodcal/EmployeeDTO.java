package com.serrodcal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public class EmployeeDTO {

    private String name;

    private String surname;

    private LocalDate birth;

    private String email;

    public EmployeeDTO() { }

    public EmployeeDTO(String name, String surname, LocalDate birth, String email) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.email = email;
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
