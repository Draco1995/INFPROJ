
package rikudo;

import java.awt.Color;

import Image.Image2d;
/**
 * 
 * The class Cell is used to construct the RikudoMap
 *
 * @author MSI
 *
 */
public class Cell {
	/**
	 * How many Cell instances;
	 */
	private static int i_number=0;
	//The list of Cells;
	//private static Cell[] cellList=new Cell[300];
	/**
	 * he label of this cell, 0 means not labelled;
	 */
	private int label=0;
	/**
	 * The nearby cells' inner number, 0 means empty;
	 * 0 left
	 * 1 upperLeft
	 * 2 upperRight
	 * 3 right
	 * 4 lowerRight
	 * 5 lowerLeft
	 */
	private int[] nearbyCells = new int[6];
	/**
	 * If the cell's label has been given;
	 */
	private boolean display;
	/**
	 * Inner number label;
	 */
	private final int number;
	/**
	 * the number of available edges;
	 */
	private int availableEdges;
	/**
	 * The list of diamonds
	 */
	private int[] diamond = new int[2];
	private int diamondNumbers = 0;
	private int diamondNumbersMax = 0;
	/**
	 * For printing
	 */
	private static int paintAjustX = 0;
	private static int paintAjustY = 0;
	private int positionX = 0;
	private int positionY = 0;
	/**
	 * if the cell have been passed
	 */
	private boolean passFlag = false;
	/**
	 * If this Cell have a UNO constraint
	 */
	private boolean flagUNO = false;
	/**
	 * If this Cell have a PI constraint
	 */
	private PI flagPI;
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
		passFlag = false;
		flagUNO = false;
		flagPI = PI.OFF;
		this.label = label;
		if(label!=0){
			display = true;
		}else{
			display = false;
		}
	}
	Cell(int number,int left,int upperLeft,int upperRight,int right,int lowerRight,int lowerLeft){
		this(number,left,upperLeft,upperRight,right,lowerRight,lowerLeft, 0);

	}
	public void initial(){
		passFlag = false;
		flagUNO = false;
		flagPI = PI.OFF;
		label = 0;
		display = false;
		diamond = new int[2];
		diamondNumbers = 0;
		diamondNumbersMax = 0;
	}
	public boolean haveConstraint(){
		if(flagUNO==true||label!=0||flagPI!=PI.OFF||diamondNumbersMax!=0){
			return true;
		}
		else{
			return false;
		}
	}
	public void changeLabel(int label){
		this.label = label;
	}
	public void setLabel(int label){
		this.label = label;
		display = true;
	}
	public void unsetLabel(){
		this.label = 0;
		display = false;
	}
	public void connect(int number){
		boolean f = false;
		for(int i = 0;i<diamondNumbersMax;i++){
			if(diamond[i]==number){
				f = true;
				break;
			}
		}
		if(f==true){
			return;
		}
		try{
		diamond[diamondNumbersMax] = number;
		diamondNumbersMax++;
		diamondNumbers = diamondNumbersMax;
		}catch(Exception e){
			System.out.println(diamondNumbers);
		}
		
	}
	public void disconnect(int number){
		boolean f = false;
		for(int i = 0;i<diamondNumbersMax;i++){
			if(diamond[i]==number){
				f = true;
				break;
			}
		}
		if(f==false){
			return;
		}
		if(diamondNumbersMax == 2){
			if(diamond[0]==number){
				diamond[0] = diamond[1];
			}
			diamondNumbers = 1;
			diamondNumbersMax = 1;
		}else{
			diamondNumbers = 0;
			diamondNumbersMax = 0;
		}

	}
	public int getDiamondNumbers(){
		return diamondNumbers;
	}
	public int[] getDiamond(){
		int[] diamond2 = new int[diamondNumbersMax];
		for(int i = 0;i<diamondNumbersMax;i++){
			diamond2[i] = diamond[i];
		}
		return diamond2;
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
		while(positionX+paintAjustX-sideLength<=0){
			paintAjustX+=sideLength;
		}
		while(positionY+paintAjustY-sideLength<=0){
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
	public void paint(Image2d img,int sl,boolean flag,Cell[] cellList){
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
		//PaintDiamond
		for(int i = 0;i<diamondNumbersMax;i++){
			Cell c = cellList[diamond[i]];
			int vecteurX = c.getPositionX()-positionX;
			int vecteurY = c.getPositionY()-positionY;
			vecteurX /= 10;
			vecteurY /= 10;
			int centerX = (c.getPositionX()+positionX)/2+paintAjustX;
			int centerY = (c.getPositionY()+positionY)/2+paintAjustY;
			img.addPolygon(new int[] {centerX-vecteurX,centerX-vecteurY,centerX+vecteurY}, 
					new int[] { centerY-vecteurY,centerY+vecteurX,centerY-vecteurX
							}, 
					Color.BLACK, Color.BLACK);
		}
		if(flagUNO==true)this.paintUNO(img,sl);
		if(flagPI!=PI.OFF)this.paintPI(img,sl);
		if(label==0){
			return;
		}
		if(display==true){
			img.addString(""+label, x-sl/4, y+sl/4, Color.black,sl/2);
		}else{
			img.addString(""+label, x-sl/4, y+sl/4, Color.cyan,sl/2);
		}
	}
	private void paintUNO(Image2d img,int sl){
		int x = positionX+paintAjustX;
		int y = positionY+paintAjustY;
		int sll = Math.toIntExact( Math.round( sl*0.433));
		img.addPolygon(new int[] {x,x-sll,x+sll}, 
				new int[] {y-sl,y-sl*3/4,y-sl*3/4}, 
				Color.BLACK, Color.BLACK);
		img.addPolygon(new int[] {x,x-sll,x+sll}, 
				new int[] {y+sl,y+sl*3/4,y+sl*3/4}, 
				Color.BLACK, Color.BLACK);
	}
	private void paintPI(Image2d img,int sl){
		int x = positionX+paintAjustX;
		int y = positionY+paintAjustY;
		int sll = Math.toIntExact( Math.round( sl*0.433));
		if(flagPI==PI.IMPAIR){
			img.addCircle(x-sl/12, y+sl*3/4-sl/12, sl/6, Color.black);
			img.addCircle(x-sl/12, y-sl*3/4-sl/12, sl/6, Color.black);
		}else{
			img.addCircle(x-sll/3-sl/12, y+sl*3/4-sl/12, sl/6, Color.black);
			img.addCircle(x+sll/3-sl/12, y-sl*3/4-sl/12, sl/6, Color.black);
			img.addCircle(x+sll/3-sl/12, y+sl*3/4-sl/12, sl/6, Color.black);
			img.addCircle(x-sll/3-sl/12, y-sl*3/4-sl/12, sl/6, Color.black);
		}
	}
	public int getLabel(){
		return label;
	}
	/**
	 * Check if this cell is available
	 * @param step
	 * @param previousCell
	 * @param UNOList
	 * @param cellList
	 * @return
	 */
	public boolean check(int step,int previousCell,java.util.LinkedList<Integer> UNOList,Cell[] cellList){
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
		if(diamondNumbersMax==2){
			if(diamond[0]!=previousCell&&diamond[1]!=previousCell){
				return false;
			}
		}
		
		//Check UNO
		if(flagUNO==true&&UNOList.isEmpty()==false){
			int f = (cellList[UNOList.getFirst()].getLabel())%10;
			if(f!=(step%10)){
				return false;
			}
		}
		//Check PI
		if((step%2==1&&flagPI==PI.PAIR)||(step%2==0&&flagPI==PI.IMPAIR)){
			return false;
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
	public void avance(int step,Cell[] cellList,java.util.LinkedList<Integer> UNOList){
		passFlag = true;
		label = step+1;
		/*
		if(diamondNumbers!=0)
			diamondNumbers--;*/
		for(int i:nearbyCells){
			if(i==0) continue;
			cellList[i].addAvailableEgde();
		}
		//UNO
		if(flagUNO==true){
			UNOList.push(number);
		}
	}
	public void addAvailableEgde(){
		availableEdges++;
	}
	public void minusAvailableEgde(){
		availableEdges--;
	}
	
	public void addDiamondNumbers(){
		if(diamondNumbers<diamondNumbersMax){
			diamondNumbers++;
		}
	}
	
	public void minusDiamondNumbers() throws Exception{
		if(diamondNumbers==0){
			
			System.err.println("error diamondNumbers");
			throw new Exception();
		}else{
			diamondNumbers--;
		}
	}
	public void retreat(int step,Cell[] cellList,java.util.LinkedList<Integer> UNOList){
		passFlag = false;
		if(display==false){
			label = 0;
		}
		/*
		if(diamondNumbers<diamondNumbersMax){
			diamondNumbers++;
		}*/
		for(int i:nearbyCells){
			if(i==0) continue;
			cellList[i].minusAvailableEgde();
		}
		if(flagUNO==true){
			int f = UNOList.pop();
			if(f!=number){
				System.out.println("ERROR");
			}
		}
	}
	public int getAvailableEdges(){
		return availableEdges;
	}
	
	public void setUNO(){
		flagUNO = true;
	}
	
	public void unsetUNO(){
		flagUNO = false;
	}
	
	public void setPI(PI pi){
		flagPI = pi;
	}
	
	public void unsetPI(PI pi){
		flagPI = PI.OFF;
	}
}

