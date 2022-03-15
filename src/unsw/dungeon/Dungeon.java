package unsw.dungeon;

import unsw.entity.*;
import unsw.observable.*;
import unsw.ui.IGridLayerFactory;

import unsw.components.MoveEvent;
import unsw.components.SpriteComponent;
import unsw.components.TransformComponent;
import unsw.components.CollectableComponent;
import unsw.components.Entity;
import unsw.components.FollowComponent;

import unsw.collectables.InvincPotion;

import unsw.goals.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Dungeon {

	private final int width;
	private final int height;
	
	private final EntityManager entityManager;
	private final RootGoalManager rootGoalManager;
	
	private boolean isPaused = false;

	private Dungeon (
		int width, int height,
		RootGoalManager rootGoalManager,
		EntityManager entityManager) {

		this.width = width;
		this.height = height;
		this.rootGoalManager = rootGoalManager;
		this.entityManager = entityManager;
	}

	public void init () {
		this.entityManager.setup();
	}

	// --------------------------------------------------------------------------------------------------

	public static Dungeon from (DungeonLoader loader, IObservable<MoveEvent> moveEvents, IGridLayerFactory layerFactory) {
		JSONObject json = loader.getJSON();
		IGoalComponent rootGoal = parseGoal(json.getJSONObject("goal-condition"));
		RootGoalManager rootGoalManager = new RootGoalManager(rootGoal);
		EntityManager entityManager = new EntityManager();
		EntityFactory entityFactory = new EntityFactory(moveEvents, layerFactory);

		JSONArray jsonEntities = json.getJSONArray("entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
            JSONObject jsonEntity = jsonEntities.getJSONObject(i);
			entityManager.addEntity(
				parseEntityFromJSON(jsonEntity, rootGoalManager, entityFactory));
		}

		return new Dungeon(
			json.getInt("width"), json.getInt("height"),
			rootGoalManager,
			entityManager);
	}

	private static IGoalComponent parseGoal (JSONObject goalSpec) {
		String goalType = goalSpec.getString("goal");
		if (!goalType.equals("AND") && !goalType.equals("OR")) {
			return new SimpleGoal(GoalType.from(goalType));
		}

		CompositeGoal logicalGoal = new CompositeGoal(
			goalType.equals("AND") ? new AndStrategy() : new OrStrategy());

		JSONArray subgoals = goalSpec.getJSONArray("subgoals");
		for (int i = 0; i < subgoals.length(); i++) {
			JSONObject subgoal = subgoals.getJSONObject(i);
			logicalGoal.addGoal(parseGoal(subgoal));
		}
		return logicalGoal;
	}

	private static Entity parseEntityFromJSON (
		JSONObject jsonEntity,
		RootGoalManager rootGoalManager, EntityFactory entityFactory) {
				
		int x = jsonEntity.getInt("x");
		int y = jsonEntity.getInt("y");
		EntityType type =
			EntityType.from(jsonEntity.getString("type"));

		switch (type) {
			case PLAYER:
				Entity player = entityFactory.createPlayer(x, y);
				rootGoalManager.setPlayer(player);
				return player;
			case WALL:
				return entityFactory.createWall(x, y);
			case EXIT:
				Entity exit = entityFactory.createExit(x, y);
				rootGoalManager.addExit(exit);
				return exit;
			case TREASURE:
				Entity treasure = entityFactory.createTreasure(x, y);
				rootGoalManager.addTreasure(treasure);
				return treasure;
			case DOOR:
				return entityFactory.createDoor(x, y, jsonEntity.getInt("id"));
			case KEY:
				return entityFactory.createKey(x, y, jsonEntity.getInt("id"));
			case BOULDER:
				Entity boulder = entityFactory.createBoulder(x, y);
				rootGoalManager.addBoulder(boulder);
				return boulder;
			case SWITCH:
				Entity floorSwitch = entityFactory.createFloorSwitch(x, y);
				rootGoalManager.addFloorSwitch(floorSwitch);
				return floorSwitch;
			case PORTAL:
				return entityFactory.createPortal(x, y, jsonEntity.getInt("id")); 
			case ENEMY:
				Entity enemy = entityFactory.createEnemy(x, y);
				rootGoalManager.addEnemy(enemy);
				return enemy;
			case SWORD:
				return entityFactory.createSword(x, y);
			case INVINCIBILITY:
				return entityFactory.createInvincibilityPotion(x, y);
			default:
				System.err.printf("unrecognised entity: %s\n", type);
				return null;
		}
	}
	
	public void update(int dt) {
		
		for (Entity potion : entityManager.getAll(EntityType.INVINCIBILITY)) {
			CollectableComponent<InvincPotion> invincPotion = 
					potion.<CollectableComponent<InvincPotion>>getComponent(CollectableComponent.class);
			invincPotion.get().update(dt);			
		}
		
		for (Entity enemy : entityManager.getAll(EntityType.ENEMY)) {
			FollowComponent enemyFollow
				= enemy.<FollowComponent>getComponent(FollowComponent.class);
			enemyFollow.nextMove();			
		}
		
	}
	
	public void render() {
		for (Entity entity : entityManager.getAll()) {
			SpriteComponent sprite
				= entity.<SpriteComponent>getComponent(SpriteComponent.class);
			if (sprite != null) {
				sprite.update();
			}
		}
	}
	
	public DungeonStatus getStatus () {		
		if (!this.entityManager.isPlayerAlive())
			return DungeonStatus.FAILED;
		else if (this.rootGoalManager.isGoalCompleted())
			return DungeonStatus.SUCCEEDED;
		else if (this.isPaused)
			return DungeonStatus.PAUSED;
		else
			return DungeonStatus.IN_PROGRESS;
	}

	public void pause () {
		this.isPaused = true;
	}
	
	public int getWidth () {
		return this.width;
	}
	
	public int getHeight () {
		return this.height;
	}
	
}
