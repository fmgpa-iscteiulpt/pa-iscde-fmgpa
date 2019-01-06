package pt.iscte.pidesco.uml.service;



public interface UmlService {
	
	/**
	 * Add a Uml Interface Listener. If listener already added, does nothing.
	 * @param listener (non-null) reference to the listener
	 */
	void addListener(UmlInterfaceListener listener);

	/**
	 * Remove a Uml Interface Listener. 
	 * @param listener (non-null) reference to the listener
	 */
	void removeListener(UmlInterfaceListener listener);

}
