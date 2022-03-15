package unsw.goals;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import unsw.components.Entity;
import unsw.components.ZoneComponent;

public class BouldersGoalManager implements IGoalManager {
	private List<Entity> boulders = new ArrayList<>();
	private List<Entity> floorSwitches = new ArrayList<>();
	private Set<Entity> switchesRemaining = new HashSet<>();

	public boolean isGoalCompleted () {
		return this.switchesRemaining.isEmpty();
	}

	public void addBoulder (Entity boulder) {
		this.boulders.add(boulder);

		for (Entity floorSwitch : this.floorSwitches) {
			ZoneComponent zone =
				floorSwitch.<ZoneComponent>getComponent(ZoneComponent.class);
			zone.onEnter(boulder, () -> {
				this.switchesRemaining.remove(floorSwitch);
			});
			zone.onLeave(boulder, () -> {
				this.switchesRemaining.add(floorSwitch);
			});
		}
	}

	public void addFloorSwitch (Entity floorSwitch) {
		this.floorSwitches.add(floorSwitch);
		this.switchesRemaining.add(floorSwitch);

		ZoneComponent zone =
			floorSwitch.<ZoneComponent>getComponent(ZoneComponent.class);

		for (Entity boulder : this.boulders) {
			zone.onEnter(boulder, () -> {
				this.switchesRemaining.remove(floorSwitch);
			});
			zone.onLeave(boulder, () -> {
				this.switchesRemaining.add(floorSwitch);
			});
		}
	}
}
