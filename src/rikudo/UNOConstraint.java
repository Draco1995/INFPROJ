package rikudo;

public class UNOConstraint extends Constraint{
	static Cell[] cellList;
	final int cell;
	public UNOConstraint(int c) {
		this.cell = c;
	}
	@Override
	public void set(){
		cellList[cell].setUNO();
	}
	@Override
	public void unset(){
		cellList[cell].unsetUNO();
	}
}
