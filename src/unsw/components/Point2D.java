package unsw.components;

public class Point2D {
	public final int x;
	public final int y;

	public Point2D (int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals (Object other) {
		if (other == null) return false;
		Point2D otherPoint = (Point2D) other;
		return this.x == otherPoint.x && this.y == otherPoint.y;
	}
}
