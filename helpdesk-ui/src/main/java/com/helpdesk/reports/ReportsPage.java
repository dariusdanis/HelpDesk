package com.helpdesk.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.request.RequestPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.utils.Constants;


public class ReportsPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private String startDateStr;
	private String endDateStr;
	private Date startDate;
	private Date endDate;
	
	@SpringBean
	private RequestService requestService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else if (!director()) {
			setResponsePage(HomePage.class);
			return;
		}
		
		WebMarkupContainer reportsContainer = initReportsContainer("reportsContainer");
		Form<?> form = initForm("reportsFiterForm", reportsContainer);
		reportsContainer.add(initReportTable("repeatingView", new ArrayList<RequestEntity>()));
		form.add(initDateInput("startDate", "startDateStr"));
		form.add(initDateInput("endDate", "endDateStr"));
		add(reportsContainer);
		add(form);
	}

	private ListView<RequestEntity> initReportTable(String wicketId, List<RequestEntity> requests) {
		ListModel<RequestEntity> requsetModel = new ListModel<RequestEntity>(requests);
		ListView<RequestEntity> listView =  new ListView<RequestEntity>(wicketId, requsetModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RequestEntity> item) {
				RequestEntity requestEntity = item.getModelObject();
				Link<Object> linkToRequest = initLinkToRequest("linkToRequest", requestEntity.getId());
				linkToRequest.add(new Label("requestId", requestEntity.getId()));
				item.add(linkToRequest);
				item.add(new Label("summary", requestEntity.getSummary()));
				item.add(new Label("date", requestEntity.getRequestDate()));
				item.add(new Label("type", requestEntity.getTypeEntity().getType()));
				item.add(new Label("assigned", requestEntity.getEngineerEntity() == null ? "-" : 
					requestEntity.getEngineerEntity().toString()));
				item.add(new Label("status", requestEntity.getStatus()));
			}

		};
		listView.setOutputMarkupId(true);
		return listView;
	}

	private Link<Object> initLinkToRequest(String wicketId, final int id) {
		return new Link<Object>(wicketId){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(RequestPage.class, RequestPage.parametersWith(id));
			}
			
		};
	}
	
	private WebMarkupContainer initReportsContainer(String wicketId) {
		WebMarkupContainer reportsContainer = new WebMarkupContainer(wicketId);
		reportsContainer.setOutputMarkupId(true);
		return reportsContainer;
	}

	private TextField<String> initDateInput(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
				new PropertyModel<String>(this, expression));
		return textField;
	}

	private Form<?> initForm(String wicketId, final WebMarkupContainer reportsContainer) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				List<Object> validationError = validateFiltersDateForm();
				if (validationError == null) {
					reportsContainer.removeAll();
					reportsContainer.add(initReportTable("repeatingView", 
							requestService.getAllOverDoRequest(startDate, endDate)));
					target.add(reportsContainer);
				} else {
					appendJavaScript(target, validationError.get(0), validationError.get(1));
				}
			}
		});
		
		return form;
	}

	protected List<Object> validateFiltersDateForm() {
		if (startDateStr == null) {
			return Arrays.asList(new Object[]{"startDate", Constants.REQUIRED});
		}
		if (endDateStr == null) {
			return Arrays.asList(new Object[]{"endDate", Constants.REQUIRED});
		}
		if (!startDateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
			return Arrays.asList(new Object[]{"startDate", Constants.BAD_DATE});
		} 
		if (!endDateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
			return Arrays.asList(new Object[]{"endDate", Constants.BAD_DATE});
		}
		try {
			startDate = new SimpleDateFormat("MM/dd/yyyy").parse(startDateStr);
			endDate = new SimpleDateFormat("MM/dd/yyyy").parse(endDateStr);
			if (!startDate.before(endDate)) {
				return Arrays.asList(new Object[]{"startDate", Constants.BAD_DATE_PERIOD});
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
