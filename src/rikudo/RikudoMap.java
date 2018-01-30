/**
 * RikudoMap Created by Xinzhi MU and Xiang CHEN
 * @author Xinzhi MU and Xiangchen
 */
package rikudo;

import Image.BinaryImage;
import Image.ColoredPolygon;
import Image.ColoredSegment;
import Image.Image2d;
import Image.Show;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class RikudoMap {
	/** 
	 * a list contains all the cells
	 */
	private Cell[] cellList;
	/** 
	 * the number of cells in the cellList
	 */
	private int cellNumbers;
	/** 
	 * the start point of path, always 1
	 */
	private int start;
	/**
	 *  the inner number of the start point(the order in the cellList)
	 */
	private int innerStart;
	/**
	 *  the end point of path
	 */
	private int end;
	/**
	 *  the inner number of the start point(the order in the cellList)
	 */
	private int innerEnd;
	/**
	 *  a list to store all the check points.
	 */
	private int[] checkPoints;
	private boolean flag;
	private int sideLength;
	private int numberOfSolutionsFound;
	private int[][] solutionsFound;
	private boolean[] hash;
	/**
	 *  s list to store all the constraints
	 */
	LinkedList<Constraint> constraintList = new LinkedList<Constraint>();

	RikudoMap(int sideLength) {
		this.sideLength = sideLength;
	}

	/**
	 *  Functions to create a graph from picture
	 * @param x
	 * @param y
	 * @param resolution
	 * @return
	 */
	public int[] imagePosition(int x, int y, int resolution) {
		int[] position = new int[2];
		double yp = resolution * 0.866;
		position[1] = (3 * x / 2 + 1) * resolution;
		position[0] = Math.toIntExact(Math.round(2 * y * yp + ((y + 1) % 2) * yp));
		return position;
	}

	public boolean calculateCoverage(int x, int y, int resolution, BinaryImage img) {
		int[] position = imagePosition(x, y, resolution);
		int white = 0;
		int black = 0;
		int all = 0;
		for (int i = 0; i < resolution; i++) {
			for (int j = 0; j < resolution; j++) {
				if (position[0] + i - resolution / 2 < 0 || position[0] + i - resolution / 2 >= img.getHeight()) {
					continue;
				}
				if (position[1] + j - resolution / 2 < 0 || position[1] + j - resolution / 2 >= img.getWidth())
					continue;
				if (img.isBlack(position[0] + i - resolution / 2, position[1] + j - resolution / 2) == true) {
					black++;
				} else {
					white++;
				}
				all++;
			}
		}
		if (1.0 * black / all >= 0.2) {
			return true;
		}
		return false;
	}

	public int getcellNumber(int[][] cellNumber, int x, int y) {
		if (x < 0 || x >= cellNumber.length || y < 0 || y > cellNumber[0].length) {
			return 0;
		} else
			return cellNumber[x][y];
	}

	public void readImage(String path, int resolution) {
		BinaryImage img = new BinaryImage(path);
		int h = img.getHeight();
		int w = img.getWidth();
		int hn = h / resolution / 3 * 2 + 1;
		int wn = Math.toIntExact(Math.round(w / resolution / 0.866 * 2)) + 2;
		boolean[][] cell = new boolean[hn + 2][wn + 2];
		int[][] cellNumber = new int[hn + 2][wn + 2];
		int count = 0;
		for (int i = 1; i <= hn; i++) {
			for (int j = 1; j <= wn; j++) {
				cell[i][j] = calculateCoverage(i - 1, j - 1, resolution, img);
			}
		}
		boolean fflag = true;
		while (true) {
			fflag = false;
			for (int i = 1; i <= hn; i++) {
				for (int j = 1; j <= wn; j++) {
					int cc = 0;
					if (cell[i][j] == true) {
						if (i % 2 == 0) {
							if (cell[i][j - 1] == false)
								cc++;
							if (cell[i - 1][j] == false)
								cc++;
							if (cell[i - 1][j + 1] == false)
								cc++;
							if (cell[i][j + 1] == false)
								cc++;
							if (cell[i + 1][j + 1] == false)
								cc++;
							if (cell[i + 1][j] == false)
								cc++;
						} else {
							if (cell[i][j - 1] == false)
								cc++;
							if (cell[i - 1][j - 1] == false)
								cc++;
							if (cell[i - 1][j] == false)
								cc++;
							if (cell[i][j + 1] == false)
								cc++;
							if (cell[i + 1][j] == false)
								cc++;
							if (cell[i + 1][j - 1] == false)
								cc++;
						}
						if (cc >= 5) {
							cell[i][j] = false;
							fflag = true;
						}
					}
				}
			}
			if (fflag == false) {
				break;
			}
		}
		count = 0;
		for (int i = 1; i <= hn; i++) {
			for (int j = 1; j <= wn; j++) {
				if (cell[i][j] == true) {
					count++;
					cellNumber[i][j] = count;
				}
			}
		}
		start = -1;
		end = -1;
		cellNumbers = count;
		cellList = new Cell[cellNumbers + 1];
		checkPoints = new int[cellNumbers + 1];
		LabelConstraint.checkPoints = checkPoints;
		LabelConstraint.cellList = cellList;
		UNOConstraint.cellList = cellList;
		PIConstraint.cellList = cellList;
		DiamondConstraint.cellList = cellList;
		for (int i = 1; i <= hn; i++) {
			for (int j = 1; j <= wn; j++) {
				int[] cc = new int[6];
				if (cell[i][j] == true) {
					if (i % 2 == 0) {
						cc[0] = getcellNumber(cellNumber, i, j - 1);
						cc[1] = getcellNumber(cellNumber, i - 1, j);
						cc[2] = getcellNumber(cellNumber, i - 1, j + 1);
						cc[3] = getcellNumber(cellNumber, i, j + 1);
						cc[4] = getcellNumber(cellNumber, i + 1, j + 1);
						cc[5] = getcellNumber(cellNumber, i + 1, j);
					} else {
						cc[0] = getcellNumber(cellNumber, i, j - 1);
						cc[1] = getcellNumber(cellNumber, i - 1, j - 1);
						cc[2] = getcellNumber(cellNumber, i - 1, j);
						cc[3] = getcellNumber(cellNumber, i, j + 1);
						cc[4] = getcellNumber(cellNumber, i + 1, j);
						cc[5] = getcellNumber(cellNumber, i + 1, j - 1);
					}
					cellList[cellNumber[i][j]] = new Cell(cellNumber[i][j], cc[0], cc[1], cc[2], cc[3], cc[4], cc[5]);
				}
			}
		}
		System.out.println(cellList.length);
	}

	public void readFile(String path) {
		try {
			InputStream is = new FileInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String str = null;
			cellNumbers = Integer.parseInt(reader.readLine());
			cellList = new Cell[cellNumbers + 1];
			checkPoints = new int[cellNumbers + 1];
			str = reader.readLine();
			String[] s = str.split(" ");
			start = Integer.parseInt(s[0]);
			end = Integer.parseInt(s[1]);
			// Create Cells
			for (int i = 1; i <= cellNumbers; i++) {
				str = reader.readLine();
				s = str.split(" ");
				cellList[Integer.parseInt(s[0])] = new Cell(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
						Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]), Integer.parseInt(s[5]),
						Integer.parseInt(s[6]));
				/*
				 * checkPoints[Integer.parseInt(s[7])]=Integer.parseInt(s[0]);
				 * if(Integer.parseInt(s[7])==start){ innerStart =
				 * Integer.parseInt(s[0]); } if(Integer.parseInt(s[7])==end){
				 * innerEnd = Integer.parseInt(s[0]); }
				 */
			}
			// Label
			str = reader.readLine();
			int n = Integer.parseInt(str);
			LabelConstraint.checkPoints = checkPoints;
			LabelConstraint.cellList = cellList;
			for (int i = 1; i <= n; i++) {
				str = reader.readLine();
				s = str.split(" ");
				if (Integer.parseInt(s[1]) == start) {
					innerStart = Integer.parseInt(s[0]);
				}
				if (Integer.parseInt(s[1]) == end) {
					innerEnd = Integer.parseInt(s[0]);
				}
				LabelConstraint lc = new LabelConstraint(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
				constraintList.add(lc);
			}
			// diamond
			str = reader.readLine();
			n = Integer.parseInt(str);
			DiamondConstraint.cellList = cellList;
			for (int i = 1; i <= n; i++) {
				str = reader.readLine();
				s = str.split(" ");
				DiamondConstraint dc = new DiamondConstraint(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
				constraintList.add(dc);

				/*
				 * cellList[Integer.parseInt(s[0])].connect(Integer.parseInt(s[1
				 * ]));
				 * cellList[Integer.parseInt(s[1])].connect(Integer.parseInt(s[0
				 * ]));
				 */
			}
			// UNO
			str = reader.readLine();
			n = Integer.parseInt(str);
			UNOConstraint.cellList = cellList;
			for (int i = 1; i <= n; i++) {
				str = reader.readLine();
				UNOConstraint uc = new UNOConstraint(Integer.parseInt(str), 0);
				constraintList.add(uc);
				// cellList[Integer.parseInt(str)].setUNO();
			}

			// PI
			str = reader.readLine();
			n = Integer.parseInt(str);
			PIConstraint.cellList = cellList;
			for (int i = 1; i <= n; i++) {
				str = reader.readLine();
				s = str.split(" ");
				PI pi;
				if (s[1].equals("p")) {
					pi = PI.PAIR;
				} else {
					pi = PI.IMPAIR;
				}
				PIConstraint pc = new PIConstraint(Integer.parseInt(s[0]), pi);
				constraintList.add(pc);
				// cellList[Integer.parseInt(s[0])].setPI(pi);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyConstraints() {
		for (Constraint c : constraintList) {
			c.set();
		}
	}

	public void removeConstraints() {
		for (Constraint c : constraintList) {
			c.unset();
		}
	}

	/**
	 *  a function to check if the map is well constructed, if not it will print
	 *  the points with errors
	 * @return
	 */

	public boolean checkMapAvailability() {
		boolean flag = true;
		for (Cell i : cellList) {
			if (i == null)
				continue;
			int[] nb = i.getNearbyCells();
			for (int j = 0; j <= 5; j++) {
				if (nb[j] == 0)
					continue;
				if ((cellList[nb[j]].getNearbyCells())[(j + 3) % 6] != i.getNumber()) {
					System.out.println(nb[j] + " " + i.getNumber());
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 *  To visualize the situation of RikudoMap, including the
	 *  graph,constraints,labels, path, etc...
	 *  
	 * @param sideLength
	 */
	public void printMap(int sideLength) {
		/*
		 * for(int i=0;i<cellNumbers;i++){ System.out.println(cellList[i+1]); }
		 */
		// To ajust the map
		Image2d img = new Image2d(700);
		ajust(sideLength);
		for (int i = 0; i < cellNumbers; i++) {
			cellList[i + 1].paint(img, sideLength, false, cellList);
			if (cellList[i + 1].getLabel() == start || cellList[i + 1].getLabel() == end) {
				cellList[i + 1].paint(img, sideLength, true, cellList);
			}
		}
		Show.show(img);
	}

	/**
	 *  Ajust the position of the cells for a better presentation
	 * @param sideLength
	 */
	private void ajust(int sideLength) {
		boolean[] hash = new boolean[cellNumbers + 1];
		Cell[] queue = new Cell[cellNumbers + 1];
		int l = 0;
		int r = 1;
		for (boolean i : hash) {
			i = false;
		}
		hash[0] = true;
		queue[0] = cellList[1];
		queue[0].setPosition(0, 0);
		hash[queue[0].getNumber()] = true;
		while (l != r) {
			Cell c = queue[l];
			int sideLength2 = Math.toIntExact(Math.round(sideLength * 0.866));
			if (hash[c.getLeft()] == false) {
				Cell c2 = cellList[c.getLeft()];
				c2.setPosition(c.getPositionX() - sideLength2 * 2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if (hash[c.getLowerLeft()] == false) {
				Cell c2 = cellList[c.getLowerLeft()];
				c2.setPosition(c.getPositionX() - sideLength2,
						Math.toIntExact(Math.round(c.getPositionY() + sideLength * (1.5))));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if (hash[c.getUpperLeft()] == false) {
				Cell c2 = cellList[c.getUpperLeft()];
				c2.setPosition(c.getPositionX() - sideLength2,
						Math.toIntExact(Math.round(c.getPositionY() - sideLength * (1.5))));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if (hash[c.getRight()] == false) {
				Cell c2 = cellList[c.getRight()];
				c2.setPosition(c.getPositionX() + sideLength2 * 2, c.getPositionY());
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if (hash[c.getUpperRight()] == false) {
				Cell c2 = cellList[c.getUpperRight()];
				c2.setPosition(c.getPositionX() + sideLength2,
						Math.toIntExact(Math.round(c.getPositionY() - sideLength * (1.5))));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			if (hash[c.getLowerRight()] == false) {
				Cell c2 = cellList[c.getLowerRight()];
				c2.setPosition(c.getPositionX() + sideLength2,
						Math.toIntExact(Math.round(c.getPositionY() + sideLength * (1.5))));
				queue[r] = c2;
				hash[c2.getNumber()] = true;
				r++;
			}
			l++;
		}
		for (Cell i : cellList) {
			if (i != null)
				i.ajust(sideLength);
		}
	}

	public void backtracking() {
		int[] trace = new int[cellNumbers + 1];
		java.util.LinkedList<Integer> UNOList = new LinkedList<Integer>();
		trace[1] = innerStart;
		int step = 1;
		flag = false;
		numberOfSolutionsFound = 0;
		solutionsFound = new int[2][cellNumbers];
		DFS(step, trace, UNOList);
		if (numberOfSolutionsFound == 0) {
			System.out.println("No solution!");
		} else if (numberOfSolutionsFound == 1) {
			System.out.println("1 solution found:");
			for (int i : solutionsFound[0]) {
				if (i == 0)
					continue;
				System.out.print(i + " ");
			}
			System.out.println();
		} else {
			System.out.println("At least 2 solutions");
			System.out.print("First one:");
			for (int i : solutionsFound[0]) {
				if (i == 0)
					continue;
				System.out.print(i + " ");
			}
			System.out.println();
			System.out.print("Second one:");
			for (int i : solutionsFound[1]) {
				if (i == 0)
					continue;
				System.out.print(i + " ");
			}
			System.out.println();
		}
	}

	/**
	 *  Using a depth-first-search to implement backtracking algorithm, at most
	 *  two solutions will be found and store in solutionsFound.
	 *  
	 * @param step
	 * @param trace
	 * @param UNOList
	 */
	public void DFS(int step, int[] trace, java.util.LinkedList<Integer> UNOList) {
		if (flag == true) {
			return;
		}
		// if we are at the final point
		if (trace[step] == innerEnd && step == cellNumbers) {
			if (numberOfSolutionsFound == 0) {
				numberOfSolutionsFound++;
				solutionsFound[0] = trace.clone();
				
				 System.out.print("One solution found: "); for(int i:trace){
				 System.out.print(i+" "); } System.out.println();
				 
			} else {
				flag = true;
				numberOfSolutionsFound++;
				solutionsFound[1] = trace.clone();
				// this.printMap(sideLength/2);
				
				 System.out.print("Second solution found: "); for(int
				 i:trace){ System.out.print(i+" "); } System.out.println();
				 
			}
			return;
		}
		// if the status of the Map is still worthy for the search
		if (checkStatus(trace, step) == false) {
			/*
			 * for(int i:trace){ System.out.print(i+" "); }
			 * System.out.println();
			 */
			return;
		} else {
			Cell c = cellList[trace[step]];
			int[] availableCells;
			boolean diamondFlag = false;
			if (c.getDiamondNumbers() == 0) {
				availableCells = c.getNearbyCells();
			} else {
				availableCells = c.getDiamond();
				diamondFlag = true;
			}
			for (int i : availableCells) {
				if (i == 0)
					continue;
				Cell cnext = cellList[i];
				if (cnext.check(step, trace[step], UNOList, cellList) == true) {
					cnext.avance(step, cellList, UNOList);
					if(diamondFlag==true){
						try{
							c.minusDiamondNumbers();
							cnext.minusDiamondNumbers();
						}
						catch(Exception e){
							printMap(sideLength);
						}
					}
					step++;
					trace[step] = i;
					DFS(step, trace, UNOList);
					if (flag == true) {
						return;
					}
					if(diamondFlag==true){
						c.addDiamondNumbers();
						cnext.addDiamondNumbers();
					}
					trace[step] = 0;
					step--;
					cnext.retreat(step, cellList, UNOList);
				}
			}
		}
	}

	/**
	 * check the status of the Map
	 * 
	 * @param trace
	 * @param step
	 * @return
	 */
	public boolean checkStatus(int[] trace, int step) {
		// check if this step is given, if it is, check if we are on the right
		// place
		if (checkPoints[step] != 0) {
			if (trace[step] != checkPoints[step]) {
				return false;
			}
		}
		// check if nearby available cells have available edges, except the
		// final points
		int[] availableCells = cellList[trace[step]].getNearbyCells();
		for (int i : availableCells) {
			if (i == 0)
				continue;
			Cell c = cellList[i];
			if (i == innerEnd) {
				continue;
			}
			if (c.check() == true) {
				if (c.getAvailableEdges() == 0) {
					return false;
				}
			}
		}
		// Check if there are more than one isolate parts
		if (BFS(step, trace) == false) {
			return false;
		}
		return true;
	}

	public static int[] createRandomSeries(int[] n, Random random) {
		int len = n.length;
		for (int i = 0; i < len; i++) {
			int a = random.nextInt(len);
			int b = random.nextInt(len);
			int c = n[a];
			n[a] = n[b];
			n[b] = c;
		}
		return n;
	}

	/**
	 * To create a puzzle
	 */
	public void createPuzzle() {
		// Start to create random answer.
		Random random = new Random();
		int[] series = new int[cellNumbers + 1];
		int start = random.nextInt(cellNumbers) + 1;
		int step = 1;
		series[1] = start;
		flag = false;
		hash = new boolean[cellNumbers + 1];
		hash[start] = true;
		CDFS(series, step, start, random);
		System.out.print("One answer created: ");
		for (int i : series) {
			System.out.print(i + " ");
		}
		System.out.println();
		for (int i = 1; i <= cellNumbers; i++) {
			cellList[series[i]].setLabel(i);
		}
		printMap(40);
		for (int i = 1; i <= cellNumbers; i++) {
			cellList[series[i]].unsetLabel();
		}
		System.out.println("Start creating constraints");
		// To create constraints
		createConstraints(series);

	}

	/**
	 * another version of DFS for the creation of the puzzle.
	 * @param series
	 * @param step
	 * @param cell
	 * @param random
	 */
	public void CDFS(int[] series, int step, int cell, Random random) {
		if (flag == true) {
			return;
		} else if (step == cellNumbers) {
			flag = true;
			return;
		} else {
			int[] nearbyCells = cellList[cell].getNearbyCells();
			int[] nc = nearbyCells.clone();
			nc = createRandomSeries(nc, random);
			for (int c : nc) {
				if (c == 0)
					continue;
				if (hash[c] == false) {
					if (CBFS(step, c) == false)
						continue;
					hash[c] = true;
					step++;
					series[step] = c;
					CDFS(series, step, c, random);
					if (flag == true)
						return;
					series[step] = 0;
					step--;
					hash[c] = false;
				}
			}
		}
	}

	/**
	 * another version of BFS for the creation of the puzzle.
	 * @param step
	 * @param start
	 * @return
	 */
	private boolean CBFS(int step, int start) {
		boolean[] BFShash = hash.clone();
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(start);
		BFShash[start] = true;
		step++;
		while (queue.isEmpty() == false) {
			int c = queue.pop();
			int[] nearbyCells = cellList[c].getNearbyCells();
			for (int i : nearbyCells) {
				if (BFShash[i] == false && i != 0) {
					BFShash[i] = true;
					queue.add(i);
					step++;
				}
			}
		}
		if (step == cellNumbers)
			return true;
		else
			return false;
	}

	/**
	 * Check if there are more than one isolate parts
	 * @param step
	 * @param trace
	 * @return
	 */
	private boolean BFS(int step, int[] trace) {
		boolean[] BFShash = new boolean[cellNumbers + 1];
		int cstart = trace[step];
		for (int i = 1; i <= step; i++) {
			BFShash[trace[i]] = true;
		}
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(cstart);
		while (queue.isEmpty() == false) {
			int c = queue.pop();
			int[] nearbyCells = cellList[c].getNearbyCells();
			for (int i : nearbyCells) {
				if (BFShash[i] == false && i != 0) {
					BFShash[i] = true;
					queue.add(i);
					step++;
				}
			}
		}
		if (step == cellNumbers)
			return true;
		else
			return false;
	}

	/**
	 * a function to prune the constraints
	 * @return
	 */
	public int prune() {
		int ans = 0;
		LinkedList<Constraint> clist = new LinkedList<Constraint>();
		for (Constraint c : constraintList) {
			for (int i = 1; i <= cellNumbers; i++) {
				cellList[i].initial();
			}
			for (Constraint cc : constraintList) {
				cc.set();
			}
			for (Constraint cc : clist) {
				cc.unset();
			}
			c.unset();
			System.out.println( c instanceof DiamondConstraint);
			int[] trace = new int[cellNumbers + 1];
			java.util.LinkedList<Integer> UNOList = new LinkedList<Integer>();
			trace[1] = innerStart;
			int step = 1;
			flag = false;
			numberOfSolutionsFound = 0;
			solutionsFound = new int[2][cellNumbers + 1];
			cellList[innerStart].setLabel(1);
			cellList[innerEnd].setLabel(cellNumbers);
			DFS(step, trace, UNOList);
			if (numberOfSolutionsFound == 2) {
				c.set();
				ans++;
				continue;
			} else if (numberOfSolutionsFound == 0) {
				System.out.println("Error");
			}
			clist.add(c);
		}
		return ans;
	}

	public void createConstraints(int[] series) {
		this.innerStart = series[1];
		this.innerEnd = series[cellNumbers];
		this.start = 1;
		this.end = cellNumbers;
		solutionsFound = new int[2][cellNumbers + 1];
		constraintList = new LinkedList<Constraint>();
		for (int i = 0; i < cellNumbers / 5; i++) { // randomly create several
													// constraint to accelerate
													// the programme.
			Constraint constraint = addNewConstraint(series);
			constraintList.add(constraint);
		}
		// Continually create new constraint till we find only one solution.
		while (true) {
			int[] trace = new int[cellNumbers + 1];
			java.util.LinkedList<Integer> UNOList = new LinkedList<Integer>();
			trace[1] = innerStart;
			int step = 1;
			flag = false;
			numberOfSolutionsFound = 0;
			solutionsFound = new int[2][cellNumbers + 1];
			for (int i = 1; i <= cellNumbers; i++) {
				cellList[i].initial();
			}
			for (Constraint c : constraintList) {
				c.set();
			}
			cellList[innerStart].setLabel(1);
			cellList[innerEnd].setLabel(cellNumbers);
			DFS(step, trace, UNOList);
			if (numberOfSolutionsFound == 2) {
				Constraint constraint = addNewConstraint(series);
				constraintList.add(constraint);
				constraint.set();
				System.out.println("2 solutions found with current constraints, new one added");
			} else if (numberOfSolutionsFound == 1) {
				System.out.println(
						"1 solution found  with current constraints the size of constraints:" + constraintList.size());
				this.printMap(sideLength);
				System.out.println("Start pruning the constraints");
				int ans = prune();
				//this.printMap(sideLength);
				System.out.println("solution pruned, " + ans + " constraints left");
				
				LinkedList<Constraint> l = (LinkedList<Constraint>) constraintList.clone();
				for(Constraint c:l) if(c.status==false) constraintList.remove(c);
				for (int i = 1; i <= cellNumbers; i++) {
					cellList[i].initial();
				}
				this.applyConstraints();
				cellList[innerStart].setLabel(1);
				cellList[innerEnd].setLabel(cellNumbers);
				//this.backtracking();
				this.printMap(sideLength);
				break;
			} else if (constraintList.isEmpty() == true) {
				System.out.println("Can't create an available constraints' list for this series");
			} else {
				System.out.println("No Solution");
				this.printMap(sideLength);
				for (Constraint c : constraintList) {
					if (c.type().equals("PI")) {
						System.out.println(c.getCell());
					}
				}
				DFS(step, trace, UNOList);
				break;

			}
		}

	}

	/**
	 * random an available constraint;
	 * @param series
	 * @return
	 */
	public Constraint addNewConstraint(int[] series) {
		// First we want to know where to create a new constraint;
		/*
		 * int starter=0; int ender=0; for(int i=1;i<=cellNumbers;i++){
		 * if(series[i]!=solutionsFound[0][i]){ starter=i; break; } } for(int
		 * i=cellNumbers;i>=1;i--){ if(series[i]!=solutionsFound[0][i]){
		 * ender=i; break; } } if(starter==0){//In case the first solution found
		 * is series itself for(int i=1;i<=cellNumbers;i++){
		 * if(series[i]!=solutionsFound[1][i]){ starter=i; break; } } }
		 * if(ender==0){ for(int i=cellNumbers;i>=1;i--){
		 * if(series[i]!=solutionsFound[1][i]){ ender=i; break; } } }
		 */
		int[] difference = new int[cellNumbers + 1];
		int diffNumber = 0;
		for (int i = 1; i < cellNumbers; i++) {
			if (series[i] != solutionsFound[0][i]) {
				difference[diffNumber] = i;
				diffNumber++;
			}
		}
		if (diffNumber == 0) {
			for (int i = 1; i < cellNumbers; i++) {
				if (series[i] != solutionsFound[1][i]) {
					difference[diffNumber] = i;
					diffNumber++;
				}
			}
		}
		// Then we random a point to create a constraint
		Random rand = new Random();
		int cellNumber;
		boolean[] h = new boolean[cellNumbers + 1];
		for (Constraint c : constraintList) {
			h[c.getCell()] = true;
		}
		int a = rand.nextInt(diffNumber);
		try {
			while (h[series[difference[a % diffNumber]]] == true)
				a++;
		} catch (Exception e) {
			e.printStackTrace();
			this.printMap(30);
		}
		cellNumber = difference[a % diffNumber];
		for (Constraint c : constraintList) {
			if (series[cellNumber] == c.getCell()) {
				System.out.println("error!");
			}
		}
		// Finally we random a constraint
		a = rand.nextInt(2);
		Constraint c;
		if (a == 0) {
			c = new LabelConstraint(series[cellNumber], cellNumber);
		} else if (a == 1) {
			c = new DiamondConstraint(series[cellNumber], series[cellNumber + 1]);
		} else if (a == 2) {
			c = new PIConstraint(series[cellNumber], cellNumber % 2 == 0 ? PI.PAIR : PI.IMPAIR);
		} else {
			for (Constraint cc : constraintList) {
				if (cc.type().equals("UNO")) {
					UNOConstraint ccc = (UNOConstraint) cc;
					int yu = ccc.getYu();
					for (int i = cellNumber; i < cellNumbers; i++) {
						if (i % 10 == yu && cellList[series[i]].haveConstraint() == false) {
							c = new UNOConstraint(series[i], yu);
							return c;
						}
					}
					c = new LabelConstraint(series[cellNumber], cellNumber);
					return c;
				}
			}
			c = new UNOConstraint(series[cellNumber], cellNumber % 10);
			return c;
		}
		return c;

	}

	public void task4() {
		int[] trace = new int[cellNumbers + 1];
		java.util.LinkedList<Integer> UNOList = new LinkedList<Integer>();
		trace[1] = innerStart;
		int step = 1;
		flag = false;
		numberOfSolutionsFound = 0;
		solutionsFound = new int[2][cellNumbers];
		DFS(step, trace, UNOList);
		int[] series = solutionsFound[0];
		this.innerStart = series[1];
		this.innerEnd = series[cellNumbers];
		this.start = 1;
		this.end = cellNumbers;
		solutionsFound = new int[2][cellNumbers + 1];
		while (true) {
			trace = new int[cellNumbers + 1];
			UNOList = new LinkedList<Integer>();
			trace[1] = innerStart;
			step = 1;
			flag = false;
			numberOfSolutionsFound = 0;
			solutionsFound = new int[2][cellNumbers + 1];
			for (int i = 1; i <= cellNumbers; i++) {
				cellList[i].initial();
			}
			for (Constraint c : constraintList) {
				c.set();
			}
			cellList[innerStart].setLabel(1);
			cellList[innerEnd].setLabel(cellNumbers);
			// this.printMap(20);
			DFS(step, trace, UNOList);
			if (numberOfSolutionsFound == 2) {
				Constraint constraint = addNewConstraint(series);
				constraintList.add(constraint);
				constraint.set();
				System.out.println("2 solutions found");
			} else if (numberOfSolutionsFound == 1) {
				System.out.println("1 solution found:" + constraintList.size());
				this.printMap(20);
				int ans = prune();
				this.printMap(30);
				System.out.println("1 solution pruned:" + ans);
				break;
			} else if (constraintList.isEmpty() == true) {
				System.out.println("Can't create an available constraints' list for this series");
			} else {
				System.out.println("No Solution");
				this.printMap(20);
				for (Constraint c : constraintList) {
					if (c.type().equals("PI")) {
						System.out.println(c.getCell());
					}
				}
				DFS(step, trace, UNOList);
				break;

			}
		}
	}

	public void SAT() {
		int n = this.cellNumbers;
		ISolver solver = SolverFactory.newDefault();
		try {
			// each vertex appears once
			int[] Clause1 = new int[n];
			for (int i = 1; i <= n; i++) {
				for (int j = 0; j < n; j++) {
					Clause1[j] = i * n + j + 1;
				}
				solver.addClause(new VecInt(Clause1));
			}

			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					for (int k = j + 1; k <= n; k++) {
						solver.addClause(new VecInt(new int[] { -(i * n + j), -(i * n + k) }));
					}
				}
			}

			// each index is occupied once
			int[] Clause2 = new int[n];
			for (int i = 1; i <= n; i++) {
				for (int j = 0; j < n; j++) {
					Clause2[j] = (j + 1) * n + i;
				}
				solver.addClause(new VecInt(Clause2));
			}
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					for (int k = j + 1; k <= n; k++) {
						solver.addClause(new VecInt(new int[] { -(j * n + i), -(k * n + i) }));
					}
				}
			}

			// cells with labels
			for (int i = 1; i <= n; i++) {
				Cell c = cellList[i];
				if (c.getLabel() > 0) {
					solver.addClause(new VecInt(new int[] { (i * n + c.getLabel()) }));
				}
			}

			// consecutive vertices are adjacent
			for (int i = 1; i <= n; i++) {
				Cell c = cellList[i];
				int[] Near = c.getNearbyCells();
				Loop: for (int j = 1; j <= n; j++) {
					for (int k = 0; k < 6; k++) {
						if (j == Near[k]) {
							continue Loop;
						}
					}
					for (int l = 1; l <= n; l++) {
						solver.addClause(new VecInt(new int[] { -(i * n + l), -(j * n + l + 1) }));
					}
				}
			}

			// diamond
			for (int i = 1; i <= n; i++) {
				Cell c = cellList[i];
				int num = c.getDiamondNumbers();
				if (num != 0) {
					for (int k = 0; k < num; k++) {
						int a = c.getDiamond()[k];
						solver.addClause(new VecInt(new int[] { -(a * n + 1), i * n + 2 }));
						for (int j = 2; j < n; j++) {
							solver.addClause(new VecInt(new int[] { i * n + j - 1, -(a * n + j), i * n + j + 1 }));
						}
						solver.addClause(new VecInt(new int[] { -(a * n + n), i * n + n - 1 }));
					}
				}

			}

			// PI
			for (Constraint c : constraintList) {
				if (c.type().equals("PI")) {
					PIConstraint pi = (PIConstraint) c;
					PI valeur = pi.getValeur();
					int cell = pi.getCell();
					if (valeur == PI.PAIR) {
						int[] clause = new int[n / 2];
						for (int i = 0; i < n / 2; i++) {
							clause[i] = cell * n + 2 * (i + 1);
						}
						solver.addClause(new VecInt(clause));
					} else {
						int[] clause = new int[(n + 1) / 2];
						for (int i = 0; i < (n + 1) / 2; i++) {
							clause[i] = cell * n + 2 * i + 1;
						}
						solver.addClause(new VecInt(clause));
					}
				}
			}

			// UNO
			LinkedList<Integer> UNO = new LinkedList<Integer>();
			for (Constraint c : constraintList) {
				if (c.type().equals("UNO")) {
					int cell = c.getCell();
					UNO.add(cell);
				}
			}
			int Num;
			if (!UNO.isEmpty()) {
				int cell1 = UNO.remove();
				for (int i = 1; i <= n; i++) {
					int u = i % 10;
					if (u <= n % 10) {
						Num = n / 10 + 1;
					} else {
						Num = n / 10;
					}
					for (int j : UNO) {
						int[] clause = new int[Num];
						for (int k = 0; k < Num; k++) {
							if (k * 10 + u == i) {
								clause[k] = -(cell1 * n + i);
							} else {
								clause[k] = j * n + k * 10 + u;
							}
						}
						solver.addClause(new VecInt(clause));

					}

				}
			}
		} catch (ContradictionException e1) {
			e1.printStackTrace();
		}

		try {
			if (solver.isSatisfiable()) {
				// System.out.println("Satisfiable problem!");
				int[] solution = solver.model();
				/*
				 * System.out.print("Solution: " + solution[0]); for (int i = 1;
				 * i<n*n; i++){ System.out.print(" " + solution[i]); }
				 */
				int[] finalLabels = new int[n + 1];
				int counter = 0;
				for (int i : solution) {
					if (i > 0) {
						counter++;
						finalLabels[counter] = i - counter * n;
					}
				}
				for (int i = 1; i <= n; i++) {
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
