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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.onosproject.orchestrator.dismi.client.controllers.DismiControllerManager;
import org.onosproject.orchestrator.dismi.client.restcli.DismitestBase;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class DismiClientMain extends Application {

	private static String name = "DismiClientMain";
	private static final Logger log = Logger.getLogger(name);
	public static Stage stageMain;
	
	public static void main(String[] args) {
		if (args.length == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Dismi Client");
			alert.setHeaderText("Dismi Client Configuration");
			alert.setContentText("Please specify Dismi URL !");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			log.error("Dismi Service endpoint is missing, please specify it as command line argument !");
			return;
		}
		DismitestBase.baiscURL = args[0];
		log.info("Dismi Service endpoint: "+DismitestBase.baiscURL);
		Application.launch(DismiClientMain.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		DismiControllerManager dismiControllerManager = new DismiControllerManager();
		Parent root = dismiControllerManager.loadMainWindow();
		root.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
			}
		});
		stage.setTitle("Dismi REST Client Application");
		stage.setScene(new Scene(root, 1305, 720));
		stage.setResizable(false);
		this.stageMain = stage;
		stage.show();
	}

	// Create log folder
	static {
		File logDir = new File("log");
		if (!logDir.exists())
			logDir.mkdir();

		File logFile = new File("log/dismiclient.log");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
			}
		}
		PropertyConfigurator.configure("log4j.properties");
	}
}