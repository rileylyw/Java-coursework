package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameController {
    private final GameState currentGame;
    private final ArrayList<String> tokens = new ArrayList<>();
    private final StringBuilder str = new StringBuilder();

    public GameController(GameState currentGame){
        this.currentGame = currentGame;
    }

    public GameState getCurrentGame() {
        return currentGame;
    }

    public void tokenize(String command){
        tokens.addAll(List.of(command.split("\\s+")));
    }

    public String handleCommand(String command){
        tokenize(command);
        String playerName = tokens.get(0).substring(0, tokens.get(0).length()-1);
        currentGame.setCurrentPlayer(playerName);
        return switch (tokens.get(1).toLowerCase()) {
            case "look" -> look();
            case "goto" -> goTo();
            case "get" -> get();
            case "drop" -> drop();
            case "inv", "inventory" -> inventory();
            //TODO: other actions --> gamestate, treemap of actions
            default -> "That's not a verb I recognize.";
        };

    }

    public String inventory(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        String playerName = currentPlayer.getPlayerName();
        str.append(playerName + " is currently carrying: \n");
        HashMap<String, String> artefacts = currentPlayer.getArtefacts();
        for(String item: artefacts.keySet()){
            str.append("A " + item + "\n");
        }
        return str.toString();
    }

    public String drop(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        String currentLocation = currentGame.getCurrentLocation();
        Location loc = currentGame.getLocation(currentLocation);
        HashMap<String, String> artefacts =  currentPlayer.getArtefacts();
        if(currentPlayer.getArtefacts().isEmpty()){
            return "Your inventory is empty, nothing can be dropped.";
        }
        for(String artefact: artefacts.keySet()){
            if(tokens.contains(artefact)){
                Artefact droppedArtefact = new Artefact(artefact, artefacts.get(artefact));
                loc.addEntity(droppedArtefact);
                currentPlayer.dropArtefact(artefact);
                str.append("You dropped the " + artefact + ".");
            }
            else{
                str.replace(0, str.length(), "Artefact does not exist in your inventory.");
            }
        }
        return str.toString();
    }

    public String get(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        String currentLocation = currentGame.getCurrentLocation();
        Location loc = currentGame.getLocation(currentLocation);
        ArrayList<GameEntity> entities = loc.getEntities();
        for(GameEntity entity: entities){
            if(tokens.contains(entity.getName())){
                if (entity instanceof Artefact) {
                    currentPlayer.addArtefact(entity.getName(), entity.getDescription());
                    str.append("You picked up a " + entity.getName() + "\n");
                    loc.removeEntity(entity);
                    return str.toString();
                }
                else {
                    return str.replace(0, str.length(),"You cannot pick "+ entity.getName() +" up.").toString();
                }
            }
            str.replace(0, str.length(), "Missing artefact.");
        }
        return str.toString();
    }

    public String goTo() {
        HashMap<String, ArrayList<String>> paths = currentGame.getPaths();
        String fromPath = currentGame.getCurrentLocation();
        ArrayList<String> possibleLoc = paths.get(fromPath);
        for(String loc: possibleLoc){
            if(tokens.contains(loc)){
                currentGame.setCurrentLocation(loc);
                return look();
            }
            else{
                str.replace(0, str.length(),"Location does not exist. / Cannot access.");
            }
        }
        return str.toString();
    }

    public String look() {
        String currentLocation = currentGame.getCurrentLocation();
        Location loc = currentGame.getLocation(currentLocation);
        str.append("You are in " +
                loc.getDescription() + ". " +
                "You can see: \n");
        ArrayList<GameEntity> entities = loc.getEntities();
        for(GameEntity entity: entities){
            if(entity instanceof Artefact){
                str.append("A " + entity.getDescription() + "\n");
            }
            else if(entity instanceof Furniture){
                str.append("A " + entity.getDescription() + "\n");
            }
            else if(entity instanceof Character){
                str.append("A " + entity.getDescription() + "\n");
            }
        }
        str.append("You can access from here: \n");
        ArrayList<String> validLocations = currentGame.getToLocations(currentLocation);
        for(String validLocation: validLocations){
            str.append(validLocation + "\n");
        }
        return str.toString();
    }
}
