import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.uml.extensionpoint.IButton;


public class UmlProvider implements IButton {
	
	public UmlProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public PackageElement action(PackageElement packageElement) {
		if (!(packageElement.getParent()==null)) {
			if(packageElement.getParent().isPackage()) {
				return packageElement.getParent();
			}
				
		}
		return packageElement;
	}

}
