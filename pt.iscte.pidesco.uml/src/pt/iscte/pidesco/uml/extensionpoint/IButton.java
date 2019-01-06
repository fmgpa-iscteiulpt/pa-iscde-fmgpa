package pt.iscte.pidesco.uml.extensionpoint;

import pt.iscte.pidesco.projectbrowser.model.PackageElement;

public interface IButton {
	/**
	 * Invoked whenever a you click the button(created under UML Actions on the UML View,
	 when the extension point is extended by a client);
	 * @param package element from the current package being displayed on the UML View;
	 * @return package element that you wish to be displayed in the UML View.
	 */
	PackageElement action(PackageElement packageElement);
}
