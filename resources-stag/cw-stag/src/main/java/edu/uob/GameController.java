package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {
    private GameState currentGame;
    private ArrayList<String> tokens = new ArrayList<>();
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
        //TODO: stringbuilder, return for look command
        if(Objects.equals(tokens.get(index), "look")){
            return look();
        }
        return "OK";
    }

    public String look() {
        Location loc = currentGame.getLocation(currentGame.getCurrentLocation());
        ArrayList<GameEntity> entities = loc.getEntities();
        for(GameEntity entity: entities){
            if(entity instanceof Artefact){
                System.out.println(entity.getName());
            }
            else if(entity instanceof Furniture){
                System.out.println("FURNITURE");
            }
            else if(entity instanceof Character){
                System.out.println("CHARACTER");
            }
        }
//        Location loc = currentGame.getLocation("cabin");
//        System.out.println(loc.getEntities().get(0).getClass().getSimpleName());
//        if(loc.getEntities().get(0) instanceof Artefact){
//            System.out.println("HI");
//        }
        return "OK";
    }
}
