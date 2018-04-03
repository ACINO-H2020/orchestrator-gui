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

import java.io.IOException;

import org.onosproject.orchestrator.dismi.client.cps.ConnectionPointController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class DismiControllerManager {
	
	private static DismiServiceController dismiServiceController ;
	private static IntentController intentController;
	private static ConnectionPointController connectionPointController;
	private ConstraintController constraintController;
	private static ASController asController;
	private static DismiLogController dismiLogController;
	public static ConfigurationController configurationController;
	private String pref=""; 
	
	public Parent loadMainWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/dismiclient.fxml"));
		Parent root = fxmlLoader.load();
		DismiServiceController dismiServiceControllerTemp = (DismiServiceController) fxmlLoader.getController();
		setDismiServiceController(dismiServiceControllerTemp);
		return root;
	}

	@FXML
	public Parent loadCPWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/newConnectionPoint.fxml"));
		Parent root = fxmlLoader.load();
		ConnectionPointController controller = (ConnectionPointController)fxmlLoader.getController();
		setConnectionPointController(controller); 
		return root;
	}
	
	@FXML
	public Parent loadHelpWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/help.fxml"));
		Parent root = fxmlLoader.load();		 
		return root;
	}
	
	
	public Parent loadDismiLogController() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/dismilog.fxml"));
		Parent root = fxmlLoader.load();
		DismiLogController dismiLogController = (DismiLogController) fxmlLoader.getController();
		setDismiLogController(dismiLogController);
		return root;
	}
	
	
	
	public Parent loadIntentASSelector() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/intentdisplay.fxml"));
		Parent root = fxmlLoader.load();
		ASController asController = (ASController) fxmlLoader.getController();
		setAsController(asController);
		return root;
	}
	
	public Parent loadConstraintWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/constraints.fxml"));
		Parent root = fxmlLoader.load();
		constraintController= (ConstraintController) fxmlLoader.getController();
		return root;
	}
	
	public Parent loadIntentWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/intentVBox.fxml"));
		Parent root = fxmlLoader.load();
		setIntentController((IntentController) fxmlLoader.getController());
		return root;
	}
	public Parent loadConfigurationWindow() throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pref+"/metadata/configuration.fxml"));
		Parent root = fxmlLoader.load();		
		setConfigurationController((ConfigurationController) fxmlLoader.getController());
		return root;
	}
	
	public static ConfigurationController getConfigurationController() {
		return configurationController;
	}

	public static void setConfigurationController(ConfigurationController configurationController) {
		DismiControllerManager.configurationController = configurationController;
	}

	public void setConstraintController(ConstraintController constraintController) {
		this.constraintController = constraintController;
	}

	public ConstraintController getConstraintController(){
		return constraintController;
	}
	public DismiServiceController getDismiServiceController() {
		return dismiServiceController;
	}

	public void setDismiServiceController(DismiServiceController dismiServiceController) {
		this.dismiServiceController = dismiServiceController;
	}

	public IntentController getIntentController() {
		return intentController;
	}

	public void setIntentController(IntentController intentController) {
		this.intentController = intentController;
	}

	public ConnectionPointController getConnectionPointController() {
		return connectionPointController;
	}

	public void setConnectionPointController(ConnectionPointController connectionPointController) {
		this.connectionPointController = connectionPointController;
	}

	public static ASController getAsController() {
		return asController;
	}

	public static void setAsController(ASController asController) {
		DismiControllerManager.asController = asController;
	}

	public static DismiLogController getDismiLogController() {
		return dismiLogController;
	}

	public static void setDismiLogController(DismiLogController dismiLogController) {
		DismiControllerManager.dismiLogController = dismiLogController;
	}
	
	

}
