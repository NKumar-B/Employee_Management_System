package com.employee.backend.controller;

import com.employee.backend.dto.EmployeeDTO;
import com.employee.backend.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeDTO dto = EmployeeDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .salary(75000.0)
                .build();

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeDTO dto1 = EmployeeDTO.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .department("HR")
                .salary(60000.0)
                .build();

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Alice")));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        EmployeeDTO dto = EmployeeDTO.builder()
                .firstName("Bob")
                .lastName("Marley")
                .email("bob@example.com")
                .department("Music")
                .salary(90000.0)
                .build();

        String responseStr = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EmployeeDTO created = objectMapper.readValue(responseStr, EmployeeDTO.class);

        mockMvc.perform(get("/api/v1/employees/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bob")))
                .andExpect(jsonPath("$.department", is("Music")));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeDTO dto = EmployeeDTO.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .email("charlie@example.com")
                .department("Finance")
                .salary(50000.0)
                .build();

        String responseStr = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EmployeeDTO created = objectMapper.readValue(responseStr, EmployeeDTO.class);
        created.setSalary(55000.0);
        created.setDepartment("Senior Finance");

        mockMvc.perform(put("/api/v1/employees/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary", is(55000.0)))
                .andExpect(jsonPath("$.department", is("Senior Finance")));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        EmployeeDTO dto = EmployeeDTO.builder()
                .firstName("David")
                .lastName("Miller")
                .email("david@example.com")
                .department("Operations")
                .salary(65000.0)
                .build();

        String responseStr = mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EmployeeDTO created = objectMapper.readValue(responseStr, EmployeeDTO.class);

        mockMvc.perform(delete("/api/v1/employees/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully!"));

        mockMvc.perform(get("/api/v1/employees/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}
