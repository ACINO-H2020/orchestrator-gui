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

public class UpdateConnectionPoint {

	private ConnectionPoint connectionPoint;

	public UpdateConnectionPoint(ConnectionPoint connectionPoint) {

		// Fetch Connection point's end points
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.getConnectionPointByName(connectionPoint.getName());// postConnectionPoint(connectionPointExtended);
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			ConnectionPointExtended cpe = (ConnectionPointExtended) returnedStatus.getInfomration();
			this.connectionPoint = cpe;
		} else {
			Alert alert1 = new Alert(AlertType.ERROR);
			alert1.setTitle("Dismi Error");
			alert1.setHeaderText("Dismi Service Connection");
			alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert1.setContentText(returnedStatus.getInfomration().toString());
			alert1.showAndWait();
		}
	}

	public void init(Parent root, CpUtils cpUtils) {
		Alert alert = new Alert(AlertType.NONE);
		ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
		alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		alert.getDialogPane().getButtonTypes().add(submit);
		alert.setTitle("Update Connection Point");
		alert.setHeaderText("End Points for '" + connectionPoint.getName() + "'");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setContent(root);
		alert.setGraphic(ImageHandler.CP_ICON);

		DismiControllerManager controllerManager = new DismiControllerManager();
		ConnectionPointController connectionPointController = controllerManager.getConnectionPointController();
		connectionPointController.setConnectionPoint(this.connectionPoint);
		connectionPointController.setSaveButtonTitle("Update End Point > > >");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != submit) {
			return;
		}
		ConnectionPoint connectionPointUpdated = connectionPointController.getConnectionPoint();
		connectionPointUpdated.setName(connectionPoint.getName());
		ConnectionPointExtended connectionPointExtended = (ConnectionPointExtended) connectionPointUpdated;
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.putConnectionPoint(connectionPoint.getName(),
				connectionPointExtended);
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			Response response = (Response) returnedStatus.getInfomration();
			if (response.getStatus() == 200){
				cpUtils.sendEvent("0:Connection point '"+connectionPoint.getName()+"' successfulyl updated !");
				
			}
			else{
				cpUtils.sendEvent("1:Problems when updating connection point '"+connectionPoint.getName()+"' !");
			}		
		} else {
			cpUtils.sendEvent("1:"+returnedStatus.getInfomration().toString());
		}
	}
}