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

package org.onosproject.orchestrator.dismi.client.controllers;

import org.onosproject.orchestrator.dismi.primitives.DismiConfiguration;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ConfigurationController {

	@FXML
	private RadioButton flatModel;

	@FXML
	private RadioButton negoModel;

	@FXML
	private CheckBox uni;
	@FXML
	private CheckBox bi;

	final ToggleGroup group = new ToggleGroup();

	// the initialize method is automatically invoked by the FXMLLoader
	public void initialize() {
		flatModel.setToggleGroup(group);
		negoModel.setToggleGroup(group);
	}

	public void setDismiConfiguration(DismiConfiguration configuration) {
		
		if (configuration.isNewModel()) {
			flatModel.selectedProperty().set(true);
		} else {
			negoModel.selectedProperty().set(true);
		}
		uni.selectedProperty().set(configuration.isUnidirectional());
		bi.selectedProperty().set(configuration.isBidirectional());
	}

	public DismiConfiguration getDismiConfiguration() {
		
		DismiConfiguration configuration = new DismiConfiguration();
		configuration.setNewModel(flatModel.selectedProperty().get());
		configuration.setUnidirectional(uni.selectedProperty().get());
		configuration.setBidirectional(bi.selectedProperty().get());
		return configuration;
	}
}