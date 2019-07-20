package environment;

import java.util.ArrayList;

import ast.expression.Variable;
import evaluator.Evaluator;

public class Environment {

	private Evaluator evaluator;
	public ArrayList<Variable> table;
	
	// constructor
	public Environment(Evaluator evaluator) {
		this.evaluator = evaluator;
		this.table = new ArrayList<Variable>();
	}

	public void store(Variable v) {
		table.add(v.getIndex(), v);
	}
	
	public Variable lookup(Variable v) {
		return table.get(v.getIndex());
	}
	 
	public void getTable() {
		for (Variable v : table) {
			System.out.println(v.getSymbol() + " " + v.getValue());
		}
		
	}
	
}
