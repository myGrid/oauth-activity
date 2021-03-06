package uk.ac.soton.mib104.t2.activities.oauth.ui.config;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;

import uk.ac.soton.mib104.t2.activities.oauth.OAuthActivity;
import uk.ac.soton.mib104.t2.activities.oauth.OAuthActivityConfigurationBean;
import uk.ac.soton.mib104.t2.activities.oauth.ui.view.IconManager;
import uk.ac.soton.mib104.t2.activities.oauth.util.ApiDescImpl;
import uk.ac.soton.mib104.t2.activities.oauth.util.HttpVerb;
import uk.ac.soton.mib104.t2.activities.oauth.util.Format;

@SuppressWarnings("serial")
public class OAuthConfigurationPanel extends ActivityConfigurationPanel<OAuthActivity, OAuthActivityConfigurationBean> {
	
	public static final JPanel createPane(final OAuthActivityConfigurationBean configBean, final Component fieldRequestTokenEndpoint, final Component fieldRequestTokenVerb, final Component fieldAuthorizationEndpoint, final Component fieldAccessTokenEndpoint, final Component fieldAccessTokenFormat, final Component fieldAccessTokenVerb) {
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		final Insets defaultInsets = c.insets, paddedInsets = new Insets(0, 5, 0, 5);
		
		int x = 0, y = 0;
		
		if (configBean.isRequestTokenRequired()) {
			final JLabel labelRequestTokenEndpoint = new JLabel("Request Token URL:", JLabel.LEFT);
			labelRequestTokenEndpoint.setToolTipText("Configures the URL that is used to obtain Request Tokens.");
			c.gridx = x++;
			c.gridy = y;
			c.insets = paddedInsets;
			contentPane.add(labelRequestTokenEndpoint, c);
			c.insets = defaultInsets;
			
			c.gridx = x++;
			c.gridy = y;
			c.weightx = 1.0f;
			contentPane.add(fieldRequestTokenEndpoint, c);
			c.weightx = 0;
			
			c.gridx = x++;
			c.gridy = y;
			contentPane.add(fieldRequestTokenVerb, c);
			
			x = 0;
			y++;
		}
		
		{
			final JLabel labelAuthorizationEndpoint = new JLabel("User Authorization URL:", JLabel.LEFT);
			labelAuthorizationEndpoint.setToolTipText("Configures the URL that is used to obtain OAuth Verifiers.");
			c.gridx = x++;
			c.gridy = y;
			c.insets = paddedInsets;
			contentPane.add(labelAuthorizationEndpoint, c);
			c.insets = defaultInsets;
			
			c.gridx = x++;
			c.gridy = y;
			c.weightx = 1.0f;
			contentPane.add(fieldAuthorizationEndpoint, c);
			c.weightx = 0;
			
			x = 0;
			y++;
		}
		
		{
			final JLabel labelAccessTokenEndpoint = new JLabel("Access Token URL:", JLabel.LEFT);
			labelAccessTokenEndpoint.setToolTipText("Configures the URL that is used to obtain Access Tokens.");
			c.gridx = x++;
			c.gridy = y;
			c.insets = paddedInsets;
			contentPane.add(labelAccessTokenEndpoint, c);
			c.insets = defaultInsets;
			
			c.gridx = x++;
			c.gridy = y;
			c.weightx = 1.0f;
			contentPane.add(fieldAccessTokenEndpoint, c);
			c.weightx = 0;
			
			c.gridx = x++;
			c.gridy = y;
			contentPane.add(fieldAccessTokenVerb, c);
			
			c.gridx = x++;
			c.gridy = y;
			contentPane.add(fieldAccessTokenFormat, c);
			
			x = 0;
			y++;
		}
		
		return contentPane;
	}
	
	private static final boolean equals(final Object a, final Object b) {
		if (a == null) {
			if (b == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (b == null) {
				return false;
			} else {
				return a.equals(b);
			}
		}
	}
	
	private final OAuthActivity activity;
	
	private OAuthActivityConfigurationBean configBean;
	
	private final JTextField fieldAccessTokenEndpoint = new JTextField(32);
	
	private final JComboBox fieldAccessTokenFormat = new JComboBox(Format.values());
	
	private final JComboBox fieldAccessTokenVerb = new JComboBox(HttpVerb.values());
	
	private final JTextField fieldAuthorizationEndpoint = new JTextField(32);
	
	private final JTextField fieldRequestTokenEndpoint = new JTextField(32);

	private final JComboBox fieldRequestTokenVerb = new JComboBox(HttpVerb.values());
	
	public OAuthConfigurationPanel(final OAuthActivity activity) {
		if (activity == null) {
			throw new NullPointerException("activity");
		}
		
		this.activity = activity;
		
		this.initGui();
	}
	
	@Override
	public boolean checkValues() {
		// TODO Auto-generated method stub.
		return true;
	}
	
	@Override
	public OAuthActivityConfigurationBean getConfiguration() {
		return configBean;
	}
	
	protected void initGui() {
		if (activity == null) {
			throw new IllegalStateException(new NullPointerException("activity"));
		}
		
		final OAuthActivityConfigurationBean configBean = activity.getConfiguration();
		
		if (configBean == null) {
			throw new IllegalStateException(new NullPointerException("activity.getConfiguration()"));
		}
		
		this.removeAll();
		this.setLayout(new BorderLayout());
		this.add(createPane(configBean, fieldRequestTokenEndpoint, fieldRequestTokenVerb, fieldAuthorizationEndpoint, fieldAccessTokenEndpoint, fieldAccessTokenFormat, fieldAccessTokenVerb), BorderLayout.CENTER);
		
		this.refreshConfiguration();
	}

	@Override
	public boolean isConfigurationChanged() {
		if (configBean == null) {
			throw new IllegalStateException(new NullPointerException("activity.getConfiguration()"));
		}
		
		if (configBean.isRequestTokenRequired()) {
			if (!equals(configBean.getRequestTokenEndpoint(), fieldRequestTokenEndpoint.getText())) {
				return true;
			} else if (!equals(configBean.getRequestTokenVerb(), fieldRequestTokenVerb.getSelectedItem())) {
				return true;
			}
		}
		
		if (!equals(configBean.getAuthorizationEndpoint(), fieldAuthorizationEndpoint.getText())) {
			return true;
		}
		
		if (!equals(configBean.getAccessTokenEndpoint(), fieldAccessTokenEndpoint.getText())) {
			return true;
		} else if (!equals(configBean.getAccessTokenFormat(), fieldAccessTokenFormat.getSelectedItem())) {
			return true;
		} else if (!equals(configBean.getAccessTokenVerb(), fieldAccessTokenVerb.getSelectedItem())) {
			return true;
		}
		
		return false;
	}

	@Override
	public void noteConfiguration() {
		if (activity == null) {
			throw new IllegalStateException(new NullPointerException("activity"));
		}
		
		final OAuthActivityConfigurationBean origConfigBean = activity.getConfiguration();
		
		if (origConfigBean == null) {
			throw new IllegalStateException(new NullPointerException("activity.getConfiguration()"));
		}
		
		final OAuthActivityConfigurationBean newConfigBean = new OAuthActivityConfigurationBean(origConfigBean);
		
		if (origConfigBean.isRequestTokenRequired()) {
			newConfigBean.setRequestTokenEndpoint(fieldRequestTokenEndpoint.getText());
			newConfigBean.setRequestTokenVerb((HttpVerb) fieldRequestTokenVerb.getSelectedItem());
		}
		
		newConfigBean.setAuthorizationEndpoint(fieldAuthorizationEndpoint.getText());
		
		newConfigBean.setAccessTokenEndpoint(fieldAccessTokenEndpoint.getText());
		newConfigBean.setAccessTokenFormat((Format) fieldAccessTokenFormat.getSelectedItem());
		newConfigBean.setAccessTokenVerb((HttpVerb) fieldAccessTokenVerb.getSelectedItem());
		
		configBean = newConfigBean;
		
		return;
	}

	@Override
	public void refreshConfiguration() {
		if (activity == null) {
			throw new IllegalStateException(new NullPointerException("activity"));
		}
		
		configBean = activity.getConfiguration();
		
		if (configBean == null) {
			throw new IllegalStateException(new NullPointerException("activity.getConfiguration()"));
		}
		
		if (configBean.isRequestTokenRequired()) {
			fieldRequestTokenEndpoint.setText(configBean.getRequestTokenEndpoint());
			fieldRequestTokenVerb.setSelectedItem(configBean.getRequestTokenVerb());
		}
		
		fieldAuthorizationEndpoint.setText(configBean.getAuthorizationEndpoint());
		
		fieldAccessTokenEndpoint.setText(configBean.getAccessTokenEndpoint());
		fieldAccessTokenFormat.setSelectedItem(configBean.getAccessTokenFormat());
		fieldAccessTokenVerb.setSelectedItem(configBean.getAccessTokenVerb());
		
		return;
	}
}
