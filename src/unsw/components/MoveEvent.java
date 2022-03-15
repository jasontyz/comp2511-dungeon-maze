package unsw.components;

public class MoveEvent {
	public enum Direction {
		UP, RIGHT, DOWN, LEFT
	};

	public final Direction direction;

	MoveEvent (Direction direction) {
		this.direction = direction;
	}

	public static MoveEvent up () {
		return new MoveEvent(Direction.UP);
	}

	public static MoveEvent right () {
		return new MoveEvent(Direction.RIGHT);
	}

	public static MoveEvent down () {
		return new MoveEvent(Direction.DOWN);
	}

	public static MoveEvent left () {
		return new MoveEvent(Direction.LEFT);
	}

}
