package com.serrodcal;

import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

import static io.quarkus.vertx.web.Route.HandlerType.FAILURE;

@Singleton
@RouteBase(path = "/api/v1", produces = "application/json")
public class EmployeeResource {

    private static final Logger log = Logger.getLogger(EmployeeResource.class.getName());

    @Inject
    EmployeeService service;

    @Route(path = "/employee", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get all",
            content=@Content(mediaType="application/json", schema=@Schema(type= SchemaType.ARRAY),
            example = "[\n" +
                    "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"name\": \"name\",\n" +
                    "  \"surname\": \"surname\",\n" +
                    "  \"birth\": [\n" +
                    "    2020,\n" +
                    "    4,\n" +
                    "    15\n" +
                    "  ],\n" +
                    "  \"email\": \"some@email.com\"\n" +
                    "}\n" +
                    "]"))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING),
                    example = "Some error message"))
    public Uni<List<Employee>> getAll() {
        log.debug("Get all");
        return service.getAll();
    }

    @Route(path = "/employee/:id", methods = HttpMethod.GET)
    @APIResponse(responseCode="200",
            description="Get single",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT),
                    example = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"name\": \"name\",\n" +
                            "  \"surname\": \"surname\",\n" +
                            "  \"birth\": [\n" +
                            "    2020,\n" +
                            "    4,\n" +
                            "    15\n" +
                            "  ],\n" +
                            "  \"email\": \"some@email.com\"\n" +
                            "}"))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING),
                    example = "Some error message"))
    public Uni<Employee> getSingle(@Param("id") Integer id) {
        log.debug("Get single with id=" + id.toString());
        return service.getSingle(id);
    }

    @Route(path = "/employee", methods = HttpMethod.POST)
    @APIResponse(responseCode="201",
            description="Create employee",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT,
                    example = "{\n" +
                            "  \"id\": 1,\n" +
                            "  \"name\": \"name\",\n" +
                            "  \"surname\": \"surname\",\n" +
                            "  \"birth\": [\n" +
                            "    2020,\n" +
                            "    4,\n" +
                            "    15\n" +
                            "  ],\n" +
                            "  \"email\": \"some@email.com\"\n" +
                            "}")))
    @APIResponse(responseCode="400",
            description="Bad Request: Constraint Violation",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT, example = "{\n" +
                    "  \"title\": \"Constraint Violation\",\n" +
                    "  \"details\": \"validation constraint violations\",\n" +
                    "  \"status\": 400,\n" +
                    "  \"violations\": [\n" +
                    "    {\n" +
                    "      \"field\": \"create.employeePayload.email\",\n" +
                    "      \"message\": \"debe ser una dirección de correo electrónico con formato correcto\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}")))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING),
                    example = "Some error message"))
    public Uni<Employee> create(@Body @Valid EmployeeRequestPayload employeePayload, HttpServerResponse response) {
        log.debug("Create employee=" + employeePayload.toString());
        if (employeePayload == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Employee invalidly set on request."));
        }

        Employee employee = mapEmployeeRequestPayloadIntoEmployee(employeePayload);

        if(employee.getId() != null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Employee id invalidly set on request."));
        }

        return service.create(employee)
                .onItem().transform(result -> {
                    response.setStatusCode(201);
                    return result;
                });
    }

    @Route(path = "/employee/:id", methods = HttpMethod.PUT)
    @APIResponse(responseCode="200",
            description="Update a given employee",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT),
            example = "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"name\": \"name\",\n" +
                    "  \"surname\": \"surname\",\n" +
                    "  \"birth\": [\n" +
                    "    2020,\n" +
                    "    4,\n" +
                    "    15\n" +
                    "  ],\n" +
                    "  \"email\": \"some@email.com\"\n" +
                    "}"))
    @APIResponse(responseCode="400",
            description="Bad Request: Constraint Violation",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT, example = "{\n" +
                    "  \"title\": \"Constraint Violation\",\n" +
                    "  \"details\": \"validation constraint violations\",\n" +
                    "  \"status\": 400,\n" +
                    "  \"violations\": [\n" +
                    "    {\n" +
                    "      \"field\": \"create.employeePayload.email\",\n" +
                    "      \"message\": \"debe ser una dirección de correo electrónico con formato correcto\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}")))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING),
                    example = "Some error message"))
    public Uni<Employee> update(@Body EmployeeRequestPayload employeePayload, @Param("id") Integer id) {
        log.debug("Update employee=" + employeePayload.toString());
        if (employeePayload == null || id == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Employee invalidly set on request."));
        }

        EmployeeDTO employeeDTO = mapEmployeeRequestPayloadIntoEmployeeDTO(employeePayload);

        return service.update(employeeDTO, id);
    }

    @Route(path = "/employee/:id", methods = HttpMethod.DELETE)
    @APIResponse(responseCode="204",
            description="Delete a given employee",
            content=@Content(mediaType="application/json", schema=@Schema(type=SchemaType.OBJECT),
            example = "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"name\": \"name\",\n" +
                    "  \"surname\": \"surname\",\n" +
                    "  \"birth\": [\n" +
                    "    2020,\n" +
                    "    4,\n" +
                    "    15\n" +
                    "  ],\n" +
                    "  \"email\": \"some@email.com\"\n" +
                    "}"))
    @APIResponse(responseCode="500",
            description="Internal Server Error",
            content=@Content(mediaType="text/plain", schema=@Schema(type=SchemaType.STRING,
            example = "Some error message")))
    public Uni<Employee> delete(@Param("id") Integer id, HttpServerResponse response) {
        log.debug("Delete employee with id=" + id.toString());
        return service.delete(id)
                .onItem().ifNotNull().transform(employee -> {
                    response.setStatusCode(204);
                    return employee;
                })
                .onItem().ifNull().fail();
    }

    @Route(path = "/*", type = FAILURE)
    public void error(RoutingContext context) {
        Throwable t = context.failure();
        if (t != null) {
            log.error("Failed to handle request", t);
            int status = context.statusCode();
            String chunk = "";
            if (t instanceof NoSuchElementException) {
                status = 404;
            } else if (t instanceof IllegalArgumentException) {
                status = 422;
                chunk = new JsonObject().put("code", status)
                        .put("exceptionType", t.getClass().getName()).put("error", t.getMessage()).encode();
            }
            context.response().setStatusCode(status).end(chunk);
        } else {
            // Continue with the default error handler
            context.next();
        }
    }

    private Employee mapEmployeeRequestPayloadIntoEmployee(EmployeeRequestPayload employeePayload) {
        Employee employee = new Employee();
        employee.setName(employeePayload.getName());
        employee.setSurname(employeePayload.getSurname());
        employee.setBirth(employeePayload.getBirth());
        employee.setEmail(employeePayload.getEmail());
        return employee;
    }

    private EmployeeDTO mapEmployeeRequestPayloadIntoEmployeeDTO(EmployeeRequestPayload employeePayload) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employeePayload.getName());
        employeeDTO.setSurname(employeePayload.getSurname());
        employeeDTO.setBirth(employeePayload.getBirth());
        employeeDTO.setEmail(employeePayload.getEmail());
        return employeeDTO;
    }

}