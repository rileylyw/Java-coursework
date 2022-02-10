package edu.uob;

class OXOController {
  OXOModel gameModel;
  public OXOController(OXOModel model) {
    gameModel = model;
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
//    assert within grid
//    System.out.println(gameModel.getWinner());
    if(gameModel.getWinner() != null){
      return;
    }
    int x = (int) command.charAt(0) - 'a';
    int y = Character.getNumericValue(command.charAt(1)) - 1;
    if(gameModel.getCurrentPlayerNumber() == 0 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCellOwner(x, y, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
      System.out.println("TEST:" + gameModel.getCellOwner(0, 0));

      if(checkWin(x, y, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()))){
        gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
//        System.out.println("finished X won");
      }
      else{
        gameModel.setCurrentPlayerNumber(1);
      }
    }
    else if(gameModel.getCurrentPlayerNumber() == 1 && gameModel.getCellOwner(x, y) == null){
      gameModel.setCellOwner(x, y, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
      if(checkWin(x, y, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()))){
        gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
//        System.out.println("finished O won");
      }
      else{
        gameModel.setCurrentPlayerNumber(0);
      }
    }
    int taken = 0;
    for(int j=0; j<gameModel.getNumberOfRows(); j++){
      for(int i=0; i<gameModel.getNumberOfColumns(); i++){
        if(gameModel.getCellOwner(j, i) != null){
          taken++;
        }
      }
    }
    if(taken == gameModel.getNumberOfRows()* gameModel.getNumberOfColumns()){
      gameModel.setGameDrawn();
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
      if(gameModel.getCellOwner(i, colNumber)==null || !(gameModel.getCellOwner(i, colNumber)==player)){
        playerWon = false;
      }
    }
    if(playerWon) return true;

    //check diagonal (right to left)
    playerWon = true;
    for(int i=0; i<gameModel.getNumberOfRows(); i++){
      if(gameModel.getCellOwner(i, i)==null || !(gameModel.getCellOwner(i, i)==player)){
        playerWon = false;
      }
    }
    if(playerWon) return true;

    //check diagonal (left to right)
    playerWon = true;
    for(int i=0; i<gameModel.getNumberOfRows(); i++){
      if(gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)==null ||
              !(gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)==player)){
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
