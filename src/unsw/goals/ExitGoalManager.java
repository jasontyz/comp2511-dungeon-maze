package unsw.goals;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import unsw.components.Entity;
import unsw.components.ZoneComponent;

public class ExitGoalManager implements IGoalManager {
	private Entity player;
	private boolean completed = false;

	public void setPlayer (Entity player) {
		this.player = player;
	}

	public boolean isGoalCompleted () {
		return this.completed;
	}
	
	public void addExit (Entity exit) {
		ZoneComponent zone =
			exit.<ZoneComponent>getComponent(ZoneComponent.class);
		if (zone == null || this.player == null) return;
		zone.onEnter(this.player, this::setGoalCompleted);
		zone.onLeave(this.player, this::unsetGoalCompleted);
	}

	private void setGoalCompleted () {
		this.completed = true;
	}

	private void unsetGoalCompleted () {
		this.completed = false;
	}
}
