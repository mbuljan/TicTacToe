package hello;

public abstract class Player {
	protected String name = "";
	protected Stat stat = null;		
		
  public Player(String name) {
     	this.name = name;
			this.stat = new Stat(name);
 	}	

	public String getName() {return name;}
	public Stat getStat() {return stat;}
}
