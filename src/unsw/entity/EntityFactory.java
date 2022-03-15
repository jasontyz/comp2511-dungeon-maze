package unsw.entity;

import unsw.components.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import unsw.collectables.*;
import unsw.observable.*;
import unsw.ui.*;

public class EntityFactory {

	public final static int BOULDER_WEIGHT = 10;
	public final static int SWORD_DAMAGE = 50;
	public final static int SWORD_NHITS = 5;
	public final static int TREASURE_VALUE = 100;
	public final static int INVINC_DURATION_MS = 10 * 1000;
	public static int UID = 0;
	
	private  IObservable<MoveEvent> moveEvents;
	private IGridLayerFactory layerFactory;
    private Map<Integer, TeleporterComponent> teleporters = new HashMap<>();

	public EntityFactory (IObservable<MoveEvent> moveEvents, IGridLayerFactory layerFactory) {
		this.moveEvents = moveEvents;
		this.layerFactory = layerFactory; 
	}

	private static String withUID(String id) {
		return String.format("entity_%s_%d", id, UID++);
	}

	public Entity createPlayer (int x, int y) {
		Entity player = new Entity(withUID("player"), EntityType.PLAYER);
		TransformComponent transform = new TransformComponent(player, x, y);
		PlayerInputComponent playerInput = new PlayerInputComponent(player, moveEvents);
		MoverComponent mover = new MoverComponent(player, BOULDER_WEIGHT);
		CollectorComponent collector = new CollectorComponent(player);
		HealthComponent health = new HealthComponent(player, SWORD_DAMAGE);		
		
		player.<TransformComponent>addComponent(transform);
		player.<PlayerInputComponent>addComponent(playerInput);
		player.<MoverComponent>addComponent(mover);
		player.<CollectorComponent>addComponent(collector);
		player.<HealthComponent>addComponent(health);

		EntitySprite sprite = new EntitySprite();
		sprite.set(SpriteState.INITIAL, "human_new.png");
		sprite.set(SpriteState.WITH_POTION, "human_new_with_potion.png");
		sprite.set(SpriteState.WITH_SWORD, "human_new_with_sword.png");
		sprite.set(SpriteState.WITH_POTION_AND_SWORD, "human_new_with_potion_and_sword.png");
		player.<SpriteComponent>addComponent(createSpriteComponent(player, sprite));
		return player;
	}

	public Entity createWall (int x, int y) {
		Entity wall = new Entity(withUID("wall"), EntityType.WALL);
		TransformComponent transform = new TransformComponent(wall, x, y);

		wall.<TransformComponent>addComponent(transform);

		EntitySprite sprite = EntitySprite.createStatic("brick_brown_0.png");
		wall.<SpriteComponent>addComponent(createSpriteComponent(wall, sprite));
		return wall;
	}

	public Entity createExit (int x, int y) {
		Entity exit = new Entity(withUID("exit"), EntityType.EXIT);
		TransformComponent transform = new TransformComponent(exit, x, y);
		ZoneComponent zone = new ZoneComponent(exit);

		exit.<TransformComponent>addComponent(transform);
		exit.<ZoneComponent>addComponent(zone);

		EntitySprite sprite = EntitySprite.createStatic("exit.png");
		exit.<SpriteComponent>addComponent(createSpriteComponent(exit, sprite));
		return exit;
	}

	public Entity createTreasure (int x, int y) {
		Entity treasureEntity = new Entity(withUID("treasure"), EntityType.TREASURE);
		TransformComponent transform = new TransformComponent(treasureEntity, x, y);
		CollectableComponent<Treasure> collectable = new CollectableComponent<>(
			treasureEntity, new Treasure(TREASURE_VALUE));

		treasureEntity.<TransformComponent>addComponent(transform);
		treasureEntity.<CollectableComponent<Treasure>>addComponent(collectable);

		EntitySprite sprite = EntitySprite.createStatic("gold_pile.png");
		treasureEntity.<SpriteComponent>addComponent(createSpriteComponent(treasureEntity, sprite));
		return treasureEntity;
	}

	public Entity createDoor (int x, int y, int keyId) {
		Entity door = new Entity(withUID("door"), EntityType.DOOR);
		TransformComponent transform = new TransformComponent(door, x, y);
		LockComponent lock = new LockComponent(door, keyId);	

		door.<TransformComponent>addComponent(transform);
		door.<LockComponent>addComponent(lock);
		
		EntitySprite sprite = new EntitySprite();
		sprite.set(SpriteState.INITIAL, "closed_door.png");
		sprite.set(SpriteState.UNLOCKED, "open_door.png");
		door.<SpriteComponent>addComponent(createSpriteComponent(door, sprite));
		return door;
	}

	public Entity createKey (int x, int y, int keyId) {
		Entity keyEntity = new Entity(withUID("doorKey"), EntityType.KEY);
		TransformComponent transform = new TransformComponent(keyEntity, x, y);
		CollectableComponent<IKey> collectable = new CollectableComponent<>(keyEntity, new DoorKey(keyId)); 

		keyEntity.<TransformComponent>addComponent(transform);
		keyEntity.<CollectableComponent<IKey>>addComponent(collectable);

		EntitySprite sprite = EntitySprite.createStatic("key.png");
		keyEntity.<SpriteComponent>addComponent(createSpriteComponent(keyEntity, sprite));
		return keyEntity;
	}

	public  Entity createBoulder (int x, int y) {
		Entity boulder = new Entity(withUID("boulder"), EntityType.BOULDER);
		TransformComponent transform = new TransformComponent(boulder, x, y);
		MoveableComponent moveable = new MoveableComponent(boulder, BOULDER_WEIGHT);

		boulder.<TransformComponent>addComponent(transform);
		boulder.<MoveableComponent>addComponent(moveable);

		EntitySprite sprite = EntitySprite.createStatic("boulder.png");
		boulder.<SpriteComponent>addComponent(createSpriteComponent(boulder, sprite));
		return boulder;
	}

	public  Entity createFloorSwitch (int x, int y) {
		Entity floorSwitch = new Entity(withUID("floorSwitch"), EntityType.SWITCH);
		TransformComponent transform = new TransformComponent(floorSwitch, x, y);
		ZoneComponent zone = new ZoneComponent(floorSwitch);

		floorSwitch.<TransformComponent>addComponent(transform);
		floorSwitch.<ZoneComponent>addComponent(zone);

		EntitySprite sprite = EntitySprite.createStatic("pressure_plate.png");
		floorSwitch.<SpriteComponent>addComponent(createSpriteComponent(floorSwitch, sprite));
		return floorSwitch;
	}

	public  Entity createPortal(int x, int y, int id) {
		Entity portal = new Entity(withUID("portal"), EntityType.PORTAL);
		TransformComponent transform = new TransformComponent(portal, x, y);
		TeleporterComponent teleporter = new TeleporterComponent(portal);
		
		if (this.teleporters.containsKey(id))
			teleporter.request(this.teleporters.get(id));
		else
			this.teleporters.put(id, teleporter);

		portal.<TransformComponent>addComponent(transform);
		portal.<TeleporterComponent>addComponent(teleporter);

		EntitySprite sprite = EntitySprite.createStatic("portal.png");
		portal.<SpriteComponent>addComponent(createSpriteComponent(portal, sprite));
		return portal;
	}

	public  Entity createEnemy (int x, int y) {
		Entity enemy = new Entity(withUID("enemy"), EntityType.ENEMY);
		TransformComponent transform = new TransformComponent(enemy, x, y);
		FollowComponent follow = new FollowComponent(enemy);
		HealthComponent health = new HealthComponent(enemy, SWORD_DAMAGE);

		enemy.<TransformComponent>addComponent(transform);
		enemy.<FollowComponent>addComponent(follow);
		enemy.<HealthComponent>addComponent(health);

		EntitySprite enemySprites[] = {
			EntitySprite.createStatic("deep_elf_master_archer.png"),
			EntitySprite.createStatic("hound.png"),
			EntitySprite.createStatic("gnome.png")
		};

		enemy.<SpriteComponent>addComponent(
			createSpriteComponent(enemy, enemySprites[new Random().nextInt(3)]));
		return enemy;
	}

	public  Entity createSword (int x, int y) {
		Entity swordEntity = new Entity(withUID("sword"), EntityType.SWORD);
		TransformComponent transform = new TransformComponent(swordEntity, x, y);
		CollectableComponent<IWeapon> collectable = new CollectableComponent<>(
			swordEntity, new Sword(SWORD_DAMAGE, SWORD_NHITS));

		swordEntity.<TransformComponent>addComponent(transform);
		swordEntity.<CollectableComponent<IWeapon>>addComponent(collectable);

		EntitySprite sprite = EntitySprite.createStatic("greatsword_1_new.png");
		swordEntity.<SpriteComponent>addComponent(createSpriteComponent(swordEntity, sprite));
		return swordEntity;
	}

	public  Entity createInvincibilityPotion (int x, int y) {
		Entity invincPotion = new Entity(withUID("invinc_potion"), EntityType.INVINCIBILITY);
		TransformComponent transform = new TransformComponent(invincPotion, x, y);
		CollectableComponent<InvincPotion> collectable = new CollectableComponent<>(
			invincPotion, new InvincPotion(INVINC_DURATION_MS));

		invincPotion.<TransformComponent>addComponent(transform);
		invincPotion.<CollectableComponent<InvincPotion>>addComponent(collectable);

		EntitySprite sprite = EntitySprite.createStatic("brilliant_blue_new.png");
		invincPotion.<SpriteComponent>addComponent(createSpriteComponent(invincPotion, sprite));
		return invincPotion;
	}
	
	private SpriteComponent createSpriteComponent (Entity entity, EntitySprite sprite) {
		ZoneComponent zone =
			entity.<ZoneComponent>getComponent(ZoneComponent.class);
		IGridLayer layer = layerFactory.create();
		if (zone != null) layer.toBack();
		return new SpriteComponent(entity, layer, sprite);
	}
}
