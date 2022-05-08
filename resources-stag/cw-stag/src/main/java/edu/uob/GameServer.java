package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/** This class implements the STAG server. */
public final class GameServer {

    private static final char END_OF_TRANSMISSION = 4;
//    private HashMap<String, HashSet<Location>> locations = new HashMap<>();
//    private TreeMap<String, HashSet<GameAction>> actions = new TreeMap<>();
    private GameState currentGame = new GameState();

    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config/basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config/basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8880); //TODO change back to 8888
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer(File, File)}) otherwise we won't be able to mark
    * your submission correctly.
    *
    * <p>You MUST use the supplied {@code entitiesFile} and {@code actionsFile}
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    *
    */
    public GameServer(File entitiesFile, File actionsFile) {

        try { //parse the dot files
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitiesFile);
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            ArrayList<Graph> locations = sections.get(0).getSubgraphs();
            for(Graph location: locations){
                String locationName = location.getNodes(false).get(0).getId().getId();
                String locationDesc = location.getNodes(false).get(0).getAttribute("description");
                Location locationToAdd = new Location(locationName, locationDesc);
                currentGame.addLocation(locationName, locationToAdd);

                ArrayList<Graph> items = location.getSubgraphs();
                for(Graph item: items){
                    String itemType = item.getId().getId();
                    if(item.getNodes(false).size()>0){
                        String itemName = item.getNodes(false).get(0).getId().getId();
                        String itemDesc = item.getNodes(false).get(0).getAttribute("description");
                        switch(itemType){
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

                    //TODO: store entities to location
                }
            }


//            Graph firstLocation = locations.get(0);
//            Node locationDetails = firstLocation.getNodes(false).get(0);
//            String locationName = locationDetails.getId().getId();

            // The paths will always be in the second subgraph
            ArrayList<Edge> paths = sections.get(1).getEdges();
//            Edge firstPath = paths.get(0);
//            Node fromLocation = firstPath.getSource().getNode();
//            String fromName = fromLocation.getId().getId();
//            Node toLocation = firstPath.getTarget().getNode();
//            String toName = toLocation.getId().getId();


        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming game commands and carries out the corresponding actions.
    */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        return "Thanks for your message: " + command;
    }

    //  === Methods below are there to facilitate server related operations. ===

    /**
    * Starts a *blocking* socket server listening for new connections. This method blocks until the
    * current thread is interrupted.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * you want to.
    *
    * @param portNumber The port to listen on.
    * @throws IOException If any IO related operation fails.
    */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
    * Handles an incoming connection from the socket server.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * * you want to.
    *
    * @param serverSocket The client socket to read/write from.
    * @throws IOException If any IO related operation fails.
    */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();

            }
        }
    }
}
