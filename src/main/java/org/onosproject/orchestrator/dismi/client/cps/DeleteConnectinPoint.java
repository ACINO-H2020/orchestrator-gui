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

import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.onosproject.orchestrator.dismi.client.restcli.ConnectionPointOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.utils.CpUtils;
import org.onosproject.orchestrator.dismi.client.utils.GenericStatusObserver;
import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;

import javafx.scene.control.ChoiceDialog;

public class DeleteConnectinPoint extends GenericStatusObserver {
	
	private List<String> cps;
	public DeleteConnectinPoint(List<String> cps){
		this.cps = cps;
	}
	
	public void init(CpUtils cpUtils){
		if (cps.size() == 0){
			this.cps.add("Empty List");
		}
		ChoiceDialog<String> dialog = new ChoiceDialog<>(this.cps.get(0), this.cps);
		dialog.setTitle("Dismi-connection point");
		dialog.setHeaderText("Delete Connection Point");
		dialog.setContentText("Select connection point:");
		dialog.setGraphic(ImageHandler.CP_ICON);
 		Optional<String> result = dialog.showAndWait();
		result.ifPresent(selectedItem -> submitDelete(selectedItem, cpUtils));
	}
	
	private void submitDelete(String selectedItem, CpUtils cpUtils){
		if (selectedItem.equalsIgnoreCase("Empty List")){
			return;
		}
		String basicPath = "connectionpoint";
		ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
		ConnectionPoint connectionPoint = new ConnectionPoint();
		connectionPoint.setName(selectedItem);
		ReturnedStatus returnedStatus = connectionPointOperations.deleteConnectionPoint(connectionPoint);
		if (returnedStatus.getStatus() == STATUS.SUCCESS){
			Response response = (Response)returnedStatus.getInfomration();
			if (response.getStatus() == 200){
				cpUtils.sendEvent("0:Connection point '"+connectionPoint.getName()+"' successfulyl deleted !");
				
			}
			else{
				cpUtils.sendEvent("1:Problems when deleting connection point '"+connectionPoint.getName()+"' !");
			}			
		}else{
			cpUtils.sendEvent("1:"+returnedStatus.getInfomration().toString());
		}
	}
}