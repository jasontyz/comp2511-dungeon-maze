package unsw.goals;

import java.util.Map;

public class SimpleGoal implements IGoalComponent {
	private final GoalType type;

	public SimpleGoal (GoalType type) {
		this.type = type;
	}

	public boolean completed (Map<GoalType, Boolean> state) {
		return state.get(this.type);
	}
}
