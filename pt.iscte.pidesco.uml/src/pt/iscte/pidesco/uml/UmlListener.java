package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;

public class UmlListener implements ProjectBrowserListener {
	private UmlView umlview;
	ArrayList<UmlClass> umlClassList = new ArrayList<UmlClass>();
	ArrayList<Dependency> dependencyList = new ArrayList<Dependency>();

	public UmlListener(UmlView umlView) {
		this.umlview = umlView;
	}
/*Method responsible for handling the SourceElement coming from the ProjectBrowserView 
	because of a double-click on one of his elements*/
	@Override
	public void doubleClick(SourceElement element) {
		umlClassList.clear();
		dependencyList.clear();
		System.out.println(element.getParent() + "." + element.getName().replaceAll(".java", ""));
		System.out.println(element.getFile());
		BundleContext context = Activator.getContext();
		ServiceReference<JavaEditorServices> javaE = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices servicesJavaE = context.getService(javaE);
//Check to see if the element is a Package if not get is Parent until it is
		if (element.isPackage()) {
			System.out.println(element.getName());
			iterateFrom((PackageElement) element, servicesJavaE);
		} else {
			while (!element.isPackage()) {
				System.out.println(element.getName());
				element = element.getParent();
			}
			iterateFrom((PackageElement) element, servicesJavaE);
		}

	}

	private void iterateFrom(PackageElement packageElement, JavaEditorServices servicesJavaE) {
		/*Iterates for each child of the Package Element 
		given and if one of these children is a package use recursiveness until it is
		When its not a package use the service .parseFile  provided by the JavaEditorServices and a AST created by this project
		to create a list of UmlClass objects and the relations between them  */
		for (SourceElement iterable_element : packageElement.getChildren()) {
			if (iterable_element.isPackage()) {
				iterateFrom((PackageElement) iterable_element, servicesJavaE);
			} else {
				System.out.println("ite " + iterable_element.getName());
				AST astvisitor = new AST(umlClassList, dependencyList, iterable_element);
				servicesJavaE.parseFile(iterable_element.getFile(), astvisitor);
			}

		}
		
		//Handle the dependences encountered and create UmlClasses for those Classes in other packages 
		boolean exist = false;
		for (Dependency dependency : dependencyList) {
			exist = false;
			System.out.println(dependency.toString());
			for (UmlClass umlClass : umlClassList) {
				if (umlClass.getClassName().equals(dependency.getParentNameClass())) {
					exist = true;
				}

			}
			if (!exist) {
				UmlClass umlClassParent = new UmlClass(null);
				umlClassParent.setClassName(dependency.getParentNameClass());
				umlClassList.add(umlClassParent);
			}
		}

		umlview.addContent(umlClassList, dependencyList,packageElement);

	}

	@Override
	public void selectionChanged(Collection<SourceElement> selection) {
		// TODO Auto-generated method stub

	}

	

}
