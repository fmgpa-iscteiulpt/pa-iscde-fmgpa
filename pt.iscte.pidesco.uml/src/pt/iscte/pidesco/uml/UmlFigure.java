package pt.iscte.pidesco.uml;


import java.util.List;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import pt.iscte.pidesco.uml.service.UmlInterfaceListener;

public class UmlFigure extends Figure {

	public static Color classColor = new Color(null, 255, 255, 206);
	private CompartmentFigureAtribute attributeFigure;
	private CompartmentFigure methodFigure = new CompartmentFigure();
	private int size;
	
	
//Class used for the display of each UmlClass in the UmlView
	public UmlFigure(UmlClass umlClass) {

		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setOpaque(true);
		size = 100;
		MouseListenerUml mouseListener = new MouseListenerUml(umlClass);
		Label name = new Label(umlClass.getClassType() + " " +umlClass.getClassName());
		name.addMouseListener(mouseListener);
		attributeFigure = new CompartmentFigureAtribute(this,size,umlClass);
		attributeFigure.addComponents(umlClass.getVariables());
		attributeFigure.addMouseListener(mouseListener);
		methodFigure.addComponents(umlClass.getMethods());
		methodFigure.addMouseListener(mouseListener);
		setSize(200, size);
		add(name);
		add(attributeFigure);
		add(methodFigure);

	}
	
//Mouse Listener used to allow double-click in every UmlFigure returning each SourceElement t posterior use
	public class MouseListenerUml implements MouseListener {
		private UmlClass umlClass;

		public MouseListenerUml(UmlClass umlClass) {
			this.umlClass = umlClass;
		}

		@Override
		public void mousePressed(MouseEvent me) {

		}

		@Override
		public void mouseReleased(MouseEvent me) {

		}

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			for (UmlInterfaceListener l : Activator.getInstance().getListeners()) {
				l.doubleClick(umlClass.getSourceElement());
			}
			System.out.println(umlClass.getSourceElement());

		}
	}
//Class used to create a compartment where the methods of each UmlClass are displayed 
	public class CompartmentFigure extends Figure {

		public CompartmentFigure() {
			ToolbarLayout layout = new ToolbarLayout();
			layout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
			layout.setStretchMinorAxis(true);
			layout.setSpacing(2);
			setLayoutManager(layout);

			setBorder(new CompartmentFigureBorder());
		}

		public void addComponents(List<?> list) {

			Label label = new Label();

			String labelString = "";

			for (Object object : list) {

				labelString += (object.toString() + "\n");
				size += 20;
			}
			label.setText(labelString);
			setSize(200, size);
			size+=30;
			add(label);

		}

		public class CompartmentFigureBorder extends AbstractBorder {
			public Insets getInsets(IFigure figure) {
				return new Insets(1, 0, 0, 0);
			}

			public void paint(IFigure figure, Graphics graphics, Insets insets) {
				graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
			}
		}
	}
}
