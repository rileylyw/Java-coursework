package edu.uob;

import edu.uob.OXOMoveException.*;

class OXOController {
  OXOModel gameModel;
  public OXOController(OXOModel model) {
    gameModel = model;
  }

  public void handleIncomingCommand(String command) throws OXOMoveException {
    if(gameModel.getWinner() != null){
      return;
    }
    checkInvalidIdentifier(command);
    int x = (int) Character.toLowerCase(command.charAt(0)) - 'a';
    int y = Character.getNumericValue(command.charAt(1)) - 1;
    int numberOfPlayer = gameModel.getNumberOfPlayers();
    int currentPlayerNumber = gameModel.getCurrentPlayerNumber();
    int nextPlayerNumber = gameModel.getCurrentPlayerNumber() + 1;
    if(nextPlayerNumber == numberOfPlayer){
      nextPlayerNumber = 0; //reset
    }
    checkInRange(x, y);
    if(gameModel.getCellOwner(x, y) != null){
      throw new CellAlreadyTakenException(x, y);
    }
    else {
      setAndCheck(x, y, currentPlayerNumber, nextPlayerNumber);
    }
    checkDraw();
  }

  public void checkInvalidIdentifier(String command) throws OXOMoveException{
    if(command.length() != 2){
      throw new InvalidIdentifierLengthException(command.length());
    }
    if(!Character.isLetter(command.charAt(0))){
      throw new InvalidIdentifierCharacterException(
              OXOMoveException.RowOrColumn.ROW, command.charAt(0));
    }
    if(!Character.isDigit(command.charAt(1))){
      throw new InvalidIdentifierCharacterException(
              OXOMoveException.RowOrColumn.COLUMN, command.charAt(1));
    }
  }

  public void checkInRange(int x, int y) throws OXOMoveException{
    if(x >= gameModel.getNumberOfRows()){
      throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.ROW, x);
    }
    if(y >= gameModel.getNumberOfColumns()){
      throw new OutsideCellRangeException(OXOMoveException.RowOrColumn.COLUMN, y);
    }
  }

  public void setAndCheck(int x, int y, int currentPlayerNumber, int nextPlayerNumber){
    gameModel.setCellOwner(x, y, gameModel.getPlayerByNumber(currentPlayerNumber));
    if(checkWin(x, y)){
      gameModel.setWinner(gameModel.getPlayerByNumber(currentPlayerNumber));
    }
    else{
      gameModel.setCurrentPlayerNumber(nextPlayerNumber);
    }
  }

  public boolean checkDraw(){
    int count = 0;
    int winnerCount = 0;
    for(int j=0; j<gameModel.getNumberOfRows(); j++){
      for(int i=0; i<gameModel.getNumberOfColumns(); i++) {
        if(checkWin(j, i)){
          winnerCount++;
        }
        if(gameModel.getCellOwner(j, i) != null){
          count++;
        }
      }
    }
    if(count == gameModel.getNumberOfRows() * gameModel.getNumberOfColumns() || winnerCount > 2){
      System.out.println("winnercount "+ winnerCount);
      gameModel.setGameDrawn();
      return true;
    }
    else if (winnerCount == 2){
      return false;
    }
    return true;
  }

  public void addRow() {
    if(gameModel.getNumberOfRows() < 9 && gameModel.getWinner()==null &&
            !gameModel.isGameDrawn()) {
      gameModel.addRow();
    }
  }

  public void removeRow() {
    if(gameModel.getNumberOfRows() > 1 && gameModel.getWinner()==null &&
            !gameModel.isGameDrawn()) {
      gameModel.removeRow();
    }
    checkDraw();
  }

  public void addColumn() {
    if(gameModel.getNumberOfColumns() < 9 && gameModel.getWinner()==null &&
            !gameModel.isGameDrawn()) {
      gameModel.addColumn();
    }
  }

  public void removeColumn() {
    if(gameModel.getNumberOfColumns() > 1 && gameModel.getWinner()==null &&
            !gameModel.isGameDrawn()) {
      gameModel.removeColumn();
    }
    checkDraw();
  }


  private boolean checkWin(int rowNumber, int colNumber) {
    int winThreshold = gameModel.getWinThreshold(); //e.g. 3
    int checkRange = winThreshold * 2 - 2;
    if(checkColWin(rowNumber, colNumber, winThreshold, checkRange)){
//      System.out.println("col win");
      return true;
    }
    else if(checkRowWin(rowNumber, colNumber, winThreshold, checkRange)){
//      System.out.println("row win");
      return true;
    }
    else if(checkDiagWinTB(rowNumber, colNumber, winThreshold, checkRange)){
//      System.out.println("diag win");
      return true;
    }
    else if(checkDiagWinBT(rowNumber, colNumber, winThreshold, checkRange)){
//      System.out.println("diag win2");
      return true;
    }
    return false;
  }

  public boolean checkColWin(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int position = rowNumber - winThreshold + 1;
    int thresholdCount = 1;
    for(int i=0; i<checkRange; i++){
      if((position+i+1) >= 0 && (position+i+1) < gameModel.getNumberOfRows() &&
              (position+i) >= 0 && (position+i) < gameModel.getNumberOfRows()) { //within arraylist range
        if (gameModel.getCellOwner(position + i, colNumber) != null) {
          if (gameModel.getCellOwner(position + i, colNumber) ==
                  gameModel.getCellOwner(position + i + 1, colNumber)) {
            thresholdCount++;
            if (thresholdCount == winThreshold) {
              return true;
            }
          }
          else {
            thresholdCount = 1; //reset
          }
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
            if (thresholdCount == winThreshold) {
              return true;
            }
          }
          else {
            thresholdCount = 1;
          }
        }
      }
    }
    return false;
  }

  public boolean checkDiagWinTB(int rowNumber, int colNumber, int winThreshold, int checkRange){
    int rowPosition = rowNumber - winThreshold + 1;
    int colPosition = colNumber - winThreshold + 1;
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
            if (thresholdCount == winThreshold) {
              return true;
            }
          }
          else {
            thresholdCount = 1;
          }
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
            if (thresholdCount == winThreshold) {
              return true;
            }
          }
          else {
            thresholdCount = 1;
          }
        }
      }
    }
    return false;
  }

  public void increaseWinThreshold() {
    if(gameModel.getWinner() == null && !gameModel.isGameDrawn()) {
      gameModel.setWinThreshold(gameModel.getWinThreshold() + 1);
      System.out.println(gameModel.getWinThreshold());
    }
  }

  public void decreaseWinThreshold() {
    if (gameModel.getWinThreshold() > 0 && gameModel.getWinner() == null &&
            !gameModel.isGameDrawn()) {
      gameModel.setWinThreshold(gameModel.getWinThreshold() - 1);
      System.out.println(gameModel.getWinThreshold());
    }
    if(!checkDraw()){
      gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
    }
  }
}
