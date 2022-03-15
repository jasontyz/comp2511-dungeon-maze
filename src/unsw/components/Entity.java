package unsw.components;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import unsw.entity.EntityType;

public class Entity {
	private Map<Class<? extends Component>, Component> components
		= new HashMap<>();

	public final String name;
	public final EntityType type;

	public Entity (String name, EntityType type) {
		this.name = name;
		this.type = type;
	}

	public Entity (String name) {
		this.name = name;
		this.type = EntityType.TEST;
	}

	public Entity () {
		this("__test__");
	}

	public <T extends Component> T getComponent (Class<? extends Component> compClass) {
		// downcast inst of type Component to type T
		return (T) this.components.get(compClass);
	}

	public <T extends Component> T addComponent (T comp) {
		if (this.<T>getComponent(comp.getClass()) != null) {
				String msg = "[warning] Entity::addComponent: component: %s already exists for entity: %s\n";
				System.err.printf(msg,
					comp.getClass().getSimpleName(), this.name);
		}
		this.components.put(comp.getClass(), comp);
		return comp;
	}

	public void removeComponent (Class<? extends Component> compClass) {
		this.components.remove(compClass);
	}

	public void addCollisionStrategy (
		List<Entity> entities,
		CollisionStrategy strategy) {
		
		if (entities != null) {
			for (Entity entity : entities)
				this.addCollisionStrategy(entity, strategy);
		}
	}

	public void addCollisionStrategy (
		Entity entity,
		CollisionStrategy strategy) {

		TransformComponent transform =
			entity.<TransformComponent>getComponent(TransformComponent.class);

		TransformComponent thisTransform =
			this.<TransformComponent>getComponent(TransformComponent.class);

		transform.addObserver((Delta<Point2D> delta) -> {
			
			if (thisTransform.equals(transform))
				strategy.exec(entity, delta);
		});
	}
}
