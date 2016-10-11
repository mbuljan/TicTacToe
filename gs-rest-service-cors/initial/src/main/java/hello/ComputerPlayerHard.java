package hello;

public class ComputerPlayerHard extends ComputerPlayer {
	public ComputerPlayerHard() {
		super("computer_hard");
	}	

	public void playMove(GameGrid gameGrid, Sign sign) {
		Sign mySign = sign;
		Sign opponentSign = Sign.UNDEFINED;
		if(mySign == Sign.X) {
			opponentSign = Sign.O;
		}
		else {
			opponentSign = Sign.X;
		}

		// checking if I have a possible victory move
		int[] myWinningCoordinates = checkWinningMove(mySign, gameGrid);
		if(myWinningCoordinates.length == 2) {
			gameGrid.setGridElement(myWinningCoordinates[0], myWinningCoordinates[1], sign);
			return;
		} else {
			// checking if my opponent has a possible victory move
			int[] opponentWinningCoordinates = checkWinningMove(opponentSign, gameGrid);
			if(opponentWinningCoordinates.length == 2) {
				// if he does, block his victory move by playing that move
				gameGrid.setGridElement(opponentWinningCoordinates[0], 
																opponentWinningCoordinates[1], 
																sign);
				return;
			}
		}

		// attempting to play impossible ultimatum move, if possible
		int[] impossibleUltimatumCoordinates = checkImpossibleUltimatumMove(mySign, gameGrid);
		if(impossibleUltimatumCoordinates.length == 2) {
			gameGrid.setGridElement(impossibleUltimatumCoordinates[0], 
															impossibleUltimatumCoordinates[1],
															mySign);
			return;
		} else {
			// checking if opponent can play an impossible ultimatum, if can, block it
			int[] opponentImpossibleUltimatumCoord = checkImpossibleUltimatumMove(opponentSign, gameGrid);
			if(opponentImpossibleUltimatumCoord.length == 2) {
				gameGrid.setGridElement(opponentImpossibleUltimatumCoord[0], 
																opponentImpossibleUltimatumCoord[1], 
																mySign);
				return;
			}
		}

		// trying to play a move in a way so that my signs on the grid are alligned
		int[] pickedElement = checkAlignedMove(sign, gameGrid);
		if(pickedElement.length == 2) {
			gameGrid.setGridElement(pickedElement[0], pickedElement[1], mySign);
			return;
		}

		// last resort, if no smart moves can be played, place the sign wherever on the grid
		int[] randomElement = playRandomMove(sign, gameGrid);
		if(randomElement.length == 2) {
			gameGrid.setGridElement(randomElement[0], randomElement[1], mySign);
		}
	} 
}
