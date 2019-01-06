package pt.iscte.pidesco.uml;

import java.util.ArrayList;

import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public class UmlClass {
	
	private String className;
	private String classType;
	private ArrayList<Variable> variables;
	private ArrayList<Method> methods;
	private SourceElement element;
	
	// Represent every different UmlClass and its methods, variables and source element
	public UmlClass(SourceElement element) {
		
		variables = new ArrayList<Variable>();
		methods = new ArrayList<Method>();
		this.element=element;
		className = "default";
		classType= "default";
	}
	public String getClassName() {
		return className;
	}
	
	public String getClassType() {
		return classType;
	}
	public ArrayList<Variable> getVariables() {
		return variables;
	}
	public ArrayList<Method> getMethods() {
		return methods;
	}	
	
	public SourceElement getSourceElement() {
		return element;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public void addVariables(ArrayList<Variable> variables) {
		this.variables.addAll(variables);
	}
	public void addMethods(Method method) {
		methods.add(method);
	}
	
	
	
}