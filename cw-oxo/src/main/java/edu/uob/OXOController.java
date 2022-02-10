package edu.uob;

class OXOController {
  OXOModel gameModel;
  OXOPlayer playerX = new OXOPlayer('X');
  OXOPlayer playerO = new OXOPlayer('O');
  public OXOController(OXOModel model) {
    gameModel = model;
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
//    assert within grid
    int x = (int) command.charAt(0) - 'a';
    int y = Character.getNumericValue(command.charAt(1)) - 1;
    if(gameModel.getCurrentPlayerNumber() == 0 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCurrentPlayerNumber(1);
      gameModel.setCellOwner(x, y, playerX);
      if(checkWin(x, y, playerX)){
        gameModel.setWinner(playerX);
        System.out.println("finished X won");
      }
    }
    else if(gameModel.getCurrentPlayerNumber() == 1 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCurrentPlayerNumber(0);
      gameModel.setCellOwner(x, y, playerO);
      if(checkWin(x, y, playerO)){
        gameModel.setWinner(playerO);
        System.out.println("finished O won");
      }
    }
//    else return error: already occupied
  }
  public void addRow() {
    gameModel.addRow();
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

  private boolean checkWin(int rowNumber, int colNumber, OXOPlayer player){
    //check row
    boolean playerWon = true;
    for(int i=0; i<gameModel.getNumberOfColumns(); i++){
      if(gameModel.getCellOwner(rowNumber, i)==null || !(gameModel.getCellOwner(rowNumber, i)==player)){
        playerWon = false;
      }
    }
    if(playerWon) return true;

    //check col
    playerWon = true;
    for(int i=0; i<gameModel.getNumberOfRows(); i++){
      if(gameModel.getCellOwner(i, colNumber)==null || !(gameModel.getCellOwner(i, colNumber)!=player)){
        playerWon = false;
      }
    }
    if(playerWon) return true;

    //check diagonal (right to left)
    playerWon = true;
    for(int i=0; i<gameModel.getNumberOfRows(); i++){
      if(gameModel.getCellOwner(i, i)==null || !(gameModel.getCellOwner(i, i)!=player)){
        playerWon = false;
      }
    }
    if(playerWon) return true;

    //check diagonal (left to right)
    playerWon = true;
    for(int i=0; i<gameModel.getNumberOfRows(); i++){
      if(gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)==null ||
              gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)!=player){
        playerWon = false;
      }
    }
    if(playerWon) return true;
    else{
      return false;
    }
  }


  public void increaseWinThreshold() {}
  public void decreaseWinThreshold() {}
}
