package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {
  OXOModel model;
  OXOController controller;

  // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
  // and O player
  private static OXOModel createStandardModel() {
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    return model;
  }

  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    model = createStandardModel();
    controller = new OXOController(model);
  }

  // here's a basic test for the `controller.handleIncomingCommand` method
  @Test
  void testHandleIncomingCommand() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
  }

  // here's a complete game where we find out if someone won
  @Test
  void testRowWinA1A2A3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("A3");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("A2");
    assertEquals(
        firstMovingPlayer,
        model.getWinner(),
        "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testRowWinC4C5C6() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.handleIncomingCommand("a1");
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("c4");
    controller.handleIncomingCommand("A2");
    controller.handleIncomingCommand("c6");
    controller.handleIncomingCommand("a5");
    controller.handleIncomingCommand("C5");
    assertEquals(
            secondMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(secondMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testColWinA1B1C1() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("B1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("c1");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testColWinA3B3C3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("C3");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("B3");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testColWinD5E5F5() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("d5");
    controller.handleIncomingCommand("f7");
    controller.handleIncomingCommand("f5");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("e5");
    controller.handleIncomingCommand("a1");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testColWinE7F7G7() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("g7");
    controller.handleIncomingCommand("f1");
    controller.handleIncomingCommand("e7");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("f7");
    controller.handleIncomingCommand("a1");
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDiagWinA1B2C3() throws OXOMoveException {
    controller.handleIncomingCommand("a2"); //X
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("b2"); //O
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("c3");
    System.out.println(model.isGameDrawn());
    assertEquals(
            secondMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(secondMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDiagWinC1B2A3() throws OXOMoveException {
    controller.handleIncomingCommand("a2"); //X
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("b2"); //O
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a3");
    System.out.println(model.isGameDrawn());
    assertEquals(
            secondMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(secondMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDiagWinF4E5D6() throws OXOMoveException {
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addRow();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.addColumn();
    controller.handleIncomingCommand("a2"); //X
    OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("f4");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("d6"); //O
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("e5");
    System.out.println(model.isGameDrawn());
    assertEquals(
            secondMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(secondMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testDraw() throws OXOMoveException {
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("c3");
    System.out.println(model.isGameDrawn());
    assertEquals(model.isGameDrawn(),true);
  }

}
