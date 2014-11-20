package uk.ac.soton.mib104.t2.activities.oauth.ui.view;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.DefaultActivityIcon;


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
			ICON_SMALL_REST = new EmptyIcon(16,16);
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
			ICON_SMALL_REST = new EmptyIcon(18,18);			
		}
		return ICON_SMALL_REST;
	}
	
	/**
	 * Sole constructor.
	 */
	private IconManager() {
		super();
	}
	
	private static final class EmptyIcon  implements Icon {

		  private int width;
		  private int height;
		  
		  public EmptyIcon() {
		    this(0, 0);
		  }
		  
		  public EmptyIcon(int width, int height) {
		    this.width = width;
		    this.height = height;
		  }

		  public int getIconHeight() {
		    return height;
		  }

		  public int getIconWidth() {
		    return width;
		  }

		  public void paintIcon(Component c, Graphics g, int x, int y) {
		  }

		}

}
