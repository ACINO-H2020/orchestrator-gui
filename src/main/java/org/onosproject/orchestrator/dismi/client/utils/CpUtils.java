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

package org.onosproject.orchestrator.dismi.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.onosproject.orchestrator.dismi.client.DismiClientMain;
import org.onosproject.orchestrator.dismi.client.controllers.DismiControllerManager;
import org.onosproject.orchestrator.dismi.client.cps.DeleteConnectinPoint;
import org.onosproject.orchestrator.dismi.client.cps.NewConnectionPoint;
import org.onosproject.orchestrator.dismi.client.cps.UpdateConnectionPoint;
import org.onosproject.orchestrator.dismi.client.cps.ViewConnectionPoint;
import org.onosproject.orchestrator.dismi.client.restcli.ConnectionPointOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointExtended;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

public class CpUtils extends GenericStatusObserver {

	public void updateCpGui() {

		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.getAllConnectionPoints();//postConnectionPoint(connectionPointExtended);

		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			ObservableList<String> cps = FXCollections.observableArrayList();
			List<ConnectionPoint> connectionPoints = (List<ConnectionPoint>) returnedStatus.getInfomration();
			for (int i = 0; i < connectionPoints.size(); i++) {
				cps.add(connectionPoints.get(i).getName());
			}
			if (cps.size() == 0) {
				cps.add("Empty List");
			}
			ChoiceDialog<String> dialog = new ChoiceDialog<String>(cps.get(0), cps);
			dialog.setTitle("Dismi-connection point");
			dialog.setHeaderText("Update Connection Point");
			dialog.setContentText("Select connection point::");
			dialog.setGraphic(ImageHandler.CP_ICON);
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(name -> displayUpdateUI(name));
		} else {
			sendEvent("1:"+returnedStatus.getInfomration().toString());
		}
	}

	private void displayUpdateUI(String name) {
		ConnectionPointExtended cpName = new ConnectionPointExtended();
		cpName.setName(name);
		if (cpName == null || cpName.getName().equalsIgnoreCase("Empty List")) {
			return;
		}
		UpdateConnectionPoint updateConnectionPoint = new UpdateConnectionPoint(cpName);
		Parent root;
		try {
			DismiControllerManager dismiControllerManager = new DismiControllerManager();
			updateConnectionPoint.init(dismiControllerManager.loadCPWindow(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void newCpGui() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Dismi Input");
		dialog.setHeaderText("New Connection Point");
		dialog.setContentText("Connection Point Name:");
		dialog.setGraphic(ImageHandler.CP_ICON);
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> displayNewUI(name));
	}

	private void displayNewUI(String value) {
		NewConnectionPoint connectionPointNew = null;
		connectionPointNew = new NewConnectionPoint(value);
		Parent root;
		try {
			DismiControllerManager dismiControllerManager = new DismiControllerManager();
			connectionPointNew.init(dismiControllerManager.loadCPWindow(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteCpGui() {
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.getAllConnectionPoints();// postConnectionPoint(connectionPointExtended);

		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			ObservableList<String> cps = FXCollections.observableArrayList();
			List<ConnectionPoint> connectionPoints = (List<ConnectionPoint>) returnedStatus.getInfomration();
			for (int i = 0; i < connectionPoints.size(); i++) {
				cps.add(connectionPoints.get(i).getName());
			}
			DeleteConnectinPoint deleteConnectinPoint = new DeleteConnectinPoint(cps);
			deleteConnectinPoint.init(this);
		} else {
			sendEvent("1:"+returnedStatus.getInfomration().toString());
		}
	}

	public void viewCpGui() {
		ObservableList<ConnectionPoint> cps = FXCollections.observableArrayList();
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ReturnedStatus returnedStatus = connectionPointOperations.getAllConnectionPoints();// postConnectionPoint(connectionPointExtended);
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			List<ConnectionPoint> connectionPoints = (List<ConnectionPoint>) returnedStatus.getInfomration();
			for (int i = 0; i < connectionPoints.size(); i++) {
				ConnectionPointExtended ob = (ConnectionPointExtended) connectionPoints.get(i);
				cps.add(ob);
			}
			ViewConnectionPoint connectionPointView = new ViewConnectionPoint(cps);
			connectionPointView.init(this);
		} else {
			sendEvent("1:"+returnedStatus.getInfomration().toString());
		}
	}

	public void loadCPGui() {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(DismiClientMain.stageMain);
		if (file != null) {
			openFile(file);
		}
	}

	private void openFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			String str = new String(data, "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			boolean loadingStatus = false;
			String strCPError= "";
			ConnectionPointExtendedList cpsStr = mapper.readValue(str, ConnectionPointExtendedList.class);
			for (ConnectionPoint cp : cpsStr) {
				ConnectionPointExtended extended = (ConnectionPointExtended) cp;
				String basicPath = "connectionpoint";
				ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
				ReturnedStatus returnedStatus = connectionPointOperations.postConnectionPoint(extended);
				if (returnedStatus.getStatus() == STATUS.SUCCESS) {
					Response response = (Response) returnedStatus.getInfomration();
					
					if (response.getStatus() == 200) {
						loadingStatus = true;
					} else if(response.getStatus() == 500){
						loadingStatus = true;
						strCPError = strCPError+"\n"+" * "+cp.getName();
						
					}else {
						sendEvent("1:Problems when loading connection points !");
						return;
					}
				} else {
					sendEvent("1:"+returnedStatus.getInfomration().toString());
					return;
				}
			}
			if (strCPError.length() != 0){
				sendEvent("1:Problems when loading folowing connection points. Either they already exist or some of its value(s) is(are) not specified !");
				return;
			}
			
			if (loadingStatus) {
				sendEvent("0:Connection points successfully loaded !");
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
