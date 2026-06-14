package lv.bootcamp.shelter.repository;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.model.AnimalStatus;
import lv.bootcamp.shelter.model.AnimalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Task: Repository tests with @DataJpaTest.
 *
 * Use entityManager.persist() + entityManager.flush() to set up test data.
 * Each test rolls back automatically — no cleanup needed.
 */
@DataJpaTest
class AnimalRepositoryTest {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_shouldPersistAnimalAndGenerateId() {
        // TODO:
        // 1. Create an Animal with id=null
        // 2. Call animalRepository.save()
        // 3. Assert the returned animal has a non-null id and the correct name
    }

    @Test
    void findByStatus_shouldReturnOnlyMatchingAnimals() {
        // TODO:
        // 1. Persist two AVAILABLE animals and one ADOPTED animal via entityManager
        //    Call entityManager.flush() after persisting
        // 2. Call animalRepository.findByStatus(AVAILABLE)
        // 3. Assert only the two available animals are returned
    }

    @Test
    void findByType_shouldReturnAnimalsOfGivenType() {
        // TODO:
        // 1. Persist one DOG and one CAT, flush
        // 2. Call animalRepository.findByType(DOG)
        // 3. Assert only the dog is returned
    }

    @Test
    void findByNameContainingIgnoreCase_shouldMatchPartialName() {
        // TODO:
        // 1. Persist animals named "Rex", "Rexy Jr", and "Mia", flush
        // 2. Call animalRepository.findByNameContainingIgnoreCase("rex")
        // 3. Assert two results are returned (case-insensitive partial match)
    }
}
