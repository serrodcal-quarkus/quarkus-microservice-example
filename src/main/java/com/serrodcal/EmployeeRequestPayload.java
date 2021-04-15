package com.serrodcal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EmployeeRequestPayload {

    private static DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd", new Locale("es", "ES"));

    @NotBlank(message = "name may not be blank")
    private String name;

    @NotBlank(message = "surname may not be blank")
    private String surname;

    @Past
    private LocalDate birth;

    @Email
    @NotBlank(message = "email may not be blank")
    String email;

    public EmployeeRequestPayload() { }

    public EmployeeRequestPayload(@NotBlank(message = "name may not be blank") String name, @NotBlank(message = "surname may not be blank") String surname, @Past LocalDate birth, @Email @NotBlank(message = "email may not be blank") String email) {
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

    public static void setFormatter(DateTimeFormatter formatter) {
        EmployeeRequestPayload.formatter = formatter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(String birth) {
        this.birth = LocalDate.parse(birth, formatter);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birth=" + birth +
                ", email='" + email + '\'' +
                '}';
    }

}
