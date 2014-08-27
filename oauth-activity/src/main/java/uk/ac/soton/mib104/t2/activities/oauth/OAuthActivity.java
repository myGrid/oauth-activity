package uk.ac.soton.mib104.t2.activities.oauth;

import java.util.Map;

import net.sf.taverna.t2.activities.interaction.InteractionActivityRunnable;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.workflowmodel.processor.activity.AbstractAsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

/**
 * OAuthActivity is an asynchronous activity that obtains OAuth Access Tokens.
 * 
 * @author Mark Borkum
 * 
 * @see AccessToken
 * @see AccessTokenFactory#createAccessToken
 * @see OAuthActivityConfigurationBean
 * @see OAuthActivityHealthChecker
 */
public class OAuthActivity extends
		AbstractAsynchronousActivity<OAuthActivityConfigurationBean> implements
		AsynchronousActivity<OAuthActivityConfigurationBean> {

	/**
	 * The name for the "client_id" port.
	 */
	protected static final String CLIENT_ID_INPUT = "client_id";

	/**
	 * The name for the "client_secret" port.
	 */
	protected static final String CLIENT_SECRET_INPUT = "client_secret";

	/**
	 * The name for the "redirect_uri" port.
	 */
	protected static final String REDIRECT_URI_INPUT = "redirect_uri";

	/**
	 * The name for the "scope" port.
	 */
	protected static final String SCOPE_INPUT = "scope";

	/**
	 * The name for the "access_token" port.
	 */
	protected static final String ACCESS_TOKEN_OUTPUT = "access_token";

	/**
	 * The name for the "access_token_secret" port.
	 */
	protected static final String ACCESS_TOKEN_SECRET_OUTPUT = "access_token_secret";

	private OAuthActivityConfigurationBean configBean;

	@Override
	public void configure(final OAuthActivityConfigurationBean configBean)
			throws ActivityConfigurationException {
		if (configBean == null) {
			throw new ActivityConfigurationException(new NullPointerException(
					"configBean"));
		}

		this.configBean = configBean;

		this.configurePorts();

		return;
	}

	protected void configurePorts() {
		this.removeInputs();
		this.removeOutputs();

		this.addInput(CLIENT_ID_INPUT, 0, true, null, String.class);
		this.addInput(CLIENT_SECRET_INPUT, 0, true, null, String.class);
		this.addInput(REDIRECT_URI_INPUT, 0, true, null, String.class);
		this.addInput(SCOPE_INPUT, 0, true, null, String.class);

		this.addOutput(ACCESS_TOKEN_OUTPUT, 0);
		this.addOutput(ACCESS_TOKEN_SECRET_OUTPUT, 0);

		return;
	}

	@Override
	public void executeAsynch(
			final Map<String, T2Reference> oauthActivityInputs,
			final AsynchronousActivityCallback callback) {

		OAuthInteractionCallbackRequestor requestor = new OAuthInteractionCallbackRequestor(
				this, oauthActivityInputs, callback);
		callback.requestRun(new InteractionActivityRunnable(requestor, null));
	}

	@Override
	public OAuthActivityConfigurationBean getConfiguration() {
		return configBean;
	}

}
