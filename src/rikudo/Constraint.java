/*
 * The abstract class for LabelConstraint, UNOConstraint, PIConstraint,DiamondConstraint
 */
package rikudo;

public abstract class Constraint {
	boolean status = false;
	public Constraint(){
		status = false;
	}
	abstract public void set();
	abstract public void unset();
	abstract public String type();
	abstract public int getCell();
	public boolean getStatus(){
		return status;
	}
}
