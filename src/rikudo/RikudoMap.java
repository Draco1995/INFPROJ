package rikudo;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
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
public class RikudoMap {
	private Cell[] cellList;
	private int cellNumbers;
	private int start;
	private int innerStart;
	private int end;
	private int innerEnd;
	private int[] checkPoints;
	private boolean flag;
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
            
            for(int i=1;i<=cellNumbers;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	cellList[Integer.parseInt(s[0])] = new Cell(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),Integer.parseInt(s[4]),Integer.parseInt(s[5]),Integer.parseInt(s[6]),Integer.parseInt(s[7]));
            	checkPoints[Integer.parseInt(s[7])]=Integer.parseInt(s[0]);
            	if(Integer.parseInt(s[7])==start){
            		innerStart = Integer.parseInt(s[0]);
            	}
            	if(Integer.parseInt(s[7])==end){
            		innerEnd = Integer.parseInt(s[0]);
            	}
            }
            //System.out.println(innerEnd);
            str = reader.readLine();
            int n = Integer.parseInt(str);
            for(int i=1;i<=n;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	cellList[Integer.parseInt(s[0])].connect(Integer.parseInt(s[1]));
            	cellList[Integer.parseInt(s[1])].connect(Integer.parseInt(s[0]));
            }
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
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
		for(int i=0;i<cellNumbers;i++){
			System.out.println(cellList[i+1]);
		}
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
		trace[1] = innerStart;
		int step = 1;
		flag = false;
		DFS(step,trace);
		for(int i:trace){
			System.out.print(i+" ");
		}
	}
	public void DFS(int step,int[] trace){
		if(flag ==true){
			return;
		}
		if(trace[step]==innerEnd && step == cellNumbers){
			flag = true;
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
				if(cnext.check(step,trace[step])==true){
					cnext.avance(step,cellList);
					step++;
					trace[step] = i;
					DFS(step,trace);
					if(flag == true){
						return;
					}
					trace[step] = 0;
					step--;
					cnext.retreat(step,cellList);
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
	
	public void SAT(){
		int n = this.cellNumbers;
		ISolver solver = SolverFactory.newDefault();
		try {
			//each vertex appears once
			int[] Clause1 = new int[n];
			for (int i = 1; i <= n; i++){				
				for (int j = 0; j < n; j++){
					Clause1[j] = i*n+j+1;
				}
				solver.addClause(new VecInt(Clause1));
			}
			
			for (int i = 1; i <= n; i++){
				for (int j = 1; j <= n; j++){
					for (int k = j+1; k <= n; k++){
						solver.addClause(new VecInt(new int[] {-(i*n+j),-(i*n+k)}));
					}
				}
			}
			
			//each index is occupied once
			int[] Clause2 = new int[n];
			for (int i = 1; i <= n; i++){				
				for (int j = 0; j < n; j++){
					Clause2[j] = (j+1)*n+i;
				}
				solver.addClause(new VecInt(Clause2));
			}
			for (int i = 1; i <= n; i++){
				for (int j = 1; j <= n; j++){
					for (int k = j+1; k <= n; k++){
						solver.addClause(new VecInt(new int[] {-(j*n+i),-(k*n+i)}));
					}
				}
			}
			
			//cells with labels
			for (int i = 1; i <= n ; i++){
				Cell c = cellList[i];
				if (c.getLabel()>0){
					solver.addClause(new VecInt(new int[] {(i*n+c.getLabel())}));
				}
			}
			
			//consecutive vertices are adjacent
			for (int i = 1; i <= n; i++){
				Cell c = cellList[i];
				int[] Near = c.getNearbyCells();
			Loop:
				for (int j = 1; j<=n; j++){
					for (int k = 0; k<6; k++){
						if (j==Near[k]){
							continue Loop;
						}
					}
					for (int l = 1; l <=n; l++){
						solver.addClause(new VecInt(new int[] {-(i*n+l),-(j*n+l+1)}));
					}
				}
			}
			
			//diamond
			for (int i = 1; i <= n; i++){
				Cell c = cellList[i];
				int num = c.getDiamondNumbers();
				if (num!=0){
					for (int k = 0; k < num; k++){
						int a = c.getDiamond()[k];
						solver.addClause(new VecInt(new int[] {-(a*n+1),i*n+2}));
						for (int j = 2; j < n; j++){
							solver.addClause(new VecInt(new int[] {i*n+j-1,-(a*n+j),i*n+j+1}));
						}
						solver.addClause(new VecInt(new int[] {-(a*n+n),i*n+n-1}));
					}
				}
				
			}
		}catch (ContradictionException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (solver.isSatisfiable()) {
				//System.out.println("Satisfiable problem!");
				int[] solution = solver.model();
				/*System.out.print("Solution: " + solution[0]);
				for (int i = 1; i<n*n; i++){
					System.out.print(" " + solution[i]);
				}*/
				int[] finalLabels = new int[n+1];
				int counter = 0;
				for (int i : solution){
					if (i>0){
						counter++;
						finalLabels[counter] = i-counter*n;
					}
				}
				for (int i = 1; i <=n; i++){
					Cell c = cellList[i];
					c.changeLabel(finalLabels[i]);
				}
			} else {
				System.out.println("Unsatisfiable problem!");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
	}
}
