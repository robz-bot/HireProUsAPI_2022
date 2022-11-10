/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.service.BusinessUnitService;
import com.promantus.hireprous.service.CommonService;
import com.promantus.hireprous.service.CustomerService;
import com.promantus.hireprous.service.RecruitmentRoleService;
import com.promantus.hireprous.service.ReportJobRequestService;
import com.promantus.hireprous.util.CacheUtil;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Promantus.
 *
 */
/**
 * @author Promantus
 *
 */
@Service
public class ReportJobRequestServiceImpl implements ReportJobRequestService {

	private static final Logger logger = LoggerFactory.getLogger(ReportJobRequestServiceImpl.class);

	@Autowired
	CommonService commonService;

	@Autowired
	CustomerService customerService;

	@Autowired
	BusinessUnitService businessUnitService;

	@Autowired
	RecruitmentRoleService recruitmentRoleService;

	@Autowired
	JobRequestRepository reportJobRequestRepository;

	@Override
	public List<JobRequestDto> getJobRequestByProjectStartDateBetween(LocalDate startDate, LocalDate endDateDate)
			throws Exception {
		System.out.println("startDate " + startDate + " " + endDateDate);
		List<JobRequest> jobRequestsList = reportJobRequestRepository.findByProjectStartDateBetween(startDate,
				endDateDate);

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getJobRequestByProjectStartDateBetweenExcel(LocalDate startDate, LocalDate endDateDate)
			throws Exception {

		List<JobRequest> jobRequestsList = reportJobRequestRepository.findByProjectStartDateBetween(startDate,
				endDateDate);

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> searchJobRequestByStatus(String jobRequestStatus) throws Exception {

		List<JobRequest> jobRequestsList = reportJobRequestRepository.getJobRequestByJobReqStatus(jobRequestStatus);

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {
			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	public ByteArrayInputStream exportasExel(List<JobRequestDto> jobRequest) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Contacts");

			Row row = sheet.createRow(0);

			// Define header cell style
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Creating header cells
			Cell cell = row.createCell(0);
			cell.setCellValue("Job Request ");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(1);
			cell.setCellValue("Customer Name");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(2);
			cell.setCellValue("Type");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(3);
			cell.setCellValue("Location");
			cell.setCellStyle(headerCellStyle);

			cell = row.createCell(4);
			cell.setCellValue("Placement For");
			cell.setCellStyle(headerCellStyle);
			cell = row.createCell(5);
			cell.setCellValue("BU Name");
			cell.setCellStyle(headerCellStyle);
			cell = row.createCell(6);
			cell.setCellValue("Role Name");
			cell.setCellStyle(headerCellStyle);

			// Creating data rows for each contact
			for (int i = 0; i < jobRequest.size(); i++) {
				Row dataRow = sheet.createRow(i + 1);
				dataRow.createCell(0).setCellValue(jobRequest.get(i).getReferenceNumber());
				dataRow.createCell(1).setCellValue(jobRequest.get(i).getCustomerName());
				dataRow.createCell(2).setCellValue(jobRequest.get(i).getEmploymentType());
				dataRow.createCell(3).setCellValue(jobRequest.get(i).getLocation());
				dataRow.createCell(4).setCellValue(jobRequest.get(i).getPlacementFor());
				dataRow.createCell(5).setCellValue(jobRequest.get(i).getBuName());
				dataRow.createCell(6).setCellValue(jobRequest.get(i).getRoleName());
			}

			// Making size of column auto resize to fit with data
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		} catch (IOException ex) {
			logger.error("Error during export Excel file", ex);
			return null;
		}
	}

	public ByteArrayInputStream exportasPdf(List<JobRequestDto> jobRequest) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter.getInstance(document, out);
			document.open();

			// Add Text to PDF file ->
			Font font = FontFactory.getFont(FontFactory.COURIER, 9, BaseColor.BLACK);
			Font fontDatas = FontFactory.getFont(FontFactory.COURIER, 7, BaseColor.BLACK);
			Paragraph para = new Paragraph("Job Request Report", font);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(7);
			// Add PDF Table Header ->
			Stream.of("Job Request", "Customer Name", "Type", "Location", "Placement For", "BU Name", "Role Name")
					.forEach(headerTitle -> {
						PdfPCell header = new PdfPCell();
						Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
						header.setBackgroundColor(BaseColor.LIGHT_GRAY);
						header.setHorizontalAlignment(Element.ALIGN_CENTER);
						header.setBorderWidth(2);
						header.setPhrase(new Phrase(headerTitle, headFont));
						table.addCell(header);
					});

			for (int i = 0; i < jobRequest.size(); i++) {

				PdfPCell refNo = new PdfPCell(new Phrase(jobRequest.get(i).getReferenceNumber(), fontDatas));
				refNo.setPaddingLeft(4);
				refNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
				refNo.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(refNo);

				PdfPCell customerCell = new PdfPCell(new Phrase(jobRequest.get(i).getCustomerName(), fontDatas));
				customerCell.setPaddingLeft(4);
				customerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				customerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(customerCell);

				PdfPCell empTypeCell = new PdfPCell(new Phrase(jobRequest.get(i).getEmploymentType(), fontDatas));
				empTypeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				empTypeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				empTypeCell.setPaddingRight(4);
				table.addCell(empTypeCell);

				PdfPCell locationCell = new PdfPCell(new Phrase(jobRequest.get(i).getLocation(), fontDatas));
				locationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				locationCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				locationCell.setPaddingRight(4);
				table.addCell(locationCell);

				PdfPCell placementCell = new PdfPCell(new Phrase(jobRequest.get(i).getPlacementFor(), fontDatas));
				placementCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				placementCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				placementCell.setPaddingRight(4);
				table.addCell(placementCell);

				PdfPCell buCell = new PdfPCell(new Phrase(jobRequest.get(i).getBuName(), fontDatas));
				buCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				buCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				buCell.setPaddingRight(4);
				table.addCell(buCell);

				PdfPCell rollCell = new PdfPCell(new Phrase(jobRequest.get(i).getRoleName(), fontDatas));
				rollCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				rollCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				rollCell.setPaddingRight(4);
				table.addCell(rollCell);

			}

			document.add(table);

			document.close();
		} catch (DocumentException e) {
			logger.error(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());

	}

	/**
	 * @param jobRequest
	 * @return
	 * @throws Exception
	 */
	private JobRequestDto getJobRequestDto(final JobRequest jobRequest) throws Exception {

		JobRequestDto jobRequestDto = new JobRequestDto();

		jobRequestDto.setId(jobRequest.getId());
		jobRequestDto.setReferenceNumber(jobRequest.getReferenceNumber());

		jobRequestDto.setCustomerId(jobRequest.getCustomerId());
		jobRequestDto.setCustomerName(customerService.getCustomerNameById(jobRequest.getCustomerId()));

		jobRequestDto.setBuId(jobRequest.getBuId());
		jobRequestDto.setBuName(businessUnitService.getBusinessUnitNameById(jobRequest.getBuId()));

		jobRequestDto.setRoleId(jobRequest.getRoleId());
		jobRequestDto.setRoleName(recruitmentRoleService.getRecruitmentRoleNameById(jobRequest.getRoleId()));

		jobRequestDto.setNoOfOpenings(jobRequest.getNoOfOpenings());
		jobRequestDto.setClosedOpenings(jobRequest.getClosedOpenings());

		jobRequestDto.setProgress(jobRequest.getClosedOpenings() != 0
				? (int) Math.round(jobRequest.getClosedOpenings() * 100 / jobRequest.getNoOfOpenings())
				: 0);

		jobRequestDto.setLocation(jobRequest.getLocation());
		jobRequestDto.setPayRange(jobRequest.getPayRange());
		jobRequestDto.setEmploymentType(jobRequest.getEmploymentType());
		jobRequestDto.setContractDuration(jobRequest.getContractDuration());

		jobRequestDto.setRequesterId(jobRequest.getRequesterId());
		jobRequestDto.setRequesterName(CacheUtil.getUsersMap().get(jobRequest.getRequesterId()));

		jobRequestDto.setPlacementFor(jobRequest.getPlacementFor());
		jobRequestDto.setProjectStartDate(jobRequest.getProjectStartDate());
		jobRequestDto.setRemoteOption(jobRequest.getRemoteOption());

		jobRequestDto.setJobDescription(jobRequest.getJobDescription());
		jobRequestDto.setMandatorySkills(jobRequest.getMandatorySkills());
		jobRequestDto.setOptionalSkills(jobRequest.getOptionalSkills());

		jobRequestDto.setJobReqStatus(jobRequest.getJobReqStatus());

		jobRequestDto.setRecruiterId(jobRequest.getRecruiterId());
		if (jobRequest.getRecruiterId() != null) {
			jobRequestDto.setRecruiterName(CacheUtil.getUsersMap().get(jobRequest.getRecruiterId()));
		}

		jobRequestDto.setCreatedBy(jobRequest.getCreatedBy());
		jobRequestDto.setCreatedByName(CacheUtil.getUsersMap().get(jobRequest.getCreatedBy()));
		jobRequestDto.setCreatedDateTime(jobRequest.getCreatedDateTime());

		jobRequestDto.setUpdatedBy(jobRequest.getUpdatedBy());
		jobRequestDto.setUpdatedByName(CacheUtil.getUsersMap().get(jobRequest.getUpdatedBy()));
		jobRequestDto.setUpdatedDateTime(jobRequest.getUpdatedDateTime());

		return jobRequestDto;
	}

	@Override
	public List<JobRequestDto> getAllJobRequestsReportCount() throws Exception {

		List<JobRequest> jobRequestsList = reportJobRequestRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		int totalRecords = 0;
		int openRecords = 0;
		int progressRecords = 0;
		int holdRecords = 0;
		int closedRecords = 0;
		for (JobRequest jobRequest : jobRequestsList) {
			totalRecords++;
			String requestJobStatus = jobRequest.getJobReqStatus();
			if (requestJobStatus.equals("Open")) {

			}
			if (requestJobStatus.equals("Open")) {
				openRecords++;
			}
			if (requestJobStatus.equals("In Progress")) {
				progressRecords++;
			}
			if (requestJobStatus.equals("Hold")) {
				holdRecords++;
			}
			if (requestJobStatus.equals("Closed")) {
				closedRecords++;
			}
		}
		JobRequestDto jobRequestDto = new JobRequestDto();
		jobRequestDto.setTotalJobRequest(totalRecords);
		jobRequestDto.setOpenJobRequest(openRecords);
		jobRequestDto.setProgressJobRequest(progressRecords);
		jobRequestDto.setHoldJobRequest(holdRecords);
		jobRequestDto.setClosedJobRequest(closedRecords);
		jobRequestDtoList.add(jobRequestDto);
		return jobRequestDtoList;
	}

	@Override
	public List<JobRequestDto> getAllJobRequestsReport() throws Exception {

		List<JobRequest> jobRequestsList = reportJobRequestRepository
				.findAll(HireProUsUtil.orderByUpdatedDateTimeDesc());

		List<JobRequestDto> jobRequestDtoList = new ArrayList<JobRequestDto>();
		for (JobRequest jobRequest : jobRequestsList) {

			jobRequestDtoList.add(this.getJobRequestDto(jobRequest));
		}

		return jobRequestDtoList;
	}

	/**
	 * @param jobRequest
	 * @return
	 * @throws Exception
	 */

}
