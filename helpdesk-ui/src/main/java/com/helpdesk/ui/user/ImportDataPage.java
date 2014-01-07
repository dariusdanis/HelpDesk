package com.helpdesk.ui.user;

import java.io.File;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.lang.Bytes;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.utils.Constants;
import com.helpdesk.ui.utils.DataImporter;

public class ImportDataPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else if (!authorize(getLoggedUser())) {
			setResponsePage(HomePage.class);
			return;
		}
		
		final ImportDataForm simpleUploadForm = new ImportDataForm("simpleUpload");
        add(simpleUploadForm);
	}
	
	private boolean authorize(UserEntity loggedUser) {
		return loggedUser.getRoleEntity().getRole().equals(Constants.Roles.DIREC.toString());
	}
	
	private class ImportDataForm extends Form<Void>
    {
		private static final long serialVersionUID = 1L;
		FileUploadField fileUploadField;

        public ImportDataForm(String name)
        {
            super(name);
            setMultiPart(true);
            add(fileUploadField = new FileUploadField("fileInput"));
            setMaxSize(Bytes.kilobytes(10000));
        }

        @Override
        protected void onSubmit()
        {
            final List<FileUpload> uploads = fileUploadField.getFileUploads();
            if (uploads != null)
            {
                for (FileUpload upload : uploads)
                {
                    try
                    {
                    	File file = new File("Test.xls");
                    	upload.writeTo(file);
                    	DataImporter importer = new DataImporter();
                    	importer.loadData(file);
                    	
                    	setResponsePage(HomePage.class);
                    }
                    catch (Exception e)
                    {
                        throw new IllegalStateException("Unable to write file", e);
                    }
                }
            }
        }
    }

}
