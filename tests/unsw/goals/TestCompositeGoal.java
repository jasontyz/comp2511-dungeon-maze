package unsw.goals;

import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCompositeGoal {
	IGoalComponent exitGoal = new SimpleGoal(GoalType.EXIT);
	IGoalComponent enemiesGoal = new SimpleGoal(GoalType.ENEMIES);
	IGoalComponent bouldersGoal = new SimpleGoal(GoalType.BOULDERS);
	IGoalComponent treasureGoal = new SimpleGoal(GoalType.TREASURE);

	Map<GoalType, Boolean> state = new HashMap<>();

	CompositeGoal andGoals = new CompositeGoal(new AndStrategy());
	CompositeGoal orGoals = new CompositeGoal(new OrStrategy());

	@Test
	void testWithSingleChild () {
		andGoals.addGoal(exitGoal);
		orGoals.addGoal(enemiesGoal);

		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, false);
		state.put(GoalType.BOULDERS, false);
		state.put(GoalType.TREASURE, false);
		assertEquals(true, andGoals.completed(state));
		assertEquals(false, orGoals.completed(state));
	}

	@Test
	void testAndStrategyOneLevelDeep () {
		andGoals.addGoal(exitGoal);
		andGoals.addGoal(enemiesGoal);
		andGoals.addGoal(bouldersGoal);
		andGoals.addGoal(treasureGoal);

		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, true);
		state.put(GoalType.BOULDERS, true);
		state.put(GoalType.TREASURE, false);
		assertEquals(false, andGoals.completed(state));

		state.put(GoalType.TREASURE, true);
		assertEquals(true, andGoals.completed(state));
	}

	@Test
	void testOrStrategyOneLevelDeep () {
		orGoals.addGoal(exitGoal);
		orGoals.addGoal(enemiesGoal);
		orGoals.addGoal(bouldersGoal);
		orGoals.addGoal(treasureGoal);

		state.put(GoalType.EXIT, false);
		state.put(GoalType.ENEMIES, false);
		state.put(GoalType.BOULDERS, false);
		state.put(GoalType.TREASURE, true);
		assertEquals(true, orGoals.completed(state));

		state.put(GoalType.TREASURE, false);
		assertEquals(false, orGoals.completed(state));
	}

	@Test
	void testAndStrategyTwoLevelsDeep () {
		/*
		 * 				AND
		 * 		AND		EXIT	AND
		 * 	EXIT ENEMIES	BOULD  TREASURE
		 */

		System.out.printf("testAndStrategyTwoLevelsDeep\n");
		CompositeGoal exitAndEnemies =
			new CompositeGoal(new AndStrategy());
		exitAndEnemies.addGoal(exitGoal);
		exitAndEnemies.addGoal(enemiesGoal);

		CompositeGoal bouldersAndTreasure =
			new CompositeGoal(new AndStrategy());
		bouldersAndTreasure.addGoal(bouldersGoal);
		bouldersAndTreasure.addGoal(treasureGoal);

		CompositeGoal rootGoal =
			new CompositeGoal(new AndStrategy());
		rootGoal.addGoal(exitAndEnemies);
		rootGoal.addGoal(exitGoal);
		rootGoal.addGoal(bouldersAndTreasure);

		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, true);
		state.put(GoalType.BOULDERS, true);
		state.put(GoalType.TREASURE, true);
		assertEquals(true, rootGoal.completed(state));

		state.put(GoalType.EXIT, false);
		assertEquals(false, rootGoal.completed(state));

		state.put(GoalType.EXIT, true);
		state.put(GoalType.TREASURE, false);
		assertEquals(false, rootGoal.completed(state));
	}

	@Test
	void testOrStrategyTwoLevelsDeep () {
		/*
		 * 				OR
		 * 		OR		EXIT	OR
		 * 	EXIT ENEMIES	BOULD  TREASURE
		 */

		CompositeGoal exitOrEnemies =
			new CompositeGoal(new OrStrategy());
		exitOrEnemies.addGoal(exitGoal);
		exitOrEnemies.addGoal(enemiesGoal);

		CompositeGoal bouldersOrTreasure =
			new CompositeGoal(new OrStrategy());
		bouldersOrTreasure.addGoal(bouldersGoal);
		bouldersOrTreasure.addGoal(treasureGoal);

		CompositeGoal rootGoal =
			new CompositeGoal(new OrStrategy());
		rootGoal.addGoal(exitOrEnemies);
		rootGoal.addGoal(exitGoal);
		rootGoal.addGoal(bouldersOrTreasure);

		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, true);
		state.put(GoalType.BOULDERS, true);
		state.put(GoalType.TREASURE, true);
		assertEquals(true, rootGoal.completed(state));

		state.put(GoalType.EXIT, false);
		assertEquals(true, rootGoal.completed(state));

		state.put(GoalType.EXIT, false);
		state.put(GoalType.ENEMIES, false);
		state.put(GoalType.BOULDERS, false);
		state.put(GoalType.TREASURE, false);
		assertEquals(false, rootGoal.completed(state));

		state.put(GoalType.TREASURE, true);
		assertEquals(true, rootGoal.completed(state));

		state.put(GoalType.ENEMIES, true);
		state.put(GoalType.BOULDERS, false);
		state.put(GoalType.TREASURE, false);
		assertEquals(true, rootGoal.completed(state));
	}

	@Test
	void testOrWithAnd () {
		/*
		 * 				AND
		 * 		OR				OR
		 * 	EXIT ENEMIES	BOULD  TREASURE
		 */

		CompositeGoal exitOrEnemies =
			new CompositeGoal(new OrStrategy());
		exitOrEnemies.addGoal(exitGoal);
		exitOrEnemies.addGoal(enemiesGoal);

		CompositeGoal bouldersOrTreasure =
			new CompositeGoal(new OrStrategy());
		bouldersOrTreasure.addGoal(bouldersGoal);
		bouldersOrTreasure.addGoal(treasureGoal);

		CompositeGoal rootGoal =
			new CompositeGoal(new AndStrategy());
		rootGoal.addGoal(exitOrEnemies);
		rootGoal.addGoal(bouldersOrTreasure);

		state.put(GoalType.EXIT, true);
		state.put(GoalType.ENEMIES, false);
		state.put(GoalType.BOULDERS, true);
		state.put(GoalType.TREASURE, false);
		assertEquals(true, rootGoal.completed(state));

		state.put(GoalType.BOULDERS, false);
		assertEquals(false, rootGoal.completed(state));
	}
}
