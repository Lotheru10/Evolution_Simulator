package agh.ics.oop.model;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GrassFieldTest {

    private GrassField grassField;

    @BeforeEach
    void setUp() {
        // Inicjalizacja mapy GrassField o rozmiarze 10x10 z 5 trawami
        grassField = new GrassField(10, 10, 5);
    }

    @Test
    void testAddGrass() {
        grassField = new GrassField(10, 10, 5);

        // Zapisz początkową liczbę trawy
        int initialGrassCount = grassField.getGrassCount();

        // Dodaj więcej trawy
        grassField.addGrass(3);

        // Sprawdź, czy liczba trawy wzrosła
        int newGrassCount = grassField.getGrassCount();
        assertEquals(initialGrassCount + 3, newGrassCount, "Trawa nie została dodana prawidłowo");
    }

    @Test
    void testRemoveGrass() {
        // Pobierz losową pozycję trawy
        Vector2d randomGrassPosition = grassField.getRandomGrass();

        // Usuń trawę z tej pozycji
        grassField.removeGrass(randomGrassPosition);

        // Sprawdź, czy trawa została usunięta
        assertFalse(grassField.grassAt(randomGrassPosition), "Trawa nie została usunięta z pozycji " + randomGrassPosition);
    }

    @Test
    void testGrassAt() {
        // Pobierz losową pozycję trawy
        Vector2d randomGrassPosition = grassField.getRandomGrass();

        // Sprawdź, czy trawa istnieje na tej pozycji
        assertTrue(grassField.grassAt(randomGrassPosition), "Trawa powinna istnieć na pozycji " + randomGrassPosition);
    }

    @Test
    void testGetRandomGrass() {
        // Pobierz losową pozycję trawy
        Vector2d randomGrassPosition = grassField.getRandomGrass();

        // Sprawdź, czy pozycja jest faktycznie zajęta przez trawę
        assertNotNull(randomGrassPosition, "Losowa pozycja trawy nie powinna być null");
        assertTrue(grassField.grassAt(randomGrassPosition), "Na pozycji " + randomGrassPosition + " powinna znajdować się trawa");
    }

    @Test
    void testIsOccupied() throws IncorrectPositionException {
        // Dodaj zwierzę na mapę
        Vector2d animalPosition = new Vector2d(2, 2);
        Animal animal = new Animal(animalPosition, null, 10); // Wartości genów i energii są nieistotne dla tego testu
        grassField.place(animal);

        // Sprawdź, czy pozycja jest zajęta
        assertTrue(grassField.isOccupied(animalPosition), "Pozycja " + animalPosition + " powinna być zajęta przez zwierzę");

        // Sprawdź losową pozycję trawy
        Vector2d grassPosition = grassField.getRandomGrass();
        assertTrue(grassField.isOccupied(grassPosition), "Pozycja " + grassPosition + " powinna być zajęta przez trawę");

        // Sprawdź niezajętą pozycję
        Vector2d emptyPosition = new Vector2d(0, 0);
        assertFalse(grassField.isOccupied(emptyPosition), "Pozycja " + emptyPosition + " powinna być pusta");
    }

    @Test
    void testHandleGrassConsumption() throws IncorrectPositionException {
        // Pobierz losową pozycję trawy
        Vector2d grassPosition = grassField.getRandomGrass();

        // Dodaj zwierzę na pozycję trawy
        Animal animal = new Animal(grassPosition, null, 10);
        grassField.place(animal);

        // Sprawdź początkową energię zwierzęcia
        int initialEnergy = animal.getEnergy();

        // Uruchom konsumpcję trawy
        grassField.handleGrassConsumption();

        // Sprawdź, czy trawa została usunięta
        assertFalse(grassField.grassAt(grassPosition), "Trawa na pozycji " + grassPosition + " powinna zostać zjedzona");

        // Sprawdź, czy zwierzę zyskało energię
        assertTrue(animal.getEnergy() > initialEnergy, "Zwierzę powinno zyskać energię po zjedzeniu trawy");
    }

    @Test
    void testGetStrongestAnimals() throws IncorrectPositionException {
        Vector2d position = new Vector2d(1, 1);

        // Dodaj 3 zwierzęta o różnej energii na tę samą pozycję
        Animal animal1 = new Animal(position, null, 10); // Najsłabsze
        Animal animal2 = new Animal(position, null, 20); // Średnie
        Animal animal3 = new Animal(position, null, 30); // Najsilniejsze
        grassField.place(animal1);
        grassField.place(animal2);
        grassField.place(animal3);

        // Pobierz dwa najsilniejsze zwierzęta
        List<Animal> strongestAnimals = grassField.getStrongestAnimals(position, 2);

        // Sprawdź, czy zwrócono odpowiednie zwierzęta
        assertEquals(2, strongestAnimals.size(), "Powinny zostać zwrócone dwa najsilniejsze zwierzęta");
        assertTrue(strongestAnimals.contains(animal3), "Najsilniejsze zwierzę powinno być zwrócone");
        assertTrue(strongestAnimals.contains(animal2), "Drugie najsilniejsze zwierzę powinno być zwrócone");
    }
}
