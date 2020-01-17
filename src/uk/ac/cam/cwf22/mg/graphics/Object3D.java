package uk.ac.cam.cwf22.mg.graphics;


//abstract class - so all objects can implement draw()

public abstract class Object3D
{
	protected boolean updated;
	
	//returns an Object2D for use on the given display
	public abstract Object2D projectOrthogonal(Display2D d);
	
	public abstract void rotateAboutY(double theta);
	public abstract void rotateAboutX(double theta);
	
	public abstract void setUpdate(boolean b);
	public abstract boolean getUpdate();
}
