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

import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.onosproject.orchestrator.dismi.client.ConfigurationPanel;
import org.onosproject.orchestrator.dismi.client.DismiClientMain;
import org.onosproject.orchestrator.dismi.client.IntentEntity;
import org.onosproject.orchestrator.dismi.client.controllers.TreeViewHandler.ServiceIntent;
import org.onosproject.orchestrator.dismi.client.utils.CpUtils;
import org.onosproject.orchestrator.dismi.client.utils.DeleteService;
import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.client.utils.ServiceUtils;
import org.onosproject.orchestrator.dismi.client.utils.ServiceWithServiceId;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Service;
import org.onosproject.orchestrator.dismi.primitives.extended.ServiceList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DismiServiceController {

	@FXML
	private TreeView<ServiceIntent> serviceTree;

	@FXML
	private TableView<IntentEntity> intentTable;
	@FXML
	private VBox contentContainer;
	@FXML
	private MenuItem configuration;
	@FXML
	private MenuItem refresh;
	@FXML
	private MenuItem logCommand;
	@FXML
	private MenuItem quit;
	// Menu Items for connection points
	@FXML
	private MenuItem viewCP;
	@FXML
	private MenuItem newCP;
	@FXML
	private MenuItem updateCP;
	@FXML
	private MenuItem deleteCP;
	@FXML
	private MenuItem loadCP;
	@FXML
	private MenuItem symbols;
	// Menu Items for handling service
	@FXML
	private MenuItem newService;
	@FXML
	private MenuItem deleteService;
	@FXML
	private Label serviceNameTitle;
	@FXML
	private Label left;

	@FXML
	public Label error;

	private static String name = "DismiServiceController";
	private final Logger log = Logger.getLogger(name);
	public static int ACTIVE_WINDOW = 1;
	private boolean isMainintentTableActive = true;
	private DismiControllerManager dismiControllerManager;
	private TreeViewHandler treeViewHandler = null;
	private IntentTableHandler intentTableHandler = null;
	private ConfigurationPanel configurationPanel = new ConfigurationPanel();
	private DeleteService deleteServiceEng = new DeleteService();
	private CpUtils cpUtils = new CpUtils();

	// Initialize method is automatically invoked by the FXMLLoader
	public void initialize() {
		serviceNameTitle.setText("");
		dismiControllerManager = new DismiControllerManager();
		treeViewHandler = new TreeViewHandler(serviceTree, left);
		intentTableHandler = new IntentTableHandler(intentTable);

		treeViewHandler.addObserver(new Observer() {
			public void update(Observable obj, Object arg) {
				if (null == arg) {
					ObservableList<Intent> observableList = FXCollections.observableArrayList();
					intentTableHandler.setItems(observableList);
					if (DismiServiceController.ACTIVE_WINDOW == 1) {
						setServiceTitle("");
					}
					return;
				}
				Service tempServ = (Service) arg;
				List<Intent> intents = tempServ.getIntents();
				ObservableList<Intent> observableList = FXCollections.observableArrayList();
				for (Intent tempIt : intents)
					observableList.add(tempIt);
				intentTableHandler.setItems(observableList);
				if (DismiServiceController.ACTIVE_WINDOW == 1) {
					setServiceTitle(tempServ.getDisplayName());
				}
			}
		});

		cpUtils.addObserver(new StatusMessageObserver());
		configurationPanel.addObserver(new StatusMessageObserver());
		deleteServiceEng.addObserver(new StatusMessageObserver());
		serviceTree.setOnMouseClicked(new MyEventHandler());
		// ---------------------
		newService(DismiClientMain.stageMain);
		deleteService();
		// ----------------------
		viewCPs();
		addNewCP();
		deleteCP();
		updateCP();
		loadCP();
		refresh();
		configuration();
		symbols();
		quit();
		log();
		left.setText("Status: ToDo");
		error.setText("[---------------------------]");

	}

	public void quit() {
		quit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				System.exit(1);
			}
		});
	}

	private void configuration() {
		configuration.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				try {
					configurationPanel.init(dismiControllerManager.loadConfigurationWindow());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void symbols() {
		symbols.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Alert alert = new Alert(AlertType.NONE);
				ButtonType submit = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
				alert.getDialogPane().getButtonTypes().add(submit);
				alert.setTitle("Help-Intent Symbols");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setHeaderText("");
				try {
					alert.getDialogPane().setContent(dismiControllerManager.loadHelpWindow());
				} catch (IOException e) {
					e.printStackTrace();
				}
				alert.setGraphic(ImageHandler.CP_ICON);
				Optional<ButtonType> result = alert.showAndWait();
			}
		});
	}

	private void refresh() {
		refresh.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				treeViewHandler.refresh();
				for (int i = 0; i < contentContainer.getChildren().size(); i++) {
					contentContainer.getChildren().remove(i);
				}
				IntentTableHandler intentTableHandler = new IntentTableHandler(intentTable);
				if (intentTableHandler.getCollectedIntetns().isEmpty()) {
					return;
				}
				TreeItem<ServiceIntent> tempRoot = serviceTree.getRoot();
				if (tempRoot.getChildren() != null) {
					Service tempServ = treeViewHandler
							.getService(tempRoot.getChildren().get(0).getValue().getServiceId());
					List<Intent> intents = tempServ.getIntents();
					contentContainer.getChildren().add(intentTable);
					ObservableList<Intent> observableList = FXCollections.observableArrayList();
					for (Intent tempIt : intents)
						observableList.add(tempIt);
					intentTableHandler.setItems(observableList);
					setServiceTitle(tempServ.getDisplayName());
					isMainintentTableActive = true;
				}
			}
		});
	}

	// Handling of Services
	private void newService(Stage stage) {
		newService.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				boolean result = getTitle(stage, "New Service");
				if (!result) {
					return;
				}
				for (int i = 0; i < contentContainer.getChildren().size(); i++) {
					contentContainer.getChildren().remove(i);
				}
				isMainintentTableActive = false;
				ServiceUtils serviceUtils = new ServiceUtils();
				serviceUtils.handleNewServiceGui(dismiControllerManager, contentContainer);
				dismiControllerManager.getIntentController().addObserver(new StatusMessageObserver());
			}
		});
	}

	// --------------------------------------------------
	private boolean getTitle(Stage stage, String message) {
		DismiServiceController.ACTIVE_WINDOW = 2;
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Dismi Input");
		dialog.setHeaderText(message);
		dialog.setContentText("Service Name:");
		dialog.setGraphic(ImageHandler.CP_ICON);
		dialog.initOwner(stage);
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent()) {
			return false;
		}
		result.ifPresent(name -> setServiceTitle(name));
		String title = getServiceTitle();
		if (title == null || title.length() == 0) {
			return getTitle(stage, "Invalid service name, please enter again");
		} else {
			return true;
		}

	}

	private void deleteService() {
		deleteService.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				TreeItem<ServiceIntent> root = serviceTree.getRoot();
				ObservableList<TreeItem<ServiceIntent>> childs = root.getChildren();
				ObservableList<ServiceIntent> servicesList = FXCollections.observableArrayList();
				for (TreeItem<ServiceIntent> aService : childs) {

					servicesList.add(aService.getValue());
				}
				ObservableList<ServiceWithServiceId> servicesWithIds = FXCollections.observableArrayList();
				for (ServiceIntent servInt : servicesList) {
					servicesWithIds.add(new ServiceWithServiceId(servInt.getServiceName(), servInt.getServiceId()));
				}
				deleteServiceEng.deleteServiceGui(servicesWithIds);
			}
		});
	}

	public void displayIntentTableWindow() {
		ServiceList serviceList = treeViewHandler.getServices();
		if (serviceList.size() > 0) {
			IntentTableHandler intentTableHandler = new IntentTableHandler(intentTable);
			Service tempServ = serviceList.get(0);
			serviceNameTitle.setText(tempServ.getDisplayName());
			List<Intent> intents = tempServ.getIntents();
			ObservableList<Intent> observableList = FXCollections.observableArrayList();
			for (Intent tempIt : intents)
				observableList.add(tempIt);
			intentTableHandler.setItems(observableList);
			setServiceTitle(tempServ.getDisplayName());
		}
		for (int i = 0; i < contentContainer.getChildren().size(); i++) {
			contentContainer.getChildren().remove(i);
		}

		contentContainer.getChildren().add(intentTable);
	}

	public void setServiceTitle(String strServiceName) {
		serviceNameTitle.setText(strServiceName);
	}

	public String getServiceTitle() {
		return serviceNameTitle.getText();
	}

	// Handling of Connection Points
	private void viewCPs() {
		viewCP.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				cpUtils.viewCpGui();
			}
		});
	}

	private void log() {
		logCommand.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Alert alert = new Alert(AlertType.NONE);
				ButtonType submit = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
				alert.getDialogPane().getButtonTypes().add(submit);
				alert.setTitle("DismiLog");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setHeaderText("");
				try {
					alert.getDialogPane().setContent(dismiControllerManager.loadDismiLogController());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Optional<ButtonType> result = alert.showAndWait();
			}
		});
	}

	private void deleteCP() {
		deleteCP.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				cpUtils.deleteCpGui();
			}
		});
	}

	private void updateCP() {
		updateCP.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				cpUtils.updateCpGui();
			}
		});

	}

	private void addNewCP() {
		newCP.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				cpUtils.newCpGui();
			}
		});
	}

	private void loadCP() {
		loadCP.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				cpUtils.loadCPGui();

			}
		});
	}

	class MyEventHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (event.getClickCount() == 2) {
				TreeItem<ServiceIntent> item = serviceTree.getSelectionModel().getSelectedItem();
				if (item.getParent() == null
						&& item.getValue().toString().equalsIgnoreCase(TreeViewHandler.ROOT_TITLE)) {

				} else if (item.getChildren() != null
						&& item.getParent().getValue().toString().equalsIgnoreCase(TreeViewHandler.ROOT_TITLE)) {
					for (int i = 0; i < contentContainer.getChildren().size(); i++) {
						contentContainer.getChildren().remove(i);
					}
					Service tempServ = treeViewHandler.getService(item.getValue().getServiceId());
					List<Intent> intents = tempServ.getIntents();

					contentContainer.getChildren().add(intentTable);
					ObservableList<Intent> observableList = FXCollections.observableArrayList();
					for (Intent tempIt : intents)
						observableList.add(tempIt);
					intentTableHandler.setItems(observableList);
					setServiceTitle(tempServ.getDisplayName());
					isMainintentTableActive = true;
				} else if (item.getChildren() == null || item.getChildren().size() == 0) {
				}
			}
		}
	}

	class StatusMessageObserver implements Observer {
		@Override
		public void update(Observable o, Object arg) {
			if (arg.toString().startsWith("1")) {
				error.setTextFill(Color.web("#FF0000"));
				Toolkit.getDefaultToolkit().beep();
			} else {
				error.setTextFill(Color.web("#0000FF"));
			}
			String msg = arg.toString().substring(2);
			error.setText("Last Action: "+msg);
		}
	}
}
