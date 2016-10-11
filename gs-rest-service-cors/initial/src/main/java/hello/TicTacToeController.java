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
				System.out.println(first + " started a game...");
				Player player1 = players.get(first);
				Player player2 = players.get(second);
				if(player1 == null) {
					if(!first.equals( "computer")) {
						player1 = new HumanPlayer(first);
					} else {
						player1 = new ComputerPlayerHard();
					}
					players.put(player1.getName(), player1);
				}

				if(player2 == null) {
					if(!second.equals("computer")) {
						player2 = new HumanPlayer(second);
					} else {
						player2 = new ComputerPlayerHard();
					}
					players.put(player2.getName(), player2);
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
			
			return new StatisticsResponse((Stat[])statsArray.toArray());
   	}
}


