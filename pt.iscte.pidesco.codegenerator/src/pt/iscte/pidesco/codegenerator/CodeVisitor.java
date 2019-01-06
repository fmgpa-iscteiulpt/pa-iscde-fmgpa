package pt.iscte.pidesco.codegenerator;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;


public class CodeVisitor extends ASTVisitor{
	
	private ArrayList<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private ArrayList<String> methodsNames = new ArrayList<String>();
	private ArrayList<Assignment> assignments = new ArrayList<Assignment>();
	
	@Override
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		return false; // false to avoid child VariableDeclarationFragment to be processed again
	}
	
	// visits assignments (=, +=, etc)
	@Override
	public boolean visit(Assignment node) {
		assignments.add(node);
		return true;
	}
	
	// visits methods 
	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		methodsNames.add(node.getName().toString());
		return true;
	}
	
	public ArrayList<FieldDeclaration> getFields() {
		return fields;
	}

	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}
	
	public ArrayList<String> getMethodsName() {
		return methodsNames;
	}
	
}
