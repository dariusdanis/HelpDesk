package com.helpdesk.ui.user;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
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
		add(new Label("date", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(requestEntity.getRequestDate())));
		add(new Label("type", requestEntity.getTypeEntity().getType()));
		add(new Label("assigned", requestEntity.getEngineerEntity() == null ? "-" : 
			requestEntity.getEngineerEntity().toString()));
		add(initStatusLabel("status", requestEntity.getStatus()));
	
	}
	
	private Label initStatusLabel(String wicketId, String value){
		boolean late = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(requestEntity.getSolveDate() == null ? BasePage.getSysteDate() : requestEntity.getSolveDate());
		DateTime solveDateDT = new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE));
	
		cal.setTime(requestEntity.getRequestDate());
		DateTime createDateDT = new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE));
		Minutes diff = Minutes.minutesBetween(createDateDT, solveDateDT);
		Integer diffInteger = Integer.valueOf(diff.toString().substring(2, diff.toString().length() - 1));
		if (requestEntity.getTypeEntity().getType().equals("REQ")) {
			if ((diffInteger / 60) >= Integer.valueOf(requestEntity.getFacilityEntity().getLhReq())) {
				late = true;
			}
		} else {
			if ((diffInteger / 60) >= Integer.valueOf(requestEntity.getFacilityEntity().getLhInc())) {
				late = true;
			}
		}
		Label label = new Label(wicketId, value);
		if (late) {
			label.add(new AttributeModifier("class","label label-error"));
		} else {
			label.add(new AttributeModifier("class","label label-success"));
		}
		return label;
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
