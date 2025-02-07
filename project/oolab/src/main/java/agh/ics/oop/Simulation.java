package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.Genes;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.Stats;

import java.util.*;

public class Simulation implements Runnable {
    private final SimulationPresenter presenter;
    private final Stats stats;
    private final AbstractWorldMap map;
    private boolean isRunning;
    private final List<Animal> animals;
    private final LinkedList<Fire> fires;
    private final int numberOfDailyPlants;
    private final int energyNeededToCopulate;
    private final int energyLose;
    private final int geneLength;
    private final int enegyStart;
    private static int energyGainFromGrass = 0;
    private final String selectedMapType;
    private final String selectedGeneType;
    private final int fireDuration;
    private final int fireFrequancy;
    private final int minMut;
    private final int maxMut;


    private int day;

    public static void setEnergyGainFromGrass(int value) {
        energyGainFromGrass = value;
    }

    public static int getEnergyGainFromGrass() {
        return energyGainFromGrass;
    }

    public Simulation(SimulationConfig config, AbstractWorldMap map, SimulationPresenter presenter, Stats stats) {
        this.presenter = presenter;
        this.fires = new LinkedList<>();
        this.isRunning = true;
        this.animals = new ArrayList<>();
        this.map = map;
        this.stats = stats;

        this.numberOfDailyPlants=config.getNumberOfDailyGrass();
        this.energyNeededToCopulate=config.getEnergyNeededToCopulate();
        this.energyLose=config.getEnergyLose();
        this.geneLength=config.getGeneLength();
        this.enegyStart=config.getNumberOfEnergy();
        this.selectedMapType=config.getSelectedMapType();
        this.selectedGeneType=config.getSelectedGenetype();
        this.minMut=config.getMinMut();
        this.maxMut=config.getMaxMut();
        this.energyGainFromGrass= config.getEnergyGain();

        if (selectedMapType.equals("FireMap")) {
            this.fireDuration = config.getFireDuration();
            this.fireFrequancy = config.getFireFrequency();
        } else {
            this.fireDuration = 0;
            this.fireFrequancy = 0;
        }

        this.day=0;
        Random random = new Random();
        for (int i = 0; i < config.getNumberOfAnimals(); i++) {
            Vector2d position;
                int x = random.nextInt(config.getMapWidth());
                int y = random.nextInt(config.getMapHeight());
                position = new Vector2d(x, y);
            try {
                ArrayList<Integer> genotype = new ArrayList<>();
                for (int j = 0; j < config.getGeneLength(); j++) {
                    genotype.add(random.nextInt(8));
                }
                if(selectedGeneType.equals("crazy")) {
                    Genes gene = new Genes(genotype, random.nextInt(genotype.size()), true);
                } else {
                    Genes gene = new Genes(genotype, random.nextInt(genotype.size()));
                }
                Animal animal = new Animal(position, new Genes(genotype, random.nextInt(genotype.size())), config.getNumberOfEnergy());
                map.place(animal);
                this.animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public void handleSex() throws IncorrectPositionException {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Vector2d position = new Vector2d(x, y);
                List<Animal> residents = ((GrassField) map).getStrongestAnimals(position, 2);
                if (residents.size() == 2) {
                    Animal animal1 = residents.getFirst();
                    Animal animal2 = residents.get(1);
                    int energy2 = animal2.getEnergy();
                    if (energy2 >= energyNeededToCopulate) {
                        int energy1 = animal1.getEnergy();
                        ArrayList<Integer> genes = new ArrayList<>();
                        int ratio = (int) ((energy1 / (energy1 + energy2) * geneLength));
                        ArrayList<Integer> genes1 = animal1.getGenes();
                        ArrayList<Integer> genes2 = animal2.getGenes();
                        double side = Math.random();
                        if (side > 0.5) {
                            for (int i = 0; i < ratio; i++) {
                                genes.add(genes1.get(i));
                            }
                            for (int i = ratio; i < geneLength; i++) {
                                genes.add(genes2.get(i));
                            }
                        } else {
                            int revRatio = geneLength - ratio;
                            for (int i = 0; i < revRatio; i++) {
                                genes.add(genes1.get(i));
                            }
                            for (int i = revRatio; i < geneLength; i++) {
                                genes.add(genes2.get(i));
                            }
                        }

                        animal1.addEnergy(-energyLose);
                        animal2.addEnergy(-energyLose);
                        animal1.addOffSpring();
                        animal2.addOffSpring();

                        for (int i = 0; i < geneLength; i++) {
                            int mutation = (int) (Math.random() * 8);
                            if (mutation == 0) {
                                genes.set(i, mutation);
                            }
                        }

                        int amountOfModGene = (int)(Math.random()*(maxMut-minMut)+minMut);
                        ArrayList<Integer> used = new ArrayList<>();
                        int i=0;
                        while (i<amountOfModGene) {
                            int gen = (int) (Math.random() * geneLength);
                            if (!used.contains(gen)) {
                                genes.set(gen, (int) (Math.random() * 7));
                                used.add(gen);
                                i++;
                            }
                        }

                        int activeGen = (int) (Math.random() * geneLength);
                        Animal child = new Animal(position, new Genes(genes, activeGen), 2 * energyLose);

                        map.place(child);
                        animals.add(child);

                        animal1.addOffSpring();
                        animal2.addOffSpring();
                    }
                }
            }
        }
    }

    public void handleFire(){
        if (fires != null && !fires.isEmpty()) {
            List<Fire> firesCopy = new ArrayList<>(fires);
            for (Fire fire : firesCopy) {
                deleteAnimalsFromPosition(fire.getPosition());
                if (fire.getDaysLeft() == 0) {
                    fires.remove(fire); // Usuwanie ognia z oryginalnej kolekcji
                    ((GrassField) map).removeGrass(fire.getPosition());
                }

                List positionsOfNewFires = fire.spread((GrassField) map);
                for (Object pos : positionsOfNewFires) {
                    deleteAnimalsFromPosition(fire.getPosition());
                    fires.add(new Fire((Vector2d) pos, 5));
                }
                fire.decreDays();
        }
    }}

    public void deleteAnimalsFromPosition(Vector2d position) {
        for (int i=0; i<animals.size();i++){
            Animal animal = animals.get(i);
            if (animal.getPosition().equals(position)) {
                stats.recordDeadAnimal(animal.getAge());
                animals.remove(animal);
                map.deleteAnimal(animal);
            }
        }
    }
    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (isRunning) {

                //umieranie
                Iterator<Animal> iterator = animals.iterator();
                while (iterator.hasNext()) {
                    Animal animal = iterator.next();
                    if (animal.getEnergy() <= 0) {
                        stats.recordDeadAnimal(animal.getAge());
                        iterator.remove();
                        map.deleteAnimal(animal);
                    }
                }

                //pozary
                if (selectedMapType.equals("FireMap")) {
                    handleFire();
                    if ((day+1)%fireFrequancy==0) {
                        fires.add(new Fire(((GrassField) map).getRandomGrass(), fireDuration));
                    }
                }


                //poruszanie
                for(Animal animal : animals){
                    map.move(animal);
                }

                //jedzenie
                ((GrassField) map).handleGrassConsumption();

                //noc
                try {
                    handleSex();
                } catch (IncorrectPositionException e) {
                    throw new RuntimeException(e);
                }

                //rosniecie roslinek
                ((GrassField) map).addGrass(numberOfDailyPlants);

                //stuff
                presenter.updateTrackedAnimalInfo();
                presenter.updateStatistics();
                day++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void pausing() {
        isRunning = !isRunning;
    }

    public boolean isRunning(){return isRunning;}

    public int getDay() {
        return day;
    }

    public int getEnergyNeededToCopulate() {
        return energyNeededToCopulate;
    }
}
