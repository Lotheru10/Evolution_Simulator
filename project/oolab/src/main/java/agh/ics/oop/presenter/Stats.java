package agh.ics.oop.presenter;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.WorldMap;

import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;

public class Stats {
    private AbstractWorldMap map;

    public Stats(AbstractWorldMap map){
        this.map = map;
    }

    private int totalAnimals = 0;
    private int totalGrass = 0;
    private int freeFields = 0;
    private Map<String, Integer> genotypeCount = new HashMap<>();
    private double averageEnergy = 0.0;
    private double averageLifespan = 0.0;
    private double averageChildren = 0.0;

    private int deadAnimalsCount = 0;
    private int totalLifespanOfDeadAnimals = 0;

    public void updateStatistics() {
        totalAnimals = map.getAnimals().size();
        totalGrass = map.getGrassCount();
        freeFields = map.getFreeFieldsCount();

        genotypeCount.clear();
        for (Animal animal : map.getAnimals()) {
            String genotype = animal.getGenes().toString();
            genotypeCount.put(genotype, genotypeCount.getOrDefault(genotype, 0) + 1);
        }

        double totalEnergy = 0.0;
        double totalChildren = 0.0;

        for (Animal animal : map.getAnimals()) {
            totalEnergy += animal.getEnergy();
            totalChildren += animal.getNumberOfOffspring();
        }

        averageEnergy = totalAnimals > 0 ? totalEnergy / totalAnimals : 0.0;
        averageChildren = totalAnimals > 0 ? totalChildren / totalAnimals : 0.0;
        averageLifespan = deadAnimalsCount > 0 ? totalLifespanOfDeadAnimals / deadAnimalsCount : 0.0;
    }

    public void recordDeadAnimal(int age) {
        deadAnimalsCount++;
        totalLifespanOfDeadAnimals += age;
    }

    // Gettery dla statystyk
    public int getTotalAnimals() { return totalAnimals; }
    public int getTotalGrass() { return totalGrass; }
    public int getFreeFields() { return freeFields; }
    public String getMostPopularGenotype() {
        return genotypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }
    public double getAverageEnergy() { return averageEnergy; }
    public double getAverageLifespan() { return averageLifespan; }
    public double getAverageChildren() { return averageChildren; }
}
