package environment;

import java.util.ArrayList;

import ast.expression.Variable;

public class Environment {

	public ArrayList<Variable> table = new ArrayList<Variable>();
	
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
