package unsw.goals;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CompositeGoal implements IGoalComponent {
	private CompositeStrategy strategy;
	private List<IGoalComponent> children = new ArrayList<>();

	public CompositeGoal (CompositeStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean completed (Map<GoalType, Boolean> state) {
		return this.strategy.reduce(this.children, state);
	}
	
	public void addGoal (IGoalComponent goal) {
		this.children.add(goal);
	}
}
