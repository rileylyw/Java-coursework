package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Location extends GameEntity{
    private ArrayList<GameEntity> entities = new ArrayList<>();


    public Location(String name, String description) {
        super(name, description);
    }

    public void addEntity(GameEntity itemType) {
//        if (!entityExists(itemType)) {
        entities.add(itemType);
//        }
//        for(GameEntity x: entities){
//            System.out.println(x.getName());
//        }
    }

    public boolean entityExists(GameEntity entity){
        for(GameEntity x: entities){
            if(Objects.equals(x.getName(), entity.getName())){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }

    public void removeEntity(GameEntity entityToRemove){
        entities.remove(entityToRemove);
    }
}
