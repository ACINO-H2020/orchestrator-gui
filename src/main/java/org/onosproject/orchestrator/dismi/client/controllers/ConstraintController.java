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

import org.onosproject.orchestrator.dismi.client.utils.ServiceUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ConstraintController {


	@FXML
	private TextField intentName;
	@FXML
	private TextField bandwidth;
	@FXML
	private ComboBox<String> bwUnit;
	@FXML
	private TextField latency;
	@FXML
	private ComboBox<String> lUnit;
	@FXML
	private ComboBox<String> security;
	@FXML
	private Button next;
	@FXML
	private Button close;
	
	private ServiceUtils serviceUtils = new ServiceUtils();

	// the initialize method is automatically invoked by the FXMLLoader
	public void initialize() {
		bwUnit.setItems(serviceUtils.getBandwidthUnits());
		bwUnit.getSelectionModel().select(0);
		lUnit.setItems(serviceUtils.getTimeUnits());
		lUnit.getSelectionModel().select(0);
		security.setItems(serviceUtils.getSecurityOptions());
		security.getSelectionModel().select(0);
		bandwidth.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		latency.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		
	}

	class SubmitActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			
		}
	}

	class CloseActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			
		}
	}
}
