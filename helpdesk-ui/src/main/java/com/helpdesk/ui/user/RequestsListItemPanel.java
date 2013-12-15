package com.helpdesk.ui.user;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.request.RequestPage;

public class RequestsListItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private RequestEntity requestEntity;
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private RequestService requestService;
	
	public RequestsListItemPanel(String wicketId, RequestEntity requestEntity) {
		super(wicketId);
		this.requestEntity = requestEntity;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(initLink("requstId", "label", requestEntity.getId()));
		add(new Label("summary", requestEntity.getSummary()));
		add(new Label("date", requestEntity.getRequestDate()));
		add(new Label("type", requestEntity.getTypeEntity().getType()));
		add(new Label("assigned", requestEntity.getEngineerEntity() == null ? "-" : 
			requestEntity.getEngineerEntity().toString()));
		add(new Label("status", "test"));
	
	}
	
	private Link<Object> initLink(String wicketId, String labelWicketId, final int parentId){
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick() {
				setResponsePage(RequestPage.class, RequestPage.parametersWith(parentId));
			}
			
		};
		link.add(new Label(labelWicketId, "R-" + parentId));
		return link;
	}
	
}
