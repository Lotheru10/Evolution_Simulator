package agh.ics.oop.model;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final List<Vector2d> positions = new ArrayList<>();

    public RandomPositionGenerator(int maxWidth,int maxHeight, int grassCount, HashMap<Vector2d, Grass> currentGrass) {
        for (int i = 0; i < grassCount; i++) {
            double equator = Math.random();
            int x = (int) (Math.random() * maxWidth);
            int y;
            if (equator < 0.8) {
                int lowerBound = (int) (maxHeight / 2 - 0.1 * maxHeight);
                int upperBound = (int) (maxHeight / 2 + 0.1 * maxHeight);
                y = lowerBound + (int) (Math.random() * (upperBound - lowerBound + 1));

            } else {
                Random rd = new Random();
                if (rd.nextBoolean()) {
                    y = (int) (Math.random() * (maxHeight / 2 - 0.1 * maxHeight));
                } else {
                    int lowerBound = (int) (maxHeight / 2 + 0.1 * maxHeight);
                    int upperBound = maxHeight;
                    y = lowerBound + (int) (Math.random() * (upperBound - lowerBound + 1));
                }

            }
            Vector2d pos = new Vector2d(x, y);
            if (!currentGrass.containsKey(pos)) this.positions.add(pos);
        }
    }
    @Override
    public Iterator<Vector2d> iterator() {
        return positions.iterator();
    }
}
