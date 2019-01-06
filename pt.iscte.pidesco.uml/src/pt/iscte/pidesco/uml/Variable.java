package pt.iscte.pidesco.uml;

public class Variable {
private String type;
private String visibility;
private String name;

//Represent each variable of each UmlClass

public Variable( String visibility,String type, String name) {
	
	this.type = type;
	this.visibility = visibility;
	this.name = name;
}
public String getType() {
	return type;
}
public String getVisibility() {
	return visibility;
}
public String getName() {
	return name;
}
@Override
public String toString() {
	return   visibility + " "+ type + " "+ name ;
}

}
