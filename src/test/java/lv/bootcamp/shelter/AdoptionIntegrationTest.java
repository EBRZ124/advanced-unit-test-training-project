package lv.bootcamp.shelter;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.transaction.annotation.Transactional;

// TODO: add imports as you write the test (e.g. assertThat, verify)

/**
 * Task: Integration test with @SpringBootTest.
 *
 * The full application context loads — use @MockBean only for the external
 * NotificationClient. Everything else (service, repository, JPA) is real.
 * @Transactional rolls back after each test.
 */
@SpringBootTest
@Transactional
class AdoptionIntegrationTest {

    @Autowired
    private AnimalService animalService;

    @MockBean
    private NotificationClient notificationClient;

    @Test
    void adoptionFlow_shouldPersistStatusAndNotifyExternalSystem() {
        // TODO:
        // 1. Create a new animal via animalService.create() — assert status is AVAILABLE
        // 2. Adopt it via animalService.adopt() with an email address
        // 3. Assert the returned response has status ADOPTED
        // 4. Verify notificationClient.sendAdoptionNotification() was called
        //    with the correct animalId, name, and email
        // 5. Re-fetch the animal via animalService.findById() and assert it is still ADOPTED
    }
}
