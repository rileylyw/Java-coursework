package edu.uob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class GameState {
    private HashMap<String, Location> locations = new HashMap<>();
    private TreeMap<String, HashSet<GameAction>> actions = new TreeMap<>();

    public void addLocation(String locationName, Location locationToAdd) {
        this.locations.put(locationName, locationToAdd);
    }


}
