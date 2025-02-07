package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    private Simulation simulation;
    private GrassField map;
    private Animal animal1;
    private Animal animal2;
    private Vector2d position;

    @BeforeEach
    void setUp() throws IncorrectPositionException {
        // Tworzenie mapy i dodanie zwierząt
        int mapWidth = 10;
        int mapHeight = 10;
        map = new GrassField(mapWidth, mapHeight, 0);
        position = new Vector2d(5, 5);

        // Tworzenie zwierząt z odpowiednią energią
        animal1 = new Animal(position, new Genes(generateGenes(8), 0), 20);
        animal2 = new Animal(position, new Genes(generateGenes(8), 0), 15);

        map.place(animal1);
        map.place(animal2);

        // Tworzenie symulacji
        SimulationConfig config = new SimulationConfig(
                "10", "10", "2", "0", "8", "0",
                "20", "10", "5", "10", "0", "8", "Dominant", "NoFire"
        );

        simulation = new Simulation(config, map, null, null);
    }

    @Test
    void testHandleSex() throws IncorrectPositionException {
        // Uruchomienie metody
        simulation.handleSex();

        // Sprawdzenie efektów
        assertEquals(3, simulation.getAnimals().size(), "Powinno być 3 zwierzęta po rozmnażaniu.");
        Animal child = simulation.getAnimals().get(2);

        // Sprawdzanie pozycji dziecka
        assertEquals(position, child.getPosition(), "Dziecko powinno być na tej samej pozycji co rodzice.");

        // Sprawdzenie genów dziecka
        List<Integer> genes = child.getGenes();
        assertEquals(8, genes.size(), "Dziecko powinno mieć 8 genów.");

        // Sprawdzanie energii rodziców
        assertEquals(15, animal1.getEnergy(), "Rodzic 1 powinien stracić energię.");
        assertEquals(10, animal2.getEnergy(), "Rodzic 2 powinien stracić energię.");
    }

    private ArrayList<Integer> generateGenes(int length) {
        ArrayList<Integer> genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add((int) (Math.random() * 8));
        }
        return genes;
    }
}
