package at.ac.tuwien.foop.client.model;

public class GameEvent {
	public enum Type {
		CLOSE, NEW_PLAYER, BOARD, START, STOP
	}

	public final Type type;

	public GameEvent(Type type) {
		this.type = type;
	}
}
