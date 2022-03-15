package unsw.entity;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import unsw.components.*;
import unsw.collectables.*;

public class EntityManager {

	List<Entity> entities = new ArrayList<>();
	Map<EntityType, List<Entity>> byType = new HashMap<>();

	public void addEntity (Entity entity) {
		this.entities.add(entity);

		List<Entity> entitiesByType = this.byType.getOrDefault(
			entity.type, new ArrayList<Entity>());

		entitiesByType.add(entity);
		this.byType.put(entity.type, entitiesByType);
	}

	public List<Entity> getAll() {
		return this.entities;
	}
	
	public List<Entity> getAll (EntityType type) {
		return this.byType.getOrDefault(type, new ArrayList<Entity>());
	}

	public boolean isPlayerAlive () {
		for (Entity player : this.getAll(EntityType.PLAYER)) {
			HealthComponent health =
				player.<HealthComponent>getComponent(HealthComponent.class);
			if (!health.isAlive()) return false;
		}
		return true;
	}

	public void setup () {
		this.setupPlayerObservers();
		this.setupEnemyObservers();
		this.setupWallObservers();
		this.setupDoorObservers();
		this.setupKeyObservers();
		this.setupBoulderObservers();
		this.setupSwitchObservers();
		this.setupPortalObservers();
		this.setupSwordObservers();
		this.setupInvinPotionObservers();
		this.setupTreasureObservers();
	}

	private void setupPlayerObservers () {
		for (Entity player : this.getAll(EntityType.PLAYER)) {
			player.addCollisionStrategy(this.getAll(EntityType.ENEMY),
				(Entity enemy, Delta<Point2D> delta) -> this.handleEnemyPlayerCollision(player, enemy));
		}
	}
	
	private void setupEnemyObservers () {
		for (Entity enemy : this.getAll(EntityType.ENEMY)) {
			for (Entity player : this.getAll(EntityType.PLAYER)) {
				enemy.addCollisionStrategy(player,
					(Entity entity, Delta<Point2D> delta) -> this.handleEnemyPlayerCollision(player, enemy));
				FollowComponent follow =
					enemy.<FollowComponent>getComponent(FollowComponent.class);
				follow.follow(player, entities);
			}
		}
	}

	private void setupWallObservers () {
		List<Entity> players = this.getAll(EntityType.PLAYER);
		List<Entity> enemies = this.getAll(EntityType.ENEMY);
		List<Entity> boulders = this.getAll(EntityType.BOULDER);
		
		CollisionStrategy strategy = (Entity entity, Delta<Point2D> delta) -> {

			TransformComponent transform =
				entity.<TransformComponent>getComponent(TransformComponent.class);
			transform.translate(delta.from.x, delta.from.y);

		};

		for (Entity wall : this.getAll(EntityType.WALL)) {
			wall.addCollisionStrategy(players, strategy);
			wall.addCollisionStrategy(enemies, strategy);
			wall.addCollisionStrategy(boulders, strategy);
		}
	}

	private void setupDoorObservers () {	
		for (Entity door : this.getAll(EntityType.DOOR)) {
			door.addCollisionStrategy(this.getAll(EntityType.PLAYER), (Entity player, Delta<Point2D> delta) -> {

				CollectorComponent playerCollector =
					player.<CollectorComponent>getComponent(CollectorComponent.class);
				TransformComponent playerTransform =
						player.<TransformComponent>getComponent(TransformComponent.class);
				LockComponent lock = door.<LockComponent>getComponent(LockComponent.class);

				if (delta.from.x == delta.to.x && (lock.isUnlocked() || playerCollector.unlock(door))) 
					playerTransform.translateY(delta.from.y < delta.to.y ? 1 : -1);
				else
					playerTransform.translate(delta.from.x, delta.from.y);

			});
			
			door.addCollisionStrategy(this.getAll(EntityType.ENEMY), (Entity enemy, Delta<Point2D> delta) -> {

				TransformComponent enemyTransform =
						enemy.<TransformComponent>getComponent(TransformComponent.class);
				LockComponent lock = door.<LockComponent>getComponent(LockComponent.class);

				if (lock.isUnlocked()) 
					enemyTransform.translateY(delta.from.y < delta.to.y ? 1 : -1);
				else
					enemyTransform.translate(delta.from);

			});
		}
	}

	private void setupKeyObservers () {
		for (Entity key : this.getAll(EntityType.KEY)) {
			key.addCollisionStrategy(this.getAll(EntityType.PLAYER), (Entity player, Delta<Point2D> delta) -> {

				CollectorComponent playerCollector =
					player.<CollectorComponent>getComponent(CollectorComponent.class);
				playerCollector.pickupKey(key);

			});
		}
	}

	private void setupBoulderObservers () {
		for (Entity boulder : this.getAll(EntityType.BOULDER)) {
			boulder.addCollisionStrategy(this.getAll(EntityType.PLAYER),
				(Entity player, Delta<Point2D> delta) -> {

					MoverComponent playerMover =
						player.<MoverComponent>getComponent(MoverComponent.class);
					
					if (!playerMover.move(boulder, delta, this.entities)) {
						TransformComponent playerTransform =
								player.<TransformComponent>getComponent(TransformComponent.class);
						playerTransform.translate(delta.from);
					}

				});
		}
	}

	private void setupSwitchObservers () {
		for (Entity floorSwitch : this.getAll(EntityType.SWITCH)) {
			for (Entity boulder : this.getAll(EntityType.BOULDER)) {
				ZoneComponent switchZone =
					floorSwitch.<ZoneComponent>getComponent(ZoneComponent.class);
				switchZone.watch(boulder);
			}
		}
	}

	private void setupPortalObservers () {
		List<Entity> players = this.getAll(EntityType.PLAYER);
		List<Entity> enemies = this.getAll(EntityType.ENEMY);
		List<Entity> boulders = this.getAll(EntityType.BOULDER);
		
		for (Entity portal : this.getAll(EntityType.PORTAL)) {
			CollisionStrategy strategy = (Entity entity, Delta<Point2D> delta) -> {

				TeleporterComponent teleporter =
					portal.<TeleporterComponent>getComponent(TeleporterComponent.class);
				teleporter.teleport(entity, delta);

			};

			portal.addCollisionStrategy(players, strategy);
			portal.addCollisionStrategy(enemies, strategy);
			portal.addCollisionStrategy(boulders, strategy);
		}
	}

	private void setupSwordObservers () {
		for (Entity sword : this.getAll(EntityType.SWORD)) {
			sword.addCollisionStrategy(this.getAll(EntityType.PLAYER), (Entity player, Delta<Point2D> delta) -> {

				CollectorComponent playerCollector =
					player.<CollectorComponent>getComponent(CollectorComponent.class);
				playerCollector.pickupSword(sword);

			});
		}
	}

	private void setupInvinPotionObservers () {
		for (Entity potion : this.getAll(EntityType.INVINCIBILITY)) {
			potion.addCollisionStrategy(this.getAll(EntityType.PLAYER), (Entity player, Delta<Point2D> delta) -> {

				CollectorComponent playerCollector =
					player.<CollectorComponent>getComponent(CollectorComponent.class);
				if (playerCollector.pickupWeapon(potion)) {
					CollectableComponent<IWeapon> invincPotion =
						potion.<CollectableComponent<IWeapon>>getComponent(CollectableComponent.class);
					invincPotion.get().activate();					
				}

			});
		}
	}
	 
	private void setupTreasureObservers() {
		for (Entity treasure : this.getAll(EntityType.TREASURE)) {
			treasure.addCollisionStrategy(this.getAll(EntityType.PLAYER), (Entity player, Delta<Point2D> delta) -> {

				CollectorComponent playerCollector =
					player.<CollectorComponent>getComponent(CollectorComponent.class);
				playerCollector.pickupTreasure(treasure);

			});
		}
	}

	private void handleEnemyPlayerCollision (Entity player, Entity enemy) {
		CollectorComponent playerCollector =
			player.<CollectorComponent>getComponent(CollectorComponent.class);

		playerCollector.attack(enemy);
		HealthComponent enemyHealth =
			enemy.<HealthComponent>getComponent(HealthComponent.class);
		if (enemyHealth.isAlive()) {
			HealthComponent playerHealth =
				player.<HealthComponent>getComponent(HealthComponent.class);
			playerHealth.die();
			player.removeComponent(TransformComponent.class);
		}
		else {
			enemyHealth.die();
			enemy.removeComponent(TransformComponent.class);
		}
	}
}
