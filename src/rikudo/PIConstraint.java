package rikudo;

public class PIConstraint extends Constraint{
	static Cell[] cellList;
	final int cell;
	final PI pi;
	public PIConstraint(int c,PI pi){
		this.cell = c;
		this.pi = pi;
	}
	public PIConstraint(int c){
		this(c,PI.OFF);
	}
	@Override
	public void set() {
		// TODO Auto-generated method stub
		cellList[cell].setPI(pi);
	}

	@Override
	public void unset() {
		// TODO Auto-generated method stub
		cellList[cell].unsetPI(pi);
	}

}
