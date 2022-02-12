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
    int numberOfPlayer = gameModel.getNumberOfPlayers();
    int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
    int nextPlayerNumber = gameModel.getCurrentPlayerNumber() + 1;
    if(nextPlayerNumber == numberOfPlayer){
      nextPlayerNumber = 0; //reset
    }

    if(gameModel.getCellOwner(x, y) == null){
      gameModel.setCellOwner(x, y, gameModel.getPlayerByNumber(currentPlayerNumber));
      if(!checkWin(x, y, gameModel.getPlayerByNumber(currentPlayerNumber))){
        gameModel.setCurrentPlayerNumber(nextPlayerNumber);
        gameModel.increaseCount();
      }
      else{
        gameModel.setWinner(gameModel.getPlayerByNumber(currentPlayerNumber));
      }
    }
    if(gameModel.getCount() == (gameModel.getNumberOfRows() * gameModel.getNumberOfColumns())){
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
    int winThreshold = gameModel.getWinThreshold(); //e.g. 3
    if((rowNumber - winThreshold + 1) < 0 || (rowNumber + winThreshold - 1) < 0){
      return false;
    }
    //check col
    int checkRange = winThreshold*2 - 2;
    int position = rowNumber - winThreshold + 1;
    for(int i=0; i<checkRange; i++){
      System.out.println("loop " +i);
      if(gameModel.getCellOwner(position+i, colNumber) == gameModel.getCellOwner(position+i+1, colNumber)){
        gameModel.increaseThresholdCount();
//        System.out.println("i "+i);
//        System.out.println(gameModel.getThresholdCount());
//        System.out.println("winthresold " + winThreshold);
      }
      else{
        gameModel.resetThresholdCount();
      }
      if(gameModel.getThresholdCount() == winThreshold){
        return true;
      }
    }
    return false;



//    //check row
//    boolean playerWon = true;
//    for(int i=0; i<gameModel.getNumberOfColumns(); i++){
//      if(gameModel.getCellOwner(rowNumber, i)==null || !(gameModel.getCellOwner(rowNumber, i)==player)){
//        playerWon = false;
//      }
//    }
//    if(playerWon) return true;
//
//    //check col
//    playerWon = true;
//    for(int i=0; i<gameModel.getNumberOfRows(); i++){
//      if(gameModel.getCellOwner(i, colNumber)==null || !(gameModel.getCellOwner(i, colNumber)==player)){
//        playerWon = false;
//      }
//    }
//    if(playerWon) return true;
//
//    //check diagonal (right to left)
//    playerWon = true;
//    for(int i=0; i<gameModel.getNumberOfRows(); i++){
//      if(gameModel.getCellOwner(i, i)==null || !(gameModel.getCellOwner(i, i)==player)){
//        playerWon = false;
//      }
//    }
//    if(playerWon) return true;
//
//    //check diagonal (left to right)
//    playerWon = true;
//    for(int i=0; i<gameModel.getNumberOfRows(); i++){
//      if(gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)==null ||
//              !(gameModel.getCellOwner(i, gameModel.getNumberOfRows()-1-i)==player)){
//        playerWon = false;
//      }
//    }
//    if(playerWon) return true;
//    else{
//      return false;
  }


  public void increaseWinThreshold() {}
  public void decreaseWinThreshold() {}
}
