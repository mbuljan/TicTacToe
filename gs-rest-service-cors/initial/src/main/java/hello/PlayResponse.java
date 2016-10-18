package hello;

public class PlayResponse {
	public static final int ILEGAL_MOVE = 412;
	public static final int NO_GAME_WITH_THAT_ID = 1;
	public static final int OK = 0;

	private int status = -1;

	public PlayResponse(int status) {
		this.status = status;
	}

	public int getStatus() {return status;}
	
}
