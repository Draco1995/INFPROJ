package rikudo;
import java.util.*;
//Backtracking for the grid graph(or others we can change the array graph)
public class BT {
	int V; 
    int path[];
    int s;
    int t;
    int numberOfResult;
    boolean flag = false;
    LinkedList<int[]> result;
    
    BT(int n, int s, int t){
    	this.V=n;
    	this.s=s;
    	this.t=t;
    	this.numberOfResult=0;
    	this.result = new LinkedList<int[]>();
    	
    }
    
    BT(int n){
    	this.V=n*n;
    	this.s=0;
    	this.t=V-1;
    	this.numberOfResult=0;
    	this.result = new LinkedList<int[]>();
    	
    }
    
    boolean isSafe(int v, int graph[][], int path[], int pos)
    {
        if (graph[path[pos - 1]][v] == 0)
            return false;
 
        for (int i = 0; i < pos; i++)
            if (path[i] == v)
                return false;
 
        return true;
    }
 
    void hamBT(int graph[][], int path[], int pos)
    {
        if (pos == V)
        {
            if (path[pos - 1] == t){
            	printSolution(path);
            	numberOfResult++; 
            	flag = true;
            	return;
            }            
        }
        if (!flag){
        for (int v = 0; v < V; v++)
        {
            if (isSafe(v, graph, path, pos))
            {
                path[pos] = v;
                hamBT(graph,path,pos+1);                
                path[pos] = -1;                
            }
        } 
        }
    }
    //Remark 2: for a given k 
    void hamBT(int graph[][], int path[], int pos, int k)
    {
        if (pos == V)
        {
            if (path[pos - 1] == t){
            	
            	numberOfResult++;
            	if (numberOfResult>=k){
            		printSolution(path);
            		return;
            	}
            }            
        }
        
	    for (int v = 1; v < V; v++){
	        if (isSafe(v, graph, path, pos))
	        {
	            path[pos] = v;
	            hamBT(graph,path,pos+1,k);                
	            path[pos] = -1;                
	        }
	    } 
        
    }
    //grid
    int ham(int n)
    {
    	int n2 = n*n;
    	int[][] graph = new int[n2][n2];
    	for (int i = 0; i< n2; i++){
    		if (i-n>=0){
    			graph[i][i-n]=1;
    		}
    		if (i+n<=n2-1){
    			graph[i][i+n]=1;
    		}
    		if (i%n!=0){
    			graph[i][i-1]=1;
    		}
    		if ((i+1)%n!=0){
    			graph[i][i+1]=1;
    		}
    	}
    	
        path = new int[V];
        for (int i = 0; i < V; i++)
            path[i] = -1;
        path[0] = 0;
        hamBT(graph, path, 1);
        
        System.out.println(numberOfResult);
        
 
        
        return 1;
    }
    
    
    int hamCycle()
    {
    	int n = this.V;
    	int[][] graph = new int[n][n];
    	//Cycle
    	/*for (int i = 0; i<n ; i++){
    		if (i-1>=0){
    			graph[i][i-1] = 1;
    		}
    		else graph[i][i-1+n] = 1;
    		if (i+1<=n-1){
    			graph[i][i+1] = 1;
    		}
    		else graph[i][i+1-n] = 1;
    	}*/
    	//Complete
    	for (int i = 0; i< n; i++){
    		for (int j = 0; j< n; j++){
    			graph[i][j]=1;
    		}
    		
    	}
    	
        path = new int[V];
        for (int i = 0; i < V; i++)
            path[i] = -1;
        path[0] = s;
        hamBT(graph, path, 1);
        
        System.out.println(numberOfResult);
        
 
        
        return 1;
    }
    //Remark 2 for a given k
    int ham(int n, int k)
    {
    	int n2 = n*n;
    	int[][] graph = new int[n2][n2];
    	for (int i = 0; i< n2; i++){
    		if (i-n>=0){
    			graph[i][i-n]=1;
    		}
    		if (i+n<=n2-1){
    			graph[i][i+n]=1;
    		}
    		if (i%n!=0){
    			graph[i][i-1]=1;
    		}
    		if ((i+1)%n!=0){
    			graph[i][i+1]=1;
    		}
    	}
        path = new int[V];
        for (int i = 0; i < V; i++)
            path[i] = -1;
        path[0] = 0;
        hamBT(graph, path, 1, k);
        if (numberOfResult<k){
        	System.out.println("Number of result is less than k");
        }      
        return 1;
    }
 
    void printSolution(int path[])
    {
        for (int i = 0; i < V; i++)
            System.out.print(" " + path[i] + " ");
        System.out.println();
        
    }

    public static void main(String args[])
    {   
    	
        BT hamiltonian = new BT(13,2,1);
        long time1 = System.currentTimeMillis();
        hamiltonian.hamCycle();
        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1);
        
       
    }
}
