package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.*;
import java.util.stream.Collectors;


public class GrassField extends AbstractWorldMap {
    private Map<Vector2d, Grass> grass = new HashMap<>();

    public GrassField(int width, int height, int grassCount) {
        super(width, height);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, grassCount, (HashMap) grass);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void addGrass(int grassCount) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, grassCount, (HashMap<Vector2d, Grass>) grass);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
    }
    public void removeGrass(Vector2d position) {
        grass.remove(position);
    }
    public boolean grassAt(Vector2d position) {
        if (grass.get(position)!=null){
            return true;
        }
        return false;
    }
    public Vector2d getRandomGrass(){
        List<Vector2d> keys = new ArrayList<>(grass.keySet());

        Random random = new Random();
        int randomIndex = random.nextInt(keys.size());


        Vector2d randomKey = keys.get(randomIndex);
        return randomKey;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement something = super.objectAt(position);
        if (something != null) return something;
        return grass.get(position);
    }

//    @Override
//    public Collection<WorldElement> getElements() {
//        Collection<WorldElement> elements = super.getElements();
//        elements.addAll(grass.values());
//        return elements;
//    }

    public List<Animal> getStrongestAnimals(Vector2d position, int n) {
        Random random = new Random();
        List<Animal> residents = animals.stream()
                .filter(animal -> animal.getPosition().equals(position))
                .collect(Collectors.toList());
        if (residents.isEmpty()) {
            return Collections.emptyList();
        }
        residents.sort((a1, a2) -> {
            int energyComparison = Integer.compare(a2.getEnergy(), a1.getEnergy());
            if (energyComparison != 0) {
                return energyComparison;
            }
            int ageComparison = Integer.compare(a2.getAge(), a1.getAge());
            if (ageComparison != 0) {
                return ageComparison;
            }
            int offspringComparison = Integer.compare(a2.getNumberOfOffspring(), a1.getNumberOfOffspring());
            if (offspringComparison != 0) {
                return offspringComparison;
            }
            return random.nextBoolean() ? 1 : -1; 
        });

        List<Animal> strongestAnimals = new ArrayList<>();
        strongestAnimals.add(residents.get(0));

        if (n == 2 && residents.size()>1) {
            strongestAnimals.add(residents.get(1));
        }
        return strongestAnimals;
    }


    public void handleGrassConsumption() {
        List<Vector2d> grassPositions = new ArrayList<>(grass.keySet());
        for (Vector2d grassPosition : grassPositions) {
            if (grass.containsKey(grassPosition)) {
                List<Animal> animalsAtPosition = getStrongestAnimals(grassPosition, 1);
                if (!animalsAtPosition.isEmpty()) {
                    Animal strongestAnimal = animalsAtPosition.getFirst();
                    strongestAnimal.addEnergy(Simulation.getEnergyGainFromGrass());
                    grass.remove(grassPosition);
                    notifyObservers("Grass eaten at position " + grassPosition);

                }
            }
        }

    }
    public void addGrass(Vector2d position){
        grass.put(position, new Grass(position));
    }
    @Override
    public int getFreeFieldsCount() {
        int freeCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d position = new Vector2d(x, y);
                Object object = this.objectAt(position);
                if (!(object instanceof Grass) && !(object instanceof Animal)) {
                    freeCount++;
                }
            }
        }
        return freeCount;
    }

    @Override
    public int getGrassCount(){
        int grassCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d position = new Vector2d(x, y);
                Object object = this.objectAt(position);
                if (object instanceof Grass) {
                    grassCount++;
                }
            }
        }
        return grassCount;
    }
}
