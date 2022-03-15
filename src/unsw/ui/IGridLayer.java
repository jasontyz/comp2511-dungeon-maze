package unsw.ui;

public interface IGridLayer {
	public void setXPos(int x);
	public void setYPos(int y);
	public void removeSelf();
	public void setImage(String imgPath);
	public void toFront();
	public void toBack();
}
