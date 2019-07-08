/*
 * Copyright 2019 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.reportportal.extension.bugtracking.jira;

import com.epam.ta.reportportal.commons.ReportPortalUser;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.epam.ta.reportportal.commons.EntityUtils.normalizeId;

/**
 * Controller implementation for working with external systems.
 *
 * @author Aliaksei_Makayed
 * @author Andrei_Ramanchuk
 */
@RestController
@RequestMapping("/test")
public class BugTrackingSystemController {

	private final CreateTicketHandler createTicketHandler;

	@Autowired
	public BugTrackingSystemController(CreateTicketHandler createTicketHandler) {
		this.createTicketHandler = createTicketHandler;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String test() {
		return "OK";
	}

	@Transactional
	@PostMapping(value = "/{projectName}/{integrationId}/ticket")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Post ticket to the bts integration")
	public Ticket createIssue(@Validated @RequestBody PostTicketRQ ticketRQ, @PathVariable String projectName,
			@PathVariable Long integrationId) {
		return createTicketHandler.createIssue(ticketRQ, integrationId);
	}

	public static ReportPortalUser.ProjectDetails extractProjectDetails(ReportPortalUser user, String projectName) {
		return Optional.ofNullable(user.getProjectDetails().get(normalizeId(projectName)))
				.orElseThrow(() -> new ReportPortalException(ErrorType.ACCESS_DENIED, "Please check the list of your available projects."));
	}

}
