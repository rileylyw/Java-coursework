package edu.uob;

import java.util.*;

public class GameController {
    private final GameState currentGame;
    private final ArrayList<String> tokens = new ArrayList<>();
    private final StringBuilder str = new StringBuilder();
    private ArrayList<String> verbs = new ArrayList<>();

    public GameController(GameState currentGame){
        this.currentGame = currentGame;
        List<String> builtin = Arrays.asList("look", "goto", "get", "drop", "inv", "inventory");
        verbs.addAll(builtin);
        verbs.addAll(currentGame.getActions().keySet());
    }

    public GameState getCurrentGame() {
        return currentGame;
    }

    public void tokenize(String command){
        tokens.addAll(List.of(command.split("\\s+")));
    }

    public String handleCommand(String command){
        tokenize(command); //TODO: playername with space
        String playerName = tokens.get(0).substring(0, tokens.get(0).length()-1);
        currentGame.setCurrentPlayer(playerName); //TODO**: 1trigger+1subject; trapdoor open
        for(String token: tokens){ // use axe to chop tree
            if(verbs.contains(token)){
                return checkVerb(token);
            }
        }

//        return switch (tokens.get(1).toLowerCase()) {
//            case "look" -> look();
//            case "goto" -> goTo();
//            case "get" -> get();
//            case "drop" -> drop();
//            case "inv", "inventory" -> inventory();
//            default -> otherActions();
////            default -> "That's not a verb I recognize.";
//        };
        return "TEST";
    }

    public String checkVerb(String token){
        return switch (token) {
            case "look" -> look();
            case "goto" -> goTo();
            case "get" -> get();
            case "drop" -> drop();
            case "inv", "inventory" -> inventory();
            default -> otherActions();
//            default -> "That's not a verb I recognize.";
        };
    }

    public String otherActions(){
        TreeMap<String, HashSet<GameAction>> actions =  currentGame.getActions();
        HashSet<GameAction> corrActions;
        String trigger = tokens.get(1).toLowerCase();
        Player currentPlayer = currentGame.getCurrentPlayer();
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        HashMap<String, String> artefacts = currentPlayer.getArtefacts();
        HashMap<String, Location> loc = currentGame.getLocations();
//        String subjectToRemove = null;

        if(actions.containsKey(trigger)){
            corrActions = actions.get(trigger);
        }
        else{
            return "That's not a verb I recognize.";
        }

        for(GameAction action: corrActions){
            ArrayList<String> subjects = action.getSubjects();
            ArrayList<String> consumed = action.getConsumed();
            ArrayList<String> produced = action.getProduced();
            HashSet temp = new HashSet(tokens);
            for(String token: tokens){ //cut the tree
                if(subjects.contains(token)){ //tree
//                    for(GameEntity x: currentLocation.getEntities()){
//                        System.out.println("H "+x.getName());
//                        System.out.println("token "+token);
//                    }
                    System.out.println(artefacts);
                    System.out.println(token);
                    System.out.println(consumed.get(0));
                    if(currentLocation.entityExists(token)){//TODO
                        for(String subject: subjects) {
                            if(artefacts.containsKey(subject)){
                                currentPlayer.dropArtefact(consumed.get(0));
//                                subjectToRemove = consumed.get(0);
//                                subjects.remove(consumed.get(0));
//                                for(GameAction x: corrActions){
//                                    x.removeSubject(consumed.get(0));
//                                }
                                currentLocation.removeEntityByName(consumed.get(0));
//                                currentGame.addEntityFromStoreroom(currentLocation.getName(), produced.get(0), currentGame);
                                currentLocation.addEntityFromStoreroom(produced.get(0), currentGame);
//                                System.out.println(produced.get(0));
//                                for(GameEntity x:currentGame.getLocation(currentGame.getCurrentLocation()).getEntities()){
//                                    System.out.println(x.getName());
//                                }
                            }
                        }
//                        ArrayList<GameEntity> x = currentLocation.getEntities();
//                        for(GameEntity y: x){
//                            System.out.println(y.getName());
//                        }
                    }

                    if(artefacts.containsKey(consumed.get(0))) { //TODO**: open trapdoor
                        System.out.println("SFOIHSDFH");
                        currentPlayer.dropArtefact(consumed.get(0));
                        subjects.remove(consumed.get(0));
                        if(loc.containsKey(produced.get(0))){ //is location
                            currentGame.addPath(currentGame.getCurrentLocation(), produced.get(0));
                        } //TODO if no have artefact to open
                        else if(isInStoreroom(produced.get(0))){

                        }
                    }
//                    for(String consumedEntity: consumed){
//                        if(subjects.contains(consumedEntity)){
//
//                        }
//                    }
                    //TODO
                }
            }
//            if(temp.containsAll(subjects)){
//                System.out.println();
//            }

        }
//        for(GameAction x: corrActions){
//            x.removeSubject(subjectToRemove);
//        }
        return "TESING";
    }

    public boolean isInStoreroom(String entity){
        ArrayList<GameEntity> storeroomEntities = currentGame.getLocation("storeroom").getEntities();
        for(GameEntity x: storeroomEntities){
            if(Objects.equals(x.getName(), entity)){
                return true;
            }
        }
        return false;
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
                    str.append("You picked up " + entity.getName() + "\n");
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
                str.append(entity.getDescription() + "\n");
            }
            else if(entity instanceof Furniture){
                str.append(entity.getDescription() + "\n");
            }
            else if(entity instanceof Character){
                str.append(entity.getDescription() + "\n");
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
