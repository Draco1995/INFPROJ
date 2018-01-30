/**
 * Info project:rikudo
 * @author Xinzhi MU, Xiangchen
 * 
 */

package rikudo;


public class Rikudo {
	static String path = "NULL";
	static int sideLength = 30;
	static int resolution = 20;
	public static void usage(){
		System.out.println("Rikudo v1.0, created by Xinzhi Mu and Xiang Chen");
		System.out.println(
				"Usage:\n"
				+"java Rikudo -s -i <graph_path> -m <SATsolver|backtracking> [-l <output_length>] 			: Solve a rikudo problem from file system, two methods available\n"
				+"java Rikudo -cg -i <graph_path> [-l <output_length>] 						: Create a puzzle from a graph\n"
				+"java Rikudo -cp -i <picture_path> [-r <resolution>] [-l <output_length>]				: Create a puzzle form a picture with certain resulution\n"
				+"\n"
				+"Example:\n"
				+"java Rikudo -s -i resource/b.txt -m SATsolver\n"
				+"java Rikudo -cg -i resource/b.txt\n"
				+"java Rikudo -cp -i resource/X.png -r 20"
				);
		return;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length==0){
			usage();
			return;
		}else{
			String method="NULL";
			String f="NULL";
			try{
				for(int i = 0;i<args.length;i++){
					if(args[i].equals("-s")){
						f = "s";
					}else if(args[i].equals("-cg")){
						f = "cg";
					}else if(args[i].equals("-cp")){
						f = "cp";
					}else if(args[i].equals("-i")){
						i++;
						path = args[i];
					}else if(args[i].equals("-l")){
						sideLength = Integer.parseInt(args[i+1]);
						i++;
					}else if(args[i].equals("-m")){
						i++;
						if(args[i].equals("backtracking")){
							method = args[i];
						}else if(args[i].equals("SATsolver")){
							method = args[i];
						}else{
							throw new Exception();
						}
					}
					else if(args[i].equals("-r")){
						resolution = Integer.parseInt(args[i+1]);
						i++;
					}else{
						throw new Exception();
					}
				}
				if(f.equals("NULL")){
					throw new Exception();
				}else if(f.equals("s")&&method.equals("NULL")){
					throw new Exception();
				}
				if(path.equals("NULL")){
					throw new Exception();
				}
			}catch(Exception e){
				System.out.println("Invalid arguments");
				usage();
				return;
			}
			if(f.equals("s")){
				if(method.equals("SATsolver")){
					SAT();
				}else BAC();
			}else if(f.equals("cg")){
				CG();
			}else{
				CP();
			}
			return;
			
		}
	}
	//SAT
	static public void SAT(){
		System.out.println("Runing SATsolver...");
		  RikudoMap rm = new RikudoMap(sideLength);
		  rm.readFile(path);
		  if(rm.checkMapAvailability()==false){
			  System.out.println("Invalid map!");
			  return;
		  }
		  rm.applyConstraints();
		  rm.printMap(sideLength);
		  rm.SAT();
		  rm.printMap(sideLength);
		  return;
	}
	//backtracking
	static public void BAC(){
		System.out.println("Runing backtracking...");
		  RikudoMap rm = new RikudoMap(sideLength);
		  rm.readFile(path);
		  if(rm.checkMapAvailability()==false){
			  System.out.println("Invalid map!");
			  return;
		  }
		  rm.applyConstraints();
		  rm.printMap(sideLength);
		  rm.backtracking();
		  rm.printMap(sideLength);
	}
	 //CreatingPuzzle_graph
	static  public void CG(){
		System.out.println("Creating puzzle from the graph from: "+path);
		  RikudoMap rm = new RikudoMap(sideLength);
		  rm.readFile(path);
		  if(rm.checkMapAvailability()==false){
			  System.out.println("Invalid map!");
			  return;
		  }
		  rm.createPuzzle();
		  //rm.printMap(sideLength);
	}
	//CreatingPuzzle_picture
	static public void CP(){
		System.out.println("Creating puzzle from the picture from: "+path+" with resolution of "+resolution);
		  RikudoMap rm = new RikudoMap(sideLength);		
		  rm.readImage("resource/X.png", resolution);
		  if(rm.checkMapAvailability()==false){
			  System.out.println("Invalid map!");
			  return;
		  }
		  rm.printMap(sideLength);
		  rm.createPuzzle();
		  //rm.printMap(sideLength);
	}

}