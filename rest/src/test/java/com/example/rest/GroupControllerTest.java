package com.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClassEmployeeRepository classEmployeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateGroup() throws Exception {
        ClassEmployee group = new ClassEmployee("Test Group", 5);

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeGroupName").value("Test Group"));
    }

    @Test
    public void shouldReturnGroupFillPercentage() throws Exception {
        ClassEmployee group = classEmployeeRepository.save(new ClassEmployee("Full Group", 3));

        mockMvc.perform(get("/api/group/" + group.getId() + "/fill"))
                .andExpect(status().isOk());
    }
}

