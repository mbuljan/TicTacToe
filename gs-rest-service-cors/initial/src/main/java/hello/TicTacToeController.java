package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;

@RestController
public class TicTacToeController {    
    private HashMap<Integer, GameSession> gameSessions = new HashMap<Integer, GameSession>();
		private HashMap<String, Player> players = new HashMap<String, Player>();		
		
		public TicTacToeController() {
			Player computerHard = new ComputerPlayerHard();
			Player computerEasy = new ComputerPlayerEasy();
			players.put(computerHard.getName(), computerHard);
			players.put(computerEasy.getName(), computerEasy);
		}

		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping("*")
		public String index() {
			
			try {
				String uri = System.getProperty("user.dir") + "/index.html";
				String html = new String(Files.readAllBytes(Paths.get(uri)));
				System.out.println(uri);
				return html; 
			} catch(IOException ex) {
				System.out.println(ex.getMessage());
			} 

			return "Error";
		}
	
		@CrossOrigin(origins = "http://localhost:8080")
		@RequestMapping("/new")
    public NewGameResponse newGame(@RequestParam(value="first", defaultValue="computer") String first, 
                                    @RequestParam(value="second", defaultValue="computer") String second) {
				// checking if players were already used in a game
				System.out.println("first: " + first + ", " + "second: " + second);
				Player player1 = null;
				Player player2 = null;
				Player humanPlayer = null;
				if(first.equals("computer")) {
					if(!second.equals("computer")) {
						// first player is computer, second is human
						humanPlayer = players.get(second);
						if(humanPlayer != null) {
							player2 = humanPlayer;
						} else {
							player2 = new HumanPlayer(second);
							players.put(second, player2);
							humanPlayer = player2;
						}
					}
				} else if(!first.equals("computer")) {
					if(second.equals("computer")) {
						// first player is a human, second is a computer
						humanPlayer = players.get(first);
						if(humanPlayer != null) {
							player1 = players.get(first);
						} else {
							player1 = new HumanPlayer(first);
							players.put(first, player1);
							humanPlayer = player1;
						}
					}	
				} else {
					return new NewGameResponse(-1);
				}
		
				float winPercentage = humanPlayer.getStat().getWinPercentage();
				if(winPercentage <= 30.0) {
					if(player1 == null) {
						player1 = players.get("computer_easy");
					} else if(player2 == null) {
						player2 = players.get("computer_easy");
					}
				} else {
					if(player1 == null) {
						player1 = players.get("computer_hard");
					} else if(player2 == null) {
						player2 = players.get("computer_hard");
					}
				}

				GameSession gameSession = new GameSession(player1, player2);
        int gameId = gameSession.getId();
        gameSessions.put(gameId, gameSession);
				System.out.println("Starting gamesession: " + gameId);
    		return new NewGameResponse(gameId);
    }
    
		@RequestMapping("/play")
    public PlayResponse play(@RequestParam(value="gameId", defaultValue="-1") int gameId,
															@RequestParam(value="row", defaultValue="-1") int row,
															@RequestParam(value="column", defaultValue="-1") int column) {
			GameSession gameSession = gameSessions.get(gameId);
			if(gameSession != null) {
				return gameSession.playMove(row, column);
			}
			
			return new PlayResponse(PlayResponse.NO_GAME_WITH_THAT_ID);  
   	}

   	@RequestMapping("/status")
   	public StatusResponse status(@RequestParam(value="gameId", defaultValue="-1") int gameId) {
			GameSession gameSession = gameSessions.get(gameId);
			if(gameSession != null) {
				return gameSession.getStatusResponse();	
			}

			return null;
    }

		@RequestMapping("/stats")
    public StatisticsResponse stats() {
			Iterator it = players.entrySet().iterator();
			ArrayList<Stat> statsArray = new ArrayList<Stat>();
			while(it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				Player player = (Player)pair.getValue();
				Stat playerStat = player.getStat();
				statsArray.add(playerStat);
			}
			Stat[] stats = new Stat[statsArray.size()];
			stats = statsArray.toArray(stats);
			return new StatisticsResponse(stats);
   	}
}


