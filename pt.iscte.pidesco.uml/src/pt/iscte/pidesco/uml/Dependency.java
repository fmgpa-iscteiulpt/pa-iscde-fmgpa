package pt.iscte.pidesco.uml;

public class Dependency {
	private UmlClass childClass;
	private String parentNameClass;
	private String type;
	//Class to represent dependency (extends, implements)
	public Dependency(UmlClass childClass, String parentNameClass, String type) {
		super();
		this.childClass = childClass;
		this.parentNameClass = parentNameClass;
		this.type=type;
	}
	public UmlClass getChildClass() {
		return childClass;
	}
	public String getParentNameClass() {
		return parentNameClass;
	}
	public String getType() {
		return type;
	}
	@Override
	public String toString() {
		return "Dependency [childClass=" + childClass.getClassName() + ", parentNameClass=" + parentNameClass + ", type=" + type + "]";
	}
	
	
}
