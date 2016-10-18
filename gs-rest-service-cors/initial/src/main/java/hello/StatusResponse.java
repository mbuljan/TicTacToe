package hello;

public abstract class StatusResponse {
    private int gameId = -1;
    private String status = "";
    private GridElementStatus[] game = new GridElementStatus[9];
    
    public StatusResponse(int gameId, String status, GridElementStatus[] game) {
        this.gameId = gameId;
        this.status = status;
        this.game = game;
    }

		public int getGameId() {return gameId;}
		public String getStatus() {return status;}
		public GridElementStatus[] getGame() {return game;}
}
