package edu.uob;

import java.util.*;

public class GameController {
    private final GameState currentGame;
    private final ArrayList<String> tokens = new ArrayList<>();
    private final StringBuilder str = new StringBuilder();
    private ArrayList<String> verbs = new ArrayList<>();

    public GameController(GameState currentGame){
        this.currentGame = currentGame;
        List<String> builtin = Arrays.asList("look", "goto", "get", "drop", "inv", "inventory", "health");
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
        tokenize(command);
        String playerName = tokens.get(0).substring(0, tokens.get(0).length()-1);
        currentGame.setCurrentPlayer(playerName);
        for(String token: tokens){ // use axe to chop tree
            if(verbs.contains(token)){
                return checkVerb(token);
            }
        }
        return "TEST";
    }

    public String checkVerb(String token){
        return switch (token) {
            case "look" -> look();
            case "goto" -> goTo();
            case "get" -> get();
            case "drop" -> drop();
            case "inv", "inventory" -> inventory();
            case "health" -> health();
            default -> otherActions(token);
        };
    }

    public String health(){
        String health = String.valueOf(currentGame.getCurrentPlayer().getHealth());
        str.append(health);
        return str.toString();
    }

    public String otherActions(String token){
        TreeMap<String, HashSet<GameAction>> actions =  currentGame.getActions();
        HashSet<GameAction> corrActions;
        Player currentPlayer = currentGame.getCurrentPlayer();
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        HashMap<String, String> artefacts = currentPlayer.getArtefacts();
        HashMap<String, Location> loc = currentGame.getLocations();
        if(actions.containsKey(token)){
            corrActions = actions.get(token);
        }
        else{
            return "That's not a verb I recognize.";
        }
        checkAction(corrActions, artefacts, currentPlayer, currentLocation, loc);
        return str.toString();
    }

    public boolean checkAction(HashSet<GameAction> corrActions, HashMap<String, String> artefacts,
                            Player currentPlayer, Location currentLocation, HashMap<String, Location> loc){
        for(GameAction action: corrActions){
            ArrayList<String> subjects = action.getSubjects();
            ArrayList<String> consumed = action.getConsumed();
            ArrayList<String> produced = action.getProduced();

            for(String token: tokens){ //cut the tree
                if(subjects.contains(token)){ //tree
                    if(requiresArtefact(action,artefacts, currentPlayer, loc)){
                        return true;
                    }
                    if(interactWithSubjects(action, artefacts, currentGame, token)){
                        return true;
                    }
//                    if(artefacts.containsKey(consumed.get(0))) { //open trapdoor
//                        if(!produced.isEmpty() && loc.containsKey(produced.get(0))){ //is location
//                            currentPlayer.dropArtefact(consumed.get(0));
//                            currentGame.addPath(currentGame.getCurrentLocation(), produced.get(0));
//                            str.append(action.getNarration());
//                        } //TODO if no have artefact to open
//                        else if(Objects.equals(produced.get(0), "health")){
//                            if(currentPlayer.getHealth() < 3) {
//                                currentPlayer.dropArtefact(consumed.get(0));
////                                subjects.remove(consumed.get(0));
//                                currentPlayer.increaseHealth();
//                                str.append(action.getNarration());
//                            }
//                            else{
//                                str.append("Health is already full.");
//                            }
//                        }
//                    }
//                    else if(currentLocation.entityExists(token)){//if loc has subject
//                        ArrayList<String> tempSubjects = subjects; //cut tree
//                        tempSubjects.retainAll(artefacts.keySet()); //if player has subject needed
//                        if(!tempSubjects.isEmpty()) {
//                            currentPlayer.dropArtefact(tempSubjects.get(0));
//                            currentLocation.removeEntityByName(token);
//                            currentLocation.addEntityFromStoreroom(produced.get(0), currentGame);
//                            str.append(action.getNarration());
//                        }
//                        else if(Objects.equals(consumed.get(0), "health")){
//                            System.out.println("HERE");
//                            System.out.println("===");
//                            System.out.println("SUB "+subjects);
//                            System.out.println("consumed "+consumed);
//                            System.out.println("producred "+produced);
//                            System.out.println("artefact "+artefacts);
//                            System.out.println("===");
//                            if(currentPlayer.getHealth() > 1) {
//                                currentPlayer.decreaseHealth();
//                                str.append(action.getNarration());
//                            }
//                            else if(currentPlayer.getHealth() == 1){
//                                for(String artefact: artefacts.keySet()){
//                                    currentPlayer.dropArtefact(artefact);
//                                }
//                                currentGame.setCurrentLocation(currentGame.getBeginningLocation());
//                                //TODO
//                                str.append("You died and lost all of your items, you must return to the start of the game");
//                            }
//                        }
//                    }

                }
            }
        }
        return false;
    }

    public boolean requiresArtefact(GameAction action, HashMap<String, String> artefacts,
                                    Player currentPlayer,
                                    HashMap<String, Location> loc){
        ArrayList<String> subjects = action.getSubjects();
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        if(artefacts.containsKey(consumed.get(0))) { //open trapdoor with key
            if(!produced.isEmpty() && loc.containsKey(produced.get(0))){ //is location
                currentPlayer.dropArtefact(consumed.get(0));
                String currentLoc = currentGame.getCurrentLocation();
                currentGame.addPath(currentLoc, produced.get(0));
                str.append(action.getNarration());
                return true;
            }
            else if(Objects.equals(produced.get(0), "health")){ //TODO potion drink once??
                if(currentPlayer.getHealth() < 3) {
                    currentPlayer.dropArtefact(consumed.get(0));
//                                subjects.remove(consumed.get(0));
                    currentPlayer.increaseHealth();
                    str.append(action.getNarration());
                }
                else{
                    str.append("Health is already full.");
                }
                return true;
            }
        }
        else if (Objects.equals(consumed.get(0), "health")){
            if(currentPlayer.getHealth() > 1) {
                currentPlayer.decreaseHealth();
                str.append(action.getNarration());
            }
            else if(currentPlayer.getHealth() == 1){
                HashMap<String, String> temp = (HashMap<String, String>) artefacts.clone();
                for(String artefact: temp.keySet()){
                    System.out.println(artefacts.keySet());
                    currentPlayer.dropArtefactToCurrLoc(artefact, artefacts.get(artefact), currentLocation);
                }
                currentGame.setCurrentLocation(currentGame.getBeginningLocation());
                //TODO
                currentPlayer.resetHealth();
                str.append("You died and lost all of your items, you must return to the start of the game");
            }
            return true;
        }
        else{
            str.append("You don't have "+consumed.get(0));
            return true;
        }
        return false;
    }

    public boolean interactWithSubjects(GameAction action, HashMap<String, String> artefacts,
                                        GameState currentGame, String entity){
        ArrayList<String> subjects = action.getSubjects();
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        Player currentPlayer = currentGame.getCurrentPlayer();
        if(currentLocation.entityExists(entity)){//if loc has subject
            ArrayList<String> tempSubjects = subjects; //cut tree
            tempSubjects.retainAll(artefacts.keySet()); //if player has subject needed
            if(!tempSubjects.isEmpty()) {
                currentPlayer.dropArtefact(tempSubjects.get(0));
                currentLocation.removeEntityByName(entity);
                currentLocation.addEntityFromStoreroom(produced.get(0), currentGame);
                str.append(action.getNarration());
                return true;
            }
            else if(Objects.equals(consumed.get(0), "health")){
                System.out.println("HERE");
                System.out.println("===");
                System.out.println("SUB "+subjects);
                System.out.println("consumed "+consumed);
                System.out.println("producred "+produced);
                System.out.println("artefact "+artefacts);
                System.out.println("===");
                if(currentPlayer.getHealth() > 1) {
                    currentPlayer.decreaseHealth();
                    str.append(action.getNarration());
                    return true;
                }
                else if(currentPlayer.getHealth() == 1){
                    for(String artefact: artefacts.keySet()){
                        currentPlayer.dropArtefact(artefact);
                    }
                    currentGame.setCurrentLocation(currentGame.getBeginningLocation());
                    //TODO
                    str.append("You died and lost all of your items, you must return to the start of the game");
                    return true;
                }
            }
        }
        return false;
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
//            str.replace(0, str.length(), "Missing artefact.");
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
