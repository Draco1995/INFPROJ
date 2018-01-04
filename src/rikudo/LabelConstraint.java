package rikudo;

public class LabelConstraint extends Constraint{
	final int i_number;
	final int label;
	static int[] checkPoints;
	static Cell[] cellList;
	public LabelConstraint(int i, int label) {
		super();
		i_number = i;
		this.label = label;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void set(){
		checkPoints[label] = i_number;
		cellList[i_number].setLabel(label);
	}
	@Override
	public void unset(){
		checkPoints[label] = 0;
		cellList[i_number].unsetLabel();
	}
}
