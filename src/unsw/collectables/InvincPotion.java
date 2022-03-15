package unsw.collectables;

import unsw.components.HealthComponent;
import unsw.observable.*;

public class InvincPotion extends Observable<Integer> implements IWeapon {

	private int activeFor;
	private boolean isActive;

	public InvincPotion (int activeFor) {
		this.activeFor = activeFor;
	}

	public void attack (HealthComponent health) {
		if (this.isActive) health.die();
	}

	@Override
	public void activate () {
		this.isActive = true;
		this.notifyObservers(this.activeFor);
	}
	
	public boolean isActive () {
		return this.isActive;
	}
	
	public void update (int dt) {
		
		if (!this.isActive) return;
		
		this.activeFor -= dt;
		
		if (this.activeFor <= 0) {
			this.isActive = false;			
		}
		
		this.notifyObservers(this.activeFor);
	}
}
