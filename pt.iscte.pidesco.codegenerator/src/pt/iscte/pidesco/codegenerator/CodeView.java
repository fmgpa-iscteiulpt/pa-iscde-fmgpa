package pt.iscte.pidesco.codegenerator;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorActivator;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class CodeView implements PidescoView{
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.VERTICAL));
		BundleContext context = CodeGeneratorActivator.getContext();
		
		ServiceReference<JavaEditorServices> editorReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices editorServ = context.getService(editorReference);
		
		TabFolder tabFolder = new TabFolder(viewArea, SWT.NONE);
		
		Composite codeGeneratorComposite = buildComposite(tabFolder, "Source", "This is tab show the basic functionalities granted with this plug-in.");
		Composite userCodeComposite = buildComposite(tabFolder, "Snippets", "This is tab shows the user snippets, implemented via extension-point.");
		
	    ButtonGenerator.addGenerateCode(codeGeneratorComposite, "Generate code", editorServ);
	    
		ButtonGenerator.addGettersAndSetters(codeGeneratorComposite, "Generate Getters and Setters...", editorServ);
		
		ButtonGenerator.addUncommentComment(codeGeneratorComposite, "Comment/Uncomment block of code", editorServ);
		
		ButtonGenerator.addToString(codeGeneratorComposite, "Generate toString()", editorServ);
		
		ButtonGenerator.addConstructorFields(codeGeneratorComposite, "Generate constructor with fields...", editorServ);

		ButtonGenerator.addSurroundWith(codeGeneratorComposite, "Suround with try/catch", editorServ);	
		
		ListGenerator.registerUserCode(userCodeComposite);
		
	}
	
	private Composite buildComposite(TabFolder tabFolder, String tabTitle, String tabToolTip) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
	    
	    TabItem tab = new TabItem(tabFolder, SWT.NONE);
	    tab.setText(tabTitle);
	    tab.setToolTipText(tabToolTip);
	    tab.setControl(composite);
   
	    return composite;
	}

	
}
