package unsw.goals;

import java.util.Set;
import java.util.HashSet;

import unsw.components.Entity;
import unsw.components.CollectorComponent;
import unsw.components.CollectableComponent;
import unsw.collectables.Treasure;

public class TreasureGoalManager implements IGoalManager {
	private Entity player;
	private Set<Entity> treasuresRemaining = new HashSet<>();

	public void setPlayer (Entity player) {
		this.player = player;
		CollectorComponent collector =
			player.<CollectorComponent>getComponent(CollectorComponent.class);
		collector.addObserver(this::handlePlayerPickup);
	}

	public boolean isGoalCompleted () {
		return this.treasuresRemaining.isEmpty();
	}
	
	public void addTreasure (Entity treasure) {
		this.treasuresRemaining.add(treasure);
	}

	private void handlePlayerPickup (CollectableComponent<?> collectable) {
		Entity treasure = collectable.entity;
		this.treasuresRemaining.remove(treasure);
	}
}
