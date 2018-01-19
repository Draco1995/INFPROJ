package rikudo;

public class UNOConstraint extends Constraint{
	static Cell[] cellList;
	final int cell;
	final int yu;
	public UNOConstraint(int c,int yu) {
		this.cell = c;
		this.yu = yu;
	}
	@Override
	public void set(){
		status = true;
		cellList[cell].setUNO();
	}
	@Override
	public void unset(){
		status = false;
		cellList[cell].unsetUNO();
	}
	@Override
	public String type() {
		// TODO Auto-generated method stub
		return "UNO";
	}
	public int getCell(){
		return cell;
	}
	public int getYu(){
		return yu;
	}
}
