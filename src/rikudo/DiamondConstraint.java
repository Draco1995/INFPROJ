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
		status = true;
			cellList[a].connect(b);
			cellList[b].connect(a);
	}
	@Override
	public void unset(){
		status = false;
		cellList[a].disconnect(b);
		cellList[b].disconnect(a);
	}
	@Override
	public String type() {
		return "Diamond";
	}
	@Override
	public int getCell() {
		// TODO Auto-generated method stub
		return a;
	}
	
	public String toString(){
		return ""+a+" "+b+ " " + status;
	}
}
