package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class GameState {
    private HashMap<String, Location> locations = new HashMap<>();
    private HashMap<String, String> paths = new HashMap<>();
    private TreeMap<String, HashSet<GameAction>> actions = new TreeMap<>();

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


    public void readInEntities(GameState currentGame, ArrayList<Graph> locations) {
        for (Graph location : locations) {
            String locationName = location.getNodes(false).get(0).getId().getId();
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

}
