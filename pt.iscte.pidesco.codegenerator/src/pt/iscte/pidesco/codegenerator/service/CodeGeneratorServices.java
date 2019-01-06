package pt.iscte.pidesco.codegenerator.service;

import java.util.List;

import pt.iscte.pidesco.codegenerator.AccessibilityType;

public interface CodeGeneratorServices {
	
	/**
	 * Creates setters and getters for the fields received.
	 * The code will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the getters and setters are going to be placed.
	 * @param fields, contains the variables to generate getters and setters. The string should be represented by the type of data and the name of the variable, with a space in between, e.g. "String name".
	 * 
	 */
	void addSettersAndGetters(String path, List<String> fields);
	
	/**
	 * Creates a setter and getter for the field received.
	 * The code will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the getter and setter are going to be placed.
	 * @param field, is the field which a getter and setter will be generated. The string should be represented by the type of data and the name of the variable, with a space in between, e.g. "String name".
	 */
	void addSettersAndGetters(String path, String field);
	
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
	void addFunction(String path, AccessibilityType accessibility, String functionName, String returnType, List<String> parameters, boolean isStatic);
	
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
	void addFunction(String path, AccessibilityType accessibility, String functionName, String returnType, String parameter, boolean isStatic);
	
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
	void addFunction(String path, AccessibilityType accessibility, String functionName, String returnType, boolean isStatic);
	
	/**
	 * Creates a method on the determined path with certain options, with more than one parameter.
	 * The method will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path represents where the method is going to be placed.
	 * @param accessibility is a ENUM representing the accessibility type of the method.
	 * @param methodName is the name of the method.
	 * @param parameters is a list of the parameters to be passed on the method.
	 * @param isStatic will give the information if the method is static or not.
	 */
	void addMethod(String path, AccessibilityType accessibility, String methodName, List<String> parameters, boolean isStatic);
	
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
	void addMethod(String path, AccessibilityType accessibility, String methodName, String parameter, boolean isStatic);
	
	/**
	 * Creates a method on the determined path with certain options, with no parameters.
	 * The method will be added after the last function/method found in the path class,
	 * if the are no other functions/methods in the class, it will be added at the end of the class.
	 * @param path, represents where the method is going to be placed.
	 * @param accessibility, is a ENUM representing the accessibility type of the method.
	 * @param methodName, is the name of the method.
	 * @param isStatic, will give the information if the method is static or not.
	 */
	void addMethod(String path, AccessibilityType accessibility, String methodName, boolean isStatic); 
	
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
	void addField(String path, AccessibilityType accessibility, String fieldName, String fieldType, boolean isStatic);
	
	/**
	 * Creates a new java class with a certain name on the determined path.
	 * @param path, represents the directory where the class will be created.
	 * @param accessibility, is a ENUM representing the accessibility type of the field.
	 * @param className, is the name of the class.
	 */
	void addClass(String path, AccessibilityType accessibility, String className);
}
