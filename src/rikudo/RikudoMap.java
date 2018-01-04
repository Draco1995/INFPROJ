package rikudo;

import Image.ColoredPolygon;
import Image.ColoredSegment;
import Image.Image2d;
import Image.Show;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
public class RikudoMap {
	private Cell[] cellList;
	private int cellNumbers;
	private int start;
	private int innerStart;
	private int end;
	private int innerEnd;
	private int[] checkPoints;
	private boolean flag;
	private int sideLength;
	private int numberOfSolutionsFound;
	private boolean[] hash;
	LinkedList<Constraint> constraintList = new LinkedList<Constraint>();
	RikudoMap(int sideLength){
		this.sideLength = sideLength;
	}
	public void readFile(String path){
        try {  
            InputStream is = new FileInputStream(path);  
            BufferedReader reader = new BufferedReader(  
                    new InputStreamReader(is));  
            String str = null;  
            cellNumbers = Integer.parseInt(reader.readLine());
            cellList = new Cell[cellNumbers+1];
            checkPoints = new int[cellNumbers+1];
            str = reader.readLine();
            String[] s = str.split(" ");
            start = Integer.parseInt(s[0]);
            end = Integer.parseInt(s[1]);
            //Create Cells
            for(int i=1;i<=cellNumbers;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	cellList[Integer.parseInt(s[0])] = new Cell(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),Integer.parseInt(s[4]),Integer.parseInt(s[5]),Integer.parseInt(s[6]));
            	/*
            	checkPoints[Integer.parseInt(s[7])]=Integer.parseInt(s[0]);
            	if(Integer.parseInt(s[7])==start){
            		innerStart = Integer.parseInt(s[0]);
            	}
            	if(Integer.parseInt(s[7])==end){
            		innerEnd = Integer.parseInt(s[0]);
            	}*/
            }
            //Label
            str = reader.readLine();
            int n = Integer.parseInt(str);
            LabelConstraint.checkPoints = checkPoints;
            LabelConstraint.cellList = cellList;
            for(int i=1;i<=n;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	if(Integer.parseInt(s[1])==start){
            		innerStart = Integer.parseInt(s[0]);
            	}
            	if(Integer.parseInt(s[1])==end){
            		innerEnd = Integer.parseInt(s[0]);
            	}
            	LabelConstraint lc = new LabelConstraint(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
            	constraintList.add(lc);
            }
            //diamond
            str = reader.readLine();
            n = Integer.parseInt(str);
            DiamondConstraint.cellList = cellList;
            for(int i=1;i<=n;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	DiamondConstraint dc = new DiamondConstraint(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
            	constraintList.add(dc);
            	
            	/*
            	cellList[Integer.parseInt(s[0])].connect(Integer.parseInt(s[1]));
            	cellList[Integer.parseInt(s[1])].connect(Integer.parseInt(s[0]));
            	*/
            }
            //UNO
            str = reader.readLine();
            n = Integer.parseInt(str);
            UNOConstraint.cellList = cellList;
            for(int i=1;i<=n;i++){
            	str = reader.readLine();
            	UNOConstraint uc = new UNOConstraint(Integer.parseInt(str));
            	constraintList.add(uc);
            	//cellList[Integer.parseInt(str)].setUNO();
            }
            
            //PI
            str = reader.readLine();
            n = Integer.parseInt(str);
            PIConstraint.cellList = cellList;
            for(int i=1;i<=n;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	PI pi;
            	if(s[1].equals("p")){
            		pi = PI.PAIR;
            	}else{
            		pi = PI.IMPAIR;
            	}
            	PIConstraint pc = new PIConstraint(Integer.parseInt(s[0]),pi);
            	constraintList.add(pc);
            	//cellList[Integer.parseInt(s[0])].setPI(pi);
            }
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	public void applyConstraints(){
		for(Constraint c:constraintList){
        	c.set();
        }
	}
	public void removeConstraints(){
		for(Constraint c:constraintList){
        	c.unset();
        }
	}
	public void checkMapAvailability(){
		for(Cell i :cellList){
			if(i==null) continue;
			int[] nb = i.getNearbyCells();
			for(int j=0;j<=5;j++){
				if(nb[j]==0)continue;
				if((cellList[nb[j]].getNearbyCells())[(j+3)%6]!=i.getNumber()){
					System.out.println(nb[j]+" "+i.getNumber());
				}
			}
		}
	}
	public void printMap(int sideLength){
		/*
		for(int i=0;i<cellNumbers;i++){
			System.out.println(cellList[i+1]);
		}*/
		//To ajust the map
		Image2d img = new Image2d(700);
		ajust(sideLength);
		for(int i=0;i<cellNumbers;i++){
			cellList[i+1].paint(img, sideLength,false,cellList);
			if(cellList[i+1].getLabel()==start||cellList[i+1].getLabel()==end){
				cellList[i+1].paint(img, sideLength,true,cellList);
			}
		}
		Show.show(img);
	}
	private void ajust(int sideLength){
		boolean[] hash = new boolean[cellNumbers+1];
		Cell[] queue = new Cell[cellNumbers+1];
		int l = 0;
		int r = 1;
		for(boolean i:hash){i = false;}
		hash[0] = true;
		queue[0] = cellList[1];
		queue[0].setPosition(0, 0);
		hash[queue[0].getNumber()] = true;
		while(l!=r){
			Cell c = queue[l];
			int sideLength2 = Math.toIntExact(Math.round(sideLength*0.866));
			if( hash[c.getLeft()] == false ){
				Cell c2 = cellList[c.getLeft()];
				c2.setPosition(c.getPositionX()-sideLength2*2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getLowerLeft()] == false ){
				Cell c2 = cellList[c.getLowerLeft()];
				c2.setPosition(c.getPositionX()-sideLength2, Math.toIntExact(Math.round( c.getPositionY()+sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getUpperLeft()] == false ){
				Cell c2 = cellList[c.getUpperLeft()];
				c2.setPosition(c.getPositionX()-sideLength2, Math.toIntExact(Math.round( c.getPositionY()-sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getRight()] == false ){
				Cell c2 = cellList[c.getRight()];
				c2.setPosition(c.getPositionX()+sideLength2*2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getUpperRight()] == false ){
				Cell c2 = cellList[c.getUpperRight()];
				c2.setPosition(c.getPositionX()+sideLength2, Math.toIntExact(Math.round( c.getPositionY()-sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getLowerRight()] == false ){
				Cell c2 = cellList[c.getLowerRight()];
				c2.setPosition(c.getPositionX()+sideLength2, Math.toIntExact(Math.round( c.getPositionY()+sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			l++;
		}
		for(Cell i: cellList){
			if(i!=null)
				i.ajust(sideLength);
		}
	}
	public void backtracking(){
		//java.util.LinkedList<Integer> queue = new LinkedList<Integer>();
		int[] trace = new int[cellNumbers+1];
		java.util.LinkedList<Integer> UNOList = new LinkedList<Integer>();
		trace[1] = innerStart;
		int step = 1;
		flag = false;
		numberOfSolutionsFound = 0;
		DFS(step,trace,UNOList);
		/*for(int i:trace){
			System.out.print(i+" ");
		}*/
		if(numberOfSolutionsFound == 0){
			System.out.println("No solution!");
		}else if(numberOfSolutionsFound == 1){
			System.out.println("1 solution found");
		}else{
			System.out.println("At least 2 solutions");
		}
	}
	public void DFS(int step,int[] trace,java.util.LinkedList<Integer> UNOList){
		if(flag ==true){
			return;
		}
		if(trace[step]==innerEnd && step == cellNumbers){
			if(numberOfSolutionsFound==0){
				this.printMap(sideLength);
				numberOfSolutionsFound++;
			}else{
				flag = true;
				numberOfSolutionsFound++;
				this.printMap(sideLength);
			}
			
			return;
		}
		if(checkStatus(trace,step)==false){
			for(int i:trace){
				System.out.print(i+" ");
			}
			System.out.println();
			return;
		}else{
			Cell c = cellList[trace[step]];
			int[] availableCells;
			if(c.getDiamondNumbers()==0){
				availableCells = c.getNearbyCells();
			}else{
				availableCells = c.getDiamond();
			}
			for(int i : availableCells){
				if(i==0) continue;
				Cell cnext = cellList[i];
				if(cnext.check(step,trace[step],UNOList,cellList)==true){
					cnext.avance(step,cellList,UNOList);
					step++;
					trace[step] = i;
					DFS(step,trace,UNOList);
					if(flag == true){
						return;
					}
					trace[step] = 0;
					step--;
					cnext.retreat(step,cellList,UNOList);
				}
			}
		}
	}
	public boolean checkStatus(int[] trace,int step){
		//check if this step is given, if it is, check if we are on the right place
		if(checkPoints[step]!=0){
			if(trace[step]!=checkPoints[step]){
				return false;
			}
		}
		//check if nearby available cells have available edges, except the final points
		int[] availableCells = cellList[trace[step]].getNearbyCells();
		for(int i:availableCells){
			if(i==0) continue;
			Cell c = cellList[i];
			if(i==innerEnd){
				continue;
			}
			if(c.check()==true){
				if(c.getAvailableEdges()==0){
					return false;
				}
			}
		}
		return true;
	}
	public static int[] createRandomSeries(int[] n,Random random){
		int len = n.length;
		for(int i=0;i<len;i++){
			int a = random.nextInt(len);
			int b = random.nextInt(len);
			int c = n[a];
			n[a] = n[b];
			n[b] = c;
		}
		return n;
	}
	public void createPuzzle(){
		Random random = new Random();
		int[] series = new int[cellNumbers+1];
		int start = random.nextInt(cellNumbers)+1;
		int step = 1;
		series[1] = start;
		flag = false;
		hash = new boolean[cellNumbers+1];
		CDFS(series,step,start,random);
		for(int i:series){
			System.out.print(i+" ");
		}
		
	}
	public void CDFS(int[] series,int step, int cell,Random random){
		for(int i:series){
			System.out.print(i+" ");
		}System.out.println();
		if(flag == true){
			return;
		}else if(step==cellNumbers){
			flag = true;
			return;
		}else{
			int[] nearbyCells = cellList[cell].getNearbyCells();
			int[] nc = nearbyCells.clone();
			nc = createRandomSeries(nc,random);
			for(int c:nc){
				if(c==0) continue;
				if(hash[c]==false){
					hash[c] = true;
					step++;
					series[step] = c;
					CDFS(series,step,c,random);
					if(flag == true) return;
					series[step] = 0;
					step--;
					hash[c] = false;
				}
			}
		}
	}
}
