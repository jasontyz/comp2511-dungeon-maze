package unsw.goals;

import java.util.Map;

public interface IGoalComponent {
	 public boolean completed (Map<GoalType, Boolean> state);
}
