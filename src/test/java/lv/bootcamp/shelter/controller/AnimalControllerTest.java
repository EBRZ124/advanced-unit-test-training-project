package lv.bootcamp.shelter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.bootcamp.shelter.config.SecurityConfig;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalNotFoundException;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task: REST controller tests with MockMvc and @WebMvcTest.
 *
 * Stub the service with @MockitoBean. Use mockMvc.perform() to make requests
 * and chain .andExpect() calls to verify status, JSON content, and error responses.
 */
@WebMvcTest(AnimalController.class)
@Import(SecurityConfig.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnimalService animalService;

    @Test
    void findAll_shouldReturnListOfAnimals() throws Exception {
        // TODO:
        // 1. Stub animalService.findAll() to return a list of two AnimalResponse objects
        // 2. GET /api/animals
        // 3. Assert status 200, JSON array length 2, and the names of both animals
    }

    @Test
    void findById_shouldReturn404WhenNotFound() throws Exception {
        when(animalService.findById(99L)).thenThrow(new AnimalNotFoundException(99L));

        mockMvc.perform(get("/api/animals/99")).andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201WithCreatedAnimal() throws Exception {
        // TODO:
        // 1. Stub animalService.create(any()) to return an AnimalResponse with id=1, name="Rex", status=AVAILABLE
        // 2. POST /api/animals with a valid AnimalCreateRequest JSON body
        // 3. Assert status 201 and that the response JSON contains id, name, and status
    }

    @Test
    void create_shouldReturn400WhenNameIsBlank() throws Exception {
        // TODO:
        // 1. POST /api/animals with a request where name is blank ("")
        // 2. Assert status 400
        // (no stub needed — validation rejects the request before the service is called)
    }

    @Test
    void create_shouldReturn400WhenTypeIsNull() throws Exception {
        // TODO:
        // 1. POST /api/animals with a JSON body where "type" is null
        // 2. Assert status 400
    }
}