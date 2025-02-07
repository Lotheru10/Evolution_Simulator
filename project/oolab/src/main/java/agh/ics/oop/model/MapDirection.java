package agh.ics.oop.model;

public enum MapDirection {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;
    private final static Vector2d N_VECTOR = new Vector2d(0,1);
    private final static Vector2d NE_VECTOR = new Vector2d(1,1);
    private final static Vector2d E_VECTOR = new Vector2d(1,0);
    private final static Vector2d SE_VECTOR = new Vector2d(1,-1);
    private final static Vector2d S_VECTOR = new Vector2d(0,-1);
    private final static Vector2d SW_VECTOR = new Vector2d(-1,-1);
    private final static Vector2d W_VECTOR = new Vector2d(-1,0);
    private final static Vector2d NW_VECTOR = new Vector2d(-1,1);

    public String toString() {
        return switch(this) {
            case N -> "N";
            case E -> "E";
            case S-> "S";
            case W -> "W";
            case NE -> "NE";
            case SE -> "SE";
            case NW -> "NW";
            case SW -> "SW";

        };
    }
    public int toMove(){
        return switch(this){
            case N -> 0;
            case NE -> 1;
            case E -> 2;
            case SE -> 3;
            case S -> 4;
            case SW -> 5;
            case W -> 6;
            case NW -> 7;
        };
    }
    public static MapDirection toDirection(int revolution){
        return switch(revolution){
            case 0 -> N;
            case 1 -> NE;
            case 2 -> E;
            case 3 -> SE;
            case 4 -> S;
            case 5 -> SW;
            case 6 -> W;
            case 7 -> NW;


            default -> throw new IllegalStateException("Unexpected value: " + revolution);
        };
    }

    public Vector2d toUnitVector(){
        return switch(this){
            case N -> N_VECTOR;
            case NE -> NE_VECTOR;
            case E -> E_VECTOR;
            case SE -> SE_VECTOR;
            case S -> S_VECTOR;
            case SW -> SW_VECTOR;
            case W -> W_VECTOR;
            case NW -> NW_VECTOR;
        };
    }
}