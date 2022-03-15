package unsw.components;

abstract class Component {
	public final Entity entity;

	Component (Entity entity) {
		this.entity = entity;
	}
}
