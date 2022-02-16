package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


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
  void test3Players() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("A3");
    model.addPlayer(new OXOPlayer('Y'));
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("A2"); //Y
    assertEquals(model.getCellOwner(0, 1).getPlayingLetter(), 'Y');
//    assertEquals(
//            firstMovingPlayer,
//            model.getWinner(),
//            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testC1B2A3_3Players() throws OXOMoveException {
    controller.addColumn();
    controller.addRow();
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("A2");
    model.addPlayer(new OXOPlayer('Y'));
    controller.handleIncomingCommand("D2");
    controller.handleIncomingCommand("C1"); //Y
    controller.handleIncomingCommand("d4"); //X
    controller.handleIncomingCommand("A4"); //O
    controller.handleIncomingCommand("B2"); //Y
    controller.handleIncomingCommand("b4"); //X
    controller.handleIncomingCommand("c4"); //O
    controller.handleIncomingCommand("A3"); //Y
    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    assertEquals(
            thirdMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(thirdMovingPlayer.getPlayingLetter()));
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
  void testDraw1() throws OXOMoveException {
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
    assertTrue(model.isGameDrawn());
  }

  @Test
  void testDraw2() throws OXOMoveException {
    controller.addColumn();
    controller.addColumn();
    controller.addRow();
    controller.addRow();
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("a3");
    controller.handleIncomingCommand("a4");
    controller.handleIncomingCommand("a5"); //X
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("b2");
    controller.handleIncomingCommand("b3");
    controller.handleIncomingCommand("b4");
    controller.handleIncomingCommand("b5"); //O
    controller.handleIncomingCommand("c2");
    controller.handleIncomingCommand("c1");
    controller.handleIncomingCommand("c4");
    controller.handleIncomingCommand("c3");
    controller.handleIncomingCommand("d3"); //X
    controller.handleIncomingCommand("c5");
    controller.handleIncomingCommand("d1");
    controller.handleIncomingCommand("d2");
    controller.handleIncomingCommand("d5");
    controller.handleIncomingCommand("d4");
    controller.handleIncomingCommand("e1");
    controller.handleIncomingCommand("e2");
    controller.handleIncomingCommand("e3");
    controller.handleIncomingCommand("e4");
    controller.handleIncomingCommand("e5");
    System.out.println(model.isGameDrawn());
    assertTrue(model.isGameDrawn());
  }

  @Test
  void testDraw3() throws OXOMoveException {
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("b1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("b2");
    controller.decreaseWinThreshold();
    System.out.println(model.isGameDrawn());
    assertTrue(model.isGameDrawn());
  }

  @Test
  void testWinA1B1() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    controller.handleIncomingCommand("a2");
    controller.handleIncomingCommand("B1");
    controller.handleIncomingCommand("b3");
    controller.removeColumn();
    controller.decreaseWinThreshold();
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testExceptions() throws OXOMoveException {
    assertThrows(OXOMoveException.OutsideCellRangeException.class,
            ()->controller.handleIncomingCommand("a5"));
    assertThrows(OXOMoveException.OutsideCellRangeException.class,
            ()->controller.handleIncomingCommand("D3"));
    assertThrows(OXOMoveException.InvalidIdentifierLengthException.class,
            ()->controller.handleIncomingCommand("D12"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class,
            ()->controller.handleIncomingCommand("93"));
    assertThrows(OXOMoveException.InvalidIdentifierCharacterException.class,
            ()->controller.handleIncomingCommand("CC"));
    controller.handleIncomingCommand("A1");
    assertThrows(OXOMoveException.CellAlreadyTakenException.class,
            ()->controller.handleIncomingCommand("a1"));
  }

//  private static OXOModel create0x0Model() {
//    new OXOModel model = new OXOModel(0, 0, 3);
//    model0x0.addPlayer(new OXOPlayer('X'));
//    model0x0.addPlayer(new OXOPlayer('O'));
//    return model0x0;
//  }
//
//  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
//  @BeforeEach
//  void setup0x0() {
//    model = create0x0Model();
//    controller = new OXOController(model);
//  }
//

  @Test
  void testEmptyBoard() {
    OXOModel model2 = new OXOModel(0, 0, 3);
    model2.addPlayer(new OXOPlayer('X'));
    model2.addPlayer(new OXOPlayer('O'));
    new OXOController(model2);
    assertTrue(model2.isGameDrawn());
  }

  @Test
  void testEmptyBoardOnePlayer() {
    OXOModel model2 = new OXOModel(0, 0, 3);
    model2.addPlayer(new OXOPlayer('X'));
    OXOPlayer firstMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
    new OXOController(model2);
    assertEquals(
            firstMovingPlayer,
            model2.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testEmptyBoardNoPlayer() {
    OXOModel model2 = new OXOModel(0, 0, 3);
//    model2.addPlayer(new OXOPlayer('X'));
//    model2.addPlayer(new OXOPlayer('O'));
//    OXOPlayer firstMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
    new OXOController(model2);
    assertTrue(model2.isGameDrawn());
//    assertEquals(
//            firstMovingPlayer,
//            model2.getWinner(),
//            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testEmptyBoardAndThresholdNoPlayer() {
    OXOModel model2 = new OXOModel(0, 0, 0);
//    model2.addPlayer(new OXOPlayer('X'));
//    model2.addPlayer(new OXOPlayer('O'));
//    OXOPlayer firstMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
    new OXOController(model2);
    assertTrue(model2.isGameDrawn());
//    assertEquals(
//            firstMovingPlayer,
//            model2.getWinner(),
//            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }

  @Test
  void testEmptyBoardAndThresholdOnePlayer()  {
    OXOModel model2 = new OXOModel(0, 0, 0);
    model2.addPlayer(new OXOPlayer('X'));
    OXOPlayer firstMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
    new OXOController(model2);
    assertEquals(
            firstMovingPlayer,
            model2.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }
}
