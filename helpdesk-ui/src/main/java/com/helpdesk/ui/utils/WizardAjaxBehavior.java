package com.helpdesk.ui.utils;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;

public class WizardAjaxBehavior extends AbstractDefaultAjaxBehavior {
	private static final long serialVersionUID = 1L;

	@Override
	protected void respond(AjaxRequestTarget target) {
		RequestCycle cycle = RequestCycle.get();
		WebRequest webRequest = (WebRequest) cycle.getRequest();
		StringValue param1 = webRequest.getQueryParameters().getParameterValue(
				"param1");
		StringValue param2 = webRequest.getQueryParameters().getParameterValue(
				"param2");
		System.out.println('a');
	}

	@Override
	public CharSequence getCallbackScript() {
		String script = super.getCallbackScript().toString();
		script = script.replace("\"PLACEHOLDER1\"", "param1Value");
		script = script.replace("\"PLACEHOLDER2\"", "param2Value");
		return script;
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getExtraParameters().put("param1", "PLACEHOLDER1");
		attributes.getExtraParameters().put("param2", "PLACEHOLDER2");
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		String script = "var param1Value = My.JavaScript.Module.calculate1();";
		script += "var param2Value = My.JavaScript.Module.calculate2();";
		script += getCallbackScript();
		response.render(OnDomReadyHeaderItem.forScript(script));
	}

}
