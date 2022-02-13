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
    //checkRowColValid
    if(command.length() != 2){
      throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
    }
    if(!Character.isLetter(command.charAt(0))){
      throw new OXOMoveException.InvalidIdentifierCharacterException(
              OXOMoveException.RowOrColumn.ROW, command.charAt(0));
    }
    if(!Character.isDigit(command.charAt(1))){
      throw new OXOMoveException.InvalidIdentifierCharacterException(
              OXOMoveException.RowOrColumn.COLUMN, command.charAt(1));
    }

    int x = (int) Character.toLowerCase(command.charAt(0)) - 'a';
    int y = Character.getNumericValue(command.charAt(1)) - 1;
    int numberOfPlayer = gameModel.getNumberOfPlayers();
    int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
    int nextPlayerNumber = gameModel.getCurrentPlayerNumber() + 1;
    if(nextPlayerNumber == numberOfPlayer){
      nextPlayerNumber = 0; //reset
    }





    if(gameModel.getCellOwner(x, y) == null){
      gameModel.setCellOwner(x, y, gameModel.getPlayerByNumber(currentPlayerNumber));
      if(checkWin(x, y)){
        gameModel.setWinner(gameModel.getPlayerByNumber(currentPlayerNumber));
      }
      else{
        gameModel.setCurrentPlayerNumber(nextPlayerNumber);
        gameModel.increaseCount();
        System.out.println("count " +gameModel.getCount());
      }
    }
    if(gameModel.getCount() == gameModel.getNumberOfRows() * gameModel.getNumberOfColumns()){
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


  private boolean checkWin(int rowNumber, int colNumber) {
    int winThreshold = gameModel.getWinThreshold(); //e.g. 3
    int checkRange = winThreshold * 2 - 2;
    if(checkColWin(rowNumber, colNumber, winThreshold, checkRange)){
      System.out.println("col win");
      return true;
    }
    else if(checkRowWin(rowNumber, colNumber, winThreshold, checkRange)){
      System.out.println("row win");
      return true;
    }
    else if(checkDiagWinTB(rowNumber, colNumber, winThreshold, checkRange)){
      System.out.println("diag win");
      return true;
    }
    else if(checkDiagWinBT(rowNumber, colNumber, winThreshold, checkRange)){
      System.out.println("diag win2");
      return true;
    }
    return false;
  }

  public boolean checkColWin(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int position = rowNumber - winThreshold + 1;
//    if((rowNumber - 1) < 0 || (rowNumber + 1) > gameModel.getNumberOfRows() || position < 0) return false;
    int thresholdCount = 1;
    for(int i=0; i<checkRange; i++){
      if((position+i+1) >= 0 && (position+i+1) < gameModel.getNumberOfRows() &&
              (position+i) >= 0 && (position+i) < gameModel.getNumberOfRows()) { //within arraylist range
        if (gameModel.getCellOwner(position + i, colNumber) != null) {
          if (gameModel.getCellOwner(position + i, colNumber) ==
                  gameModel.getCellOwner(position + i + 1, colNumber)) {
            thresholdCount++;
          }
        } else {
          thresholdCount = 1; //reset
        }
        if (thresholdCount == winThreshold) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean checkRowWin(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int position = colNumber - winThreshold + 1;
//    if((colNumber - 1) < 0 || (colNumber + 1) > gameModel.getNumberOfColumns() || position < 0) return false;
    int thresholdCount = 1;
    for(int i=0; i<checkRange; i++){
      if((position+i+1) >= 0 && (position+i+1) < gameModel.getNumberOfColumns() &&
              (position+i) >= 0 && (position+i) < gameModel.getNumberOfColumns()) {
        if (gameModel.getCellOwner(rowNumber, position + i) != null) {
          if (gameModel.getCellOwner(rowNumber, position + i) ==
                  gameModel.getCellOwner(rowNumber, position + i + 1)) {
            thresholdCount++;
          }
        } else {
          thresholdCount = 1;
        }
        if (thresholdCount == winThreshold) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean checkDiagWinTB(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int rowPosition = colNumber - winThreshold + 1;
    int colPosition = rowNumber - winThreshold + 1;
    int thresholdCount = 1;
    for(int i=0; i<checkRange; i++){
      if((colPosition+i+1) >= 0 && (colPosition+i+1) < gameModel.getNumberOfColumns() &&
              (rowPosition+i+1) >= 0 && (rowPosition+i+1) < gameModel.getNumberOfRows() &&
              (rowPosition+i) >= 0 && (rowPosition+i) < gameModel.getNumberOfRows() &&
              (colPosition+i) >= 0 && (colPosition+i) < gameModel.getNumberOfColumns()) {
        if (gameModel.getCellOwner(rowPosition + i, colPosition + i) != null) {
          if (gameModel.getCellOwner(rowPosition + i, colPosition + i) ==
                  gameModel.getCellOwner(rowPosition + i + 1, colPosition + i + 1)) {
            thresholdCount++;
          }
        } else {
          thresholdCount = 1;
        }
        if (thresholdCount == winThreshold) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean checkDiagWinBT(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int colPosition = colNumber - winThreshold + 1;
    int rowPosition = rowNumber + winThreshold - 1;
    int thresholdCount = 1;
    for(int i=0; i<checkRange; i++){
      if((colPosition+i+1) >= 0 && (colPosition+i+1) < gameModel.getNumberOfColumns() &&
              (rowPosition-i-1) >= 0 && (rowPosition-i-1) < gameModel.getNumberOfRows() &&
              (rowPosition-i) >= 0 && (rowPosition-i) < gameModel.getNumberOfRows() &&
              (colPosition+i) >= 0 && (colPosition+i) < gameModel.getNumberOfColumns()) {
        if (gameModel.getCellOwner(rowPosition - i, colPosition + i) != null) {
          if (gameModel.getCellOwner(rowPosition - i, colPosition + i) ==
                  gameModel.getCellOwner(rowPosition - i - 1, colPosition + i + 1)) {
            thresholdCount++;
          }
        } else {
          thresholdCount = 1;
        }
        if (thresholdCount == winThreshold) {
          return true;
        }
      }
    }
    return false;
  }

  public void increaseWinThreshold() {}
  public void decreaseWinThreshold() {}
}
