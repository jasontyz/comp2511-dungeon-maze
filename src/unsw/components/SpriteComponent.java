package unsw.components;

import unsw.ui.IGridLayer;
import unsw.ui.EntitySprite;
import unsw.ui.SpriteState;

public class SpriteComponent extends Component {
	private IGridLayer layer;
	private EntitySprite sprite;

	public SpriteComponent (Entity entity, IGridLayer layer, EntitySprite sprite) {
		super(entity);
		this.layer = layer;
		this.sprite = sprite;

		// initial render
		TransformComponent transform =
			entity.<TransformComponent>getComponent(TransformComponent.class);
		layer.setImage(sprite.get(SpriteState.INITIAL));
		layer.setXPos(transform.getX());
		layer.setYPos(transform.getY());
	}
	
	public void update () {
		TransformComponent transform =
			entity.<TransformComponent>getComponent(TransformComponent.class);

		if (transform == null)
			layer.removeSelf();
		else {
			layer.setXPos(transform.getX());
			layer.setYPos(transform.getY());
		}

		CollectorComponent collector =
			entity.<CollectorComponent>getComponent(CollectorComponent.class);

		if (collector != null) {
			if (collector.hasSword() && !collector.hasPotion())
				layer.setImage(sprite.get(SpriteState.WITH_SWORD));
			else if (collector.hasPotion() && !collector.hasSword())
				layer.setImage(sprite.get(SpriteState.WITH_POTION));
			else if (collector.hasSword() && collector.hasPotion())
				layer.setImage(sprite.get(SpriteState.WITH_POTION_AND_SWORD));
			else {
				layer.setImage(sprite.get(SpriteState.INITIAL));
			}
		}
		
		LockComponent lock =
			entity.<LockComponent>getComponent(LockComponent.class);

		if (lock != null && lock.isUnlocked())
			layer.setImage(sprite.get(SpriteState.UNLOCKED));
	}
}
