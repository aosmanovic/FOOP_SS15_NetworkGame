package at.ac.tuwien.foop.client.domain;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.foop.client.events.GameEvent;
import at.ac.tuwien.foop.client.events.GameEventListener;
import at.ac.tuwien.foop.domain.Update;

public class Game {
	private List<GameEventListener> listeners = new ArrayList<>();

	private boolean running = false;
	private boolean joined = false;
	private List<Player> players = new ArrayList<>();
	private Board board;

	public void board(Board board) {
		this.board = board;
	}

	public void addGameEventListener(GameEventListener listener) {
		listeners.add(listener);
	}

	public void removeGameEventListener(GameEventListener listener) {
		listeners.remove(listener);
	}

	public void fireGameEvent(GameEvent event) {
		listeners.forEach(e -> e.onUpdate(event));
	}

	public void start() {
		running = true;
		fireGameEvent(new GameEvent(GameEvent.Type.START));
	}

	public void stop() {
		running = false;
		fireGameEvent(new GameEvent(GameEvent.Type.STOP));
	}

	public void join() {
		joined = true;
	}

	public boolean running() {
		return running;
	}

	public void update(Update update) {
		// TODO: implement
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void addPlayer(Player player) {
		players.add(player);
		fireGameEvent(new GameEvent(GameEvent.Type.NEW_PLAYER));
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setBoard(Board board) {
		if (running) {
			throw new RuntimeException("can't set board on a running game!");
		}
		this.board = board;
		fireGameEvent(new GameEvent(GameEvent.Type.BOARD));
	}

	public Board getBoard() {
		return board;
	}

	public boolean joined() {
		return joined;
	}
}
