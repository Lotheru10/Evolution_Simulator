package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fire {
    private Vector2d position;
    private int daysLeft;

    public Fire(Vector2d position, int daysLeft){
        this.position=position;
        this.daysLeft=daysLeft;
    }
    public List<Vector2d> spread(GrassField map ) {
        List<Vector2d> surr = position.surrounding();
        List<Vector2d> realSpread = new ArrayList<>();

        for (Vector2d potenPos : surr) {
            if (map.grassAt(position)) {
                if (map.canMoveTo(potenPos)) {
                    if (map.onEdge(potenPos)) {
                        realSpread.add(map.edgeLoop(potenPos));
                    }
                }
                realSpread.addFirst(potenPos);
            }
        }
        return realSpread;
    }

    public void decreDays(){
        this.daysLeft-=1;
    }


    public Vector2d getPosition() {
        return position;
    }

    public int getDaysLeft() {
        return daysLeft;
    }
}
