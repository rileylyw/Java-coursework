package edu.uob;

import java.util.ArrayList;
import java.util.Objects;

public class Location extends GameEntity{
    private ArrayList<GameEntity> entities = new ArrayList<>();


    public Location(String name, String description) {
        super(name, description);
    }

    public void addEntity(GameEntity itemType) {
        entities.add(itemType);
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

    public void addEntityFromLocation(String entityToAdd, GameState currentGame, String loc) {
        ArrayList<GameEntity> x = currentGame.getLocation(loc).getEntities();
        ArrayList<GameEntity> temp = (ArrayList<GameEntity>) x.clone();
        for (GameEntity item : temp) {
            if (Objects.equals(entityToAdd, item.getName())) {
                if (item instanceof Artefact) {
                    Artefact newEntity = new Artefact(entityToAdd, item.getDescription());
                    entities.add(newEntity);
                } else if (item instanceof Furniture) {
                    Furniture newEntity = new Furniture(entityToAdd, item.getDescription());
                    entities.add(newEntity);
                } else if (item instanceof Character) {
                    Character newEntity = new Character(entityToAdd, item.getDescription());
                    entities.add(newEntity);
                }
            }
        }
    }
}