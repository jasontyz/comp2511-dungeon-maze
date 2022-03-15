package unsw.components;

public class MoveableComponent extends Component {

	public final int weight;

	public MoveableComponent (Entity entity, int weight) {
		super(entity);
		this.weight = weight;
	}
}
