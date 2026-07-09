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
        Animal animal = new Animal();
        animal.setId(null);
        animal.setName("Joanne");
        animal.setType(AnimalType.DOG);
        animal.setBreed("Daschund");
        animal.setAge(3);
        animal.setDescription("Tiny baby friend");
        animal.setStatus(AnimalStatus.AVAILABLE);

        Animal savedAnimal = animalRepository.save(animal);

        assertThat(savedAnimal.getId()).isNotNull();
        assertThat(savedAnimal.getName()).isEqualTo("Joanne");
    }

    @Test
    void findByStatus_shouldReturnOnlyMatchingAnimals() {
        Animal availabelAnimalOne = new Animal();
        availabelAnimalOne.setName("Joanne");
        availabelAnimalOne.setType(AnimalType.DOG);
        availabelAnimalOne.setBreed("Daschund");
        availabelAnimalOne.setAge(3);
        availabelAnimalOne.setDescription("Tiny baby friend");
        availabelAnimalOne.setStatus(AnimalStatus.AVAILABLE);

        Animal availableAnimalTwo = new Animal();
        availableAnimalTwo.setName("Jonathan");
        availableAnimalTwo.setType(AnimalType.CAT);
        availableAnimalTwo.setBreed("Orange");
        availableAnimalTwo.setAge(2);
        availableAnimalTwo.setDescription("Very orange");
        availableAnimalTwo.setStatus(AnimalStatus.AVAILABLE);

        Animal adopted = new Animal();
        adopted.setName("Mark");
        adopted.setType(AnimalType.DOG);
        adopted.setStatus(AnimalStatus.ADOPTED);

        entityManager.persist(availabelAnimalOne);
        entityManager.persist(availableAnimalTwo);
        entityManager.persist(adopted);
        entityManager.flush();

        List<Animal> result = animalRepository.findByStatus(AnimalStatus.AVAILABLE);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Animal::getName)
                .containsExactlyInAnyOrder("Joanne", "Jonathan");
        assertThat(result).allMatch(a -> a.getStatus() == AnimalStatus.AVAILABLE);
    }

    @Test
    void findByType_shouldReturnAnimalsOfGivenType() {
        Animal testDog = new Animal();
        testDog.setName("Joanne");
        testDog.setType(AnimalType.DOG);
        testDog.setBreed("Daschund");
        testDog.setAge(3);
        testDog.setDescription("Tiny baby friend");
        testDog.setStatus(AnimalStatus.AVAILABLE);

        Animal testCat = new Animal();
        testCat.setName("Jonathan");
        testCat.setType(AnimalType.CAT);
        testCat.setBreed("Orange");
        testCat.setAge(2);
        testCat.setDescription("Very orange");
        testCat.setStatus(AnimalStatus.AVAILABLE);

        entityManager.persist(testDog);
        entityManager.persist(testCat);
        entityManager.flush();

        List<Animal> result = animalRepository.findByType(AnimalType.DOG);

        assertThat(result.get(0).getName()).isEqualTo("Joanne");
    }

    @Test
    void findByNameContainingIgnoreCase_shouldMatchPartialName() {
        Animal rex = new Animal();
        rex.setName("Rex");
        rex.setType(AnimalType.DOG);
        rex.setStatus(AnimalStatus.AVAILABLE);

        Animal rexyJr = new Animal();
        rexyJr.setName("Rexy Jr");
        rexyJr.setType(AnimalType.DOG);
        rexyJr.setStatus(AnimalStatus.AVAILABLE);

        Animal mia = new Animal();
        mia.setName("Mia");
        mia.setType(AnimalType.CAT);
        mia.setStatus(AnimalStatus.AVAILABLE);

        entityManager.persist(rex);
        entityManager.persist(rexyJr);
        entityManager.persist(mia);
        entityManager.flush();

        List<Animal> result = animalRepository.findByNameContainingIgnoreCase("rex");

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Animal::getName)
                .containsExactlyInAnyOrder("Rex", "Rexy Jr");
    }
}
