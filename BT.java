package rikudo;
import java.util.*;

public class BT {
	int V; 
    int path[];
    int s;
    int t;
    int numberOfResult;
    boolean flag;
    LinkedList<int[]> result;
    BT(int n){
    	this.V=n*n;
    	this.s=0;
    	this.t=n*n-1;
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
            	
            }
            
        }
        for (int v = 1; v < V; v++)
        {
            if (isSafe(v, graph, path, pos))
            {
                path[pos] = v;
                hamBT(graph,path,pos+1);
                
                path[pos] = -1;
                
            }
        }
        
    }
 
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
        flag = true;
        path[0] = 0;
        hamBT(graph, path, 1);
        
        System.out.println(numberOfResult);
        
 
        
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
        BT hamiltonian = new BT(7);

        hamiltonian.ham(7);

       
    }
}
