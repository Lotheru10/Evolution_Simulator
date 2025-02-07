package agh.ics.oop.model;


import java.util.ArrayList;

public class Animal implements WorldElement{
    private final int id;
    private static int idCounter = 0;

    private MapDirection direction;
    private Vector2d position;
    private int energy;
    private final Genes genes;

    private int age;
    private int numberOfOffspring;
    private int numberOfGrassEaten;

    public Animal(Vector2d position, Genes genes, int energy){
        this.id = idCounter++;

        this.direction = MapDirection.toDirection((int) Math.random()*8);
        this.position = position;
        this.energy=energy;
        this.genes = genes;

        this.age = 0;
        this.numberOfGrassEaten = 0;
        this.numberOfOffspring = 0;

        //System.out.println(genes.getGenotype());
    }

    @Override
    public String toString() {
        int wyswietlanieZwierza = energy;
        //return String.valueOf(energy);
        //return String.valueOf(this.genes.getGene());
//        return switch (direction) {
//            case N -> "N";
//            case E -> "E";
//            case S-> "S";
//            case W -> "W";
//            case NE -> "NE";
//            case SE -> "SE";
//            case NW -> "NW";
//            case SW -> "SW";
//        };
        return String.valueOf(wyswietlanieZwierza);
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public void addEnergy(int value) {
        this.energy += value;
    }
    public void addOffSpring(){
        this.numberOfOffspring+=1;
    }

    public void move(WorldMap map){
        Vector2d potentialNewPosition;

        this.energy=this.energy-1;
        this.age++;
        this.direction = this.direction.toDirection( (this.direction.toMove() + this.genes.getGene()) % 8 );
        this.genes.nextGene();

        potentialNewPosition = this.position.add(this.direction.toUnitVector());

        if (map.canMoveTo(potentialNewPosition)) {
            if (map.onEdge(potentialNewPosition)){
                this.position = map.edgeLoop(potentialNewPosition);
            }
            else this.position = potentialNewPosition;
        }

    }

    //Gettery
    @Override
    public Vector2d getPosition() {return this.position;}
    public MapDirection getDirection() {return this.direction;}
    public int getEnergy() {return energy;}
    public int getId() {return id;}
    public int getNumberOfGrassEaten() {return numberOfGrassEaten;}
    public int getNumberOfOffspring() {return numberOfOffspring;}
    public int getAge() {return age;}
    public ArrayList<Integer> getGenes(){
        return genes.getGenotype();
    }
    public int getGene(){return genes.getGene();}

    public void setEnergy(int energy){
        this.energy=energy;
    }

}