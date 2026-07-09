package lv.bootcamp.shelter.controller;

import lv.bootcamp.shelter.config.SecurityConfig;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task: View controller tests with MockMvc and @WebMvcTest.
 *
 * A @Controller returns a view name, not JSON.
 * Use view().name() and model().attribute() instead of jsonPath().
 * Use content().string(containsString(...)) to check rendered HTML.
 */
@WebMvcTest(AnimalPageController.class)
@Import(SecurityConfig.class)
class AnimalPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalService animalService;

    @Test
    void listAnimals_shouldRenderAnimalsView() throws Exception {
        when(animalService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/animals")).andExpect(status().isOk())
                .andExpect(view().name("animals"));
    }

    @Test
    void listAnimals_shouldAddAnimalsToModel() throws Exception {
        AnimalResponse testAnimal = new AnimalResponse(
                1L, "Peter", AnimalType.DOG, "Retriever", 3,
                "Yellow dog", AnimalStatus.AVAILABLE);

        when(animalService.findAll()).thenReturn(List.of(testAnimal));

        mockMvc.perform(get("/animals")).andExpect(status().isOk())
                .andExpect(model().attributeExists("animals"))
                .andExpect(model().attribute("animals", hasSize(1)))
                .andExpect(model().attribute("animals", List.of(testAnimal)));
    }

    @Test
    void listAnimals_shouldRenderAnimalNameInHtml() throws Exception {
        AnimalResponse testAnimal = new AnimalResponse(
                1L, "Joe", AnimalType.DOG, "Labrador", 3,
                "Friendly dog", AnimalStatus.AVAILABLE);

        when(animalService.findAll()).thenReturn(List.of(testAnimal));

        mockMvc.perform(get("/animals")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Joe")));
    }
}
