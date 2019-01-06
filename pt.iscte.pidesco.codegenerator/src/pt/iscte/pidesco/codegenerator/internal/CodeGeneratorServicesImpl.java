package pt.iscte.pidesco.codegenerator.internal;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.Position;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import pt.iscte.pidesco.codegenerator.AccessibilityType;
import pt.iscte.pidesco.codegenerator.CodeVisitor;
import pt.iscte.pidesco.codegenerator.StatementPosition;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class CodeGeneratorServicesImpl implements CodeGeneratorServices{
	
	/**
	 * Creates setters and getters for the fields received.
	 * The code will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the getters and setters are going to be placed.
	 * @param fields, contains the variables to generate getters and setters. The string should be represented by the type of data and the name of the variable, with a space in between, e.g. "String name".
	 * 
	 */
	@Override
	public void addSettersAndGetters(String path, List<String> fields) {
		JavaEditorServices editorServ = getEditorService();
		File file = new File(path);
		int offset = getInsertLocation(file, editorServ, true);
		boolean firstSetter = true;
		String gettersAndSetters = "\n\n\t";
		for(String field: fields) {
			String[] splitted = field.replace(";", "").split(" ");
			String fieldName = splitted[splitted.length-1].replaceAll("\n", "");
			String fieldType = splitted[splitted.length-2];
			gettersAndSetters += GenerateSetter(firstSetter,  fieldName, fieldType);
			if(firstSetter = true) firstSetter = false; 
			gettersAndSetters += GenerateGetter(fieldName, fieldType);
		}
		editorServ.insertText(file, gettersAndSetters, offset, 0);
		editorServ.saveFile(file);
	}	
	
	/**
	 * Creates a setter and getter for the field received.
	 * The code will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the getter and setter are going to be placed.
	 * @param field, is the field which a getter and setter will be generated. The string should be represented by the type of data and the name of the variable, with a space in between, e.g. "String name".
	 */
	@Override
	public void addSettersAndGetters(String path, String field) {
		List<String> fields = new ArrayList<String>();
		fields.add(field);
		addSettersAndGetters(path, fields);
	} 
	
	/**
	 * Creates a function on the determined path with certain options, with more than one parameter.
	 * The function will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the function is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the function.
	 * @param functionName, is the name of the function.
	 * @param returnType, is the type of data that will be returned in the function.
	 * @param parameters, is a list of the parameters to be passed on the function.
	 * @param isStatic, will give the information if the function is static or not.
	 */
	@Override
	public void addFunction(String path, AccessibilityType acessability, String functionName, String returnType, List<String> parameters, boolean isStatic) {
		JavaEditorServices editorServ = getEditorService();
		File file = new File(path);
		int offset = getInsertLocation(file, editorServ, true);
		String function = startStatement(acessability, functionName, returnType, isStatic);
		function += String.join(", ", parameters);
		function += endStatement(returnType!="");
		editorServ.insertText(file, function, offset, 0);
		editorServ.saveFile(file);
	}
	
	/**
	 * Creates a function on the determined path with certain options, with only one parameter.
	 * The function will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the function is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the function.
	 * @param functionName, is the name of the function.
	 * @param returnType, is the type of data that will be returned in the function.
	 * @param parameter, is a parameter to be passed on the function.
	 * @param isStatic, will give the information if the function is static or not.
	 */
	@Override
	public void addFunction(String path, AccessibilityType acessability, String functionName, String returnType, String parameter, boolean isStatic) {
		List<String> parameters = new ArrayList<String>();
		parameters.add(parameter);
		addFunction(path, acessability, functionName, returnType, parameters, isStatic);
	}
	
	/**
	 * Creates a function on the determined path with certain options, with no parameters.
	 * The function will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the function is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the function.
	 * @param functionName, is the name of the function.
	 * @param returnType, is the type of data that will be returned in the function.
	 * @param isStatic, will give the information if the function is static or not.
	 */
	@Override
	public void addFunction(String path, AccessibilityType acessability, String functionName, String returnType, boolean isStatic) {
		List<String> parameters = new ArrayList<String>();
		addFunction(path, acessability, functionName, returnType, parameters, isStatic);
	}
	
	/**
	 * Creates a method on the determined path with certain options, with more than one parameter.
	 * The method will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the method is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the method.
	 * @param methodName, is the name of the method.
	 * @param parameters, is a list of the parameters to be passed on the method.
	 * @param isStatic, will give the information if the method is static or not.
	 */
	@Override
	public void addMethod(String path, AccessibilityType acessability, String methodName, List<String> parameters, boolean isStatic) {
		JavaEditorServices editorServ = getEditorService();
		File file = new File(path);
		int offset = getInsertLocation(file, editorServ, true);
		String function = startStatement(acessability, methodName, "", isStatic);
		function += String.join(", ", parameters);
		function += endStatement(false);
		editorServ.insertText(file, function, offset, 0);
		editorServ.saveFile(file);
	}
	
	/**
	 * Creates a method on the determined path with certain options, with only one parameter.
	 * The method will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the method is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the method.
	 * @param methodName, is the name of the method.
	 * @param parameter, is a parameter to be passed on the method.
	 * @param isStatic, will give the information if the method is static or not.
	 */
	@Override
	public void addMethod(String path, AccessibilityType acessability, String methodName, String parameter, boolean isStatic) {
		List<String> parameters = new ArrayList<String>();
		parameters.add(parameter);
		addMethod(path, acessability, methodName, parameters, isStatic);	
	}
	
	/**
	 * Creates a method on the determined path with certain options, with no parameters.
	 * The method will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the method is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the method.
	 * @param methodName, is the name of the method.
	 * @param isStatic, will give the information if the method is static or not.
	 */
	@Override
	public void addMethod(String path, AccessibilityType acessability, String methodName, boolean isStatic) {
		List<String> parameters = new ArrayList<String>();
		addMethod(path, acessability, methodName, parameters, isStatic);
	}
	
	/**
	 * Creates a field on the determined path.
	 * The field will be added after the last field found in the path class,
	 * if the are no other fields in the class, it will be added at the end of the class.
	 * @param path, represents where the field is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the field.
	 * @param fieldName, is the name of the field.
	 * @param fieldType, is the type of the field.
	 * @param isStatic, will give the information if the field is static or not.
	 */
	@Override
	public void addField(String path, AccessibilityType acessability, String fieldName, String fieldType, boolean isStatic) {
		JavaEditorServices editorServ = getEditorService();
		File file = new File(path);
		int offset = getInsertLocation(file, editorServ, false);
		String field = "\n\t" + acessability.toString().toLowerCase() + " ";
		if(isStatic) {
			field += "static ";
		 }
		field += fieldType + " " + fieldName + ";";
		editorServ.insertText(file, field, offset, 0);
		editorServ.saveFile(file);
	}
	
	/**
	 * Creates a new java class with a certain name on the determined path.
	 * @param path, represents the directory where the class will be created.
	 * @param accessibility, is a ENUM representing the accessibility type of the field.
	 * @param className, is the name of the class.
	 */
	@Override
	public void addClass(String path, AccessibilityType acessability, String className) {
		JavaEditorServices editorServ = getEditorService();
		File file = new File(path + "\\" + className + ".java");
		String classBody = "\n" + acessability.toString().toLowerCase() + " class " + className + "{\n\n}";
		editorServ.insertText(file, classBody, 0, 0);
		editorServ.saveFile(file);
	}
	
	/** Function to return the instance of the JavaEditorServices.
	 */
	private JavaEditorServices getEditorService() {
		return CodeGeneratorActivator.getInstance().getJavaEditorServices();
	}
	
	/** Function to return the position of the file where the last method/function or attribute in the file.
	 * @param file, where the position will be searched.
	 * @param  editorServ, is the JavaEditorServices to be used to search in the file.
	 * @param isMethod, variable to validate if the position is to be searched is a method/function or attribute.  
	 * @return the offset of the end of the file before the last "}" will be searched.
	 */
	private int getInsertLocation(File file, JavaEditorServices editorServ, boolean isMethod) {
		CodeVisitor codeVisitor = new CodeVisitor();
		editorServ.parseFile(file, codeVisitor);
		int endPosition = 0;
		if(isMethod) { 
			if(!codeVisitor.getMethods().isEmpty()) {
				MethodDeclaration method = codeVisitor.getMethods().get(codeVisitor.getMethods().size()-1);
				endPosition = method.getStartPosition() + method.getLength();
			}
			if(endPosition == 0) {
				if(!codeVisitor.getFields().isEmpty()) {
					FieldDeclaration field = codeVisitor.getFields().get(codeVisitor.getFields().size()-1);
					endPosition = field.getStartPosition() + field.getLength();
				}
			}
		}else{
			if(!codeVisitor.getFields().isEmpty()) {
				FieldDeclaration field = codeVisitor.getFields().get(codeVisitor.getFields().size()-1);
				endPosition = field.getStartPosition() + field.getLength();
			}
			if(endPosition == 0) {
				if(!codeVisitor.getMethods().isEmpty()) {
					MethodDeclaration method = codeVisitor.getMethods().get(codeVisitor.getMethods().size()-1);
					endPosition = method.getStartPosition() + method.getLength();
				}
			}
		}
		
		// Returns the offset of the end of the file, before the last "}" in the file.
		if(endPosition==0) {
			StatementPosition position = StatementPosition.searchStatementInFile(file, file.getName().replace(".java", ""));
			endPosition =  position.getEndPosition();
		}
		
		return endPosition;
	}
	
	/** Function to generate a setter for the respective field.
	 * @param firstSetter, if it is the first setter to be added.
	 * @param fieldName, is the field name.
	 * @param fieldType, is the type of the field.  
	 * @return the code of the respective setter to the field given.
	 */
	private String GenerateSetter(boolean firstSetter, String fieldName,  String fieldType) {
		String extraTab = "";
		if(!firstSetter) extraTab = "\t";
		String setter = extraTab + "public void set" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()) + "(" + fieldType + " " + fieldName + "){ \n\t\tthis." + fieldName + "=" + fieldName + ";\n\t}\n\n";
		return setter;
	}
	
	/** Function to generate a getter for the respective field.
	 * @param fieldName, is the field name.
	 * @param fieldType, is the type of the field.  
	 * @return the code of the respective getter to the field given.
	 */
	private String GenerateGetter(String fieldName, String fieldType) {
		String getter = "\tpublic " + fieldType + " get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()) + "(){ \n\t\treturn this." + fieldName + "; \n\t}\n\n";
		return getter;
	}
	
	/** Function to generate the start of the code to a function/method.
	 * @param acessability, is a ENUM with the type of accessibility of the function/method.
	 * @param functionName, is the name of the function/method.
	 * @param returnType, is the type of the return of the function, in case of the method it should be passed an empty string.
	 * @param isStatic, check if the code for the function/method is static or not.    
	 * @return the code of the start of the function/method.
	 */
	private String startStatement(AccessibilityType accessibility, String functionName, String returnType, boolean isStatic) {
		String start = "\n\n\t" + accessibility.toString().toLowerCase() + " "; 
		if(isStatic) start += "static ";
		if(returnType.equals("")) {
			start += "void";
		}else {
			start += returnType;
		}
		start += " " + functionName + "(";
		return start;
	}
	
	/** Function to generate the end of the code to a function/method.
	 * @param hasReturn, check if the code to be generated has a return.   
	 * @return the code to the end of the function/method.
	 */
	private String endStatement(boolean hasReturn) {
		String end = "){\n\t\t// TODO Auto-generated function";
		if(hasReturn) {
			end += "\n\t\treturn null;";
		}else {
			end += "\n";
		}
		end += "\n\t}";
		return end;
	}

}
