package pt.iscte.pidesco.codegenerator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.eclipse.jdt.core.dom.FieldDeclaration;

public class WindowGenerator{
	
	private Frame frame;
	private int value;
	
	private WindowGenerator() {
		if(frame == null) {
			frame = new Frame();
		}
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setAlwaysOnTop(true);
	}

	public static ArrayList<String> createConstructorOrGS(ArrayList<FieldDeclaration> fields, boolean isConstructor) {
		WindowGenerator windowConstructor = new WindowGenerator();
		ArrayList<String> selectedFields = new ArrayList<String>();
		windowConstructor.frame.setVisible(true);
		JPanel panel = new JPanel();
		String title, information;
		if(isConstructor) {
			title = "Generate Constuctor";
			information = "Select the fields to create the constructor.";
		}else {
			title = "Generate Getters and Setters";
			information = "Select the fields to create the getters and setters.";
		}
		panel.add(new JLabel(information));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		Object[] options = {"Create", "Cancel"};
		for(FieldDeclaration f : fields) {
			String[] splitted = f.toString().replace(";", "").split(" ");
			String fieldName = splitted[splitted.length-1].replaceAll("\n", "");
			String fieldType = splitted[splitted.length-2];
			JCheckBox checkbox = new JCheckBox(fieldType + " " + fieldName);
			panel.add(checkbox);
		}
		
		windowConstructor.value = JOptionPane.showOptionDialog(windowConstructor.frame, panel, title, 
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		Component[] components = panel.getComponents();
		for (Component comp : components) {
	        if (comp instanceof JCheckBox) {
	        	JCheckBox box = (JCheckBox) comp;
	        	if(box.isSelected()) {
	        		selectedFields.add(box.getText());
	        	}
	        }
	    }
		windowConstructor.frame.dispose();
		return selectedFields;
	}
	
}
