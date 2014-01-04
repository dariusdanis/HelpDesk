package com.helpdesk.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		}
		
		filterOption = getHDSession().getHomePageStatus();
		WebMarkupContainer container = initRequsetConteiner("requsetConteiner");
		ListView<RequestEntity> listView = initRequestTable("repeatingView", getRequestList());
		add(initFilterOptions("filterOptions", "filterOption", container, listView));
		add(initReauestLink("requestLink"));
		container.add(listView);
		add(container);
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

	private WebMarkupContainer initRequsetConteiner(String wicketId) {
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
			ListView<RequestEntity> listView) {
		DropDownChoice<String> choice = new DropDownChoice<String>(wicketId,
				new PropertyModel<String>(this, expression), initFilterOptionsList()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		
		choice.setOutputMarkupId(true);
		choice.add(initChangeBehaviour(container, listView));
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
			final ListView<RequestEntity> listView) {
		return new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
		    protected void onUpdate(AjaxRequestTarget target) {
				getHDSession().setHomePageStatus(filterOption);
				listView.removeAll();
				container.removeAll();
				container.add(initRequestTable("repeatingView", getRequestList()));
				target.add(container);
			}
			
		};
	}
	
	private List<RequestEntity> getRequestList() {
		switch (getRole()) {
		case "ADMIN":
			if (equalsFOCurrent()) {
				return requestService.getAllByStatus(Constants.Status.NOT_ASSIGNED.toString());
			} else if (equalsFOHistory()) {
				return requestService.getAllByAdminAndNotStatus(getLoggedUser(), 
						Constants.Status.NOT_ASSIGNED.toString());
			} else {
				return requestService.getAll();
			}
		case "CLIEN":
			if (equalsFOCurrent()) {
				return requestService.getAllByBelongsTosAndNotStatu(getLoggedUser(), 
						Constants.Status.SOLVED.toString());
			} else {
				return requestService.getAllByBelongsToAndStatus(getLoggedUser(),
						Constants.Status.SOLVED.toString());
			}
		case "ENGIN":
			if (equalsFOCurrent()) {
				return requestService.getAllByEngineerAndStatus(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString());
			} else {
				return requestService.getAllByEngineerAndNotStatus(getLoggedUser(), 
						Constants.Status.ASSIGNED.toString());
			}
		case "DIREC":
			if (equalsFOCurrent()) {
				return requestService.getAllByStatusOrAssignetToUser(Constants.Status.NOT_ASSIGNED.toString(),
						getLoggedUser());
			} else if (equalsFOHistory()) {
				return requestService.getDirectorHistory(getLoggedUser(), Constants.Status.SOLVED.toString(),
						Constants.Status.ASSIGNED.toString());
			} else {
				return requestService.getAll();
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
