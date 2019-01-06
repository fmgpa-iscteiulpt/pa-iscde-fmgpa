package pt.iscte.pidesco.uml;

import java.awt.Color;
import java.util.List;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Display;

import pt.iscte.pidesco.uml.UmlFigure.CompartmentFigure.CompartmentFigureBorder;
import pt.iscte.pidesco.uml.service.UmlInterfaceListener;
//Class used to create a compartment where the variables of the UmlClass are displayed 
public class CompartmentFigureAtribute  extends Figure {
	private int size;
	private UmlClass umlClass;
	private UmlFigure umlFigure;
	public CompartmentFigureAtribute(UmlFigure umlFigure, int size, UmlClass umlClass) {
		this.umlFigure=umlFigure;
		this.umlClass=umlClass;
		ToolbarLayout layout = new ToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_BOTTOMRIGHT);
		layout.setStretchMinorAxis(true);
		layout.setSpacing(2);
		setLayoutManager(layout);

		setBorder(new CompartmentFigureBorder());
		this.size=size;
	}
	
	public void addComponents(List list) {
		
		for (Object object : list) {
			Label variablelabel = new Label();
			
			variablelabel.setText(object.toString());
			variablelabel.setSize(200, 20);
		
			variablelabel.addMouseListener(new MouseListenerVariable((Variable) object,variablelabel));
			add(variablelabel);
		}
	
	}

	public class CompartmentFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1, 0, 0, 0);
		}

		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
	//Mouse Listener for each variable in the class allowing this to be selected and store in the UmlView to posterior use
	public class MouseListenerVariable implements MouseListener {
		private Variable variable;
		private Label variablelabel;
		private boolean pressed;
		private org.eclipse.swt.graphics.Color blue;
		private org.eclipse.swt.graphics.Color black;
		public MouseListenerVariable(Variable variable, Label variablelabel) {
			Device device = Display.getCurrent ();
			blue = new org.eclipse.swt.graphics.Color (device, 0, 0, 255);
			black= new org.eclipse.swt.graphics.Color (device, 0, 0, 0);
			this.variablelabel=variablelabel;
			this.variable=variable;
			pressed=false;
			
		}

		@Override
		public void mousePressed(MouseEvent me) {
			if (!pressed) {
				pressed=true;
				UmlView.getInstance().setSelectedVariable(variable,umlClass);
				variablelabel.setForegroundColor(blue);
				System.out.println(variable.toString());
			}
			else {
				variablelabel.setForegroundColor(black);
				UmlView.getInstance().setSelectedVariable(null,null);
				pressed=false;
			}
				
			
			
		}

		@Override
		public void mouseReleased(MouseEvent me) {

		}

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
	

		}
	}
}
