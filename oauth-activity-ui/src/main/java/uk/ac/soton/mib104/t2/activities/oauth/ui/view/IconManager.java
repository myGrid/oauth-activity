package uk.ac.soton.mib104.t2.activities.oauth.ui.view;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.beanshell.servicedescriptions.BeanshellActivityIcon;

/**
 * IconManager is a factory for icons.
 * 
 * @author Mark Borkum
 */
public final class IconManager {
	
	private static Icon ICON_SMALL;
	
	private static Icon ICON_SMALL_REST;
	
	/**
	 * Returns the small OAuth icon (16x16 pixels).
	 * 
	 * @return  the small OAuth icon.
	 */
	public static final Icon getSmallIcon() {
		if (ICON_SMALL == null) {
			try {
			ICON_SMALL = new ImageIcon(IconManager.class.getResource("/oauthIcon_16x16.png"));
			} catch (NullPointerException e) {
				// nowt
			}
		}
		
		if (ICON_SMALL == null) {
			return BeanshellActivityIcon.getBeanshellIcon();
		}
		return ICON_SMALL;
	}
	
	/**
	 * Returns the "small" OAuth+REST icon (18x18 pixels).
	 * 
	 * @return  the small OAuth+REST icon.
	 */
	public static final Icon getSmallRESTIcon() {
		if (ICON_SMALL_REST == null) {
			try {
				ICON_SMALL_REST = new ImageIcon(IconManager.class.getResource("/service_type_rest_oauth.png"));
			} catch (NullPointerException e) {
				// nowt
			}
		}
		
		if (ICON_SMALL_REST == null) {
			return BeanshellActivityIcon.getBeanshellIcon();			
		}
		return ICON_SMALL_REST;
	}
	
	/**
	 * Sole constructor.
	 */
	private IconManager() {
		super();
	}

}
