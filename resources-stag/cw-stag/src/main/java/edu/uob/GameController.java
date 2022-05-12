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
        tokenize(command); //TODO player name with spaces hyphens etc
        //TODO: multiplayer
//        String playerName = tokens.get(0).substring(0, tokens.get(0).length()-1);
        String[] temp = command.split(":");
        String playerName = temp[0];
        if(!currentGame.playerExists(playerName)){
            currentGame.addPlayer(playerName);
        }
        currentGame.setCurrentPlayer(playerName);
        System.out.println("here "+currentGame.getCurrentPlayer().playerName);
        for(String token: tokens){ // use axe to chop tree
            if(verbs.contains(token)){
                return checkVerb(token);
            }
        }
        return "No valid verb recognized";
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
        HashMap<String, String> artefacts = currentPlayer.getArtefacts();
        HashMap<String, Location> loc = currentGame.getLocations();
        if(actions.containsKey(token)){
            corrActions = actions.get(token);
//            for(GameAction x: actions.get(token)){
//                System.out.println("t "+x.getSubjects().get(0));
//            }
        }
        else{
            return "That's not a verb I recognize.";
        }
        if(!checkAction(corrActions, artefacts, currentPlayer, loc)){
            return "Invalid command";
        }
        return str.toString();
    }

    public boolean checkAction(HashSet<GameAction> corrActions, HashMap<String, String> artefacts,
                            Player currentPlayer, HashMap<String, Location> loc){
        for(GameAction action: corrActions){
            ArrayList<String> subjects = action.getSubjects();
            for(String token: tokens){ //attack elf
                if(subjects.contains(token)){ //elf
                    if(action.getConsumed().isEmpty()){
                        if(currentPlayer.hasArtefact(subjects.get(0))) {
                            str.append(action.getNarration());
                        }
                        else{
                            str.append("You don't have "+subjects.get(0));
                        }
                        return true;
                    }
                    if(requiresArtefact(action,artefacts, currentPlayer, loc, token)){
                        return true;
                    }
                    if(interactWithSubjects(action, artefacts, currentGame, token)){
                        return true;
                    };
                }
            }
        }
        return false;
    }

    public boolean requiresArtefact(GameAction action, HashMap<String, String> artefacts,
                                    Player currentPlayer,
                                    HashMap<String, Location> loc, String entity){
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> subjects = action.getSubjects();
        if(consumed.isEmpty()){return false;}
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        ArrayList<String> tempSubjects = new ArrayList<>();
        tempSubjects.addAll(subjects);
        tempSubjects.removeAll(consumed); //if player has subject needed
//        System.out.println("temp "+tempSubjects);
//        System.out.println("subjetc "+subjects);
        if(artefacts.containsKey(consumed.get(0))) { //open trapdoor with key
            if(isLocation(action, loc, currentPlayer)) {
                return true;
            }
            else if(isHealth(action, currentPlayer)){
                return true;
            }
            else if(willProduce(action, currentPlayer, currentLocation, entity)){
                return true;
            }
        }
//        else if(willConsumeHealth(action, currentPlayer, currentLocation, artefacts)){
//            return true;
//        }
        else if(!tempSubjects.isEmpty()){
            if(willExchangeItems(action, currentPlayer, currentLocation, entity, tempSubjects.get(0))){
                return true;
            }
        }
        else if(tempSubjects.isEmpty()){
             if(willExchangeItems(action, currentPlayer, currentLocation, entity, "empty")) {
                 return true;
             }
        }
//        else{
//            System.out.println("HERE");
//            str.append("You don't have "+consumed.get(0));
//            return true;
//        }
        return false;
    }

    public boolean isLocation(GameAction action, HashMap<String, Location> loc,
                              Player currentPlayer){
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        if(!produced.isEmpty() && loc.containsKey(produced.get(0))){ //is location
            currentPlayer.dropArtefact(consumed.get(0));
            String currentLoc = currentGame.getCurrentLocation();
            currentGame.addPath(currentLoc, produced.get(0));
            str.append(action.getNarration());
            return true;
        }
        return false;
    }

    public boolean isHealth(GameAction action, Player currentPlayer){
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        if(Objects.equals(produced.get(0), "health")){ //TODO potion drink once??
            if(currentPlayer.getHealth() < 3) {
                currentPlayer.dropArtefact(consumed.get(0));
                currentPlayer.increaseHealth();
                str.append(action.getNarration());
            }
            else{
                str.append("Health is already full.");
            }
            return true;
        }
        return false;
    }

    public boolean willProduce(GameAction action, Player currentPlayer,
                               Location currentLocation, String entity){
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        if(currentLocation.entityExists(entity)){
            currentPlayer.dropArtefact(consumed.get(0));
            currentLocation.addEntityFromStoreroom(produced.get(0), currentGame);
            str.append(action.getNarration());
            return true;
        }
        return false;
    }

    public boolean willConsumeHealth(GameAction action, Player currentPlayer,
                                     Location currentLocation, HashMap<String, String> artefacts){
        ArrayList<String> consumed = action.getConsumed();
//        if (Objects.equals(consumed.get(0), "health")){
//            if(currentPlayer.getHealth() > 1) {
//                currentPlayer.decreaseHealth();
//                str.append(action.getNarration());
//            }
//            else if(currentPlayer.getHealth() == 1){
//                HashMap<String, String> temp = (HashMap<String, String>) artefacts.clone();
//                for(String artefact: temp.keySet()){
//                    System.out.println(artefacts.keySet());
//                    currentPlayer.dropArtefactToCurrLoc(artefact, artefacts.get(artefact), currentLocation);
//                }
//                currentGame.setCurrentLocation(currentGame.getBeginningLocation());
//                currentPlayer.resetHealth();
//                str.append("You died and lost all of your items, you must return to the start of the game");
//            }
//            return true;
//        }
        return false;
    }

    public boolean willExchangeItems(GameAction action, Player currentPlayer,
                                     Location currentLocation, String entity,
                                    String artefactNeeded){
        ArrayList<String> consumed = action.getConsumed();
        ArrayList<String> produced = action.getProduced();
        GameEntity e = currentLocation.getEntityByName(entity);
        System.out.println("entity "+entity);
        if(currentLocation.entityExists(consumed.get(0))){
            if(!currentPlayer.hasArtefact(entity) && (e instanceof Artefact)){
                str.append("You don't have "+entity);
                return true;
            }
            else if((!currentPlayer.hasArtefact(artefactNeeded) && (e instanceof Furniture))){ //chop tree
                str.append("You don't have "+artefactNeeded);
                return true;
            }
            currentLocation.removeEntityByName(consumed.get(0));
            for(String x: produced){
                currentLocation.addEntityFromStoreroom(x, currentGame);
            }
            str.append(action.getNarration());
            return true;
        }
        return false;
    }



    public boolean interactWithSubjects(GameAction action, HashMap<String, String> artefacts,
                                        GameState currentGame, String entity){
        Location currentLocation = currentGame.getLocation(currentGame.getCurrentLocation());
        Player currentPlayer = currentGame.getCurrentPlayer();
        if(currentLocation.entityExists(entity)){//if loc has subject
            if(deductHealth(action, artefacts, currentPlayer, currentLocation)){
                return true;
            }
            else if(hasSubjectNeeded(action, artefacts, currentPlayer, currentLocation, entity)){
                return true;
            }


//            else if(Objects.equals(consumed.get(0), "health")){
//                if(currentPlayer.getHealth() > 1) {
//                    currentPlayer.decreaseHealth();
//                    str.append(action.getNarration());
//                    return true;
//                }
//                else if(currentPlayer.getHealth() == 1){
//                    for(String artefact: artefacts.keySet()){
//                        currentPlayer.dropArtefact(artefact);
//                    }
//                    currentGame.setCurrentLocation(currentGame.getBeginningLocation());
//                    str.append("You died and lost all of your items, you must return to the start of the game");
//                    return true;
//                }
//            }
        }
        return false;
    }

    public boolean hasSubjectNeeded(GameAction action, HashMap<String, String> artefacts,
                                    Player currentPlayer, Location currentLocation,
                                    String entity){
        ArrayList<String> subjects = action.getSubjects();
        ArrayList<String> produced = action.getProduced();
        ArrayList<String> tempSubjects = subjects; //cut tree
        tempSubjects.retainAll(artefacts.keySet()); //if player has subject needed
        if(!tempSubjects.isEmpty()) {
            currentPlayer.dropArtefact(tempSubjects.get(0));
            currentLocation.removeEntityByName(entity);
            currentLocation.addEntityFromStoreroom(produced.get(0), currentGame);
            str.append(action.getNarration());
            return true;
        }
        return false;
    }

    public boolean deductHealth(GameAction action, HashMap<String, String> artefacts,
                                Player currentPlayer, Location currentLocation){
        ArrayList<String> consumed = action.getConsumed();
        if(Objects.equals(consumed.get(0), "health")){
            if(currentPlayer.getHealth() > 1) {
                currentPlayer.decreaseHealth();
                str.append(action.getNarration());
                return true;
            }
            else if(currentPlayer.getHealth() == 1){
                HashMap<String, String> temp = new HashMap<>();
                for(String x: artefacts.keySet()){
                    temp.put(x, artefacts.get(x));
                }
//                HashMap<String, String> temp = (HashMap<String, String>) artefacts.clone();
                for(String artefact: temp.keySet()){
                    currentPlayer.dropArtefactToCurrLoc(artefact, artefacts.get(artefact), currentLocation);
                }
                currentGame.setCurrentLocation(currentGame.getBeginningLocation());
                currentPlayer.resetHealth();
                str.append("You died and lost all of your items, you must return to the start of the game");
                return true;
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
        }
        if(str.isEmpty()){
            str.append("Missing artefact");
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
        for(Player player: currentGame.getPlayers()){
            if(!Objects.equals(player.getPlayerName(), currentGame.getCurrentPlayer().getPlayerName())){

                str.append("Other player(s): \n"+player.getPlayerName());
            }
        }

        return str.toString();
    }
}
