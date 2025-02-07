package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<Integer> parse(String[] args) {
        List<Integer> directions = new ArrayList<>();

        for (String arg : args) {
            directions.add(Integer.valueOf(arg));
        }

        return directions;
    }
}