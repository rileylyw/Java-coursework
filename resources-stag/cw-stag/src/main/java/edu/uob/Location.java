package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class Location extends GameEntity{
    private ArrayList<GameEntity> entities = new ArrayList<>();


    public Location(String name, String description) {
        super(name, description);
    }

    public void addEntity(GameEntity itemType) {
        entities.add(itemType);
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }
}
