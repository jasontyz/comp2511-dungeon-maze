package unsw.components;

public class LockComponent extends Component {
	

	private final int keyId;
	private LockState status = LockState.LOCKED;
	
	public LockComponent(Entity entity, int keyId) {
		super(entity);
		this.keyId = keyId;
	}

	public boolean unlock (int keyId) {
		if (this.keyId == keyId) {
			this.status = LockState.UNLOCKED;
			return true;
		}
		return false;
	}
	
	public boolean isUnlocked() {
		if (this.status == LockState.LOCKED) return false;
		return true;
	}
	
}
