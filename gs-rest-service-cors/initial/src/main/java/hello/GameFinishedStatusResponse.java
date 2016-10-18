package hello;

public class GameFinishedStatusResponse extends StatusResponse{
	private String winner = "";
	
	public GameFinishedStatusResponse(int gameId, GridElementStatus[] game, String winner) {
		super(gameId, "finished", game);
		this.winner = winner;
}	

	public String getWinner() {return winner;}
}
