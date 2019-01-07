package pt.iscte.pidesco.uml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.uml.extensionpoint.IButton;

public class UmlView implements PidescoView {
	public static final String VIEW_ID = "pt.iscte.pidesco.uml.view";
	private List<UmlClass> umlClassList;
	private ArrayList<Dependency> dependencyList;
	private GraphViewer graphViewArea;
	private PackageElement currentPackage;
	private UmlListener listener;
	private static UmlView instance;
	private Group buttonRow;
	private Variable selectedVariable;
	private UmlClass selectedUmlClass;
	private Composite viewArea;
	private Composite innerComposite;
	private ScrolledComposite scrolledComposite;
	private Composite graphComposite;
	private Map<String, Image> imageMap;

	public static UmlView getInstance() {
		return instance;
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		instance = this;
		this.viewArea = viewArea;
		this.imageMap = imageMap;
		viewArea.setLayout(new GridLayout(2, false));
		selectedVariable = null;
		selectedUmlClass = null;
		createSideComposites(viewArea);
		createGraphViewArea(viewArea);

		listener = new UmlListener(this);
		Activator.getInstance().addUmlListener(listener);
		Activator.getInstance().getServicesProjS().addListener(listener);
		if (Activator.getInstance().getServicesCodeG() != null) {
			addButtonCodeGS(Activator.getInstance().getServicesCodeG());
		}

		addRefreshButton();
		viewArea.layout();

	}
//Ads a Button to refresh the view area
	private void addRefreshButton() {
		org.eclipse.swt.widgets.Button b = new org.eclipse.swt.widgets.Button(buttonRow, SWT.PUSH);
		b.setText("Refresh");
		buttonRow.pack();
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}

		});

	}
//Refreshes the view area 
	public void refresh() {
		for (Control control : viewArea.getChildren()) {
			control.dispose();
		}
		createContents(viewArea, imageMap);
		if (currentPackage != null) {
			listener.doubleClick(currentPackage);
		}

	}

	/*
	 * Ads a button to be use the service from the CodeGeneratorServices to create
	 * getters and setters from the last variable select in the UMLView
	 */
	private void addButtonCodeGS(CodeGeneratorServices servicesCodeG) {
		org.eclipse.swt.widgets.Button b = new org.eclipse.swt.widgets.Button(buttonRow, SWT.PUSH);
		b.setText("GenerateGetters&Setters");
		buttonRow.pack();
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!(currentPackage == null)) {
					if (selectedUmlClass != null || selectedVariable != null) {
						servicesCodeG.addSettersAndGetters(selectedUmlClass.getSourceElement().getFile().toString(),
								selectedVariable.getType() + " " + selectedVariable.getName());
					}

				}

			}

		});

	}

//Creates the area where are going to be displayed the UML
	private void createGraphViewArea(Composite viewArea) {
		graphComposite = new Composite(viewArea, SWT.BORDER);
		graphViewArea = new GraphViewer(graphComposite, SWT.BORDER);
		GridData gridDataFill = new GridData(SWT.FILL, SWT.FILL, true, true);
		graphComposite.setLayoutData(gridDataFill);
		graphComposite.setLayout(new GridLayout());
		graphViewArea.setContentProvider(new ZestNodeContentProvider());
		graphViewArea.setLabelProvider(new ZestFigureProvider());
		graphViewArea.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		graphViewArea.getControl().setLayoutData(gridDataFill);
		graphComposite.pack();
		graphViewArea.applyLayout();

	}

	/*
	 * Creates the side column and the Uml Actions composite where all the buttons
	 * are displayed and checks if are clients extending the IButton extension point
	 * and creates each of them a button
	 */
	private void createSideComposites(Composite viewArea) {
		scrolledComposite = new ScrolledComposite(viewArea, SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		innerComposite = new Composite(scrolledComposite, SWT.NONE);
		innerComposite.setLayout(new GridLayout());

		buttonRow = new Group(innerComposite, SWT.NONE);
		buttonRow.setText("Uml Actions");
		buttonRow.setLayout(new RowLayout(SWT.VERTICAL));
		scrolledComposite.setContent(innerComposite);
		scrolledComposite.setMinSize(innerComposite.computeSize(35, SWT.DEFAULT));
		scrolledComposite.setSize(50, SWT.DEFAULT);
		createSideButtons();
		buttonRow.pack();
		innerComposite.pack();
		scrolledComposite.pack();

	}

	private void createSideButtons() {
		IConfigurationElement[] elements = checkIButtonExtensionClients();
		// Checking and creating buttons for each client
		for (IConfigurationElement e : elements) {
			String name = e.getAttribute("name");

			org.eclipse.swt.widgets.Button b = new org.eclipse.swt.widgets.Button(buttonRow, SWT.PUSH);
			b.setText(name);
			try {
				IButton action = (IButton) e.createExecutableExtension("class");
				b.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (!(currentPackage == null)) {
							PackageElement parentPackage = action.action(currentPackage);
							System.out.println(parentPackage);
							listener.doubleClick(parentPackage);
							buttonRow.pack();

						}

					}

				});
			} catch (CoreException e1) {
				e1.printStackTrace();
			}

		}
	}

	private IConfigurationElement[] checkIButtonExtensionClients() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.uml.button");
		return elements;

	}

	void addContent(List<UmlClass> umlClassList, ArrayList<Dependency> dependencyList, PackageElement currentPackage) {
		this.umlClassList = umlClassList;
		this.dependencyList = dependencyList;
		this.currentPackage = currentPackage;
		graphViewArea.setInput(umlClassList);
		graphViewArea.applyLayout();
	}

	class ZestNodeContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {
		@Override
		public Object[] getConnectedTo(Object entity) {
			ArrayList<Object> umlDependencyList = new ArrayList<Object>();
			for (Dependency dependency : dependencyList) {
				if (dependency.getChildClass().equals(entity)) {
					for (UmlClass parent : umlClassList) {
						if (dependency.getParentNameClass().equals(parent.getClassName())) {
							umlDependencyList.add(parent);
						}
					}

				}
			}
			return umlDependencyList.toArray();
		}
	}

	class ZestFigureProvider extends LabelProvider
			implements IFigureProvider, IEntityConnectionStyleProvider, ISelfStyleProvider {
		private UmlClass umlClass;

		@Override
		public IFigure getFigure(Object element) {
			umlClass = (UmlClass) element;
			return new UmlFigure((UmlClass) element);
		}

		@Override
		public String getText(Object element) {
			return "";
		}

		@Override
		public IFigure getTooltip(Object entity) {
			return null;
		}

		@Override
		public void selfStyleConnection(Object element, GraphConnection connection) {
			PolylineConnection connectionFig = (PolylineConnection) connection.getConnectionFigure();
			PolygonDecoration decoration = new PolygonDecoration();
			decoration.setScale(20, 10);
			decoration.setLineWidth(2);
			decoration.setOpaque(true);
			decoration.setBackgroundColor(ColorConstants.white);
			connectionFig.setTargetDecoration(decoration);
		}

		@Override
		public void selfStyleNode(Object element, GraphNode node) {

		}

		@Override
		public int getConnectionStyle(Object src, Object dest) {
			UmlClass parent = (UmlClass) dest;
			if (parent.getClassType().equals("interface")) {
				return ZestStyles.CONNECTIONS_DASH;
			} else {
				return ZestStyles.CONNECTIONS_DIRECTED;
			}

		}

		@Override
		public Color getColor(Object src, Object dest) {
			// TODO Auto-generated method stub
			return ColorConstants.red;
		}

		@Override
		public Color getHighlightColor(Object src, Object dest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLineWidth(Object src, Object dest) {
			// TODO Auto-generated method stub
			return 1;
		}
	}

	public void setSelectedVariable(Variable variable, UmlClass umlClass) {
		selectedVariable = variable;
		selectedUmlClass = umlClass;

	}

}
