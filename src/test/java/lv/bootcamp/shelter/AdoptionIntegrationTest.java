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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Task: Integration test with @SpringBootTest.
 *
 * The full application context loads — use @MockitoBean only for the external
 * NotificationClient. Everything else (service, repository, JPA) is real.
 * @Transactional rolls back after each test.
 */
@SpringBootTest
@Transactional
class AdoptionIntegrationTest {

    @Autowired
    private AnimalService animalService;

    @MockitoBean
    private NotificationClient notificationClient;

    @Test
    void adoptionFlow_shouldPersistStatusAndNotifyExternalSystem() {
        AnimalCreateRequest createRequest = new AnimalCreateRequest("Pie-O-My", AnimalType.DOG,
                "Jack russel", 3, "Playful dog");

        AnimalResponse animalResponse = animalService.create(createRequest);

        assertThat(animalResponse.status()).isEqualTo(AnimalStatus.AVAILABLE);
        assertThat(animalResponse.id()).isNotNull();

        AdoptionRequest adoptionRequest = new AdoptionRequest(
                animalResponse.id(), "Tony Soprano", "mrtony@gmail.com");
        AnimalResponse adopted = animalService.adopt(adoptionRequest);

        assertThat(adopted.status()).isEqualTo(AnimalStatus.ADOPTED);
        verify(notificationClient).sendAdoptionNotification(
                animalResponse.id(), "Pie-O-My", "mrtony@gmail.com");

        AnimalResponse refetched = animalService.findById(animalResponse.id());

        assertThat(refetched.status()).isEqualTo(AnimalStatus.ADOPTED);
    }
}
