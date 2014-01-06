package com.helpdesk.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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
		add(requsetConteiner);
		add(pageConteiner);
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
							getRequestList((Integer.valueOf(page)) * 2));
					requsetConteiner.add(listView);
					pageConteiner.removeAll();
					pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, requsetConteiner, 	
							pageConteiner, listView));
					target.add(requsetConteiner);
					target.add(pageConteiner);
				}
			}
		};
		return ajaxLink;
	}

	private List<String> initList(Long totalRequest) {
		List<String> list = new ArrayList<String>();
		int counter = 0;
		int from = pageId;
		int to = totalRequest.intValue() / 2;
		to = (totalRequest.intValue() % 2) == 0 ? to : ++to;
		
		for (int i = from; i < to; i++) {
			list.add(String.valueOf(i));
			counter++;
			if (counter == 5) {
				break;
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

	private OnChangeAjaxBehavior initChangeBehaviour(final WebMarkupContainer container,
			final ListView<RequestEntity> listView, final WebMarkupContainer pageConteiner) {
		return new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				getHDSession().setHomePageStatus(filterOption.toLowerCase());
				listView.removeAll();
				container.removeAll();
				pageConteiner.removeAll();
				ListView<RequestEntity> requestlistView = initRequestTable("repeatingView", getRequestList(0));
				pageConteiner.add(initPaging("repeatingViewPaging", totalRequest, container, 
						pageConteiner, requestlistView));
				container.add(requestlistView);
				target.add(container);
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
						Constants.Status.SOLVED.toString());
				return requestService.getAllByBelongsTosAndNotStatu(getLoggedUser(), 
						Constants.Status.SOLVED.toString(), from);
			} else {
				totalRequest = requestService.getAllByBelongsToAndStatusCount(getLoggedUser(),
						Constants.Status.SOLVED.toString());
				return requestService.getAllByBelongsToAndStatus(getLoggedUser(),
						Constants.Status.SOLVED.toString(), from);
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
			System.out.println(filterOption);
			if (equalsFOCurrent()) {
				System.out.println(filterOption);
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
	
}
