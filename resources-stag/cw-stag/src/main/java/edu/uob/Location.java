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
//        for(GameEntity x: entities){
//            System.out.println(x.getName());
//        }
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }

    public void removeEntity(GameEntity entityToRemove){
        entities.remove(entityToRemove);
    }
}
