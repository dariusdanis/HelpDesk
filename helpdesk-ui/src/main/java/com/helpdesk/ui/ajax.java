package com.helpdesk.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.template.PackageTextTemplate;

public class ajax extends WebPage {
	private static final long serialVersionUID = 1L;

	private AbstractDefaultAjaxBehavior click;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		Label label = new Label("title", "helloword");

		click = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				RequestCycle cycle = RequestCycle.get();
				WebRequest webRequest = (WebRequest) cycle.getRequest();
				StringValue param1 = webRequest.getQueryParameters()
						.getParameterValue("param1");
				StringValue param2 = webRequest.getQueryParameters()
						.getParameterValue("param2");
				cycle.getResponse().write("{\"jsonKey\":\"jsonValue\"}");

		
			
			}

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getExtraParameters().put("param1", "value1");
				attributes.getExtraParameters().put("param2", "value2");
				
			}

			@Override
			public CharSequence getCallbackScript() {
				String script = super.getCallbackScript().toString();
				script = script.replace("\"PLACEHOLDER1\"", "param1Value");
				script = script.replace("\"PLACEHOLDER2\"", "param2Value");
				return script;
			}
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "callbackUrl", getCallbackUrl().toString());
				PackageTextTemplate ptt = new PackageTextTemplate(ajax.class, "ajax.html" );
				response.render(OnDomReadyHeaderItem.forScript(ptt.asString(map)));
			}
			
		};

		Button mouseAre = new Button("mouseare") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("onClick",
						"myFunction(" + "\"" + click.getCallbackUrl() + "\"" + ")");
			}

		};
		mouseAre.add(click);
		add(label);
		add(mouseAre);
	}

}
