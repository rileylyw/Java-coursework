package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GameController {
    private final GameState currentGame;
    private final ArrayList<String> tokens = new ArrayList<>();
    private final StringBuilder str = new StringBuilder();
    private int index = 0;

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
        index++;
        //TODO: switch for actions (look, goto...)
        return switch (tokens.get(index).toLowerCase()) {
            case "look" -> look();
            case "goto" -> goTo();
            case "get", "drop" -> manipulateArtefacts();
            case "inv", "inventory" -> inventory();
            default -> "That's not a verb I recognize.";
        };

    }

    public String inventory(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        String playerName = currentPlayer.getPlayerName();
        String currentLocation = currentGame.getCurrentLocation();
        Location loc = currentGame.getLocation(currentLocation);
        str.append(playerName + " is currently carrying: \n");
        HashMap<String, String> artefacts = currentPlayer.getArtefacts();
        for(String item: artefacts.keySet()){
            str.append("A " + item + "\n");
        }
        return str.toString();
    }

    public String manipulateArtefacts(){
        index++;
        if(tokens.size()<=(index)){
            return "Missing artefact name";
        }
        String artefactToMove = tokens.get(index);
        Player currentPlayer = currentGame.getCurrentPlayer();
        String currentLocation = currentGame.getCurrentLocation();
        Location loc = currentGame.getLocation(currentLocation);
        if(Objects.equals(tokens.get(index - 1), "drop")) {
            return drop(currentPlayer, artefactToMove, loc);
        }
        else if(Objects.equals(tokens.get(index - 1), "get")){
            return get(currentPlayer, artefactToMove, loc);
        }
        return "OK";
    }

    public String drop(Player currentPlayer, String artefactToMove, Location loc){
        HashMap<String, String> artefacts =  currentPlayer.getArtefacts();
        if(artefacts.containsKey(artefactToMove)){
            Artefact droppedArtefact = new Artefact(artefactToMove, artefacts.get(artefactToMove));
            loc.addEntity(droppedArtefact);
            currentPlayer.dropArtefact(artefactToMove);
            str.append("You dropped " + artefactToMove + ".");
        }
        return str.toString();
    }

    public String get(Player currentPlayer, String artefactToMove, Location loc){
        ArrayList<GameEntity> entities = loc.getEntities();
        for(GameEntity entity: entities) {
            if(Objects.equals(entity.getName(), artefactToMove)){
                if (entity instanceof Artefact) {
                    currentPlayer.addArtefact(entity.getName(), entity.getDescription());
                    str.append("You picked up a " + entity.getName() + "\n");
                    loc.removeEntity(entity);
                    return str.toString();
                }
                else {
                    str.replace(0, str.length(),"You cannot pick "+ entity.getName() +" up.");
                }
            }
            else{
                str.replace(0, str.length(), "Invalid artefact name");
            }
        }
        return str.toString();
    }

    public String goTo() {
        index++;
        if(tokens.size()<=(index)){
            return "Missing location";
        }
        String nextLocation = tokens.get(index);
        if(currentGame.locationExists(nextLocation)){
            currentGame.setCurrentLocation(nextLocation);
            return look();
        }
        else{
            return "Location does not exist.";
        }
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
