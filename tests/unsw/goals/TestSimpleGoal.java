package unsw.goals;

import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSimpleGoal {
	IGoalComponent exitGoal = new SimpleGoal(GoalType.EXIT);
	IGoalComponent enemiesGoal = new SimpleGoal(GoalType.ENEMIES);
	IGoalComponent bouldersGoal = new SimpleGoal(GoalType.BOULDERS);
	IGoalComponent treasureGoal = new SimpleGoal(GoalType.TREASURE);

	Map<GoalType, Boolean> state = new HashMap<>();

	@Test
	void testCompleted () {
		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, false);
		state.put(GoalType.BOULDERS, true);
		state.put(GoalType.TREASURE, false);

		assertEquals(true, exitGoal.completed(state));
		assertEquals(false, enemiesGoal.completed(state));
		assertEquals(true, bouldersGoal.completed(state));
		assertEquals(false, treasureGoal.completed(state));
	}
}
