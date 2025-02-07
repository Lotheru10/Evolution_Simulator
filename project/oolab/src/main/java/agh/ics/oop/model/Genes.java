package agh.ics.oop.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Genes {
    private Integer currentGene;
    private final ArrayList<Integer> genotype;
    private final boolean iScrazy;
    private final static int CRAZY = 2;

    public Genes(ArrayList<Integer> genotype, Integer currentGene) {
        this.genotype = genotype;
        this.currentGene = currentGene;
        this.iScrazy = false;
    }

    public Genes(ArrayList<Integer> genotype, int currentGene, boolean iScrazy) {
        this.genotype = genotype;
        this.currentGene = currentGene;
        this.iScrazy = iScrazy;
    }

    public ArrayList<Integer> getGenotype() {
        return this.genotype;
    }

    public Integer getGene() {
        return genotype.get(currentGene);
    }

    public void nextGene() {
        Random random = new Random();

        if (iScrazy) {
            int chance = random.nextInt(10);

            if (chance < CRAZY) {
                currentGene = random.nextInt(genotype.size());
            } else {
                currentGene = (currentGene + 1) % genotype.size();
            }
        } else {
            currentGene = (currentGene + 1) % genotype.size();
        }
    }

}
