package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.dto.AdoptionRequest;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import lv.bootcamp.shelter.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * Task: Service-layer tests with Mockito.
 *
 * Use @Mock, @InjectMocks, stubbing, verify(), and ArgumentCaptor.
 * Write Arrange-Act-Assert for each method.
 */
@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private AnimalService animalService;

    @Captor
    private ArgumentCaptor<Animal> animalCaptor;

    @Captor
    private ArgumentCaptor<List<Long>> idCaptor;

    @Test
    void create_shouldSaveAnimalWithAvailableStatus() {
        AnimalCreateRequest request = new AnimalCreateRequest(
                "Rex", AnimalType.DOG, "Labrador", 3, "Friendly dog"
        );
        Animal savedAnimal = new Animal();
        savedAnimal.setId(1L);
        savedAnimal.setName("Rex");
        savedAnimal.setType(AnimalType.DOG);
        savedAnimal.setBreed("Labrador");
        savedAnimal.setAge(3);
        savedAnimal.setDescription("Friendly dog");
        savedAnimal.setStatus(AnimalStatus.AVAILABLE);

        when(animalRepository.save(any(Animal.class))).thenReturn(savedAnimal);

        AnimalResponse response = animalService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Rex");
        assertThat(response.status()).isEqualTo(AnimalStatus.AVAILABLE);

        verify(animalRepository).save(animalCaptor.capture());
        Animal captured = animalCaptor.getValue();
        assertThat(captured.getStatus()).isEqualTo(AnimalStatus.AVAILABLE);
        assertThat(captured.getName()).isEqualTo("Rex");
    }

    @Test
    void findById_shouldThrowWhenAnimalNotFound() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(()->animalService.findById(99L)).isInstanceOf(AnimalNotFoundException.class).hasMessageContaining("99");

        verify(animalRepository).findById(99L);
    }

    @Test
    void adopt_shouldChangeStatusAndSendNotification() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Rex");
        animal.setStatus(AnimalStatus.AVAILABLE);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdoptionRequest adoptionRequest = new AdoptionRequest(1L, "James", "James@gmail.com");
        AnimalResponse animalResponse = animalService.adopt(adoptionRequest);

        assertThat(animalResponse.status()).isEqualTo(AnimalStatus.ADOPTED);
        verify(notificationClient).sendAdoptionNotification(1L, "Rex", "James@gmail.com");
    }

    @Test
    void adopt_shouldThrowWhenAnimalAlreadyAdopted() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Rex");
        animal.setStatus(AnimalStatus.ADOPTED);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        AdoptionRequest adoptionRequest = new AdoptionRequest(1L, "James", "James@gmail.com");

        assertThatThrownBy(()->animalService.adopt(adoptionRequest)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not available");

        verifyNoInteractions(notificationClient);
        verify(animalRepository, never()).save(any());
    }

    @Test
    void reserveMultiple_shouldNotifyWithReservedIds() {
        Animal animal1 = new Animal();
        animal1.setId(1L);
        animal1.setName("Rex");
        animal1.setStatus(AnimalStatus.AVAILABLE);

        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Milo");
        animal2.setStatus(AnimalStatus.AVAILABLE);

        when(animalRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(animal1, animal2));
        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<AnimalResponse> responses = animalService.reserveMultiple(List.of(1L, 2L));

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(AnimalResponse::status)
                .containsOnly(AnimalStatus.RESERVED);

        verify(notificationClient).sendBulkStatusNotification(idCaptor.capture(), eq("RESERVED"));
        assertThat(idCaptor.getValue()).containsExactly(1L, 2L);

    }
}
