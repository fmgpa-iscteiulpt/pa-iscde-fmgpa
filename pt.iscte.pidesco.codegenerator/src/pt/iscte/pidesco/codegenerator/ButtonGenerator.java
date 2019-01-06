package pt.iscte.pidesco.codegenerator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.framework.Bundle;

import pt.iscte.pidesco.codegenerator.extensibility.RangeScope;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ButtonGenerator {
	
	private Button justButton;
	private Composite viewArea;
	
	//Constructor used to instantiate a button with the name of the text and placed on the viewArea.
	private ButtonGenerator(Composite viewArea, String text) {
		justButton = new Button(viewArea, SWT.VERTICAL);
		justButton.setText(text);
		this.viewArea = viewArea;
	}
	
	//Function used to give the button "Generate code" its listener with the functionalities
	//of generating code depending on the text selected.
	public static void addGenerateCode(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				CodeVisitor visitor = new CodeVisitor();
				editorServ.parseFile(f, visitor);
				boolean alreadyExists = false;
				boolean overwriteExists = false;
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					IExtensionRegistry reg = Platform.getExtensionRegistry();
					IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.codegenerator.snippets");
					if(elements.length > 0) {
						JSONObject object = getUserCode(sel.getText());
						if(object != null) {
							RangeScope scopeRange = RangeScope.valueOf(object.get("scope").toString());
							if(checkIfCursorIsInRange(f, editorServ, scopeRange)) {
								String code = (String) object.get("code");
								ClearSelected(editorServ);
								overwriteExists = true;
								editorServ.insertTextAtCursor(code);
								editorServ.saveFile(f);
							}
						}
					}
					if(!overwriteExists) {
						if((sel.getText() + ".java").equals(f.getName())) {
							for(MethodDeclaration method : visitor.getMethods()) {
								if(method.getName().equals(sel.getText()) && method.parameters().isEmpty()) {
									alreadyExists = true;
								}
							}
							if(!alreadyExists) {
								if(checkIfCursorIsInRange(f, editorServ, RangeScope.OUTSIDEMETHOD)) {
									String name = sel.getText();
									ClearSelected(editorServ);
									String text = "\n\tpublic " + name + "(){" + "\n\n\t}";
									editorServ.insertTextAtCursor(text);
									editorServ.saveFile(f);
								}
							}
						}else if((sel.getText()).equals("main")) {
							if(!visitor.getMethodsName().contains("main")) {
								if(checkIfCursorIsInRange(f, editorServ, RangeScope.OUTSIDEMETHOD)) {
									ClearSelected(editorServ);
									String text = "\n\tpublic static void main(String[] args){\n\n\t}";
									editorServ.insertTextAtCursor(text);
									editorServ.saveFile(f);
								}
							}
						}else if((sel.getText()).equals("sysout")) {
							if(checkIfCursorIsInRange(f, editorServ, RangeScope.INSIDEMETHOD)) {
								ClearSelected(editorServ);
								String text = "\n\tSystem.out.println();";
								editorServ.insertTextAtCursor(text);
								editorServ.saveFile(f);
							}
						}
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate code" its listener with the 
	//ability to create setters and getters from the open file.
	public static void addGettersAndSetters(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					if(!visitor.getFields().isEmpty()) {
						if(checkIfCursorIsInRange(f, editorServ, RangeScope.OUTSIDEMETHOD)) {
							String statement = "";
							ArrayList<String> selectedFields = WindowGenerator.createConstructorOrGS(visitor.getFields(), false);
							boolean firstStatement = true;
							for(String field: selectedFields) {
								String[] splitted = field.split(" ");
								String fieldType = splitted[0];
								String fieldName = splitted[1];
								if(firstStatement) { 
									statement += "\n"; 
									firstStatement = false; 
								}
								statement += GenerateSetter(fieldName, fieldType, visitor.getMethodsName());
								statement += GenerateGetter(fieldName, fieldType, visitor.getMethodsName());
							}
							
							if(!statement.equals("\n")) {
								editorServ.insertTextAtCursor(statement);
								editorServ.saveFile(f);
							}	
						}
					}
					buttonGenerator.viewArea.layout();
				}
			}
			
			private String GenerateSetter(String fieldName, String fieldType, ArrayList<String> allMethodsNames) {
				String statement = "";
				String methodName = "set" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
				if(!allMethodsNames.contains(methodName)) {
					statement = "\tpublic void " + methodName + "(" + fieldType + " " + fieldName + "){ \n\t\tthis." + fieldName + "=" + fieldName + ";\n\t}\n\n";
				}
				return statement;
			}
	
			private String GenerateGetter(String fieldName, String fieldType, ArrayList<String> allMethodsNames) {
				String statement = "";
				String methodName = "get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
				if(!allMethodsNames.contains(methodName)) {
					statement += "\tpublic " + fieldType + " " + methodName + "(){ \n\t\treturn this." + fieldName + "; \n\t}\n\n";
				}
				return statement;
			}
	
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Comment/Uncomment block of code" its 
	//listener with the ability to comment and uncomment on the text selected.
	public static void addUncommentComment(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					String textSelected = sel.getText();
					String text = sel.getText().replaceAll("\t", "").replaceAll("\n", "");
					ClearSelected(editorServ);
					if(!text.startsWith("/*") && !text.endsWith("*/")) {
						editorServ.insertTextAtCursor("/*" + textSelected + "*/");
					}else {
						textSelected = textSelected.replace("/*", "").replace("*/", "");
						editorServ.insertTextAtCursor(textSelected);
					}
					editorServ.saveFile(f);
					buttonGenerator.viewArea.layout();
				}
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate toString()" its 
	//listener with the ability to create a toString function with the name of the class
	//and its variables.
	public static void addToString(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					if(!visitor.getMethodsName().equals("toString")) {
						String fileName = f.getName().replaceAll(".java", "");
						String toString = "@Override\n\tpublic String toString(){\n\t\treturn \""+ fileName + " [";
						if(checkIfCursorIsInRange(f, editorServ, RangeScope.OUTSIDEMETHOD)) {
							if(!visitor.getFields().isEmpty()) {
								for(int i = 0; i< visitor.getFields().size(); i++) {
									String[] splitted = visitor.getFields().get(i).toString().replace(";", "").split(" ");
									String fieldName = splitted[splitted.length - 1].replace("\n", "");
									toString += fieldName + "=\" + " + fieldName + " + \"";
									if(i!=visitor.getFields().size()-1) {
										toString += ", ";
									}
								}
							}
							toString += "]\";\n\t}";
							editorServ.insertTextAtCursor(toString);
							editorServ.saveFile(f);
						}
					}
					buttonGenerator.viewArea.layout();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate Constructor using Fields..." its 
	//listener with the to open a window and the user will selected which
	//fields wants to takes part on creating the constructor method.
	public static void addConstructorFields(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					if(!visitor.getFields().isEmpty()) {
						ArrayList<String> selectedFields = WindowGenerator.createConstructorOrGS(visitor.getFields(), true);
						if(checkIfCursorIsInRange(f, editorServ, RangeScope.OUTSIDEMETHOD)) {
							String className = f.getName().replace(".java", "");
							String statement = "public " + className + "(";
							String setValues = "){\n";
							String lastFieldName = selectedFields.get(selectedFields.size()-1).split(" ")[1];
							for(String field: selectedFields) {
								String[] splitted = field.split(" ");
								String fieldName = splitted[1];
								String fieldType = splitted[0];
								statement += fieldType + " " + fieldName;
								setValues += "\t\tthis." + fieldName + "=" + fieldName + ";\n";
								if(!lastFieldName.equals(fieldName)){
									statement += ", "; 
								}	
								
							}
							
							String auxiliar = statement.replace("protected ", "").replace("private ", "").replace("public ", "");
							boolean constructorExists = false;
							for(MethodDeclaration method: visitor.getMethods()) {
								if(auxiliar.equals(method.getName() + "(" +  createParameters(method))) {
									constructorExists = true;
								}
							}
							
							if(!constructorExists) {
								statement = statement + setValues + "\t}";
								editorServ.insertTextAtCursor(statement);
								editorServ.saveFile(f);
							}
						}
					}
					buttonGenerator.viewArea.layout();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}			
		});
		
		
	}
	
	//Function used to give the button "Surround with..." its 
	//listener with the ability to surround the selected statement with a try/catch block.
	public static void addSurroundWith(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				editorServ.saveFile(f);
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					if(sel.getText().isEmpty()) {
						System.out.println("Select a statment...");
					}else{
						CodeVisitor visitor = new CodeVisitor();
						editorServ.parseFile(f, visitor);
						if(checkIfCursorIsInRange(f, editorServ, RangeScope.INSIDEMETHOD)) {
							String text = "try {\n\t\t\t" + sel.getText() + "\n\t\t}catch(Exception e){ \n \t\t\t// TODO Auto-generated catch block\n\t\t\te.printStackTrace();\n\t\t}";
							ClearSelected(editorServ);
							editorServ.insertTextAtCursor(text);
							editorServ.saveFile(f);
						}
					}
				}
				buttonGenerator.viewArea.layout();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		
	}
	
	private static boolean checkIfCursorIsInRange(File file, JavaEditorServices editorServ, RangeScope scope) {
		editorServ.saveFile(file);
		int cursorPosition = editorServ.getCursorPosition();
		CodeVisitor visitor = new CodeVisitor();
		editorServ.parseFile(file, visitor);
		switch(scope.toString()) {
			case "ALL":
				return true;
			case "INSIDECLASS":
				StatementPosition positionInner = StatementPosition.searchStatementInFile(file, file.getName().replace(".java", ""));
				if(cursorPosition > positionInner.getStartPosition() && cursorPosition < positionInner.getEndPosition()) {
					return true;
				}
				return false;
			case "OUTOFCLASS":
				StatementPosition positionOutter = StatementPosition.searchStatementInFile(file, file.getName().replace(".java", ""));
				if(cursorPosition < positionOutter.getStartPosition() || cursorPosition > positionOutter.getEndPosition()) {
					return true;
				}
				return false;
			case "INSIDEMETHOD":
				if(visitor.getMethods().size() > 0) {
					for(MethodDeclaration method : visitor.getMethods()) {
						if(method.getStartPosition() <= cursorPosition && method.getStartPosition() + method.getLength() >= cursorPosition) {
							return true;
						}
					}
				}
				return false;
			case "OUTSIDEMETHOD":
				if(visitor.getMethods().size() > 0) {
					for(MethodDeclaration method : visitor.getMethods()) {
						if(method.getStartPosition() <= cursorPosition && method.getStartPosition() + method.getLength() >= cursorPosition) {
							return false;
						}
					}
				}
				return true;
			default:
				return false;
		}
	}
	
	private static String createParameters(MethodDeclaration method) {
		String parameters = "";
		if(method.parameters().size() > 0) {
			for(int i = 0; i < method.parameters().size(); i++) {
				SingleVariableDeclaration variableDeclaration = (SingleVariableDeclaration) method.parameters().get(i);
				if(i!=0) parameters += ", ";
				parameters += variableDeclaration.getType() + " " + variableDeclaration.getName();
			}
		}
		return parameters;
	}
	
	private static JSONObject getUserCode(String macro) {
		try {
			JSONParser jsonParser = new JSONParser();	
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(getUserCodeFile()));
			JSONArray allMacros = (JSONArray) jsonObject.get("all_macros");
			for(int i = 0; i < allMacros.size(); i++) {
				JSONObject object = (JSONObject) allMacros.get(i);
				if(object.get("macro").toString().equals(macro)) {
					return object;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	//Function used to clear the text previously selected so it could be replaced.
	private static void ClearSelected(JavaEditorServices javaServ) {
		File f = javaServ.getOpenedFile();
		javaServ.insertText(f, "", javaServ.getTextSelected(f).getOffset(), javaServ.getTextSelected(f).getLength());
	}
	
	private static File getUserCodeFile() {
		Bundle bundle = Platform.getBundle("pt.iscte.pidesco.codegenerator");
		URL fileURL = bundle.getEntry("src/pt/iscte/pidesco/codegenerator/user_code.json");
		File file = null;
		try {
			file = new File(FileLocator.resolve(fileURL).toURI());
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}	
		return file;
	}

	
}
