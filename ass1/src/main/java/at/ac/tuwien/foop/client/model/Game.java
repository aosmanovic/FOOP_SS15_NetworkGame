package at.ac.tuwien.foop.client.model;

import java.util.List;

public class Game {
	private boolean started;
	private Board board;
	private List<Player> player;
	
	public void board(Board board) {
		this.board = board;
	}
	
}
