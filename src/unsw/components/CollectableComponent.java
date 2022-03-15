package unsw.components;


public class CollectableComponent<T> extends Component {

	T collectable;

	public CollectableComponent (Entity entity, T collectable) {
		super(entity);
		this.collectable = collectable;
	}

	public T get () {
		return this.collectable;
	}
}
