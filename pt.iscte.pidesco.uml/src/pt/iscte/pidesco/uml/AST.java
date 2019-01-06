package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public class AST extends ASTVisitor {
	private UmlClass currentClass;
	private ArrayList<UmlClass> umlClassList;
	private ArrayList<UmlClass> umlFileClassList;
	private ArrayList<Dependency> dependencyList;
	private SourceElement element;

	public AST(ArrayList<UmlClass> umlClassList, ArrayList<Dependency> dependencyList, SourceElement element) {
		this.umlClassList = umlClassList;
		this.dependencyList = dependencyList;
		this.element = element;
		umlFileClassList = new ArrayList<UmlClass>();
	}

	// visits class/interface declaration
	@Override
	public boolean visit(TypeDeclaration node) {
		UmlClass classinFile = new UmlClass(element);
		umlClassList.add(classinFile);

		String name = node.getName().toString();
		classinFile.setClassName(name);
		umlFileClassList.add(classinFile);
		Type superClass = node.getSuperclassType();
		if (node.isInterface()) {
			classinFile.setClassType("interface");
		} else {
			classinFile.setClassType("class");
		}
		
		if (!(superClass == null)) {
			Dependency dependencySuperClass = new Dependency(classinFile, superClass.toString(), "Extends");
			dependencyList.add(dependencySuperClass);
		}

		List<Type> interfaces = node.superInterfaceTypes();
		if (!(interfaces.isEmpty())) {
			for (Type type : interfaces) {
				Dependency dependencyImplements = new Dependency(classinFile, type.toString(), "Implements");
				dependencyList.add(dependencyImplements);
			}
		}
		currentClass = classinFile;
		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		UmlClass classinFile = new UmlClass(element);
		umlClassList.add(classinFile);
		classinFile.setClassType("enum");
		String name = node.getName().toString();
		classinFile.setClassName(name);
		umlFileClassList.add(classinFile);

		List<Type> interfaces = node.superInterfaceTypes();
		if (!(interfaces.isEmpty())) {
			for (Type type : interfaces) {
				Dependency dependencyImplements = new Dependency(classinFile, type.toString(), "Implements");
				dependencyList.add(dependencyImplements);
			}
		}
		currentClass = classinFile;
		return true;
	}

	// visits attributes
	@Override
	public boolean visit(FieldDeclaration node) {
		ArrayList<Variable> fields = new ArrayList<Variable>();
		// loop for several variables in the same declaration
		for (Object o : node.fragments()) {
			VariableDeclarationFragment var = (VariableDeclarationFragment) o;
			String name = var.getName().toString();
			System.out.println("name variable " + name);
			String visibility = getVisibilityModifier(node);
			String type = node.getType().toString();
			Variable v = new Variable(visibility, type, name);
			fields.add(v);

		}

		currentClass.addVariables(fields);
		return false; // false to avoid child VariableDeclarationFragment to be processed again
	}


	//Visits methods and stores them in the correct UmlClass
	@Override
	public boolean visit(MethodDeclaration node) {
		AbstractTypeDeclaration t = (AbstractTypeDeclaration) node.getParent();
		for (UmlClass umlClass : umlFileClassList) {
			if (t.getName().toString().equals(umlClass.getClassName())) {

				String name = node.getName().toString();
				String visibility = getVisibilityModifier(node);
				String returnType;
				if (!(node.getReturnType2() == null)) {
					returnType = node.getReturnType2().toString();
				} else {
					returnType = "";
				}
				String parameterstype = ",";
				for (Object parameter : node.parameters()) {
					parameterstype += ",";
					VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
					parameterstype += variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY)
							.toString();
				}
				umlClass.addMethods(new Method(visibility, name, returnType, parameterstype.replaceFirst(",*", "")));
			}
		}

		return true;
	}

	private String getVisibilityModifier(BodyDeclaration pInstanceVariableNode) {

		Iterator<Object> it = pInstanceVariableNode.modifiers().iterator();

		// Find the visibility in the modifiers
		while (it.hasNext()) {
			String modifier = it.next().toString();
			switch (modifier) {
			case "private":
				return "private";
			case "protected":
				return "protected";
			case "public":
				return "public";
			default:
				break;
			}
		}

		// No visibility found
		return null;

	}
	// endVisit
}
