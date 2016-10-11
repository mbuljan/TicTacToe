package hello;

public class NewGameResponse {
    private final int gameId;

		public NewGameResponse() {
			gameId = -1;
		}

    public NewGameResponse(int gameId) {
        this.gameId = gameId;
    }
    
    public int getGameId() {
        return gameId;
    }
}
