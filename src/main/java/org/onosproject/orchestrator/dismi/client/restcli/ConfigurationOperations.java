/*
 * Copyright (c) 2018 ACINO Consortium
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

package org.onosproject.orchestrator.dismi.client.restcli;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.onosproject.orchestrator.dismi.api.ApiResponseMessage;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.primitives.DismiConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationOperations extends DismitestBase {

	private static String name = "ConfigurationOperations";
	private final Logger log = Logger.getLogger(name);

	public ConfigurationOperations() {

	}

	public ConfigurationOperations(String basicPath) {
		super(basicPath);
	}

	// ---------------UPDATED FOR GUI---------------------------------------
	public ReturnedStatus getConfigurations() {
		log.info("Sending request to fetch current configurations !");
		Client client = ClientBuilder.newClient();
		try {
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath());
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		
			String body = response.readEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				DismiConfiguration dismiConfiguration = objectMapper.readValue(body, DismiConfiguration.class);
				return new ReturnedStatus(STATUS.SUCCESS, dismiConfiguration);				
			} catch (Exception e) {
				ApiResponseMessage apiResponseMessage = objectMapper.readValue(body, ApiResponseMessage.class);
				log.error("Problems when fetching current configurations !");
				log.error("Exception", e);
				return new ReturnedStatus(STATUS.FAIL, "Problems when fetching configurations.  Please check audit log for more information !");
			}
		} catch (Exception exp) {
			log.error("*Problems when fetching configurations !");
			log.debug("*Exception", exp);
			return new ReturnedStatus(STATUS.FAIL, "Problems when fetching configurations.  Please check audit log for more information !");			
		}
	}

	// ------------------------------------------------------
	public ReturnedStatus putDismiConfigurations(DismiConfiguration configuration) {
		try {
			log.info("Sending request to update current configurations !");
			Client client = ClientBuilder.newClient();
			ObjectMapper objectMapper = new ObjectMapper();

			String dismiJson = objectMapper.writeValueAsString(configuration);
			WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath());
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.put(Entity.entity(dismiJson, MediaType.APPLICATION_JSON_TYPE));
			if (response.getStatus() == 200) {
				return new ReturnedStatus(STATUS.SUCCESS, "Configuration options successfully updated !");
			} else {
				return new ReturnedStatus(STATUS.FAIL, "Problems when setting new configuration options !");
			}
		} catch (Exception exp) {
			log.error("*Problems when setting configurations !");
			log.error("*Exception", exp);
			return new ReturnedStatus(STATUS.FAIL, "Problems when setting new configuration options.  Please check audit log for more information !");
		}
	}

	// ------------------------------------------------------
	public ReturnedStatus setDefaultConfigurations() {
		log.info("Sending request to setting default configurations !");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath() + "/default");
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.put(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		
		if (response.getStatus() == 200) {
			return new ReturnedStatus(STATUS.SUCCESS, "Default configuration options successfully updated !");
		} else {
			return new ReturnedStatus(STATUS.FAIL, "Problems when setting new default configuration options !");
		}
	}
}
