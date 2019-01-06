package pt.iscte.pidesco.codegenerator.internal;

import java.util.Set;

import org.eclipse.ui.ISelectionListener;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;


public class CodeGeneratorActivator implements BundleActivator {
	
	private static CodeGeneratorActivator instance;

	private static BundleContext context;
	private CodeGeneratorServices services;
	private JavaEditorServices javaEditorServices;
	private ProjectBrowserServices projectBrowserServices;
	private ServiceRegistration<CodeGeneratorServices> service;
	private ISelectionListener selectionListener;
	private PidescoServices pidescoServices;
	
	public static CodeGeneratorActivator getInstance() {
		return instance;
	}
	
	public CodeGeneratorServices getServices() {
		return services;
	}
	
	public PidescoServices getPidescoServices() {
		return pidescoServices;
	}
	
	public JavaEditorServices getJavaEditorServices() {
		return javaEditorServices;
	}
	
	public ProjectBrowserServices getProjectBrowserServices() {
		return projectBrowserServices;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		instance = this;
		this.context = context;
		service = context.registerService(CodeGeneratorServices.class, new CodeGeneratorServicesImpl(), null);
		ServiceReference<PidescoServices> ref = context.getServiceReference(PidescoServices.class);
		pidescoServices = context.getService(ref);
		ServiceReference<JavaEditorServices> editorReference = context.getServiceReference(JavaEditorServices.class);
		javaEditorServices = context.getService(editorReference);
		
		ServiceReference<ProjectBrowserServices> browserReference = context.getServiceReference(ProjectBrowserServices.class);
		projectBrowserServices = context.getService(browserReference);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		service.unregister();
	}

	public static BundleContext getContext() {
		// TODO Auto-generated method stub
		return context;
	}


}
