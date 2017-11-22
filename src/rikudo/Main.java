/*
 * Info project:rikudo
 * Author: Xinzhi MU, Xiang Chen
 * 
 */

package rikudo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("resource/a.txt");
		if(file.exists()){
			try {
				FileReader fileReader = new FileReader(file);
				char[]c = new char[100];
				fileReader.read(c,0,99);
				System.out.println(c);
				fileReader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("file: "+file.getPath()+" not found");
		}
	}

}
