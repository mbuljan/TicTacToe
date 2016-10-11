package hello;

import java.util.*;

public class GameSession {
    private static final Sign[] SIGNS = new Sign[]{Sign.X, Sign.O};
		private static Object lock = new Object();
    private static int gameSessionCounter = 0; 

		private int id = -1;
		private HashMap<Sign, Player> players = new HashMap<Sign, Player>();
		private GameGrid gameGrid = new GameGrid();		
		private Sign currentSign = Sign.X;		

    public GameSession(Player firstPlayer, Player secondPlayer) {
    	synchronized(lock) {
      	id = gameSessionCounter++;
     	}

			players.put(Sign.X, firstPlayer);
			players.put(Sign.O, secondPlayer);

			// if the first player is an AI, he immediatley plays
			if(firstPlayer instanceof ComputerPlayer) {
				((ComputerPlayer)firstPlayer).playMove(gameGrid, Sign.X);
				advanceToNextPlayer();
			}
		}
		
		public PlayResponse playMove(int row, int column) {
			if(gameGrid.isEntireGridFilled()) {
				return new PlayResponse(PlayResponse.ILEGAL_MOVE);
			} else {
				Player winner = getWinner();
				if(winner != null) {
					return new PlayResponse(PlayResponse.ILEGAL_MOVE);
				}
			}

			Player currentPlayer = players.get(currentSign);
			boolean legalMove = gameGrid.setGridElement(row, column, currentSign);
			if(legalMove) {
				Player winner = getWinner();
				if(winner != null || gameGrid.isEntireGridFilled()) {
					return new PlayResponse(PlayResponse.OK);
				}

				System.out.println("player " + currentPlayer.getName() + " is playing " + row + ", " + column);
				advanceToNextPlayer();
				System.out.println("Next player is: " + players.get(currentSign).getName());
				Player nextPlayer = players.get(currentSign);
				if(nextPlayer instanceof ComputerPlayer) {
					System.out.println("player " + nextPlayer.getName() + " is playing a move");
					// in case the next player is an AI he has to play
					ComputerPlayer computerPlayer = (ComputerPlayer)nextPlayer;
					computerPlayer.playMove(gameGrid, currentSign);
					advanceToNextPlayer();
				} else {
					System.out.println("Next player is not a computer");
				}
				return new PlayResponse(PlayResponse.OK);
			} else {
				return new PlayResponse(PlayResponse.ILEGAL_MOVE);
			}
		}

		public StatusResponse getStatusResponse() {
			ArrayList<GridElementStatus> gridElementStatuses = new ArrayList<GridElementStatus>();
			int gridElementIndex = 0;
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					Sign sign = gameGrid.getGridElement(i, j);
					if(sign == Sign.X || sign == Sign.O) {
						gridElementStatuses.add(new GridElementStatus(i, j, sign.getCharValue()));
					}
				}
			}			
			
			int elementCount = gridElementStatuses.size();
			boolean isGameFinished = gameGrid.isEntireGridFilled();
			if(isGameFinished) {
				Player winner = getWinner();
				if(winner == null) {
					// in case of a draw
					players.get(Sign.X).getStat().addDraw(1);
					players.get(Sign.O).getStat().addDraw(1);
					return new GameFinishedStatusResponse(id, (GridElementStatus[])gridElementStatuses.toArray(new GridElementStatus[elementCount]), "draw"); 
				}
				
				// updating player win/lose statistics
				Player player1 = players.get(Sign.X);
				Player player2 = players.get(Sign.O);
				if(player1 != winner) {
					player1.getStat().addLoss(1);
					player2.getStat().addWin(1);
				} else {
					player1.getStat().addWin(1);
					player2.getStat().addLoss(1);
				}
				
				return new GameFinishedStatusResponse(id, (GridElementStatus[])gridElementStatuses.toArray(new GridElementStatus[elementCount]), winner.getName());
			} else {
				return new GameInProgressStatusResponse(id, (GridElementStatus[])gridElementStatuses.toArray(new GridElementStatus[elementCount]));
			}
		}
		
    public int getId() {return id;}

		private void advanceToNextPlayer() {
			for(int i = 0; i < SIGNS.length; i++) {
				if(currentSign != SIGNS[i]) {
					currentSign = SIGNS[i];
					break;
				}
			}
		}

		private Player getWinner() {
			for(int i = 0; i < 3; i++) {
				// checking horizontal victory conditions
				Sign h0 = gameGrid.getGridElement(i, 0);
				Sign h1 = gameGrid.getGridElement(i, 1);
				Sign h2 = gameGrid.getGridElement(i, 2);			
				if(h0 == h1 && h1 == h2) {
					return players.get(h0);		
				}

				// checking vertical victory conditions
				Sign v0 = gameGrid.getGridElement(0, i);
				Sign v1 = gameGrid.getGridElement(1, i);
				Sign v2 = gameGrid.getGridElement(2, i);
				if(v0 == v1 && v1 == v2) {
					return players.get(v0);
				}
			}
			
			// checking diagonal victory conditions
			Sign diagonalSign00 = gameGrid.getGridElement(0, 0);
			Sign diagonalSign01 = gameGrid.getGridElement(1, 1);
			Sign diagonalSign02 = gameGrid.getGridElement(2, 2);
			if(diagonalSign00 == diagonalSign01 && diagonalSign01 == diagonalSign02) {
				return players.get(diagonalSign00);
			}

			Sign diagonalSign10 = gameGrid.getGridElement(0, 2);
			Sign diagonalSign11 = gameGrid.getGridElement(1, 1);
			Sign diagonalSign12 = gameGrid.getGridElement(2, 0);
			if(diagonalSign10 == diagonalSign11 && diagonalSign11 == diagonalSign12) {
				return players.get(diagonalSign10);
			}

			return null;
		}
}
