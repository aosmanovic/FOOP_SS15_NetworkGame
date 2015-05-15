package at.ac.tuwien.foop.server.service;


import org.junit.Assert;
import org.junit.Test;



import at.ac.tuwien.foop.domain.Coordinates;
import at.ac.tuwien.foop.domain.Player;
import at.ac.tuwien.foop.server.domain.BoardString;
import at.ac.tuwien.foop.server.domain.Game;

public class GameLogicServiceTest {
	
	GameLogicService service = new GameLogicService();
	
	
	@Test
	public void testMovement() {
		Game game = new Game();
		//game.start();
		Coordinates c = new Coordinates(3,4);
		Player player = new Player("Al", c);
		game.join(player);
		String b = 
				  "wwwwwwww"
				+ "w----Cww"
				+ "w-wmwwww"
				+ "w---wwww"
				+ "wwwwwwww";
		BoardString bs = new BoardString(b,8);
		
		game.setBoard(bs);
		service.movement(game);
		Player actual = game.getPlayer().get(0);
		Assert.assertEquals(new Coordinates(2, 4), actual.getCoordinates());
		
	}
	
	/*public String makeMaze(String in) {
		return in.replace(" ", "");
	}*/

}