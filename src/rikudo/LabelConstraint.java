package rikudo;

public class LabelConstraint extends Constraint{
	final int i_number;
	final int label;
	static int[] checkPoints;
	static Cell[] cellList;
	public LabelConstraint(int i, int label) {//i is the inner number of this cell, label is the label for this cell
		super();
		i_number = i;
		this.label = label;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void set(){
		status = true;
		checkPoints[label] = i_number;
		cellList[i_number].setLabel(label);
	}
	@Override
	public void unset(){
		status = false;
		checkPoints[label] = 0;
		cellList[i_number].unsetLabel();
	}
	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "Label";
	}
	@Override
	public int getCell() {
		// TODO Auto-generated method stub
		return i_number;
	}
}
