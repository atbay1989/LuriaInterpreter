package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class ASTTool {

	public static void main(String[] args) throws IOException {                                                        
	      String outputDirectory = "C:\\Users\\james\\Desktop\\EclipseSpace\\Fluorescent\\Luria\\src\\syntacticanalysis";                                    
	    
	      defineAST(outputDirectory, "Expression", Arrays.asList(
	    		  "Binary	: Expression left, Token operator, Expression right",
	    		  "Grouping	: Expression expression",
	    		  "Literal	: Object value",
	    		  "Unary	: Token operator, Expression right"));      
		}
	
	private static void defineAST(String outputDirectory, String name, List<String> type) throws IOException {
		String path = outputDirectory + "/" + name + ".java";
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		
		writer.println("package syntacticanalysis;");
		writer.println();
		writer.println("import java.util.List;");
		writer.println();
		writer.println("public abstract class " + name + " {");
		
		defineVisitor(writer, name, type);
		
		// AST class
		
		for (String t : type) {
			String className = t.split(":")[0].trim();
			String fields = t.split(":")[1].trim();
			defineType(writer, name, className, fields);
		}
		
		// accept()
		writer.println();
		writer.println("	abstract <T> T accept(Visitor<T> visitor);");
		writer.println("}");
		writer.close();
	}
	
	private static void defineVisitor(PrintWriter writer, String name, List<String> type) {
		writer.println("  interface Visitor<T> {");
		for (String t : type) {
			String typeName = t.split(":")[0].trim();
			writer.println("    T visit" + typeName + name + "(" + typeName + " " + name.toLowerCase() + ");");
		}
		writer.println("  }");
	}
	
	public static void defineType(PrintWriter writer, String name, String className, String fieldList) {	
		// decorator, class
		writer.println(" static class " + className + " extends " + name + " {");
		
		// constructor
		writer.println("	" + className + "(" + fieldList + ") {");
		
		// field parameters
		String[] fields = fieldList.split(", ");
		for (String field : fields) {
			String n = field.split(" ")[1];
			writer.println("	this." + n + " = " + n + ";");
		}
		writer.println("	}");
		
		// visitor
		writer.println();
		writer.println("	<T> T accept(Visitor<T> visitor) {");
		writer.println("	return visitor.visit" + className + name + "(this);");
		writer.println("	}");
		
		// fields
		writer.println();
		for (String field : fields) {
			writer.println("	final " + field + ";");
		}
		writer.println("	}");
	}
	
}
