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

import java.util.Optional;

import javax.ws.rs.core.Response;

import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.restcli.ServicesOperations;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;

public class DeleteService extends GenericStatusObserver {

	public void deleteServiceGui(ObservableList<ServiceWithServiceId> servicesWithIds) {
		if (servicesWithIds.size() == 0) {
			servicesWithIds.add(new ServiceWithServiceId("Empty List", "Empty List"));
		}

		ChoiceDialog<ServiceWithServiceId> dialog = new ChoiceDialog<>(servicesWithIds.get(0), servicesWithIds);
		dialog.setTitle("Dismi-Service");
		dialog.setHeaderText("Delete Dismi Service");
		dialog.setContentText("Select Service::");
		dialog.setGraphic(ImageHandler.CP_ICON);
		Optional<ServiceWithServiceId> result = dialog.showAndWait();
		result.ifPresent(selectedItem -> submitDelete(selectedItem));
	}

	private void submitDelete(ServiceWithServiceId selectedItem) {
		if (selectedItem.getServiceName().equalsIgnoreCase("Empty List")) {
			return;
		}
		String basicPath = "service";
		ServicesOperations sp = new ServicesOperations(basicPath);
		ReturnedStatus returnedStatus = sp.serviceServiceIdDelete(selectedItem.getServiceId());
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			Response response = (Response) returnedStatus.getInfomration();
			if (response.getStatus() == 200) {
				sendEvent("0:Service '" + selectedItem.toString() + "' successfulyl deleted (Withdrawn) !");
				
			} else {
				sendEvent("0:Problems when deleting (Withdraw) service'" + selectedItem.toString() + " !");
			}
		}
	}

}
