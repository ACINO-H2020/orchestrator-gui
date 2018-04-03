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
import org.onosproject.orchestrator.dismi.primitives.Service;
import org.onosproject.orchestrator.dismi.primitives.extended.IntentList;
import org.onosproject.orchestrator.dismi.primitives.extended.ServiceList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public class ServicesOperations extends DismitestBase {

	private static String name = "ServicesOperations";
	private final Logger log = Logger.getLogger(name);

	public ServicesOperations(String basicPath) {
		super(basicPath);
	}

	public ReturnedStatus serviceGet() {
		log.debug("Sending GET request to fetch services !");
		ServiceList services = new ServiceList();
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath());
		Response response = null;
		try {
			response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		} catch (Exception exp) {
			log.error("Problems when creating connection with Dismi services !");
			log.error(exp.getMessage());
			return new ReturnedStatus(STATUS.FAIL, exp);
		}
		try {
			String body = response.readEntity(String.class);
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				services = objectMapper.readValue(body, ServiceList.class);				
				return new ReturnedStatus(STATUS.SUCCESS, services);
			} catch (Exception e) {
				log.error("Problems when parsing reterived service information. Method: 'serviceGet()' !");
				log.error("*Exception:"+ e.getMessage());
				ApiResponseMessage apiResponseMessage = objectMapper.readValue(body, ApiResponseMessage.class);
				return new ReturnedStatus(STATUS.FAIL, apiResponseMessage);
			}

		} catch (Exception exp) {
			log.error("Problems when parsing received entities. 'serviceGet()' !");
			return new ReturnedStatus(STATUS.FAIL, exp);
		}
	}

	public ReturnedStatus fetchAlternativeSolutions(String intentId) {
		log.info("Sending GET request to fetch AS !");
		Service serviceWithAS = new Service();
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath()+"/iid="+intentId);
		Response response = null;
		try {
			response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		} catch (Exception exp) {
			log.error("Problems when creating connection with Dismi services 'fetchAlternativeSolutions()'!");
			log.error(exp.getCause());
			return new ReturnedStatus(STATUS.FAIL, exp);
		}
		try {
			String body = response.readEntity(String.class);			
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				IntentList intentList = objectMapper.readValue(body, IntentList.class);
				log.info("No. of services reterived : " + serviceWithAS.getIntents().size());
				return new ReturnedStatus(STATUS.SUCCESS, intentList);
			} catch (Exception e) {
				log.error("Problems when parsing reterived AS information. Method: 'fetchAlternativeSolutions()' !");
				log.error("*Exception", e);
				ApiResponseMessage apiResponseMessage = objectMapper.readValue(body, ApiResponseMessage.class);
				return new ReturnedStatus(STATUS.FAIL, apiResponseMessage);
			}

		} catch (Exception exp) {
			log.error("Problems when parsing received entities. 'fetchAlternativeSolutions()' !");
			log.error("**Exception", exp);
			return new ReturnedStatus(STATUS.FAIL, exp);
		}
	}

	public ReturnedStatus servicePost(Service service) {
		log.info("Sending POST request to create a service !");
		log.info("Connecting to : "+super.getBaiscURL()+""+super.getBaiscPath());
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath());
		ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(service);
			log.info(str);
			Response response = null;
			try {
				response = target.request(MediaType.APPLICATION_JSON_TYPE)
						.post(Entity.entity(str, MediaType.APPLICATION_JSON_TYPE));
			} catch (Exception exp) {
				log.error("Problems when creating connection with Dismi services. Method: servicePost(service)!");
				log.error(exp.getCause());
				return new ReturnedStatus(STATUS.FAIL, exp);
			}
			return new ReturnedStatus(STATUS.SUCCESS, response);
		} catch (JsonProcessingException e) {
			log.error("Problems when parsing reterived service information. Method: servicePost(service)!");
			log.error("Exception", e);
			return new ReturnedStatus(STATUS.FAIL, "Problems when submitting new\nservice !");
		}
	}

	public ReturnedStatus serviceServiceIdDelete(String serviceId) {
		log.info("Sending DELETE request to delete a service !");
		Client client = ClientBuilder.newClient();
		Service service = null;
		if (null == serviceId && serviceId.length() <= 0) {
			return new ReturnedStatus(STATUS.FAIL, "Please specify service id !");
		}
		WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath() + "/" + serviceId);
		try {
			Response response = null;
			try {
				response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
				return new ReturnedStatus(STATUS.SUCCESS, response);
			} catch (Exception exp) {
				log.error(
						"Problems when creating connection with Dismi services. Method: serviceServiceIdDelete(serviceId) !");
				log.error(exp.getCause());
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Dismi Error");
				alert.setHeaderText("Dismi Service Connection");
				alert.setContentText("Problems when creating connection with\nDismi services !");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.showAndWait();
				return new ReturnedStatus(STATUS.FAIL, exp);
			}

		} catch (Exception e) {
			log.error(
					"Problems when parsing reterived service information. Method: serviceServiceIdDelete(serviceId)!");
			log.error("Exception", e);
			return new ReturnedStatus(STATUS.FAIL, "Problems when deleting service !\n" + e.getMessage());
		}
	}
	//----------------------------------------------------
	public int serviceUpdateIntentPut( String intentId, String updatedintent_id) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(super.getBaiscURL())
				.path(super.getBaiscPath() + "/" + intentId+"="+updatedintent_id+"");
		ObjectMapper mapper = new ObjectMapper();
		try {
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.put(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public int serviceUpdateIntentDelete(String serviceId, String intentId) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(super.getBaiscURL())
				.path(super.getBaiscPath() + "/" +serviceId+"/"+intentId);
		ObjectMapper mapper = new ObjectMapper();
		try {
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.delete();
			return response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
