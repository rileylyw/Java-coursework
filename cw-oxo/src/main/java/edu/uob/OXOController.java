package edu.uob;

class OXOController {
  OXOModel gameModel;
  OXOPlayer playerX = new OXOPlayer('X');
  OXOPlayer playerO = new OXOPlayer('O');
  public OXOController(OXOModel model) {
    gameModel = model;
//    System.out.println("TEST");
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
//    assert within grid
    int x = (int) command.charAt(0) - 'a';
    int y = Character.getNumericValue(command.charAt(1)) - 1;
    if(gameModel.getCurrentPlayerNumber() == 0 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCurrentPlayerNumber(1);
      gameModel.setCellOwner(x, y, playerX);
    }
    else if(gameModel.getCurrentPlayerNumber() == 1 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCurrentPlayerNumber(0);
      gameModel.setCellOwner(x, y, playerO);
    }
//    else return error: already occupied
  }
  public void addRow() {
    gameModel.addRow();
//    System.out.println(gameModel.getNumberOfRows());
  }

  public void removeRow() {
    gameModel.removeRow();
  }

  public void addColumn() {
    gameModel.addColumn();
  }

  public void removeColumn() {
    gameModel.removeColumn();
  }

  public void increaseWinThreshold() {}
  public void decreaseWinThreshold() {}
}
