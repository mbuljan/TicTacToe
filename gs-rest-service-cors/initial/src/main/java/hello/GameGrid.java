package hello;

public class GameGrid {
    private int fieldsFilled = 0;
    private Sign[][] gameGrid = new Sign[3][3]; 

    public GameGrid() {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					gameGrid[i][j] = Sign.UNDEFINED;
				}
			}
		}		
		
		public GameGrid(GameGrid other) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					gameGrid[i][j] = other.getGridElement(i, j);
				}
			}
		}

		public GameGrid clone() {
			return new GameGrid(this);
		}			

    public boolean setGridElement(int row, int column, Sign sign) {
        if(row < 0 || row > 2 || column < 0 || column > 2 ||  
						gameGrid[row][column] != Sign.UNDEFINED || sign == Sign.UNDEFINED)
            return false;

        gameGrid[row][column] = sign;
        fieldsFilled++;
        return true;
    }		
		
		public Sign getGridElement(int row, int column) {
			if(row < 0 || row > 2 || column < 0 || column > 2) {
				return Sign.OUT_OF_RANGE_SIGN;
			}

			return gameGrid[row][column];
		}

    public boolean isEntireGridFilled() {return fieldsFilled == 9;}
}
