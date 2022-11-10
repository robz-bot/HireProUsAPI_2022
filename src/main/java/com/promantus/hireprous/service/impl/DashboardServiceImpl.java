/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.promantus.hireprous.HireProUsConstants;
import com.promantus.hireprous.dto.BUsCountDto;
import com.promantus.hireprous.dto.JobRequestAgingCountDto;
import com.promantus.hireprous.dto.JobRequestDto;
import com.promantus.hireprous.dto.JobRequestStagesCountDto;
import com.promantus.hireprous.dto.WidgetDto;
import com.promantus.hireprous.entity.BusinessUnit;
import com.promantus.hireprous.entity.Candidate;
import com.promantus.hireprous.entity.InterviewSchedule;
import com.promantus.hireprous.entity.JobRequest;
import com.promantus.hireprous.repository.BusinessUnitRepository;
import com.promantus.hireprous.repository.CandidateRepository;
import com.promantus.hireprous.repository.InterviewScheduleRepository;
import com.promantus.hireprous.repository.JobRequestRepository;
import com.promantus.hireprous.service.DashboardService;
import com.promantus.hireprous.service.InterviewScheduleService;
import com.promantus.hireprous.service.JobRequestService;
import com.promantus.hireprous.util.HireProUsUtil;

/**
 * @author Sihab.
 *
 */
@Service
public class DashboardServiceImpl implements DashboardService {

//	private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	InterviewScheduleService interviewScheduleService;

	@Autowired
	JobRequestRepository jobRequestRepository;

	@Autowired
	BusinessUnitRepository businessUnitRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	InterviewScheduleRepository interviewScheduleRepository;

	@Override
	public List<WidgetDto> getWidgetData(String userId, String lang) throws Exception {

		List<WidgetDto> resultDtoList = new ArrayList<WidgetDto>();

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setTitle("My Work Items");
		widgetDto.setCount(7L);
		resultDtoList.add(widgetDto);

		// Job Requests.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Job Requests");
		widgetDto.setCount(jobRequestService.getMyJobRequestsCount(Long.parseLong(userId), lang));
		resultDtoList.add(widgetDto);

		// Interviews.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Interviews");
		widgetDto.setCount(interviewScheduleService.getMyInterviewsCount(Long.parseLong(userId)));
		resultDtoList.add(widgetDto);

		// Active Job Requests.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Active Job Requests");
		widgetDto.setCount(jobRequestRepository.countByJobReqStatusOrJobReqStatus(
				HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS, HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START));
		resultDtoList.add(widgetDto);

		// Total Selected Candidates.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Total Selected Candidates");
		widgetDto.setCount(candidateRepository.countByRecStatus(HireProUsConstants.REC_STATUS_SELECTED));
		resultDtoList.add(widgetDto);

		// Total On-boarded Candidates.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Total On-boarded Candidates");
		widgetDto.setCount(candidateRepository.countByRecStatus(HireProUsConstants.REC_STATUS_ONBOARDED));
		resultDtoList.add(widgetDto);

		return resultDtoList;
	}

	@Override
	public List<WidgetDto> getWidgetDataForVendor(String vendorId, String lang) throws Exception {

		List<WidgetDto> resultDtoList = new ArrayList<WidgetDto>();

		WidgetDto widgetDto = new WidgetDto();
		widgetDto.setTitle("My Work Items");
		widgetDto.setCount(7L);
		resultDtoList.add(widgetDto);

		// Job Requests.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Job Requests");
		widgetDto.setCount(jobRequestService.getMyJobRequestsCountForVendor(Long.parseLong(vendorId), lang));
		resultDtoList.add(widgetDto);

		// Interviews.
//		widgetDto = new WidgetDto();
//		widgetDto.setTitle("Interviews");
//		widgetDto.setCount(interviewScheduleService.getMyInterviewsCount(Long.parseLong(userId)));
//		resultDtoList.add(widgetDto);

		// Active Job Requests.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Active Job Requests");
		widgetDto.setCount(jobRequestService.getMyJobRequestsCountForVendor(Long.parseLong(vendorId), lang));
		resultDtoList.add(widgetDto);

		// Total Selected Candidates.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Total Selected Candidates");
		widgetDto.setCount(candidateRepository.countByRecStatusAndVendorId(HireProUsConstants.REC_STATUS_SELECTED,
				Long.parseLong(vendorId)));
		resultDtoList.add(widgetDto);

		// Total On-boarded Candidates.
		widgetDto = new WidgetDto();
		widgetDto.setTitle("Total On-boarded Candidates");
		widgetDto.setCount(candidateRepository.countByRecStatusAndVendorId(HireProUsConstants.REC_STATUS_ONBOARDED,
				Long.parseLong(vendorId)));
		resultDtoList.add(widgetDto);

		return resultDtoList;
	}

	@Override
	public List<BUsCountDto> getBUsAndJobRequestCount(String lang) {

		List<BUsCountDto> resultDtoList = new ArrayList<BUsCountDto>();

		List<BusinessUnit> buList = businessUnitRepository.findAll();
		for (BusinessUnit businessUnit : buList) {

			BUsCountDto buCountDto = new BUsCountDto();
			buCountDto.setBuId(businessUnit.getId());
			buCountDto.setBuName(businessUnit.getBusinessUnitName());

			final List<Criteria> criteriaList = new ArrayList<>();
			criteriaList.add(Criteria.where("buId").is(businessUnit.getId()));
			criteriaList.add(Criteria.where("jobReqStatus").in(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
					HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START));
			List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
			if (!criteriaList.isEmpty()) {
				Query searchQuery = new Query();
				searchQuery.addCriteria(
						new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
				jobRequestsList = mongoTemplate.find(searchQuery, JobRequest.class);
			}

			buCountDto.setCount((long) jobRequestsList.size());

			long open = 0;
			long inprogress = 0;
			List<String> jrNumbers = new ArrayList<String>();
			for (JobRequest jobRequest : jobRequestsList) {

				if (jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS)) {
					inprogress += 1;
				} else if (jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START)) {
					open += 1;
				}

				jrNumbers.add(jobRequest.getReferenceNumber());
			}

			buCountDto.setInprogress(inprogress);
			buCountDto.setOpen(open);

			// Select Candidates.
			List<Candidate> candidatesList = candidateRepository.findByJrNumberIn(jrNumbers,
					HireProUsUtil.orderByUpdatedDateTimeDesc());

			buCountDto.setTotalTagged((long) candidatesList.size());

			long uploaded = 0;
			long shortlisted = 0;
			long holded = 0;
			long rejected = 0;
			long selected = 0;
			long onboarded = 0;
			for (Candidate candidate : candidatesList) {

				String recStatus = candidate.getRecStatus();
				if (recStatus.equals(HireProUsConstants.REC_STATUS_UPLOADED)) {
					uploaded += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_SHORTLISTED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_HR4)) {
					shortlisted += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_HR4)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_BU)) {
					holded += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_HR4)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_BU)) {
					rejected += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_SELECTED)) {
					selected += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_ONBOARDED)) {
					onboarded += 1;
				}
			}

			buCountDto.setUploaded(uploaded);
			buCountDto.setShortlisted(shortlisted);
			buCountDto.setHold(holded);
			buCountDto.setRejected(rejected);
			buCountDto.setSelected(selected);
			buCountDto.setOnboarded(onboarded);

			// add
			resultDtoList.add(buCountDto);
		}

		// Send result by count wise reverse order.
		Comparator<BUsCountDto> countSorter = (a, b) -> a.getCount().compareTo(b.getCount());
		Collections.sort(resultDtoList, countSorter.reversed());
		for(int i=0;i<resultDtoList.size(); i++)
		{
         System.out.println(resultDtoList.get(i).getCount());
		}

		return resultDtoList;
	}

	@Override
	public List<BUsCountDto> getBUsAndJobRequestCountForVendor(String vendorId, String lang) {

		List<BUsCountDto> resultDtoList = new ArrayList<BUsCountDto>();

		List<BusinessUnit> buList = businessUnitRepository.findAll();
		for (BusinessUnit businessUnit : buList) {

			BUsCountDto buCountDto = new BUsCountDto();
		//	System.out.println(businessUnit.getId());
			buCountDto.setBuId(businessUnit.getId());
			buCountDto.setBuName(businessUnit.getBusinessUnitName());

			final List<Criteria> criteriaList = new ArrayList<>();
			criteriaList.add(Criteria.where("buId").is(businessUnit.getId()));
			/*criteriaList.add(Criteria.where("vendorId").is(Long.parseLong(vendorId)));
			criteriaList.add(Criteria.where("recStatus").in(HireProUsConstants.REC_STATUS_SELECTED,
					HireProUsConstants.REC_STATUS_ONBOARDED));*/
			criteriaList.add(Criteria.where("jobReqStatus").in(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
					HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START));

			List<JobRequest> jobRequestsList = new ArrayList<JobRequest>();
			if (!criteriaList.isEmpty()) {
				Query searchQuery = new Query();
				searchQuery.addCriteria(
						new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
				jobRequestsList = mongoTemplate.find(searchQuery,JobRequest.class);
			
			}

			buCountDto.setCount((long) jobRequestsList.size());

			long open = 0;
			long inprogress = 0;
			List<String> jrNumber = new ArrayList<String>();
			for (JobRequest jobRequest : jobRequestsList) {
				
				if (jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS)) {
					inprogress += 1;
				} else if (jobRequest.getJobReqStatus().equals(HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START)) {
					open += 1;
				}

				jrNumber.add(jobRequest.getReferenceNumber());
			}
			buCountDto.setInprogress(inprogress);
			buCountDto.setOpen(open);
			List<JobRequest> jobRequest=jobRequestRepository.findByBuId(businessUnit.getId());
			List<String> jrNumber2=new ArrayList<String>();
			for(JobRequest jobrequest:jobRequest)
			{
				jrNumber2.add(jobrequest.getReferenceNumber());
			}

			// Select Candidates.
			//String recstatus="26";
			List<Candidate> candidatesList = candidateRepository.findByJrNumberInAndVendorId(jrNumber2,
					Long.parseLong(vendorId));
				
			long uploaded = 0;
			long shortlisted = 0;
			long holded = 0;
			long rejected = 0;
			long selected = 0;
			long onboarded = 0;
			long dropped=0;
	
			for (Candidate candidate : candidatesList) {
				String recStatus = candidate.getRecStatus();
				if (recStatus.equals(HireProUsConstants.REC_STATUS_UPLOADED)) {
					uploaded += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_SHORTLISTED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_HR4)) {
					shortlisted += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_HR4)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_BU)) {
					holded += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_0)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R1)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_R2)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_CR3)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_HR4)
						|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_BU)) {
					rejected += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_SELECTED)) {
					selected += 1;
				}
				if (recStatus.equals(HireProUsConstants.REC_STATUS_ONBOARDED)) {
					onboarded += 1;
				}

				if(recStatus.equals(HireProUsConstants.REC_STATUS_DROPPED))
				{
					dropped+=1;
				//	candidatesList.remove(index);
				}
			}
			buCountDto.setTotalTagged((long) candidatesList.size()-dropped);


			buCountDto.setUploaded(uploaded);
			buCountDto.setShortlisted(shortlisted);
			buCountDto.setHold(holded);
			buCountDto.setRejected(rejected);
			buCountDto.setSelected(selected);
			buCountDto.setOnboarded(onboarded);

			// add
			resultDtoList.add(buCountDto);
		}

		// Send result by count wise reverse order.
		Comparator<BUsCountDto> countSorter = (a, b) -> a.getCount().compareTo(b.getCount());
		Collections.sort(resultDtoList, countSorter.reversed());
	
		return resultDtoList;
	}

	@Override
	public JobRequestAgingCountDto getJobRequestAgingCount(String lang) {

		List<JobRequest> jobRequestList = jobRequestRepository.getJobRequestByJobReqStatusIn(
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getJobRequestAgingCount(jobRequestList);
	}

	@Override
	public JobRequestAgingCountDto getJobRequestAgingCountForVendor(String vendorId, String lang) {

		List<JobRequest> jobRequestList = jobRequestRepository.getJobRequestByJobReqStatusIn(
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
						HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getJobRequestAgingCount(jobRequestList);
	}

	@Override
	public JobRequestAgingCountDto getJobRequestAgingCountByBuId(String buId, String lan) {

		List<JobRequest> jobRequestList = jobRequestRepository.getJobRequestByBuIdAndJobReqStatusIn(
				Long.parseLong(buId),
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getJobRequestAgingCount(jobRequestList);
	}

	@Override
	public JobRequestAgingCountDto getJobRequestAgingCountByBuIdForVendor(String buId, String vendorId, String lan) {

		List<JobRequest> jobRequestList = jobRequestRepository.getJobRequestByBuIdAndJobReqStatusInAndVendorId(
				Long.parseLong(buId),
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				Long.parseLong(vendorId), HireProUsUtil.orderByUpdatedDateTimeDesc());

		return this.getJobRequestAgingCount(jobRequestList);
	}

	private JobRequestAgingCountDto getJobRequestAgingCount(List<JobRequest> jobRequestList) {

		long totalCount = 0;
		long age10Days = 0;
		long age20Days = 0;
		long age30Days = 0;
		long age60Days = 0;
		long age90Days = 0;
		long moreThan90Days = 0;
		for (JobRequest jobRequest : jobRequestList) {

			totalCount += 1;
			long diffDays = HireProUsUtil.getDiffDays(jobRequest.getCreatedDateTime(), LocalDateTime.now());
			if (diffDays > 90) {
				moreThan90Days += 1;
			} else if (diffDays > 60) {
				age90Days += 1;
			} else if (diffDays > 30) {
				age60Days += 1;
			} else if (diffDays > 20) {
				age30Days += 1;
			} else if (diffDays > 10) {
				age20Days += 1;
			} else {
				age10Days += 1;
			}
		}

		JobRequestAgingCountDto resultDto = new JobRequestAgingCountDto();

		resultDto.setAge10Days(age10Days);
		resultDto.setAge20Days(age20Days);
		resultDto.setAge30Days(age30Days);
		resultDto.setAge60Days(age60Days);
		resultDto.setAge90Days(age90Days);
		resultDto.setMoreThan90Days(moreThan90Days);

		resultDto.setTotalCount(totalCount);
		return resultDto;
	}

	@Override
	public JobRequestStagesCountDto getAllJobRequestStagesCount(String lang) {

		return getJobRequestStagesCountData(jobRequestRepository.getJobRequestByJobReqStatusIn(
				Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
						HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
				HireProUsUtil.orderByUpdatedDateTimeDesc()));
	}

	@Override
	public JobRequestStagesCountDto getAllJobRequestStagesCountByBuId(String buId, String lang) {

		return getJobRequestStagesCountData(
				jobRequestRepository.getJobRequestByBuIdAndJobReqStatusIn(Long.parseLong(buId),
						Arrays.asList(HireProUsConstants.JOB_REQUEST_STATUS_IN_PROGRESS,
								HireProUsConstants.JOB_REQUEST_STATUS_YET_TO_START),
						HireProUsUtil.orderByUpdatedDateTimeDesc()));
	}

	/**
	 * @param jobRequestList
	 * @return
	 */
	private JobRequestStagesCountDto getJobRequestStagesCountData(List<JobRequest> jobRequestList) {

		long contract = 0;
		long fullTime = 0;
		List<String> jrNumbers = new ArrayList<String>();
		for (JobRequest jobRequest : jobRequestList) {

			if (jobRequest.getEmploymentType().equals(HireProUsConstants.EMPLOYMENT_TYPE_CONTRACT)) {
				contract += 1;
			}
			if (jobRequest.getEmploymentType().equals(HireProUsConstants.EMPLOYMENT_TYPE_FULLTIME)) {
				fullTime += 1;
			}

			jrNumbers.add(jobRequest.getReferenceNumber());
		}

		final JobRequestStagesCountDto resultDto = new JobRequestStagesCountDto();
		resultDto.setTotalRequest((long) jobRequestList.size());
		resultDto.setContract(contract);
		resultDto.setFullTime(fullTime);

		long tagged = 0;
		long shortlisted = 0;
		long ir1Cleared = 0;
		long ir2Cleared = 0;
		long crCleared = 0;
		long hrCleared = 0;
		long selected = 0;
		long onboarded = 0;
		List<InterviewSchedule> interviewScheduleList = interviewScheduleRepository.findByJrNumberIn(jrNumbers);
		for (InterviewSchedule interviewSchedule : interviewScheduleList) {

			String recStatus = interviewSchedule.getRecStatus();
			if (recStatus.equals(HireProUsConstants.REC_STATUS_UPLOADED)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_SHORTLISTED_0)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_HOLDED_0)
					|| recStatus.equals(HireProUsConstants.REC_STATUS_REJECTED_0)) {
				tagged += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_SHORTLISTED_0)) {
				shortlisted += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R1)) {
				ir1Cleared += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_R2)) {
				ir2Cleared += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_CR3)) {
				crCleared += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_PASSED_HR4)) {
				hrCleared += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_SELECTED)) {
				selected += 1;
			}
			if (recStatus.equals(HireProUsConstants.REC_STATUS_ONBOARDED)) {
				onboarded += 1;
			}
		}

		resultDto.setResumesTagged(tagged);
		resultDto.setResumesShortlisted(shortlisted);
		resultDto.setIr1Cleared(ir1Cleared);
		resultDto.setIr2Cleared(ir2Cleared);
		resultDto.setCrCleared(crCleared);
		resultDto.setHrCleared(hrCleared);
		resultDto.setSelected(selected);
		resultDto.setOnboarded(onboarded);

		return resultDto;
	}

	@Override
	public List<JobRequestDto> getLatestJobRequests(String lang) throws Exception {

		return jobRequestService.getLatestJobRequests(lang);
	}

	@Override
	public List<JobRequestDto> getLatestJobRequestsForVendor(String vendorId, String lang) throws Exception {

		return jobRequestService.getLatestJobRequestsForVendor(vendorId, lang);
	}
}
