package uk.ac.soton.mib104.t2.activities.oauth;

import java.util.HashMap;
import java.util.Map;

import net.sf.taverna.t2.activities.interaction.InteractionActivityType;
import net.sf.taverna.t2.activities.interaction.InteractionRequestor;
import net.sf.taverna.t2.activities.interaction.InteractionType;
import net.sf.taverna.t2.invocation.InvocationContext;
import net.sf.taverna.t2.reference.ReferenceService;
import net.sf.taverna.t2.reference.T2Reference;
import net.sf.taverna.t2.reference.WorkflowRunIdEntity;
import net.sf.taverna.t2.workflowmodel.processor.activity.AsynchronousActivityCallback;

import org.scribe.builder.api.Api;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import uk.ac.soton.mib104.t2.activities.oauth.util.OAuthActivityUtils;

public class OAuthInteractionCallbackRequestor implements InteractionRequestor {
	
	private static final String DESTINATION = "destination";

	private final AsynchronousActivityCallback callback;
	
	private String destinationUrl = null;
	
	private final HashMap<String, Object> interactionInputs = new HashMap<String, Object>();

	private String path;

	private Integer count;
	
	private static Map<String, Integer> invocationCount = new HashMap<String, Integer> ();

	private OAuthService serviceInFactory;

	private Token requestTokenInFactory;

	private ReferenceService referenceService;

	private InvocationContext context;

	public OAuthInteractionCallbackRequestor(final OAuthActivity oauthActivity,
			final Map<String, T2Reference> oauthActivityInputs,
			final AsynchronousActivityCallback callback) {
		this.callback = callback;
		this.path = calculatePath();
		this.count = calculateInvocationCount(path);


		context = callback.getContext();
		
		referenceService = context.getReferenceService();
		
		final String clientId         = OAuthActivityUtils.renderIdentifier(oauthActivityInputs, context, referenceService, OAuthActivity.CLIENT_ID_INPUT,     String.class, null);
		final String clientSecret     = OAuthActivityUtils.renderIdentifier(oauthActivityInputs, context, referenceService, OAuthActivity.CLIENT_SECRET_INPUT, String.class, null);
		final String redirectEndpoint = OAuthActivityUtils.renderIdentifier(oauthActivityInputs, context, referenceService, OAuthActivity.REDIRECT_URI_INPUT,  String.class, null);
		final String scope            = OAuthActivityUtils.renderIdentifier(oauthActivityInputs, context, referenceService, OAuthActivity.SCOPE_INPUT,         String.class, null);

		if (clientId == null) {
			callback.fail(String.format("%s is undefined", OAuthActivity.CLIENT_ID_INPUT));
			return;
		} else if (clientSecret == null) {
			callback.fail(String.format("%s is undefined", OAuthActivity.CLIENT_SECRET_INPUT));
			return;
		} else {
			// Realize the API description.  
			final Api apiInFactory = OAuthActivityUtils.createApi(oauthActivity.getConfiguration());
			
			serviceInFactory = OAuthActivityUtils.createServiceBuilder(apiInFactory, clientId, clientSecret, redirectEndpoint, scope).build();
			
				requestTokenInFactory = null;
				
				try {
					// Attempt to obtain a new request token. 
					// If request tokens are not required by the service, then the call will raise an exception.   
					requestTokenInFactory = serviceInFactory.getRequestToken();
				} catch (final UnsupportedOperationException ex) {
					callback.fail(ex.getMessage());
					return;
				}
				
				// Construct the User Authorization URL using the request token.  
				destinationUrl = serviceInFactory.getAuthorizationUrl(requestTokenInFactory);
			}
		interactionInputs.put(DESTINATION, destinationUrl);
		
	}

	@Override
	public String getRunId() {
		return this.callback.getContext()
		.getEntities(WorkflowRunIdEntity.class).get(0)
		.getWorkflowRunId();
	}

	@Override
	public Map<String, Object> getInputData() {
		return interactionInputs;
	}

	@Override
	public void fail(String string) {
		this.callback.fail(string);
	}

	@Override
	public void carryOn() {
		this.callback.receiveResult(new HashMap<String, T2Reference>(),
				new int[0]);
	}

	@Override
	public String generateId() {
		final String workflowRunId = getRunId();
		final String parentProcessIdentifier = this.callback
				.getParentProcessIdentifier();
		return (workflowRunId + ":" + parentProcessIdentifier);
	}

	private String calculatePath() {
		final String parentProcessIdentifier = this.callback
		.getParentProcessIdentifier();
String result = "";
String parts[] = parentProcessIdentifier.split(":");

for (int i = 2; i < parts.length; i += 4) {
	if (!result.isEmpty()) {
		result += ":";
	}
	result += parts[i];
}
return result;

	}

	@Override
	public String getPath() {
		return this.path;
	}

	private synchronized static Integer calculateInvocationCount(String path) {
		Integer currentCount = invocationCount.get(path);
		if (currentCount == null) {
			currentCount = Integer.valueOf(0);
		} else {
			currentCount = currentCount + 1;
		}
		invocationCount.put(path, currentCount);
		return currentCount;
	}

	@Override
	public Integer getInvocationCount() {
		return count;
	}

	@Override
	public InteractionActivityType getPresentationType() {
		return InteractionActivityType.LocallyPresentedHtml;
	}

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.AuthenticationRequest;
	}

	@Override
	public String getPresentationOrigin() {
		return "http://build.mygrid.org.uk/taverna/internal/biovel/oauth.html";
	}

	@Override
	public void receiveResult(Map<String, Object> resultMap) {
		String codeInFactory = (String) resultMap.get("code");
		if (codeInFactory == null) {
			this.callback.fail("Null code");
			return;
		}
		final Verifier verifierInFactory = new Verifier(codeInFactory);
		
		// Construct a new access token. 
		final Token accessTokenInFactory = serviceInFactory.getAccessToken(requestTokenInFactory, verifierInFactory);

		final Map<String, T2Reference> outputs = new HashMap<String, T2Reference>();

		if (accessTokenInFactory != null) {
			// Success!
			outputs.put(OAuthActivity.ACCESS_TOKEN_OUTPUT, referenceService.register(accessTokenInFactory.getToken(), 0, true, context));
			outputs.put(OAuthActivity.ACCESS_TOKEN_SECRET_OUTPUT, referenceService.register(accessTokenInFactory.getSecret(), 0, true, context));
			
			callback.receiveResult(outputs, new int[0]);
			return;
		}
	}
	
}
