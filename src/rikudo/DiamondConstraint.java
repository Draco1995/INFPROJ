package rikudo;

public class DiamondConstraint extends Constraint{
	final int a;
	final int b;
	static Cell[] cellList;
	public DiamondConstraint(int a,int b) {
		super();
		this.a = a;
		this.b = b;
	}
	@Override
	public void set(){
		cellList[a].connect(b);
    	cellList[b].connect(a);
	}
	@Override
	public void unset(){
		cellList[a].disconnect(b);
		cellList[b].disconnect(a);
	}
}
