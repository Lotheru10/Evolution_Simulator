package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
//import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap{
    protected final List<Animal> animals = new ArrayList<>();
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected int width;
    protected int height;

    protected AbstractWorldMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }
    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (!canMoveTo(animal.getPosition())) {
            throw new IncorrectPositionException(animal.getPosition());
        }
        animals.add(animal);
        notifyObservers("Placed an animal at position " + animal.getPosition());
    }
    @Override
    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        int index=animals.indexOf(animal);
        animal.move(this);
        Vector2d newPosition = animal.getPosition();
        if (!oldPosition.equals(newPosition)) {
            animals.remove(index);
            animals.add(animal);
            notifyObservers("Moved an animal to position " + newPosition);
        }
    }
    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getPosition().equals(position)) {
                return animal;
            }
        }
        return null;
    }

    public List<Animal> animalsAt(Vector2d position){
        List<Animal> residents = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getPosition().equals(position)) {
                residents.add(animal);
            }

        }
        return residents;
    }

    @Override
    public boolean canMoveTo(Vector2d position)
    {
        return position.getY()>=0 && position.getY()<height;
    }
    public boolean onEdge(Vector2d position) {
        return position.getX()==-1|| position.getX()==width;
    }

    public Vector2d edgeLoop(Vector2d position) {
        if (position.getX()==-1)
            return new Vector2d(width-1, position.getY());
        else
            return new Vector2d(0, position.getY());
    }

//    @Override
//    public Collection<WorldElement> getElements() {
//        return new ArrayList<>(animals.values());
//    }

//    @Override
//    public String toString() {
//        return visualizer.draw(new Vector2d(0,0), new Vector2d(width-1,height-1));
//    }

    @Override
    public void deleteAnimal(Animal animal) {
        animals.remove(animal);
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }


    public abstract int getFreeFieldsCount();

    public abstract int getGrassCount();

    public int getAnimalCount() {
        return animals.size();
    }
}