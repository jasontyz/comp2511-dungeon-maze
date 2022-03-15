package unsw.goals;

import java.util.Map;
import java.util.HashMap;

import unsw.components.Entity;

public class RootGoalManager implements IGoalManager {
	private IGoalComponent rootGoal;

	private ExitGoalManager exitGoalManager; 
	private EnemiesGoalManager enemiesGoalManager; 
	private BouldersGoalManager bouldersGoalManager; 
	private TreasureGoalManager treasureGoalManager; 

	public RootGoalManager (IGoalComponent rootGoal) {
		this.rootGoal = rootGoal;

		this.exitGoalManager = new ExitGoalManager();
		this.enemiesGoalManager = new EnemiesGoalManager();
		this.bouldersGoalManager = new BouldersGoalManager();
		this.treasureGoalManager = new TreasureGoalManager();
	}
	
	public boolean isGoalCompleted () {
		Map<GoalType, Boolean> goalState = new HashMap<>();
		goalState.put(GoalType.EXIT, this.exitGoalManager.isGoalCompleted());	
		goalState.put(GoalType.ENEMIES, this.enemiesGoalManager.isGoalCompleted());	
		goalState.put(GoalType.BOULDERS, this.bouldersGoalManager.isGoalCompleted());	
		goalState.put(GoalType.TREASURE, this.treasureGoalManager.isGoalCompleted());	

		return this.rootGoal.completed(goalState);
	}

	public void setPlayer (Entity player) {
		this.exitGoalManager.setPlayer(player);
		this.treasureGoalManager.setPlayer(player);
	}

	public void addExit (Entity exit) {
		this.exitGoalManager.addExit(exit);
	}

	public void addEnemy (Entity enemy) {
		this.enemiesGoalManager.addEnemy(enemy);
	}

	public void addBoulder (Entity boulder) {
		this.bouldersGoalManager.addBoulder(boulder);
	}

	public void addFloorSwitch (Entity floorSwitch) {
		this.bouldersGoalManager.addFloorSwitch(floorSwitch);
	}

	public void addTreasure (Entity exit) {
		this.treasureGoalManager.addTreasure(exit);
	}
}
