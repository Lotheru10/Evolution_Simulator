package agh.ics.oop.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    public boolean precedes(Vector2d other) {
        return x <= other.getX() && y <= other.getY();
    }
    public boolean follows(Vector2d other) {
        return x >= other.getX() && y >= other.getY();
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.getX(), y + other.getY());
    }
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.getX(), y - other.getY());
    }
    public Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }
    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public ArrayList<Vector2d> surrounding(){
        ArrayList<Vector2d> surr = new ArrayList<Vector2d>();
        surr.add(new Vector2d(this.x-1, this.y));
        surr.add(new Vector2d(this.x+1, this.y));
        surr.add(new Vector2d(this.x, this.y+1));
        surr.add(new Vector2d(this.x, this.y-1));
        return surr;
    }
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return x == that.getX() && y == that.getY();
    }
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}