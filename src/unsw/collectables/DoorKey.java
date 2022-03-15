package unsw.collectables;

import unsw.components.LockComponent;
import unsw.observable.*;

public class DoorKey extends Observable<Boolean> implements IKey {
	private final int keyId;

	public DoorKey (int keyId) {
		this.keyId = keyId;
	}

	public boolean unlock (LockComponent lock) {
		boolean used = lock.unlock(keyId);
		this.notifyObservers(used);
		return used;
	}
}
