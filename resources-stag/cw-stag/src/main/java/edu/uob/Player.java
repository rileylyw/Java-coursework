package edu.uob;

import java.util.HashMap;

public class Player {
    String playerName;
    HashMap<String, String> artefacts = new HashMap<>();
    private int health = 3;

    public int getHealth() {
        return health;
    }

    public void increaseHealth() {
        this.health += 1;
    }

    public void decreaseHealth() {
        this.health -= 1;
    }

    public void resetHealth(){
        this.health = 3;
    }

    public Player(String name) {
        playerName = name;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addArtefact(String artefactName, String artefactDesc) {
        this.artefacts.put(artefactName, artefactDesc);
    }

    public void dropArtefact(String artefactToDrop){
        this.artefacts.remove(artefactToDrop);
    }

    public void dropArtefactToCurrLoc(String artefactToAdd, String artefactDesc, Location currentLocation){
        currentLocation.addArtefactToCurrLoc(artefactToAdd, artefactDesc);
        dropArtefact(artefactToAdd);
    }

    public HashMap<String, String> getArtefacts() {
        return artefacts;
    }

    public boolean hasArtefact(String artefact){
        if(artefacts.containsKey(artefact)){
            return true;
        }
        return false;
    }

    //TODO: inventory list
}
