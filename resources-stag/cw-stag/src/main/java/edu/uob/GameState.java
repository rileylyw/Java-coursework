package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class GameState {
    private HashMap<String, Location> locations = new HashMap<>();
    private HashMap<String, String> paths = new HashMap<>();
    private TreeMap<String, HashSet<GameAction>> actions = new TreeMap<>();
    private Player currentPlayer;
    private String currentLocation;

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setCurrentPlayer(String playerName) {
        if(currentPlayer == null) {
            currentPlayer = new Player(playerName);
            this.currentPlayer = currentPlayer;
        }
        else{
            currentPlayer.setPlayerName(playerName);
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void addLocation(String locationName, Location locationToAdd) {
        this.locations.put(locationName, locationToAdd);
    }

    public Location getLocation(String locationName) {
        return locations.get(locationName);
    }

    public void addPath(String fromName, String toName) {
        paths.put(fromName, toName);
    }

    public HashMap<String, String> getPaths() {
        return paths;
    }

    public void setActions(String keyword, HashSet<GameAction> actionSet) {
        actions.put(keyword, actionSet);
    }

    public TreeMap<String, HashSet<GameAction>> getActions() {
        return actions;
    }

    public void readInEntities(GameState currentGame, ArrayList<Graph> locations) {
        for (Graph location : locations) {
            String locationName = location.getNodes(false).get(0).getId().getId();
            if(this.currentLocation == null){setCurrentLocation(locationName);}
            String locationDesc = location.getNodes(false).get(0).getAttribute("description");
            Location locationToAdd = new Location(locationName, locationDesc);
            currentGame.addLocation(locationName, locationToAdd);
            ArrayList<Graph> items = location.getSubgraphs();
            for (Graph item : items) {
                String itemType = item.getId().getId();
                if (item.getNodes(false).size() > 0) {
                    addToEntities(item, itemType, currentGame, locationName);
                }
            }
        }
    }

    public void addToEntities(Graph item, String itemType, GameState currentGame, String locationName){
        String itemName = item.getNodes(false).get(0).getId().getId();
        String itemDesc = item.getNodes(false).get(0).getAttribute("description");
        switch (itemType) {
            case "artefacts":
                Artefact artefactToAdd = new Artefact(itemName, itemDesc);
                currentGame.getLocation(locationName).addEntity(artefactToAdd);
                break;
            case "furniture":
                Furniture furnitureToAdd = new Furniture(itemName, itemDesc);
                currentGame.getLocation(locationName).addEntity(furnitureToAdd);
                break;
            case "characters":
                Character characterToAdd = new Character(itemName, itemDesc);
                currentGame.getLocation(locationName).addEntity(characterToAdd);
                break;
        }
    }

    public void readInPaths(GameState currentGame, ArrayList<Edge> paths){
        for(Edge path: paths){
            String fromName = path.getSource().getNode().getId().getId();
            String toName = path.getTarget().getNode().getId().getId();
            currentGame.addPath(fromName, toName);
        }
    }

    public void readInActions(NodeList actions, int i, GameState currentGame){
        HashSet<GameAction> actionSet = new HashSet<>();
        GameAction actionToAdd = new GameAction();
        Element action = (Element) actions.item(i);
        Element triggers = (Element) action.getElementsByTagName("triggers").item(0);
        Element subjects = (Element) action.getElementsByTagName("subjects").item(0);
        Element consumed = (Element) action.getElementsByTagName("consumed").item(0);
        Element produced = (Element) action.getElementsByTagName("produced").item(0);
        for(int j=0; j<subjects.getElementsByTagName("entity").getLength(); j++) {
            String subjectEntity = subjects.getElementsByTagName("entity").item(j).getTextContent();
            actionToAdd.addSubject(subjectEntity);
        }
        for(int j=0; j<consumed.getElementsByTagName("entity").getLength(); j++) {
            String consumedEntity = consumed.getElementsByTagName("entity").item(j).getTextContent();
            actionToAdd.addConsumed(consumedEntity);
        }
        for(int j=0; j<produced.getElementsByTagName("entity").getLength(); j++) {
            String producedEntity = produced.getElementsByTagName("entity").item(j).getTextContent();
            actionToAdd.addProduced(producedEntity);
        }
        actionSet.add(actionToAdd);
        for(int j=0; j<triggers.getElementsByTagName("keyword").getLength(); j++) {
            String keyword = triggers.getElementsByTagName("keyword").item(j).getTextContent();
            currentGame.setActions(keyword, actionSet);
        }
    }


}
