package edu.uob;

import java.util.ArrayList;

class OXOModel {
  private final ArrayList<ArrayList<OXOPlayer>> cells;
  private final OXOPlayer[] players;
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    cells = new ArrayList<ArrayList<OXOPlayer>>();
    for(int j=0; j<numberOfRows; j++){
      cells.add(new ArrayList<OXOPlayer>(numberOfColumns));
      for(int i=0; i<numberOfColumns; i++){
        cells.get(j).add(null);
      }
    }
    players = new OXOPlayer[2];

  }

  public int getNumberOfPlayers() {
    return players.length;
  }

  public void addPlayer(OXOPlayer player) {
    for (int i = 0; i < players.length; i++) {
      if (players[i] == null) {
        players[i] = player;
        return;
      }
    }
  }

  public OXOPlayer getPlayerByNumber(int number) {
    return players[number];
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
    return cells.get(0).size();
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
    for(int i=0; i<getNumberOfRows(); i++){
      cells.get(i).remove(getNumberOfColumns()-1);
    }
  }

  public void removeRow() {
    cells.remove(getNumberOfRows()-1);
  }



}
