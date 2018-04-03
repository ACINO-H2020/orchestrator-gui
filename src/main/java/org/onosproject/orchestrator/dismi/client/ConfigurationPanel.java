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

package org.onosproject.orchestrator.dismi.client;

import java.util.Optional;

import org.onosproject.orchestrator.dismi.client.controllers.ConfigurationController;
import org.onosproject.orchestrator.dismi.client.controllers.DismiControllerManager;
import org.onosproject.orchestrator.dismi.client.restcli.ConfigurationOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.utils.GenericStatusObserver;
import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.primitives.DismiConfiguration;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class ConfigurationPanel extends GenericStatusObserver{
	public void init(Parent root) {
		String basicPath = "admin/conf";
		ConfigurationOperations configurationOperations = new ConfigurationOperations(basicPath);
		ReturnedStatus returnedStatus = configurationOperations.getConfigurations();
		if (returnedStatus.getStatus() == STATUS.FAIL)
		{
			this.sendEvent("1:"+returnedStatus.getInfomration().toString());
			return;
		}
		Alert alert = new Alert(AlertType.NONE);
		ButtonType defaultCon = new ButtonType("Default", ButtonBar.ButtonData.BACK_PREVIOUS);
		ButtonType submit = new ButtonType("Apply", ButtonBar.ButtonData.APPLY);
		alert.getDialogPane().getButtonTypes().add(defaultCon);
		alert.getDialogPane().getButtonTypes().add(submit);
		alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		alert.setTitle("DISMI/ACINO Configuration");
		alert.setHeaderText("Configuration Options");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setContent(root);
		alert.setGraphic(ImageHandler.CONF_ICON);
		
		ConfigurationController configurationController = DismiControllerManager.getConfigurationController();
		
		configurationController.setDismiConfiguration((DismiConfiguration) returnedStatus.getInfomration());
		 
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == submit) {
			DismiConfiguration configuration = new DismiConfiguration();
			configuration = configurationController.getDismiConfiguration(); 
			returnedStatus = configurationOperations.putDismiConfigurations(configuration);
			this.sendEvent("0:"+returnedStatus.getInfomration().toString());
			return;
		}
		if (result.get() == defaultCon) {
			this.sendEvent("0:"+returnedStatus.getInfomration().toString());
			return;
		}
	}
}