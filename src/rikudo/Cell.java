/*
 * The class Cell is used to construct the RikudoMap
 */
package rikudo;

import java.awt.Color;

import Image.Image2d;

public class Cell {
	//How many Cell instances;
	private static int i_number=0;
	//The list of Cells;
	//private static Cell[] cellList=new Cell[300];
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
	private int diamandNumbers = 0;
	private int diamandNumbersMax = 0;
	private static int paintAjustX = 0;
	private static int paintAjustY = 0;
	private int positionX = 0;
	private int positionY = 0;
	private boolean passFlag = false;
	Cell(int number,int left,int upperLeft,int upperRight,int right,int lowerRight,int lowerLeft, int label){
		i_number++;
		this.number = number;
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
		passFlag = false;
	}
	public void connect(int number){
		diamand[diamandNumbers] = number;
		diamandNumbers++;
		diamandNumbersMax = diamandNumbers;
	}
	public int getDiamandNumbers(){
		return diamandNumbers;
	}
	public int[] getDiamand(){
		return diamand;
	}
	public int[] getNearbyCells(){
		return nearbyCells;
	}
    public int getUpperLeft(){
		return nearbyCells[1];
	}
	public int getLeft(){
		return nearbyCells[0];
	}
	public int getUpperRight(){
		return nearbyCells[2];
	}
	public int getRight(){
		return nearbyCells[3];
	}
	public int getLowerRight(){
		return nearbyCells[4];
	}
	public int getLowerLeft(){
		return nearbyCells[5];
	}
	@Override
	public String toString(){
		String str = "\t"+nearbyCells[1]+"\t\t"+nearbyCells[2]+"\r"+nearbyCells[0]+"\t\t"+number
				+"\t\t"+nearbyCells[3]+"\r\t"+nearbyCells[5]+"\t\t"+nearbyCells[4]+"\r";
		return str;
		
	}
	public void ajust(int sideLength){
		while(positionX+paintAjustX*0.866<=0){
			paintAjustX+=sideLength;
		}
		while(positionY+paintAjustY<=0){
			paintAjustY+=sideLength;
		}
	}
	public int getNumber(){
		return number;
	}
	public void setPosition(int x,int y){
		positionX = x;
		positionY = y;
	}
	public int getPositionX(){
		return positionX;
	}
	public int getPositionY(){
		return positionY;
	}
	public void paint(Image2d img,int sl,boolean flag){
		int x = positionX+paintAjustX;
		int y = positionY+paintAjustY;
		int sll = Math.toIntExact( Math.round( sl*0.866 ));
		if(display==true){
			img.addPolygon(new int[] {x-sll,x,x+sll,x+sll,x,x-sll}, 
					new int[] {
							Math.toIntExact( Math.round( y-sl*0.5 )),
							y-sl,
							Math.toIntExact( Math.round( y-sl*0.5 )),
							Math.toIntExact( Math.round( y+sl*0.5 )),
							y+sl,
							Math.toIntExact( Math.round( y+sl*0.5 ))
							}, 
					Color.ORANGE, Color.BLACK);
		}else{
			img.addPolygon(new int[] {x-sll,x,x+sll,x+sll,x,x-sll}, 
					new int[] {
							Math.toIntExact( Math.round( y-sl*0.5 )),
							y-sl,
							Math.toIntExact( Math.round( y-sl*0.5 )),
							Math.toIntExact( Math.round( y+sl*0.5 )),
							y+sl,
							Math.toIntExact( Math.round( y+sl*0.5 ))
							}, 
					Color.WHITE, Color.BLACK);
		}
		
		img.addString(""+number, x-sl/8, y-sl/3*2+sl/8, Color.GRAY,sl/4);
		if(label==0){
			return;
		}
		if(display==true){
			img.addString(""+label, x-sl/4, y+sl/4, Color.black,sl/2);
		}else{
			img.addString(""+label, x-sl/4, y+sl/4, Color.cyan,sl/2);
		}
		
		if(flag==true){
			sl = sl*9/10;
			sll = sll*9/10;
			img.addPolygon(new int[] {x-sll,x,x+sll,x+sll,x,x-sll}, 
					new int[] {
							Math.toIntExact( Math.round( y-sl*0.5 )),
							y-sl,
							Math.toIntExact( Math.round( y-sl*0.5 )),
							Math.toIntExact( Math.round( y+sl*0.5 )),
							y+sl,
							Math.toIntExact( Math.round( y+sl*0.5 ))
							}, 
					Color.ORANGE, Color.BLACK);
		}
	}
	public int getLabel(){
		return label;
	}
	//Check if this cell is available
	public boolean check(int step,int previousCell){
		step++;
		//if passed before
		if(passFlag == true){
			return false;
		}
		//if stepped on a label-given cell, check if we have the correct step
		if(label!=0&&step!=label){
			return false;
		}
		//if this cell has two diamonds, thats means the previous must be one of given cells
		if(diamandNumbersMax==2){
			if(diamand[0]!=previousCell&&diamand[1]!=previousCell){
				return false;
			}
		}
		return true;
	}
	public boolean check(){
		//if passed before
		if(passFlag == true){
			return false;
		}
		return true;
	}
	public void avance(int step,Cell[] cellList){
		passFlag = true;
		label = step+1;
		if(diamandNumbers!=0)
			diamandNumbers--;
		for(int i:nearbyCells){
			if(i==0) continue;
			cellList[i].addAvailableEgde();
		}
	}
	public void addAvailableEgde(){
		availableEdges++;
	}
	public void minusAvailableEgde(){
		availableEdges--;
	}
	public void retreat(int step,Cell[] cellList){
		passFlag = false;
		if(display==false){
			label = 0;
		}
		if(diamandNumbers<diamandNumbersMax){
			diamandNumbers++;
		}
		for(int i:nearbyCells){
			if(i==0) continue;
			cellList[i].minusAvailableEgde();
		}
		
	}
	public int getAvailableEdges(){
		return availableEdges;
	}
}
