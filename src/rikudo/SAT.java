package rikudo;

import java.util.LinkedList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SAT {
	int n;
	int s;
	int t;
	int graph[][];
		
	SAT(int n, int s, int t){
		this.n = n;
		this.s = s;
		this.t = t;
		this.graph = new int[n][n];
		
		//Cycle
		/*
    	for (int i = 0; i<n ; i++){
    		if (i-1>=0){
    			graph[i][i-1] = 1;
    		}
    		else graph[i][i-1+n] = 1;
    		if (i+1<=n-1){
    			graph[i][i+1] = 1;
    		}
    		else graph[i][i+1-n] = 1;
    	}
    	*/
		
		//complete
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				graph[i][j] = 1;
			}
		}
    }
	
	//grid
	SAT(int n){
		this.n = n*n;
		this.s = 1;
		this.t = n*n;
		this.graph = new int[this.n][this.n];
		for (int i = 0; i< this.n; i++){
    		if (i-n>=0){
    			graph[i][i-n]=1;
    		}
    		if (i+n<=this.n-1){
    			graph[i][i+n]=1;
    		}
    		if (i%n!=0){
    			graph[i][i-1]=1;
    		}
    		if ((i+1)%n!=0){
    			graph[i][i+1]=1;
    		}
    	}
	}
	
	public void sat(){
		int n = this.n;
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
			solver.addClause(new VecInt(new int[]{1*n+s}));
			solver.addClause(new VecInt(new int[]{n*n+t}));
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
					
			//consecutive vertices are adjacent
		/*	for (int i = 1; i <= n; i++){
				for (int j = 1; j<=n; j++){
					if (graph[i-1][j-1]==0){
						for (int l = 1; l <n; l++){
							solver.addClause(new VecInt(new int[] {-(i*n+l),-(j*n+l+1)}));
						}
					}
				}
			}
			*/
		}catch (ContradictionException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (solver.isSatisfiable()) {
				System.out.println("Satisfiable problem!");
				int[] solution = solver.model();
				System.out.print("Solution: ");
				int count = n;
				for (int i = 0; i<n*n; i++){
					if (solution[i]>0){
						System.out.print(" " + (solution[i]-count));
						count+=n;
					}
				}
			} else {
				System.out.println("Unsatisfiable problem!");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
	}
	
	public static void main(String args[])
    {
        SAT hamiltonian = new SAT(50,18,7);
        long time1 = System.currentTimeMillis();      
        hamiltonian.sat();
        long time2 = System.currentTimeMillis();
        System.out.println();
        System.out.println(time2-time1);
       
    }
}
