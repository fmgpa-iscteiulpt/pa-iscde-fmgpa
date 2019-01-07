package pt.iscte.pidesco.uml;


import pt.iscte.pidesco.uml.service.UmlInterfaceListener;
import pt.iscte.pidesco.uml.service.UmlService;

public class UmlServiceImpl implements UmlService {
	/**
	 * Ads a UmlInterfaceListener to a list in the Activator to store each user of the service
	 * @param UmlInterfaceListener client listener to be added
	 */
	@Override
	public void addListener(UmlInterfaceListener listener) {
		Activator.getInstance().addListener(listener);
	}
	/**
	 * Removes a UmlInterfaceListener from a list in the Activator that stores each user of the service
	 *  @param UmlInterfaceListener client listener to be removed
	 *  */
	@Override
	public void removeListener(UmlInterfaceListener listener) {
		Activator.getInstance().removeListener(listener);
	}
}
