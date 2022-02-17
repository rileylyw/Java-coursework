package edu.uob;

import java.util.ArrayList;

class OXOModel {
  private final ArrayList<ArrayList<OXOPlayer>> cells;
  private final ArrayList<OXOPlayer> players;
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    if(numberOfRows==0 && numberOfColumns==0){
      gameDrawn = true;
    }
    cells = new ArrayList<ArrayList<OXOPlayer>>();
    for(int j=0; j<numberOfRows; j++){
      cells.add(new ArrayList<OXOPlayer>(numberOfColumns));
      for(int i=0; i<numberOfColumns; i++){
        cells.get(j).add(null);
      }
    }
    players = new ArrayList<>(2);
  }

  public int getNumberOfPlayers() {
    return players.size();
  }

  public void addPlayer(OXOPlayer player) {
    players.add(player);
  }

  public OXOPlayer getPlayerByNumber(int number) {
    return players.get(number);
  }

  public OXOPlayer getWinner() {
    return winner;
  }

  public void setWinner(OXOPlayer player) {
    winner = player;
  }

  public int getCurrentPlayerNumber() {
    return currentPlayerNumber;
  }

  public void setCurrentPlayerNumber(int playerNumber) {
    currentPlayerNumber = playerNumber;
  }

  public int getNumberOfRows() {
    return cells.size();
  }

  public int getNumberOfColumns() {
    if(cells.get(0) == null){
      return 0;
    }
    else {
      return cells.get(0).size();
    }
  }

  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
    return cells.get(rowNumber).get(colNumber);
  }

  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    cells.get(rowNumber).set(colNumber, player);
  }

  public void setWinThreshold(int winThresh) {
    winThreshold = winThresh;
  }

  public int getWinThreshold() {
    return winThreshold;
  }

  public void setGameDrawn() {
    gameDrawn = true;
  }

  public void resetGameDrawn() {
    gameDrawn = false;
  }

  public boolean isGameDrawn() {
    return gameDrawn;
  }

  public void addColumn() {
    for(int i=0; i<getNumberOfRows(); i++){
      cells.get(i).add(null);
    }
  }

  public void addRow() {
    cells.add(new ArrayList<OXOPlayer>(getNumberOfColumns()));
    for(int i=0; i<getNumberOfColumns(); i++){
      cells.get(getNumberOfRows()-1).add(null);
    }
  }

  public void removeColumn() {
    for(int i=getNumberOfRows()-1; i>=0; i--){
      cells.get(i).remove(getNumberOfColumns()-1);
    }
  }

  public void removeRow() {
    cells.remove(getNumberOfRows()-1);
  }
}
