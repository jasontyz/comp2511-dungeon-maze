package unsw.collectables;

import unsw.components.LockComponent;
import unsw.observable.*;

public interface IKey extends IObservable<Boolean> {
	 public boolean unlock (LockComponent lock);
}
