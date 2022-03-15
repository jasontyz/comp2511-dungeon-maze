package unsw.goals;

import java.util.Map;
import java.util.List;

public interface CompositeStrategy {
	public boolean reduce (
		List<IGoalComponent> children,
		Map<GoalType, Boolean> state
	);
}
