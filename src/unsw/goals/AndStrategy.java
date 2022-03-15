package unsw.goals;

import java.util.Map;
import java.util.List;

public class AndStrategy implements CompositeStrategy {
	public boolean reduce (
		List<IGoalComponent> children,
		Map<GoalType, Boolean> state) {
		for (IGoalComponent goal : children)
			if (!goal.completed(state)) return false;
		return true;
	}
}
