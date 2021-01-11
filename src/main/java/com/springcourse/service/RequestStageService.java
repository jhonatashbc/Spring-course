package com.springcourse.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springcourse.domain.RequestStage;
import com.springcourse.domain.enums.RequestState;
import com.springcourse.exception.NotFoundException;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.repository.RequestRepository;
import com.springcourse.repository.RequestStageRepository;

@Service
public class RequestStageService {

	@Autowired
	private RequestStageRepository stageRepository;

	@Autowired
	private RequestRepository requestRepository;

	public RequestStage save(RequestStage stage) {
		stage.setRealizationDate(new Date());
		RequestStage createdStage = stageRepository.save(stage);

		Long requestId = stage.getRequest().getId();
		RequestState state = stage.getState();
		requestRepository.updateStatus(requestId, state);

		return createdStage;
	}

	public RequestStage getById(Long id) {
		Optional<RequestStage> result = stageRepository.findById(id);
		return result.orElseThrow(() -> new NotFoundException("There are no request stage with the id = " + id));
	}

	public List<RequestStage> listAllByRequestId(Long requestId) {
		List<RequestStage> stages = stageRepository.findAllByRequestId(requestId);
		return stages;
	}

	public PageModel<RequestStage> listAllByRequestIdOnLazyModel(Long ownerId, PageRequestModel pr) {
		Pageable pageable = pr.toSpringPageRequest();
		Page<RequestStage> page = stageRepository.findAllByRequestId(ownerId, pageable);

		PageModel<RequestStage> pm = new PageModel<>((int) page.getTotalElements(), page.getSize(),
				page.getTotalPages(), page.getContent());
		return pm;
	}
}
