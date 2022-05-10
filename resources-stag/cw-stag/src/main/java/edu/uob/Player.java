package edu.uob;

import java.util.HashMap;

public class Player {
    String playerName;
    HashMap<String, String> artefacts = new HashMap<>();


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

    public HashMap<String, String> getArtefacts() {
        return artefacts;
    }

    //TODO: inventory list
}
