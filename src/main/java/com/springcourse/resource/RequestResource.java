package com.springcourse.resource;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springcourse.domain.Request;
import com.springcourse.domain.RequestStage;
import com.springcourse.dto.RequestSavedto;
import com.springcourse.dto.RequestUpdatedto;
import com.springcourse.model.PageModel;
import com.springcourse.model.PageRequestModel;
import com.springcourse.service.RequestService;
import com.springcourse.service.RequestStageService;

@RestController
@RequestMapping(value = "requests")
public class RequestResource {

	@Autowired
	private RequestService requestService;

	@Autowired
	private RequestStageService requestStageService;
	
	@PostMapping
	public ResponseEntity<Request> save(@RequestBody @Valid RequestSavedto requestdto) {
		Request createdRequest = requestService.save(requestdto.transformToRequest());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
	}

	@PreAuthorize("@accessManager.isRequestOwner(#id)")
	@PutMapping("/{id}")
	public ResponseEntity<Request> update(@PathVariable(name = "id") Long id, @RequestBody @Valid RequestUpdatedto requestdto) {
		Request request = requestdto.transformToRequest();
		request.setId(id);

		Request updatedRequest = requestService.update(request);
		return ResponseEntity.ok(updatedRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Request> getById(@PathVariable(name = "id") Long id) {
		Request request = requestService.getById(id);
		return ResponseEntity.ok(request);
	}

	@GetMapping
	public ResponseEntity<PageModel<Request>> listAll(@RequestParam Map<String, String> params) {
		PageRequestModel pr = new PageRequestModel(params);
		PageModel<Request> pm = requestService.listAllOnLazyModel(pr);
		return ResponseEntity.ok(pm);
	}

	@GetMapping("/{id}/stages")
	public ResponseEntity<PageModel<RequestStage>> listAllStagesByRequestId(@PathVariable(name = "id") Long id,
			@RequestParam Map<String, String> params) {
		PageRequestModel pr = new PageRequestModel(params);
		PageModel<RequestStage> pm = requestStageService.listAllByRequestIdOnLazyModel(id, pr);

		return ResponseEntity.ok(pm);
	}

}
