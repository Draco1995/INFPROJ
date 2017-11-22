package rikudo;

import Image.ColoredPolygon;
import Image.ColoredSegment;
import Image.Image2d;
import Image.Show;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
public class RikudoMap {
	private Cell[] cellList = new Cell[300];
	private int cellNumbers;
	private int start;
	private int end;
	public void readFile(String path){
        try {  
            InputStream is = new FileInputStream(path);  
            BufferedReader reader = new BufferedReader(  
                    new InputStreamReader(is));  
            String str = null;  
            cellNumbers = Integer.parseInt(reader.readLine());
            str = reader.readLine();
            String[] s = str.split(" ");
            start = Integer.parseInt(s[0]);
            end = Integer.parseInt(s[1]);
            for(int i=1;i<=cellNumbers;i++){
            	str = reader.readLine();
            	s = str.split(" ");
            	cellList[Integer.parseInt(s[0])] = new Cell(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]),Integer.parseInt(s[4]),Integer.parseInt(s[5]),Integer.parseInt(s[6]),Integer.parseInt(s[7]));
            }
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
	public void printMap(int sideLength){
		for(int i=0;i<cellNumbers;i++){
			System.out.println(cellList[i+1]);
		}
		//To ajust the map
		Image2d img = new Image2d(700);
		ajust(sideLength);
		for(int i=0;i<cellNumbers;i++){
			cellList[i+1].paint(img, sideLength);
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
			if( hash[c.getLeft()] == false ){
				Cell c2 = cellList[c.getLeft()];
				c2.setPosition(c.getPositionX()-sideLength*2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getLowerLeft()] == false ){
				Cell c2 = cellList[c.getLowerLeft()];
				c2.setPosition(c.getPositionX()-sideLength, Math.toIntExact(Math.round( c.getPositionY()+sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getUpperLeft()] == false ){
				Cell c2 = cellList[c.getUpperLeft()];
				c2.setPosition(c.getPositionX()-sideLength, Math.toIntExact(Math.round( c.getPositionY()-sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getRight()] == false ){
				Cell c2 = cellList[c.getRight()];
				c2.setPosition(c.getPositionX()+sideLength*2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getUpperRight()] == false ){
				Cell c2 = cellList[c.getUpperRight()];
				c2.setPosition(c.getPositionX()+sideLength, Math.toIntExact(Math.round( c.getPositionY()-sideLength*(1.5) )));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if( hash[c.getLowerRight()] == false ){
				Cell c2 = cellList[c.getLowerRight()];
				c2.setPosition(c.getPositionX()+sideLength, Math.toIntExact(Math.round( c.getPositionY()+sideLength*(1.5) )));
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
}
