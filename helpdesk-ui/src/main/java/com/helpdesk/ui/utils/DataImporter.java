package com.helpdesk.ui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.CompanyFacilityEntity;
import com.helpdesk.domain.entity.FacilityEntity;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.RoleEntity;
import com.helpdesk.domain.entity.TypeEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.CompanyFacilityService;
import com.helpdesk.domain.service.CompanyService;
import com.helpdesk.domain.service.FacilityService;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.UserService;

public class DataImporter {

	@SpringBean
	private FacilityService facilityService;

	@SpringBean
	private UserService userService;
	
	@SpringBean
	private CompanyService companyService;
	
	@SpringBean 
	private CompanyFacilityService companyFacilityService;
	
	@SpringBean 
	private RequestService requestService;
	
	HashMap<String, Integer> facilityHashmap;
	HashMap<String, Integer> employeesHashmap;
	HashMap<String, Integer> companyHashmap;
	HashMap<String, Integer> companyFacilityEntity;
	HashMap<String, Integer> clientHashmap;
	ListMultimap<String, String> facilitiesAndCompanyFacilitiesHashmap;
	
	HashMap<Integer, Assigment>  assigmentHashmap;
	
	HashMap<String, Integer> workbookSheetsHashmap;
	
	public DataImporter() {
		Injector.get().inject(this);
		facilityHashmap = new HashMap<String, Integer>();
		employeesHashmap = new HashMap<String, Integer>();
		companyHashmap = new HashMap<String, Integer>();
		clientHashmap = new HashMap<String, Integer>();
		facilitiesAndCompanyFacilitiesHashmap = ArrayListMultimap.create();
		assigmentHashmap = new HashMap<Integer, Assigment>();
		workbookSheetsHashmap = new HashMap<String, Integer>();
	}

	public void addAssigment(Assigment assigment) {
		Assigment previousAssigment = assigmentHashmap.get(assigment.getRequestFK());
		
		
		if (previousAssigment != null) {
			assigment.addTimeSpend(previousAssigment.getTimeSpend());
		}
		assigmentHashmap.put(assigment.getRequestFK(), assigment);
	}
	
	public Integer StringToInt(String value) {
		
		if (value == null) {
			return null;
		}
		
		try {
		Double doubleNumber = Double.parseDouble(value);
		return doubleNumber.intValue();
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public String getStringValueFromCell(Cell cell) {

		String value = null;
		if (cell == null) {
			return null;
		}
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			value = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		}
		return value;
	}
	
	public Date getDateFromCell(Cell cell) {
		String dateString = getStringValueFromCell(cell);
		
		if (dateString == null) {
			return null;
		}
		
		Double dateNumber = Double.parseDouble(dateString);
		return DateUtil.getJavaDate(dateNumber);
	}

	public void parseFacilities(HSSFSheet facilities) {
		for (int i = 1; i < facilities.getPhysicalNumberOfRows(); i++) {
			Row row = facilities.getRow(i);
			String employeeId = getStringValueFromCell(row.getCell(0));
			String name = getStringValueFromCell(row.getCell(1));
			String inc = getStringValueFromCell(row.getCell(2));
			String rec = getStringValueFromCell(row.getCell(3));
			
			inc = StringToInt(inc).toString();
			rec = StringToInt(rec).toString();
			FacilityEntity facilityEntity = facilityService
					.merge(new FacilityEntity(name, inc, rec));
			facilityHashmap.put(employeeId, facilityEntity.getId());
		}
	}

	public void parseEmployees(HSSFSheet employees) {
		for (int i = 1; i < employees.getPhysicalNumberOfRows(); i++) {
			Row row = employees.getRow(i);

			String employeeId = getStringValueFromCell(row.getCell(0));
			String name = getStringValueFromCell(row.getCell(1));
			String surname = getStringValueFromCell(row.getCell(2));
			String roleLetter = getStringValueFromCell(row.getCell(3));
			String phone = getStringValueFromCell(row.getCell(4));
			String email = getStringValueFromCell(row.getCell(5));

			int role = 0;
			
			if(roleLetter.equals("I")) {
				role = 3;
			} else if (roleLetter.equals("V")) {
				role = 4;
			} else if (roleLetter.equals("A")) {
				role = 1;
			}
			
			UserEntity userEntity = userService.merge(new UserEntity(name, surname, email, phone, name, new CompanyEntity(1), new RoleEntity(role), true));
			
			employeesHashmap.put(employeeId, userEntity.getId());
		}
	}

	public void parseCompanies(HSSFSheet companies) {
		for (int i = 1; i < companies.getPhysicalNumberOfRows(); i++) {
			Row row = companies.getRow(i);

			String Id = getStringValueFromCell(row.getCell(0));
			String companyName = getStringValueFromCell(row.getCell(1));
			String companyAdress = getStringValueFromCell(row.getCell(2));

			CompanyEntity entity = companyService.merge(new CompanyEntity(companyName, companyAdress));
			
			companyHashmap.put(Id, entity.getId());
		}
	}

	public void parseClients(HSSFSheet clients) {
		for (int i = 1; i < clients.getPhysicalNumberOfRows(); i++) {
			Row row = clients.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			String FKCompanyId = getStringValueFromCell(row.getCell(1));
			String name = getStringValueFromCell(row.getCell(2));
			String surname = getStringValueFromCell(row.getCell(3));
			String phone = getStringValueFromCell(row.getCell(4));
			String email = getStringValueFromCell(row.getCell(5));
			String active = getStringValueFromCell(row.getCell(6));

			boolean isActive = false;
			if (active.equals("true")) {
				isActive = true;
			}
			
			UserEntity userEntity = userService.merge(new UserEntity(name, surname, email, phone, "password", new CompanyEntity(companyHashmap.get(FKCompanyId)), new RoleEntity(2), isActive ));
			
			clientHashmap.put(id, userEntity.getId());
		}
	}

	public void parseCompanyFacilities(HSSFSheet companyFacilities) {
		for (int i = 1; i < companyFacilities.getPhysicalNumberOfRows(); i++) {
			Row row = companyFacilities.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			//String facilityNumber = getStringValueFromCell(row.getCell(1));
			//String name = getStringValueFromCell(row.getCell(2));
			String FKCompany = getStringValueFromCell(row.getCell(3));
			//String dateFrom = getStringValueFromCell(row.getCell(4));
			String dateTo = getStringValueFromCell(row.getCell(5));

			Date expireDate;
			if(dateTo != null) {
			Double dateDouble = Double.parseDouble(dateTo);
			expireDate = DateUtil.getJavaDate(dateDouble);
			} else {
				expireDate = null;
			}
			
			//TODO: facilityNumber
			//TODO: registrationDate
			List<String> FacilityFKList = facilitiesAndCompanyFacilitiesHashmap.get(id);
			
			CompanyEntity companyEntity = new CompanyEntity(companyHashmap.get(FKCompany));
			for (int j = 0; j < FacilityFKList.size(); j++) {
				Integer facilityId = facilityHashmap.get(FacilityFKList.get(j));
				FacilityEntity facilityEntity = new FacilityEntity(facilityId);
				companyFacilityService.merge(new CompanyFacilityEntity(companyEntity, facilityEntity, expireDate));
			}
			
		}
	}

	public void parseFacilitiesAndCompanyFacilities(
			HSSFSheet facilitiesAndCompanyFacilities) {
		for (int i = 1; i < facilitiesAndCompanyFacilities
				.getPhysicalNumberOfRows(); i++) {
			Row row = facilitiesAndCompanyFacilities.getRow(i);
			String FKFacility = getStringValueFromCell(row.getCell(0));
			String FKCompanyFacility = getStringValueFromCell(row.getCell(1));

			facilitiesAndCompanyFacilitiesHashmap.put(FKFacility, FKCompanyFacility);
		}
	}

	public void parseRequests(HSSFSheet requests) {
		for (int i = 1; i < requests.getPhysicalNumberOfRows(); i++) {
			Row row = requests.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			String FKClient = getStringValueFromCell(row.getCell(1));
			String FKFacility = getStringValueFromCell(row.getCell(2));
			String type = getStringValueFromCell(row.getCell(3));
			String requestMethod = getStringValueFromCell(row.getCell(4));
			String requestText = getStringValueFromCell(row.getCell(5));
			Date requestDate = getDateFromCell(row.getCell(6));
			Date solveDate = getDateFromCell(row.getCell(7));
			String status = getStringValueFromCell(row.getCell(8));
			//String Ivertinimas = getStringValueFromCell(row.getCell(9));
			String parentRequsetId = getStringValueFromCell(row.getCell(10));

			Assigment assigment = assigmentHashmap.get(StringToInt(id));
			
			String summary = requestText;
			
			
			if (summary.length() > 20){
			summary = requestText.substring(0, 19);
			}
			
			TypeEntity typeEntity = new TypeEntity();
			if (type.equals("REQ")) {
				typeEntity.setId(1);
			} else
			if (type.equals("INC")) {
				typeEntity.setId(2);
			}
			
			if (requestMethod.equals("S")) {
				requestMethod = "SELF_SERVICE";
			};
			
			if (requestMethod.equals("T") || requestMethod.equals("t")) {
				requestMethod = "PHONE";
			};
			
			if (requestMethod.equals("E")) {
				requestMethod = "EMAIL";
			};
			
			
			if (status.equals("A")) {
				status = "WONT_SOLVE";
			}
			
			if (status.equals("I")) {
				status = "SOLVED";
			}
			
			if (status.equals("P")) {
				status = "ASSIGNED";
			}
			
			if (status.equals("N")) {
				status = "NOT_ASSIGNED";
			}
			

			Integer companyFK = companyHashmap.get(FKClient);
			UserEntity creator = userService.findByCompanyFK(companyFK);
			
			
			
			FacilityEntity facilityEntity = new FacilityEntity();
			facilityEntity.setId((facilityHashmap.get(FKFacility)));
			
			if (assigment != null) {
			UserEntity engineerEntity = new UserEntity();
			engineerEntity.setId(employeesHashmap.get(assigment.getEngineerFK()));
			UserEntity administratorEntity = new UserEntity();
			administratorEntity.setId(employeesHashmap.get(assigment.getAdministratorFK()));
			requestService.merge(new RequestEntity(status, requestText, assigment.getRequestSolution(), requestDate, summary, typeEntity, creator, engineerEntity, administratorEntity, null, facilityEntity, solveDate, assigment.getRequestSolution(), requestMethod , assigment.getTimeSpend().toString(), StringToInt(parentRequsetId)) );
			} else {
				assigment = new Assigment();
				requestService.merge(new RequestEntity(status, requestText, assigment.getRequestSolution(), requestDate, summary, typeEntity, creator, null, null, null, facilityEntity, solveDate, assigment.getRequestSolution(), requestMethod , assigment.getTimeSpend().toString(), StringToInt(parentRequsetId)) );
			}
		}
	}

	public void parseBelongsTo(HSSFSheet belongsTo) {
		for (int i = 1; i < belongsTo.getPhysicalNumberOfRows(); i++) {
			Row row = belongsTo.getRow(i);

			//String id = getStringValueFromCell(row.getCell(0));
			String FKRequest = getStringValueFromCell(row.getCell(1));
			String FKAdministrator = getStringValueFromCell(row.getCell(2));
			String FKEngineer = getStringValueFromCell(row.getCell(3));
			//String assignDate = getStringValueFromCell(row.getCell(4));
			Date   returnDate = getDateFromCell(row.getCell(5));
			String requestSolution = getStringValueFromCell(row.getCell(6));
			String status = getStringValueFromCell(row.getCell(7));
			String timeSpend = getStringValueFromCell(row.getCell(8));

			addAssigment(new Assigment(StringToInt(FKRequest), FKAdministrator, FKEngineer, returnDate, requestSolution, status, StringToInt(timeSpend)));
		}
	}

	public void loadData(File xlsFile) {
		try {
			FileInputStream file = new FileInputStream(xlsFile);

			HSSFWorkbook workbook = new HSSFWorkbook(file);
			
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				String sheetName = workbook.getSheetName(i);
				workbookSheetsHashmap.put(sheetName, i);
			}
			
			HSSFSheet facilities = workbook.getSheetAt(workbookSheetsHashmap.get("Paslaugos"));
			parseFacilities(facilities);

			HSSFSheet employees = workbook.getSheetAt(workbookSheetsHashmap.get("Darbuotojai"));
			parseEmployees(employees);

			HSSFSheet companies = workbook.getSheetAt(workbookSheetsHashmap.get("Klientai"));
			parseCompanies(companies);

			HSSFSheet clients = workbook.getSheetAt(workbookSheetsHashmap.get("Atstovai"));
			parseClients(clients);

			HSSFSheet facilitiesAndCompanyFacilities = workbook.getSheetAt(workbookSheetsHashmap.get("SutPasl"));
			parseFacilitiesAndCompanyFacilities(facilitiesAndCompanyFacilities);
			
			HSSFSheet companyFacilities = workbook.getSheetAt(workbookSheetsHashmap.get("Sutartys"));
			parseCompanyFacilities(companyFacilities);

			HSSFSheet belongsTo = workbook.getSheetAt(workbookSheetsHashmap.get("Paskyrimai"));
			parseBelongsTo(belongsTo);

			HSSFSheet requests = workbook.getSheetAt(workbookSheetsHashmap.get("Kreipiniai"));
			parseRequests(requests);


			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
