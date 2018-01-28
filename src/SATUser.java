import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class SATUser {

	public static void main(String[] args) {
		// Create the solver 
		ISolver solver = SolverFactory.newDefault();
		// Feed the solver using arrays of int in Dimacs format
		try {
			for (int i = 1; i <= 7; i++){
				solver.addClause(new VecInt(new int[] {i*7+1, i*7+2, i*7+3, i*7+4, i*7+5, i*7+6, i*7+7}));
			}
			for (int i = 1; i <= 7; i++){
				for (int j = 1; j <= 7; j++){
					for (int k = j+1; k <= 7; k++){
						solver.addClause(new VecInt(new int[] {-(i*7+j),-(i*7+k)}));
					}
				}
			}
			for (int i = 1; i <= 7; i++){
				solver.addClause(new VecInt(new int[] {1*7+i, 2*7+i, 3*7+i, 4*7+i, 5*7+i, 6*7+i, 7*7+i}));
			}
			for (int i = 1; i <= 7; i++){
				for (int j = 1; j <= 7; j++){
					for (int k = j+1; k <= 7; k++){
						solver.addClause(new VecInt(new int[] {-(j*7+i),-(k*7+i)}));
					}
				}
			}
			for (int i = 1; i < 7; i++){
				solver.addClause(new VecInt(new int[] {-(1*7+i),-(5*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(1*7+i),-(6*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(1*7+i),-(7*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(2*7+i),-(3*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(2*7+i),-(6*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(2*7+i),-(7*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(3*7+i),-(2*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(3*7+i),-(5*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(3*7+i),-(7*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(5*7+i),-(1*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(5*7+i),-(3*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(5*7+i),-(6*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(6*7+i),-(1*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(6*7+i),-(2*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(6*7+i),-(5*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(7*7+i),-(1*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(7*7+i),-(2*7+i+1)}));
				solver.addClause(new VecInt(new int[] {-(7*7+i),-(3*7+i+1)}));
			}
			solver.addClause(new VecInt(new int[] {1*7+1}));
			solver.addClause(new VecInt(new int[] {3*7+7}));
			solver.addClause(new VecInt(new int[] {5*7+3}));
			
			solver.addClause(new VecInt(new int[] {-(4*7+1),7*7+2}));
			for (int i = 2; i < 7; i++){
				solver.addClause(new VecInt(new int[] {7*7+i-1,-(4*7+i),7*7+i+1}));
			}
			solver.addClause(new VecInt(new int[] {-(4*7+7),7*7+6}));
		} catch (ContradictionException e1) {
			e1.printStackTrace();
		}
		// Print parameters of the problem
		System.out.println("Number of variables: " + solver.nVars());
		System.out.println("Number of constraints: " + solver.nConstraints());
		// Solve the problem
		try {
			if (solver.isSatisfiable()) {
				System.out.println("Satisfiable problem!");
				int[] solution = solver.model();
				System.out.print("Solution: " + solution[0]);
				for (int i = 1; i<49; i++){
					System.out.print(" " + solution[i]);
				}
			} else {
				System.out.println("Unsatisfiable problem!");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
	}
}
