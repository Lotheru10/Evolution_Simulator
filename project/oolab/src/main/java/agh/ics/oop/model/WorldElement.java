package agh.ics.oop.model;

public interface WorldElement {
    /*
    Zwraca swoją interpretację znakową na mapie
     */
    String toString();

    /*
    zwraca pozycję obiektu na mapie
     */
    Vector2d getPosition();

}