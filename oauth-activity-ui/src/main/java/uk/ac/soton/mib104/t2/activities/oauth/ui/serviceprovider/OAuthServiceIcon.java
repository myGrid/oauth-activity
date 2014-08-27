package uk.ac.soton.mib104.t2.activities.oauth.ui.serviceprovider;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.activities.beanshell.servicedescriptions.BeanshellActivityIcon;
import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workbench.ui.impl.configuration.colour.ColourManager;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import uk.ac.soton.mib104.t2.activities.oauth.OAuth10aRESTActivity;
import uk.ac.soton.mib104.t2.activities.oauth.OAuthActivity;
import uk.ac.soton.mib104.t2.activities.oauth.ui.view.IconManager;

public class OAuthServiceIcon implements ActivityIconSPI {

	static Icon icon = null;

	static {
		final ColourManager colourManager = ColourManager.getInstance();
		
		if (colourManager != null) {
			colourManager.setPreferredColour(OAuthActivity.class.getCanonicalName(), Color.decode("#CCCCCC"));
			colourManager.setPreferredColour(OAuth10aRESTActivity.class.getCanonicalName(), Color.decode("#7AAFFF"));
		}
	}

	public int canProvideIconScore(final Activity<?> activity) {
		if ((activity != null) && ((activity instanceof OAuthActivity) || (activity instanceof OAuth10aRESTActivity))) {
			return DEFAULT_ICON + 1;
		} else {
			return NO_ICON;	
		}
	}

	public Icon getIcon(final Activity<?> activity) {
		if (icon == null) {
			icon = new ImageIcon(BeanshellActivityIcon.class.getResource("/beanshell.png"));
		}
		return icon;
	}

}
