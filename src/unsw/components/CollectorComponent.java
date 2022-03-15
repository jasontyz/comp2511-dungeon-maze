package unsw.components;

import java.util.HashSet;
import java.util.Set;

import unsw.collectables.*;

public class CollectorComponent extends ObservableComponent<CollectableComponent<?>> {

	private Sword sword = null;
	private Set<IWeapon> weapons = new HashSet<>();
	private Set<IKey> keys = new HashSet<>();

	public CollectorComponent (Entity entity) {
		super(entity);
	}

	public boolean hasSword () {
		return this.sword != null;
	}

	public boolean hasPotion () {
		return this.weapons.size() > 0;
	}

	// ---------------------------------------------------

	public boolean pickupSword (Entity entity) {
		CollectableComponent<Sword> collectable =
			entity.<CollectableComponent<Sword>>getComponent(CollectableComponent.class);
		if (collectable == null) return false;

		if (this.sword != null) return false;
		this.sword = collectable.get();
		entity.removeComponent(TransformComponent.class);
		sword.addObserver((Integer hits) -> {
			if (hits <= 0) this.sword = null;
		});
		this.notifyObservers(collectable);
		return true;
	}
	
	public boolean pickupWeapon (Entity entity) {
		CollectableComponent<IWeapon> collectable =
			entity.<CollectableComponent<IWeapon>>getComponent(CollectableComponent.class);
		if (collectable == null) return false;

		IWeapon weapon = collectable.get();
		this.weapons.add(weapon);
		entity.removeComponent(TransformComponent.class);
		weapon.addObserver((Integer ammo) -> {
			if (ammo <= 0) this.weapons.remove(weapon);
		});
		this.notifyObservers(collectable);
		
		
		return true;
	}

	public boolean pickupKey (Entity entity) {
		CollectableComponent<IKey> collectable =
			entity.<CollectableComponent<IKey>>getComponent(CollectableComponent.class);
		if (collectable == null) return false;

		IKey key = collectable.get();
		this.keys.add(key);
		entity.removeComponent(TransformComponent.class);
		key.addObserver((Boolean used) -> {
			if (used) this.keys.remove(key);
		});
		this.notifyObservers(collectable);
		return true;
	}

	public void pickupTreasure (Entity entity) {
		CollectableComponent<Treasure> collectable =
				entity.<CollectableComponent<Treasure>>getComponent(CollectableComponent.class);
		if (collectable == null) return;
			
		entity.removeComponent(TransformComponent.class);
		this.notifyObservers(collectable);		
	}

	// ---------------------------------------------------

	public void attack (Entity entity) {
		HealthComponent health =
			entity.<HealthComponent>getComponent(HealthComponent.class);

		if (health == null) return;

		if (this.sword == null) {
			for (IWeapon weapon : this.weapons)
				weapon.attack(health);
			return;
		}

		this.sword.attack(health);
	}

	public boolean unlock (Entity entity) {
		LockComponent lock =
			entity.<LockComponent>getComponent(LockComponent.class);

		if (lock == null) return false;
		for (IKey key : this.keys)
			if (key.unlock(lock)) return true;
		return false;
	}

}
