package unsw.collectables;

import unsw.observable.*;
import unsw.components.HealthComponent;

public class Sword extends Observable<Integer> implements IWeapon {

	private final int damage;
	private int nhits;

	public Sword (int damage, int nhits) {
		this.damage = damage;
		this.nhits = nhits;
	}

	public void attack (HealthComponent health) {
		health.hit(this.damage);
		this.notifyObservers(--this.nhits);
	}
}
