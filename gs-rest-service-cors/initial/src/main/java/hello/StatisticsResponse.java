package hello;

public class StatisticsResponse {
	private Stat[] stats = new Stat[0];

	public StatisticsResponse(Stat[] stats) {
		this.stats = stats;
	}
	
	public StatisticsResponse() {}

	public Stat[] getStats() {return stats;}
}

