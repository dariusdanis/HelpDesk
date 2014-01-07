package com.helpdesk.ui.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.TypeService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.request.AddRequestPage;
import com.helpdesk.ui.utils.Constants;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private TypeService typeService;

	@SpringBean
	private UserService userService;
	
	@SpringBean
	private RequestService requestService;
	
	private String filterOption;
	
	private Long totalRequest;
	
	private int pageId;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		}
		
		filterOption = getHDSession().getHomePageStatus();
		WebMarkupContainer requsetConteiner = initConteiner("requsetConteiner");
		WebMarkupContainer pageConteiner = initConteiner("pageConteiner");
		ListView<RequestEntity> listView = initRequestTable("repeatingView", getRequestList(0));
		add(initFilterOptions("filterOptions", "filterOption", requsetConteiner, listView, pageConteiner));
		add(initReauestLink("requestLink"));
		pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, requsetConteiner, 
				pageConteiner, listView));
		requsetConteiner.add(listView);
		pageConteiner.add(initNextButton("next", requsetConteiner, pageConteiner, listView));
		pageConteiner.add(initPreviousButton("previous", requsetConteiner, pageConteiner, listView));
		add(requsetConteiner);
		add(pageConteiner);
		addInfoMessage();
		updateMainStats();
	}

	private Component initNextButton(String wicketId,
			final WebMarkupContainer requsetConteiner,
			final WebMarkupContainer pageConteiner, final ListView<RequestEntity> requestlistView) {
		AjaxFallbackLink<Object> ajaxFallbackLink = new AjaxFallbackLink<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				int totalPages = totalRequest.intValue() / 10;
				totalPages = (totalRequest.intValue() % 10) == 0 ? totalPages : ++totalPages;
				if (++pageId != totalPages){
					changePage(target, String.valueOf(pageId), requsetConteiner, requestlistView, pageConteiner);
				}
			}
		};
		
		ajaxFallbackLink.setOutputMarkupId(true);
		return ajaxFallbackLink;
	}

	private Component initPreviousButton(String wicketId,
			final WebMarkupContainer requsetConteiner,
			final WebMarkupContainer pageConteiner, final ListView<RequestEntity> requestlistView) {
		AjaxFallbackLink<Object> ajaxFallbackLink = new AjaxFallbackLink<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if ((pageId) != 0) {
					changePage(target, String.valueOf(--pageId), requsetConteiner, requestlistView, pageConteiner);
				}
			}
		};
		
		ajaxFallbackLink.setOutputMarkupId(true);
		return ajaxFallbackLink;
	}


	private ListView<String> initPaging(String wicketId, Long totalRequest, 
			final WebMarkupContainer requsetConteiner, final WebMarkupContainer pageConteiner,
			final ListView<RequestEntity> requestlistView) {
		ListModel<String> requsetModel = new ListModel<String>(initList(totalRequest));
		ListView<String> listView =  new ListView<String>(wicketId, requsetModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<String> item) {
				AjaxLink<Object> ajaxLink = initLinkToPage("linkToPage", item.getModel().getObject(), 
						requsetConteiner, requestlistView, pageConteiner);
				ajaxLink.add(new Label("pageNumber", 1 + Integer.valueOf(item.getModel().getObject())));
				if (Integer.valueOf(item.getModel().getObject()) == pageId) {
					ajaxLink.add(new AttributeModifier("class", "active"));
				}
				item.add(ajaxLink);
			}

		};
		listView.setOutputMarkupId(true);
		return listView;
	}

	protected AjaxLink<Object> initLinkToPage(String wicketId, final String page,
			final WebMarkupContainer requsetConteiner, final ListView<RequestEntity> requestlistView,
			final WebMarkupContainer pageConteiner) {
		AjaxLink<Object> ajaxLink = new AjaxLink<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				if (pageId != Integer.valueOf(page)) {
					pageId = Integer.valueOf(page);
					requsetConteiner.removeAll();
					requestlistView.removeAll();
					ListView<RequestEntity> listView = initRequestTable("repeatingView", 
							getRequestList((Integer.valueOf(page)) * 10));
					requsetConteiner.add(listView);
					pageConteiner.removeAll();
					pageConteiner.add(initNextButton("next", requsetConteiner, pageConteiner, listView));
					pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, requsetConteiner, pageConteiner, listView));
					pageConteiner.add(initPreviousButton("previous", requsetConteiner, pageConteiner, listView));
					target.add(requsetConteiner);
					target.add(pageConteiner);
				}
			}
		};
		return ajaxLink;
	}
	
	private void changePage(AjaxRequestTarget target, String page, WebMarkupContainer requsetConteiner,
			ListView<RequestEntity> requestlistView, WebMarkupContainer pageConteiner){
		pageId = Integer.valueOf(page);
		requsetConteiner.removeAll();
		requestlistView.removeAll();
		ListView<RequestEntity> listView = initRequestTable("repeatingView", 
				getRequestList((Integer.valueOf(page)) * 10));
		requsetConteiner.add(listView);
		pageConteiner.removeAll();
		pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, requsetConteiner, 	
				pageConteiner, listView));
		pageConteiner.add(initNextButton("next", requsetConteiner, pageConteiner, listView));
		pageConteiner.add(initPreviousButton("previous", requsetConteiner, pageConteiner, listView));
		target.add(requsetConteiner);
		target.add(pageConteiner);
	}
	
	private List<String> initList(Long totalRequest) {
		List<String> list = new ArrayList<String>();
		int pageRadius = 3;
		int perPage = 10;
		int currentPage = pageId + 1;
		int totalPages = totalRequest.intValue() / perPage;
		totalPages = (totalRequest.intValue() % perPage) == 0 ? totalPages : ++totalPages;
		
		int rangeFrom;
		int rangeTo;
		
		int toAdd = 0;
		int toSub = 0;
		
		if (currentPage < pageRadius) {
			rangeFrom = 1;
			toAdd = pageRadius - currentPage;
		} else {
			rangeFrom = currentPage - pageRadius + 1;
		}
		
		if (currentPage > totalPages - pageRadius) {
			rangeTo = totalPages;
			toSub = totalPages - pageRadius - currentPage + 1;
		} else {
			rangeTo = currentPage + pageRadius - 1;
		}

		for (int i = rangeFrom + toSub; i <= rangeTo + toAdd; i++) {
			if (i >= 1 && i <= totalPages) {
		    	list.add(String.valueOf(i - 1));
			}
		}
		
		return list;
	}

	private Link<Object> initReauestLink(String wicketId) {
		return new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(AddRequestPage.class);
			}
		};
	}

	private WebMarkupContainer initConteiner(String wicketId) {
		WebMarkupContainer containe = new WebMarkupContainer(wicketId);
		containe.setOutputMarkupId(true);
		return containe;
	}

	private ListView<RequestEntity> initRequestTable(String wicketId, List<RequestEntity> list) {
		ListModel<RequestEntity> requsetModel = new ListModel<RequestEntity>(list);
		ListView<RequestEntity> listView =  new ListView<RequestEntity>(wicketId, requsetModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<RequestEntity> item) {
				item.add(new RequestsListItemPanel("list", item.getModel().getObject()));
			}

		};
		listView.setOutputMarkupId(true);
		return listView;
	}

	private DropDownChoice<String> initFilterOptions(String wicketId,
			String expression, WebMarkupContainer container,
			ListView<RequestEntity> listView, WebMarkupContainer pageConteiner) {
		DropDownChoice<String> choice = new DropDownChoice<String>(wicketId,
				new PropertyModel<String>(this, expression), initFilterOptionsList()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		
		choice.setOutputMarkupId(true);
		choice.add(initChangeBehaviour(container, listView, pageConteiner));
		return choice;
	}
	
	private List<String> initFilterOptionsList() {
		switch (getRole()) {
			case "ADMIN":
				return Constants.filterOptionsAdminAndDirect;
			case "CLIEN":
				return Constants.filterOptionsClient;
			case "ENGIN":
				return Constants.filterOptionsEngin;
			case "DIREC":
				return Constants.filterOptionsAdminAndDirect;
		}
		return new ArrayList<String>();
	}

	private OnChangeAjaxBehavior initChangeBehaviour(final WebMarkupContainer requsetConteiner,
			final ListView<RequestEntity> listView, final WebMarkupContainer pageConteiner) {
		return new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				getHDSession().setHomePageStatus(filterOption.toLowerCase());
				listView.removeAll();
				requsetConteiner.removeAll();
				pageConteiner.removeAll();
				ListView<RequestEntity> requestlistView = initRequestTable("repeatingView", getRequestList(0));
				pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, requsetConteiner, 
						pageConteiner, requestlistView));
				requsetConteiner.add(requestlistView);
				pageConteiner.add(initNextButton("next", requsetConteiner, pageConteiner, listView));
				pageConteiner.add(initPreviousButton("previous", requsetConteiner, pageConteiner, listView));
				target.add(requsetConteiner);
				target.add(pageConteiner);
			}
			
		};
	}
	
	private List<RequestEntity> getRequestList(int from) {
		switch (getRole()) {
		case "ADMIN":
			if (equalsFOCurrent()) {
				totalRequest = requestService.getAllByStatusCount(Constants.Status.NOT_ASSIGNED.toString());
				return requestService.getAllByStatus(Constants.Status.NOT_ASSIGNED.toString(), from);
			} else if (equalsFOHistory()) {
				totalRequest = requestService.getAllByAdminAndNotStatusCount(getLoggedUser(), 
						Constants.Status.NOT_ASSIGNED.toString());
				return requestService.getAllByAdminAndNotStatus(getLoggedUser(), 
						Constants.Status.NOT_ASSIGNED.toString(), from);
			} else {
				totalRequest = requestService.getAllCount();
				return requestService.getAll(from);
			}
		case "CLIEN":
			if (equalsFOCurrent()) {
				totalRequest = requestService.getAllByBelongsTosAndNotStatuCount(getLoggedUser(), 
						Constants.Status.SOLVED.toString(), Constants.Status.WONT_SOLVE.toString());
				return requestService.getAllByBelongsTosAndNotStatu(getLoggedUser(), 
						Constants.Status.SOLVED.toString(), Constants.Status.WONT_SOLVE.toString(), from);
			} else {
				totalRequest = requestService.getAllByBelongsToAndStatusCount(getLoggedUser(),
						Constants.Status.SOLVED.toString(), Constants.Status.WONT_SOLVE.toString());
				return requestService.getAllByBelongsToAndStatus(getLoggedUser(),
						Constants.Status.SOLVED.toString(), Constants.Status.WONT_SOLVE.toString(), from);
			}
		case "ENGIN":
			if (equalsFOCurrent()) {
				totalRequest = requestService.getAllByEngineerAndStatusCount(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString());
				return requestService.getAllByEngineerAndStatus(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString(), from);
			} else {
				totalRequest = requestService.getAllByEngineerAndNotStatusCount(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString());
				return requestService.getAllByEngineerAndNotStatus(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString(), from);
			}
		case "DIREC":
			if (equalsFOCurrent()) {
				totalRequest = requestService.getAllByStatusOrAssignetToUserCount(Constants.Status.NOT_ASSIGNED.toString(),
						getLoggedUser());
				return requestService.getAllByStatusOrAssignetToUser(Constants.Status.NOT_ASSIGNED.toString(),
						getLoggedUser(), from);
			} else if (equalsFOHistory()) {
				totalRequest = requestService.getDirectorHistoryCount(getLoggedUser(), Constants.Status.SOLVED.toString(),
						Constants.Status.ASSIGNED.toString());
				return requestService.getDirectorHistory(getLoggedUser(), Constants.Status.SOLVED.toString(),
						Constants.Status.ASSIGNED.toString(), from);
			} else {
				totalRequest = requestService.getAllCount();
				return requestService.getAll(from);
			}
		}
		return new ArrayList<RequestEntity>();
	}

	private boolean equalsFOCurrent(){
		return getHDSession().getHomePageStatus().equals(Constants.FOCurrent());
	}
	
	private boolean equalsFOHistory(){
		return getHDSession().getHomePageStatus().equals(Constants.FOHistory());
	}
	
	public void addInfoMessage() {
        Label infoMessage;
        if (!getSession().getFeedbackMessages().isEmpty()) {
            infoMessage = new Label("infoMessage", getSession()
                    .getFeedbackMessages().iterator().next().getMessage()
                    .toString());
            getSession().getFeedbackMessages().clear();
        } else {
            infoMessage = new Label("infoMessage", "");
            infoMessage.setVisible(false);
        }
        add(infoMessage);
    }
    
    public void updateMainStats() {
		Long countS = requestService.getTodaySolvedCount(BasePage.getSysteDate());
		Label solvedRequests = new Label("solvedRequests", String.valueOf(countS));
		add(solvedRequests);
		   
		Long countR = requestService.getTodayNewReq(BasePage.getSysteDate());
		Label newRequests = new Label("newRequests", String.valueOf(countR));
		add(newRequests);
		    
		Long countI = requestService.getTodayNewInfoReq(BasePage.getSysteDate());
		Label newInfoRequests = new Label("newInfoRequests", String.valueOf(countI));
		add(newInfoRequests);
    }
}
