package com.helpdesk.ui.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.FacilityEntity;
import com.helpdesk.domain.entity.RoleEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.FacilityService;
import com.helpdesk.domain.service.UserService;

public class DataImporter {

	@SpringBean
	private FacilityService facilityService;

	@SpringBean
	private UserService userService;

	HashMap<String, Integer> facilityHashmap;
	HashMap<String, Integer> employeesHashmap;
	
	public DataImporter() {
		Injector.get().inject(this);
		facilityHashmap = new HashMap<String, Integer>();
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

	public void parseFacilities(HSSFSheet facilities) {
		for (int i = 1; i < facilities.getPhysicalNumberOfRows(); i++) {
			Row row = facilities.getRow(i);
			String employeeId = getStringValueFromCell(row.getCell(0));
			String name = getStringValueFromCell(row.getCell(1));

			FacilityEntity facilityEntity = facilityService.merge(new FacilityEntity(name));
			facilityHashmap.put(employeeId, facilityEntity.getId());

			System.out.println("Inserting: " + employeeId + " " + name);
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
			
			UserEntity userEntity = userService.merge(new UserEntity(name, surname, email, phone, name, new CompanyEntity(1), new RoleEntity(role)));
			System.out.println("NEW ID: " + userEntity.getId());
			System.out.println("Inserting: " + employeeId + " " + name + " " + surname + " "
					+ role + " " + phone + role + " " + email);
		}
	}

	public void parseCompanies(HSSFSheet companies) {
		for (int i = 1; i < companies.getPhysicalNumberOfRows(); i++) {
			Row row = companies.getRow(i);

			String Id = getStringValueFromCell(row.getCell(0));
			String companyName = getStringValueFromCell(row.getCell(1));
			String companyAdress = getStringValueFromCell(row.getCell(2));

			System.out.println(Id + " " + companyName + " " + companyAdress);
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

			System.out.println(id + " " + FKCompanyId + " " + name + " "
					+ surname + " " + phone + " " + email + " " + active);
		}
	}

	public void parseCompanyFacilities(HSSFSheet companyFacilities) {
		for (int i = 1; i < companyFacilities.getPhysicalNumberOfRows(); i++) {
			Row row = companyFacilities.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			String facilityNumber = getStringValueFromCell(row.getCell(1));
			String name = getStringValueFromCell(row.getCell(2));
			String FKCompany = getStringValueFromCell(row.getCell(3));
			String dateFrom = getStringValueFromCell(row.getCell(4));
			String dateTo = getStringValueFromCell(row.getCell(5));

			System.out.println(id + " " + facilityNumber + " " + name + " "
					+ FKCompany + " " + dateFrom + " " + dateTo);
		}
	}

	public void parseFacilitiesAndCompanyFacilities(
			HSSFSheet facilitiesAndCompanyFacilities) {
		for (int i = 1; i < facilitiesAndCompanyFacilities
				.getPhysicalNumberOfRows(); i++) {
			Row row = facilitiesAndCompanyFacilities.getRow(i);
			String FKFacility = getStringValueFromCell(row.getCell(0));
			String FKCompanyFacility = getStringValueFromCell(row.getCell(1));

			System.out.println(FKFacility + " " + FKCompanyFacility);
		}
	}

	public void parseRequests(HSSFSheet requests) {
		for (int i = 1; i < requests.getPhysicalNumberOfRows(); i++) {
			Row row = requests.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			String FKCompany = getStringValueFromCell(row.getCell(1));
			String FKCompanyFacility = getStringValueFromCell(row.getCell(2));
			String type = getStringValueFromCell(row.getCell(3));
			String requestMethod = getStringValueFromCell(row.getCell(4));
			String requestText = getStringValueFromCell(row.getCell(5));
			String requestDate = getStringValueFromCell(row.getCell(6));
			String solveDate = getStringValueFromCell(row.getCell(7));
			String status = getStringValueFromCell(row.getCell(8));
			String Ivertinimas = getStringValueFromCell(row.getCell(9));
			String parentRequsetId = getStringValueFromCell(row.getCell(10));

			System.out.println(id + " " + FKCompany + " " + FKCompanyFacility
					+ " " + type + " " + requestMethod + " " + requestText
					+ " " + requestDate + " " + solveDate + " " + status + " "
					+ Ivertinimas + " " + parentRequsetId);

		}
	}

	public void parseBelongsTo(HSSFSheet belongsTo) {
		for (int i = 1; i < belongsTo.getPhysicalNumberOfRows(); i++) {
			Row row = belongsTo.getRow(i);

			String id = getStringValueFromCell(row.getCell(0));
			String FKRequest = getStringValueFromCell(row.getCell(1));
			String FKAdministrator = getStringValueFromCell(row.getCell(2));
			String FKEngineer = getStringValueFromCell(row.getCell(3));
			String assignDate = getStringValueFromCell(row.getCell(4));
			String returnDate = getStringValueFromCell(row.getCell(5));
			String returnText = getStringValueFromCell(row.getCell(6));

			System.out.println(id + " " + FKRequest + " " + FKAdministrator
					+ " " + FKEngineer + " " + assignDate + " " + returnDate
					+ " " + returnText);

		}
	}

	public void loadData() {
		try {
			System.out.println("IMPORTING data");
			// facilityService = new FacilityService();
			FileInputStream file = new FileInputStream(new File(
					"C:\\import\\sgp.xls"));

			HSSFWorkbook workbook = new HSSFWorkbook(file);

			HSSFSheet facilities = workbook.getSheetAt(0);
			parseFacilities(facilities);

			HSSFSheet employees = workbook.getSheetAt(1);
			parseEmployees(employees);

			HSSFSheet companies = workbook.getSheetAt(2);
			parseCompanies(companies);

			HSSFSheet clients = workbook.getSheetAt(3);
			parseClients(clients);

			HSSFSheet companyFacilities = workbook.getSheetAt(4);
			parseCompanyFacilities(companyFacilities);

			HSSFSheet facilitiesAndCompanyFacilities = workbook.getSheetAt(5);
			parseFacilitiesAndCompanyFacilities(facilitiesAndCompanyFacilities);

			HSSFSheet requests = workbook.getSheetAt(6);
			parseRequests(requests);

			HSSFSheet belongsTo = workbook.getSheetAt(7);
			parseBelongsTo(belongsTo);

			file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DataImporter dataImporter = new DataImporter();
		dataImporter.loadData();

	}
}
