package hello;

public class GameInProgressStatusResponse extends StatusResponse {
	public GameInProgressStatusResponse(int gameId, GridElementStatus[] game) {
		super(gameId, "in progress", game);
	}
}
