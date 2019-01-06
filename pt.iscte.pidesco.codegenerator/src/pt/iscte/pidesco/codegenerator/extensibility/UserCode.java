package pt.iscte.pidesco.codegenerator.extensibility;

import java.util.List;

/**
 ** Interface needed to be used to implement the snippet extension-point.
 **/
public interface UserCode {
	
	/**
	 ** Function used to add user functionalities to the "Generate code" button;
	 * This function needs to return a list of UserCodeGenerator.
	 **/
	List<UserCodeGenerator> addUserCode();
}

