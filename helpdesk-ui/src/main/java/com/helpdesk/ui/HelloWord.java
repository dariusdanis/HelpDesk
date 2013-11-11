package com.helpdesk.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class HelloWord extends WebPage {
	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Label helloword = new Label("test", "helloWord");
		add(helloword);
	}
	
}
