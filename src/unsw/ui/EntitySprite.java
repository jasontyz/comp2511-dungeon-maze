package unsw.ui;

import java.util.Map;
import java.util.HashMap;

public class EntitySprite {

	private Map<SpriteState, String> states = new HashMap<>();

	public static EntitySprite createStatic (String imgName) {
		EntitySprite staticSprite = new EntitySprite();
		staticSprite.set(SpriteState.INITIAL, imgName);
		return staticSprite;
	}

	public void set (SpriteState state, String imgName) {
		this.states.put(state, imgName);
	}

	public String get (SpriteState state) {
		return this.states.get(state);
	}
}
