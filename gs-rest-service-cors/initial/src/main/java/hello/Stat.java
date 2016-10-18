package hello;

public class Stat {
	private String name = "";
	private int wins = 0;
	private int losses = 0;
	private int draws = 0;

	public Stat(String name, int wins, int losses, int draws) {
		this.name = name;
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
	}

	public Stat(String name) {
		this.name = name;
	}
	
	public void addWin(int win) {wins += win;}
	public void addLoss(int loss) {losses += loss;}
	public void addDraw(int draw) {draws += draw;}

	public String getName() {return name;}
	public int getWins() {return wins;}
	public int getLosses() {return losses;}
	public int getDraws() {return draws;}
	
	public float getWinPercentage() {
		if(wins == 0 && losses == 0 && draws == 0)
			return 0.0f;

		return (wins / (wins + losses + draws)) * 100.0f;	
	}
}

