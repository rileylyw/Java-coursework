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

    public boolean entityExists(String entity){
        boolean exist = false;
        for(GameEntity x: entities){
            if(Objects.equals(x.getName(), entity)){
                exist = true;
            }
        }
        return exist;
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    }

    public void removeEntity(GameEntity entityToRemove){
        entities.remove(entityToRemove);
    }

    public void removeEntityByName(String entityToRemove){
        entities.removeIf(x -> Objects.equals(x.getName(), entityToRemove));
    }

    public GameEntity getEntityByName(String entityName){
        GameEntity target = null;
        for(GameEntity x: entities){
            if(Objects.equals(x.getName(), entityName)){
                target = x;
            }
        }
        return target;
    }

    public void addArtefactToCurrLoc(String artefactToAdd, String artefactDesc){
        Artefact newEntity = new Artefact(artefactToAdd, artefactDesc);
        entities.add(newEntity);
    }

    public void addEntityFromStoreroom(String entityToAdd, GameState currentGame){
        ArrayList<GameEntity> x = currentGame.getLocation("storeroom").getEntities();
        for(GameEntity item: x){
            if(Objects.equals(entityToAdd, item.getName())){
                Artefact newEntity = new Artefact(entityToAdd, item.getDescription());
                entities.add(newEntity);
            }
        }
    }
}
