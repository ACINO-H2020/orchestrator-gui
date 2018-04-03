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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.onosproject.orchestrator.dismi.api.ApiResponseMessage;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.EndPoint;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointExtended;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointList;
import org.onosproject.orchestrator.dismi.primitives.extended.EndPointList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConnectionPointOperations extends DismitestBase {
	
	private static String name = "ConnectionPointOperations";
	private final Logger log = Logger.getLogger(name);
	
	public ConnectionPointOperations() {
    	
    }
    public ConnectionPointOperations(String basicPath) {
    	super(basicPath);
    }

    //---------------UPDATED FOR GUI---------------------------------------
    public ReturnedStatus getAllConnectionPoints() {
    	log.info("Sending request to fetch connection points !");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(super.getBaiscURL())
                .path(super.getBaiscPath());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        try {
            String body = response.readEntity(String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
            	ArrayList<ConnectionPointExtended > connectionPointExtendeds = new ArrayList<ConnectionPointExtended>();
            	ConnectionPointList cpsStr = objectMapper.readValue(body,ConnectionPointList.class);
            	log.info("No. of Connection points received: "+cpsStr.size());
            	for(int i=0; i< cpsStr.size(); i++){
            		String temp = cpsStr.get(i).toString();
            		temp = temp.substring(temp.indexOf("=")+1, temp.length()-1);
            		ConnectionPointExtended connectionPointExtended = new ConnectionPointExtended(cpsStr.get(i).getName());
            		connectionPointExtendeds.add(connectionPointExtended); 
            	}
            	return new ReturnedStatus(STATUS.SUCCESS, connectionPointExtendeds );
            } catch (Exception e) {
                ApiResponseMessage apiResponseMessage = objectMapper
                        .readValue(body, ApiResponseMessage.class);       
                log.error("Problems when fetching connection points !");
                log.error("Exception", e);
                return new ReturnedStatus(STATUS.FAIL, apiResponseMessage.getErrorCode()+"->"+apiResponseMessage.getMessage());
            }

        } catch (Exception exp) {
        	log.error("*Problems when fetching connection points !");
            log.error("*Exception", exp);
            return new ReturnedStatus(STATUS.FAIL, "*Problems when fetching\nconnection points !");
        }
    }
    
    public ReturnedStatus getConnectionPointByName(String name) {
    	log.info("Sending request to fetch connection point by name !");
        Client client = ClientBuilder.newClient();
        ConnectionPointExtended cps = null;
        WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath() + "/" + name);
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        try {
            String body = response.readEntity(String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            try {

            	EndPointList endPointList = objectMapper.readValue(body, EndPointList.class);
            	Set<EndPoint> endPoints = new HashSet<EndPoint>();
                for (EndPoint point : endPointList){
                	endPoints.add(point);
                }
            	cps = new ConnectionPointExtended();
                cps.setName(name);
                cps.setEndpoints(endPoints);
                return new ReturnedStatus(STATUS.SUCCESS,cps);
                
            } catch (Exception e) {
                ApiResponseMessage apiResponseMessage = objectMapper
                        .readValue(body, ApiResponseMessage.class);
                log.error("Problems when fetching connection points by name !");
                log.error("Exception", e);
                return new ReturnedStatus(STATUS.FAIL, apiResponseMessage.getErrorCode()+"->"+apiResponseMessage.getMessage());
            }

        } catch (Exception exp) {
        	log.error("*Problems when fetching connection points by name !");
            log.error("*Exception", exp);
        	return new ReturnedStatus(STATUS.FAIL, "Problems when fetching\nconnection points !");
        }
    }

    
    public ReturnedStatus postConnectionPoint(ConnectionPointExtended connectionPoint ) {
    	log.info("Sending POST request to create a connection point !");        
        Client client = ClientBuilder.newClient();
        ConnectionPoint cps = null;
        WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath());

        if (connectionPoint == null || connectionPoint.getEndpoints().size() ==0) {
        	log.error("New connection point name or end-points are missing !");            
            return new ReturnedStatus(STATUS.FAIL,"New connection point name or\nend-points are missing !");
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String str = mapper.writeValueAsString(connectionPoint);
             Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(str, MediaType.APPLICATION_JSON_TYPE));
             return new ReturnedStatus(STATUS.SUCCESS,response);
        } catch (JsonProcessingException e) {
        	log.error("*Problems when creating new connection point !");
            log.error("*Exception", e);       
			return new ReturnedStatus(STATUS.FAIL,"Problems when creating new\nconnection point !");
        }        
        
    }
    
    public ReturnedStatus putConnectionPoint(String oldname, ConnectionPointExtended connectionPoint) {
    	log.info("Sending PUT request to update a connection point !");
        Client client = ClientBuilder.newClient();
        ConnectionPoint cps = null;
        if (null == oldname && oldname.length() <= 0) {
        	log.error("Connection point name is not specified !");
            return new ReturnedStatus(STATUS.FAIL,"Please specify current\nconnection point name !");
        }
        WebTarget target = client.target(super.getBaiscURL()).path(super.getBaiscPath() + "/" + oldname);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String str = mapper.writeValueAsString(connectionPoint);
            Response response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(str, MediaType.APPLICATION_JSON_TYPE));
            return new ReturnedStatus(STATUS.SUCCESS,response );
            
        } catch (JsonProcessingException e) {
        	log.error("*Problems when updating a connection point !");
            log.error("*Exception", e);
        	return new ReturnedStatus(STATUS.FAIL,"Problems when updating\nconnection point !");
        }        
    }
    public ReturnedStatus deleteConnectionPoint(ConnectionPoint name) {
    	log.info("Sending DELETE request to delete a connection point !");
        Client client = ClientBuilder.newClient();
        ConnectionPoint cps = null;
        if (null == name && name.getName().length() <= 0) {
        	log.error("*Connection point name is not specified !");
        	return new ReturnedStatus(STATUS.FAIL,"Please specify current\nconnection point name !");            
        }
        WebTarget target = client.target(super.getBaiscURL())
                .path(super.getBaiscPath() + "/" + name.getName());
        try {

            Response response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
            return new ReturnedStatus(STATUS.SUCCESS,response);            

        } catch (Exception e) {
        	log.error("*Problems when deleting a connection point !");
            log.error("*Exception", e);
        	return new ReturnedStatus(STATUS.FAIL,"Problems when updating\nconnection point !");
        }
    }
    //------------------------------------------------------    
}
