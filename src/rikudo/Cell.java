/*
 * The class Cell is used to construct the RikudoMap
 */
package rikudo;

public class Cell {
	//How many Cell instances;
	private static int i_number=0;
	//The list of Cells;
	private static Cell[] cellList=new Cell[300];
	//The label of this cell, 0 means not labelled;
	private int label=0;
	//The nearby cells' inner number, 0 means empty;
	/*
	 * 0 left
	 * 1 upperLeft
	 * 2 upperRight
	 * 3 right
	 * 4 lowerRight
	 * 5 lowerLeft
	 */
	private int[] nearbyCells = new int[6];
	//If the cell's label has been given;
	private boolean display;
	//Inner number label;
	private final int number;
	//the number of available edges;
	private int availableEdges;
	private int[] diamand = new int[2];
	Cell(int left,int upperLeft,int upperRight,int right,int lowerRight,int lowerLeft, int label){
		i_number++;
		number = i_number;
		nearbyCells[0] = left;
		nearbyCells[1] = upperLeft;
		nearbyCells[2] = upperRight;
		nearbyCells[3] = right;
		nearbyCells[4] = lowerRight;
		nearbyCells[5] = lowerLeft;
		availableEdges = 0;
		for(int i : nearbyCells){
			if(i!=0){
				availableEdges++;
			}
		}
		this.label = label;
		if(label!=0){
			display = true;
		}else{
			display = false;
		}
	}
	public Cell getUpperLeft(){
		if(nearbyCells[0]!=0){
			return cellList[nearbyCells[0]];
		}else{
			return null;
		}
	}
	
}
