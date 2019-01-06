package pt.iscte.pidesco.uml;

public class Method {
	private String returnType;
	private String name;
	private String visibility;
	private String parameterstype;
	
	
	//Represent each method of each UmlClass
	public Method(String visibility, String name, String returnType, String parameterstype ) {
		super();
		this.returnType = returnType;
		this.visibility = visibility;
		this.parameterstype=parameterstype;
		this.name = name;
	}


	public String getReturnType() {
		return returnType;
	}


	public String getVisibility() {
		return visibility;
	}


	public String getName() {
		return name;
	}
	

	public String getParameterstype() {
		return parameterstype;
	}


	@Override
	public String toString() {
		return visibility + " " + returnType + " " + name+"("+parameterstype+")";
	}


	
}
