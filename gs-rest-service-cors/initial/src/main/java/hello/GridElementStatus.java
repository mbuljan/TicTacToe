package hello;


public class GridElementStatus {

    private int row = -1;
    private int column = -1;
    private char value = 'U';
    
    public GridElementStatus(int row, int column, char value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }
    
    public int getRow() {return row;}
    public int getColumn() {return column;}
    public char getValue() {return value;}
    
}
