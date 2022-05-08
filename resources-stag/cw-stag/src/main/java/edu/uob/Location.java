package edu.uob;

import java.util.HashMap;

public class Location extends GameEntity{
    private HashMap<String, GameEntity> entities;


    public Location(String name, String description) {
        super(name, description);
    }

}
