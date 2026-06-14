package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.client.NotificationClient;
import lv.bootcamp.shelter.repository.AnimalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: add any imports you need as you write the tests

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

    @Test
    void create_shouldSaveAnimalWithAvailableStatus() {
        // TODO:
        // 1. Arrange: create an AnimalCreateRequest for a dog named "Rex"
        //    Stub animalRepository.save() to return a saved Animal with id=1 and status=AVAILABLE
        // 2. Act: call animalService.create(request)
        // 3. Assert: response has id=1, name="Rex", status=AVAILABLE
        // 4. Use ArgumentCaptor to capture the Animal passed to save()
        //    and assert its status was set to AVAILABLE before saving
    }

    @Test
    void findById_shouldThrowWhenAnimalNotFound() {
        // TODO:
        // 1. Arrange: stub animalRepository.findById(99L) to return Optional.empty()
        // 2. Act & Assert: calling animalService.findById(99L) should throw
        //    AnimalNotFoundException with the id in the message
    }

    @Test
    void adopt_shouldChangeStatusAndSendNotification() {
        // TODO:
        // 1. Arrange: create an AVAILABLE animal, stub findById() to return it,
        //    stub save() to return the argument passed to it (hint: thenAnswer)
        // 2. Act: call animalService.adopt() with animalId=1 and email="john@example.com"
        // 3. Assert: response status is ADOPTED
        // 4. Verify: notificationClient.sendAdoptionNotification() was called
        //    with the correct animalId, name, and email
    }

    @Test
    void adopt_shouldThrowWhenAnimalAlreadyAdopted() {
        // TODO:
        // 1. Arrange: create an ADOPTED animal, stub findById() to return it
        // 2. Act & Assert: calling adopt() should throw IllegalStateException
        // 3. Verify: notificationClient had NO interactions at all
    }

    @Test
    void reserveMultiple_shouldNotifyWithReservedIds() {
        // TODO:
        // 1. Arrange: create two AVAILABLE animals (id=1, id=2),
        //    stub findAllById() to return them,
        //    stub save() to return the argument (thenAnswer)
        // 2. Act: call animalService.reserveMultiple(List.of(1L, 2L))
        // 3. Assert: both responses have status RESERVED
        // 4. Use @Captor (ArgumentCaptor<List<Long>>) to capture the list
        //    passed to notificationClient.sendBulkStatusNotification()
        //    and assert it containsExactly(1L, 2L)
    }
}
