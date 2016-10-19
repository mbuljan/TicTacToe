package hello;

import java.util.*;

public abstract class ComputerPlayer extends Player{

	public ComputerPlayer(String name) {
  	super(name);
	}

	public abstract void playMove(GameGrid gameGrid, Sign sign);

	protected int[] checkWinningMove(Sign sign, GameGrid gameGrid) {
		for(int i = 0; i < 3; i++) {
			int horizontalCount = 0;
			int horizontalEmptyCount = 0;
			int horizontalEmptyColumnIndex = 0;
			int verticalCount = 0;
			int verticalEmptyCount = 0;
			int verticalEmptyRowIndex = 0;
			for(int j = 0; j < 3; j++) {
				// checking horizontal winning move
				Sign currentSign = gameGrid.getGridElement(i, j);
				if(currentSign == sign) {
					horizontalCount++;
				} else if(currentSign == Sign.UNDEFINED){
					horizontalEmptyCount++;
					horizontalEmptyColumnIndex = j;
				}

				// checking vertical winning move
				currentSign = gameGrid.getGridElement(j, i);
 				if(currentSign == sign) {
					verticalCount++;
				} else if(currentSign == Sign.UNDEFINED) {
					verticalEmptyCount++;
					verticalEmptyRowIndex = j;
				}
			}

			if(horizontalCount == 2 && horizontalEmptyCount == 1) {
				return new int[] {i, horizontalEmptyColumnIndex};
			} else if(verticalCount == 2 && verticalEmptyCount == 1) {
				return new int[] {verticalEmptyRowIndex, i};
			}
		}

		// checking for diaognal winning moves
		int diagonal1Count = 0;
		int diagonal1EmptyCount = 0;
		int[] diagonal1EmptySlot = new int[0];
		int diagonal2Count = 0;
		int diagonal2EmptyCount = 0;
		int[] diagonal2EmptySlot = new int[0];
		for(int i = 0; i < 3; i++) {
			// checking top left to bottom right diagonal
			Sign currentSign = gameGrid.getGridElement(i, i);
			if(currentSign == sign) {
				diagonal1Count++;
			}	else if(currentSign == Sign.UNDEFINED) {
				diagonal1EmptyCount++;
				diagonal1EmptySlot = new int[]{i, i};
			}

			currentSign = gameGrid.getGridElement(i, 2 - i);
			if(currentSign == sign) {
				diagonal2Count++;
			} else if( currentSign == Sign.UNDEFINED) {
				diagonal2EmptyCount++;
				diagonal2EmptySlot = new int[]{i, 2 - i};
			}
		}

		if(diagonal1Count == 2 && diagonal1EmptyCount == 1) {
			return diagonal1EmptySlot;
		} else if(diagonal2Count == 2 && diagonal2EmptyCount == 1) {
			return diagonal2EmptySlot;
		}

		return new int[0];
	}

	protected int[] checkImpossibleUltimatumMove(Sign sign, GameGrid gameGrid) {
		// collecting coordinates of all my signs on the grid
		ArrayList<int[]> listOfYourSignCoordinates = collectCoordinatesOfSign(sign, gameGrid);
		ArrayList<int[]> possibleCrossPoints = getSwipeResult(sign, listOfYourSignCoordinates, gameGrid);
		ArrayList<int[]> candidatePoints = new ArrayList<int[]>();

		// getting candidate points for the possible ultimatum move
		for(int j = 0; j < possibleCrossPoints.size(); j++) {
			boolean found = false;
			for(int k = 0; k < candidatePoints.size(); k++) {
				if(candidatePoints.get(k)[0] == possibleCrossPoints.get(j)[0] && 
					candidatePoints.get(k)[1] == possibleCrossPoints.get(j)[1]) {
					found = true;
					break;
				}
			}
				
			if(!found) {
				candidatePoints.add(possibleCrossPoints.get(j));
			}
		}	
	
		for(int[] candidate : candidatePoints) {
			GameGrid testGrid = gameGrid.clone();
			testGrid.setGridElement(candidate[0], candidate[1], sign);
			int[] winningMove = checkWinningMove(sign, testGrid);
			if(winningMove.length == 2) {
				return candidate;
			}		
		}

		return new int[0];
	}

	protected int[] checkAlignedMove(Sign sign, GameGrid gameGrid) {
		ArrayList<int[]> signCoordinates = collectCoordinatesOfSign(sign, gameGrid);	
		
		if(signCoordinates.size() == 0) {
			return new int[0];
		}

		ArrayList<int[]> possibleTargets = getSwipeResult(sign, signCoordinates, gameGrid);

		if(possibleTargets.size() == 0) {
			return new int[0];
		}

		Random random = new Random();
		int randomElementIndex = random.nextInt(possibleTargets.size());
		
		return possibleTargets.get(randomElementIndex);
	}

	protected int[] playRandomMove(Sign sign, GameGrid gameGrid) {
		ArrayList<int[]> signCoordinates = collectCoordinatesOfSign(Sign.UNDEFINED, gameGrid);		
		if(signCoordinates.size() == 0) {
			return new int[0];
		}
		
		Random random = new Random();
		int randomElementIndex = random.nextInt(signCoordinates.size());
		
		return signCoordinates.get(randomElementIndex);
	}

	private ArrayList<int[]> collectCoordinatesOfSign(Sign sign, GameGrid gameGrid) {
		ArrayList<int[]> listOfYourSignCoordinates = new ArrayList<int[]>();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Sign currentSign = gameGrid.getGridElement(i, j);
				if(currentSign == sign) {
					listOfYourSignCoordinates.add(new int[]{i, j});
				}
			}
		}
		return listOfYourSignCoordinates;
	}
	
	private ArrayList<int[]> getSwipeResult(Sign sign, ArrayList<int[]> listOfYourSignCoordinates, GameGrid gameGrid) {
		ArrayList<int[]> possibleCrossPoints = new ArrayList<int[]>();
		for(int i = 0; i < listOfYourSignCoordinates.size(); i++) {
			int x = listOfYourSignCoordinates.get(i)[0];
			int y = listOfYourSignCoordinates.get(i)[1];
			
			// checking for obstacles horizontally
			boolean horizontalSwipeBlocked = false;
			for(int j = 0; j < 3; j++) {
				Sign currentSign = gameGrid.getGridElement(x, j);
				horizontalSwipeBlocked = currentSign != Sign.UNDEFINED && currentSign != sign;
			}
		
			// swiping horizontally
			if(!horizontalSwipeBlocked) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(x, j);
					if(currentSign == Sign.UNDEFINED) {
						possibleCrossPoints.add(new int[]{x, j});
					}
				}
			}
			
			// checking for obstacles vertically
			boolean verticalSwipeBlocked = false;
			for(int j = 0; j < 3; j++) {
				Sign currentSign = gameGrid.getGridElement(j, y);
				verticalSwipeBlocked = currentSign != Sign.UNDEFINED && currentSign != sign;
			}

			// swiping vertically
			if(!verticalSwipeBlocked) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(j, y);
					if(currentSign == Sign.UNDEFINED) {
						possibleCrossPoints.add(new int[]{j, y});
					}
				}
			}

			// determining on which diagonals to swipe
			boolean swipeUpperLeftToLowerRight = false;
			boolean swipeUpperRightToLowerLeft = false;
			if(x == 1 && y == 1) {
				swipeUpperLeftToLowerRight = true;
				swipeUpperRightToLowerLeft = true;
			} else if(x == y) {
				swipeUpperLeftToLowerRight = true;
			} else if((x == 0 && y == 2) || (x == 2 && y == 0)) {
				swipeUpperRightToLowerLeft = true;
			}			

			// diagonal swipe check 1
			if(swipeUpperLeftToLowerRight) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(j, j);
					if(currentSign != Sign.UNDEFINED && currentSign != sign) {
						swipeUpperLeftToLowerRight = false;
					}
				}
			}

			if(swipeUpperLeftToLowerRight) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(j, j);
					if(currentSign == Sign.UNDEFINED) {
						possibleCrossPoints.add(new int[]{j, j});
					}
				}
			}

			// diagonal swipe check 2
			if(swipeUpperRightToLowerLeft) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(j, j);
					if(currentSign != Sign.UNDEFINED && currentSign != sign) {
						swipeUpperRightToLowerLeft = false;
					}
				}
			}
			
			if(swipeUpperRightToLowerLeft) {
				for(int j = 0; j < 3; j++) {
					Sign currentSign = gameGrid.getGridElement(j, 2 - j);
					if(currentSign == Sign.UNDEFINED) {
						possibleCrossPoints.add(new int[]{j, 2 - j});
					}
				}
			}
		}

		return possibleCrossPoints;	
	}
}














