package pt.iscte.pidesco.uml.service;

import pt.iscte.pidesco.projectbrowser.model.SourceElement;



public interface UmlInterfaceListener {

	/**
	 * Invoked whenever a mouse double-click is performed on UML class
	 * @param element (non-null) element under the double-click
	 */
	void doubleClick(SourceElement element);
	

}
