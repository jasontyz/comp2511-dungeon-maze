package unsw.components;

public class Delta<T> {
	public final T from;
	public final T to;

	public Delta (T from, T to) {
		this.from = from;
		this.to = to;
	}
}
