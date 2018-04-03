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

package org.onosproject.orchestrator.dismi.client.cps;

import java.util.Optional;

import javax.ws.rs.core.Response;

import org.onosproject.orchestrator.dismi.client.controllers.DismiControllerManager;
import org.onosproject.orchestrator.dismi.client.restcli.ConnectionPointOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.utils.CpUtils;
import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointExtended;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
public class NewConnectionPoint {

	private String connectionName;
	public NewConnectionPoint(String connectionName) {
		this.connectionName = connectionName;
	}

	public void init(Parent root, CpUtils cpUtils) {
		Alert alert = new Alert(AlertType.NONE);
		ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
		alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		alert.getDialogPane().getButtonTypes().add(submit);
		alert.setTitle("New Connection Point");
		alert.setHeaderText("End Points for '"+connectionName+"'");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setContent(root);
		alert.setGraphic(ImageHandler.CP_ICON);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != submit){
		  return;  
		} 
		DismiControllerManager controllerManager = new DismiControllerManager();
		ConnectionPointController connectionPointController = controllerManager.getConnectionPointController();
		ConnectionPoint connectionPoint = connectionPointController.getConnectionPoint();
		connectionPoint.setName(connectionName);
		ConnectionPointExtended connectionPointExtended = (ConnectionPointExtended) connectionPoint;
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.postConnectionPoint(connectionPointExtended);
		if (returnedStatus.getStatus() == STATUS.SUCCESS){
			Response response = (Response)returnedStatus.getInfomration();
			
		}
		else{
			cpUtils.sendEvent("1:"+returnedStatus.getInfomration().toString());
		}	
	}	
}