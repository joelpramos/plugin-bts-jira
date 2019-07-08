/*
 * Copyright 2019 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.reportportal.extension.bugtracking.jira;

import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.dao.TestItemRepository;
import com.epam.ta.reportportal.ws.model.externalsystem.PostTicketRQ;
import com.epam.ta.reportportal.ws.model.externalsystem.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.epam.ta.reportportal.commons.Predicates.notNull;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;
import static com.epam.ta.reportportal.ws.model.ErrorType.UNABLE_POST_TICKET;

/**
 * Default implementation of {@link CreateTicketHandler}
 *
 * @author Aliaksei_Makayed
 * @author Andrei_Ramanchuk
 */
@Service
public class CreateTicketHandlerImpl implements CreateTicketHandler {

	@Autowired
	private JiraStrategy jiraStrategy;

	private final TestItemRepository testItemRepository;

	private final IntegrationRepository integrationRepository;

	@Autowired
	public CreateTicketHandlerImpl(TestItemRepository testItemRepository, IntegrationRepository integrationRepository) {
		this.testItemRepository = testItemRepository;
		this.integrationRepository = integrationRepository;
	}

	@Override
	public Ticket createIssue(PostTicketRQ postTicketRQ, Long integrationId) {
		validatePostTicketRQ(postTicketRQ);

		return jiraStrategy.submitTicket(postTicketRQ, integrationRepository.findById(integrationId).get());

	}

	/**
	 * Additional validations to {@link PostTicketRQ}.
	 *
	 * @param postTicketRQ
	 */
	private void validatePostTicketRQ(PostTicketRQ postTicketRQ) {
		if (postTicketRQ.getIsIncludeLogs() || postTicketRQ.getIsIncludeScreenshots()) {
			expect(postTicketRQ.getBackLinks(), notNull()).verify(UNABLE_POST_TICKET,
					"Test item id should be specified, when logs required in ticket description."
			);
		}
	}
}