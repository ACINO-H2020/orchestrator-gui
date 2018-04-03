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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.restcli.ServicesOperations;
import org.onosproject.orchestrator.dismi.primitives.DismiIntentState;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Service;
import org.onosproject.orchestrator.dismi.primitives.extended.ServiceList;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class TreeViewHandler extends Observable {

	public static final String ROOT_TITLE = "Dismi Services";
	private TreeView<ServiceIntent> serviceTree;
	private Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/root.png")));
	private ServiceList services;
	private String myServiceIcon = "/icons/service.png";
	private String myIntentIcon = "/icons/intent1.png";
	private String myRootIcon = "/icons/root.png";

	private String installingIcon = "/icons/installing.png";
	private String installedIcon = "/icons/installed.png";
	private String withdrawingIcon = "/icons/withdrawing.png";
	private String witchdrawnIcon = "/icons/withdrawn.png";
	private String processingIcon = "/icons/processing.png";
	private String failedIcon = "/icons/failed.png";
	private String errorIcon = "/icons/installation_failed.png";
	private String unknownIcon = "/icons/unknown.png";
	private Label status = null;

	private TreeItem<ServiceIntent> rootNode = new TreeItem<ServiceIntent>(
			new ServiceIntent(0, null, null, myRootIcon, null, null, null), rootIcon);

	public TreeViewHandler(TreeView<ServiceIntent> serviceTree, Label status) {
		this.serviceTree = serviceTree;
		this.status = status;
		this.status.setTextFill(Color.web("#000000"));
		String basicPath = "service";
		ServicesOperations sp = new ServicesOperations(basicPath);
		ReturnedStatus returnedStatus = sp.serviceGet();
		serviceTree.setRoot(rootNode);
		rootNode.setExpanded(true);
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			services = (ServiceList) returnedStatus.getInfomration();
			List<ServiceIntent> serviceIntents = new ArrayList<ServiceIntent>();
			for (Service serv : services) {
				ServiceIntent servNode = new ServiceIntent(1, serv.getDisplayName(), "", myServiceIcon,
						serv.getServiceId(), "", null);
				Image serviceIcon = new Image(getClass().getResourceAsStream(servNode.getImageName()));
				TreeItem<ServiceIntent> depNode = new TreeItem<ServiceIntent>(servNode, new ImageView(serviceIcon));
				rootNode.getChildren().add(depNode);
				List<Intent> intents = serv.getIntents();
				sendEvent();
			}
			status.setText("Connected with DISMI services and Total Registerd Services are ["+services.size()+"] !");
			this.status.setTextFill(Color.web("#000000"));
			handleBackgroundRefresh();

		} else {
			services = new ServiceList();
			status.setText("Problems when establishing connection with Dismi Service !");
			this.status.setTextFill(Color.web("#ff0000"));
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Dismi Error");
			alert.setHeaderText("Dismi Service Connection");
			alert.setContentText("Problems when establishing connection with\nDismi Service !");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.showAndWait();
			System.exit(0);
		}
	}

	public class ServiceIntent {

		private final SimpleStringProperty serviceName;
		private final SimpleStringProperty intentName;
		private final SimpleStringProperty imageName;
		private final SimpleStringProperty serviceId;
		private final SimpleStringProperty intentId;
		private final DismiIntentState intentState;

		private int inService;

		public ServiceIntent() {
			this.serviceName = null;
			this.intentName = null;
			this.imageName = null;
			this.serviceId = null;
			this.intentId = null;
			this.inService = 0;
			this.intentState = null;
		}

		public ServiceIntent(int inService, String serviceName, String intentName, String imageName, String serviceId,
				String intentId, DismiIntentState intentState) {
			this.serviceName = new SimpleStringProperty(serviceName);
			this.intentName = new SimpleStringProperty(intentName);
			this.imageName = new SimpleStringProperty(imageName);
			this.serviceId = new SimpleStringProperty(serviceId);
			this.intentId = new SimpleStringProperty(intentId);
			this.inService = inService;
			this.intentState = intentState;
		}

		public void setIntentName(String intentName) {
			this.intentName.setValue(intentName);
		}

		public void setServiceName(String serviceName) {
			this.serviceName.setValue(serviceName);
		}

		public void setImageName(String imageName) {
			this.imageName.setValue(imageName);
		}

		public void setServiceId(String serviceId) {
			this.serviceId.setValue(serviceId);
		}

		public void setIntentId(String intentId) {
			this.intentId.setValue(intentId);
		}

		public String getIntentName() {
			return intentName.getValue();
		}

		public String getServiceName() {
			return serviceName.getValue();
		}

		public String getImageName() {

			if (null == intentState) {
				return imageName.getValue();
			}
			switch (intentState) {
			case FAILED:
				this.imageName.setValue(failedIcon);
				break;
			case INSTALLED:
				this.imageName.setValue(installedIcon);
				break;
			case INSTALLING:
				this.imageName.setValue(installingIcon);
				break;
			case PROCESSING:
				this.imageName.setValue(processingIcon);
				break;
			case PROCESSING_FAILED:
				this.imageName.setValue(errorIcon);
				break;
			case UNKNOWN:
				this.imageName.setValue(unknownIcon);
				break;
			case WITHDRAWING:
				this.imageName.setValue(withdrawingIcon);
				break;
			case WITHDRAWN:
				this.imageName.setValue(witchdrawnIcon);
				break;
			}
			return imageName.getValue();

		}

		public String getServiceId() {
			return serviceId.getValue();
		}

		public String getIntentId() {
			return intentId.getValue();
		}

		public String toString() {
			if (inService == 0) {
				return ROOT_TITLE;
			} else if (inService == 1) {
				return this.serviceName.getValue();
			} else {
				return this.intentName.getValue();
			}

		}
	}

	public ServiceList getServices() {
		return services;
	}

	public Service getService(String servicename) {
		for (Service serv : services) {

			if (serv.getServiceId().equals(servicename)) {
				return serv;
			}
		}
		return new Service();
	}

	boolean isMsgDisplayed = false;

	public void refresh() {
		rootNode = new TreeItem<ServiceIntent>(new ServiceIntent(0, null, null, myRootIcon, null, null, null),
				rootIcon);
		String basicPath = "service";
		ServicesOperations sp = new ServicesOperations(basicPath);
		ReturnedStatus returnedStatus = sp.serviceGet();
		rootNode = serviceTree.getRoot();
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			isMsgDisplayed = false;
			services = (ServiceList) returnedStatus.getInfomration();
			List<ServiceIntent> serviceIntents = new ArrayList<ServiceIntent>();
			for (Service serv : services) {
				if (!isServiceExists(rootNode, serv)) {
					ServiceIntent servNode = new ServiceIntent(1, serv.getDisplayName(), "", myServiceIcon,
							serv.getServiceId(), "", null);
					Image serviceIcon = new Image(getClass().getResourceAsStream(servNode.getImageName()));
					TreeItem<ServiceIntent> depNode = new TreeItem<ServiceIntent>(servNode, new ImageView(serviceIcon));
					rootNode.getChildren().add(depNode);
				}
			}
			serviceTree.refresh();
			status.setText("Connected with DISMI services and Total Registerd Services are ["+services.size()+"]");
			this.status.setTextFill(Color.web("#000000"));
			sendEvent();

		} else {
			services = new ServiceList();
			ObservableList list = rootNode.getChildren();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					rootNode.getChildren().remove(i);
				}
			}
			if (!isMsgDisplayed) {
				status.setText("Problems when establishing connection with Dismi Service !");
				this.status.setTextFill(Color.web("#ff0000"));
				isMsgDisplayed = true;
			}
		}
	}

	private boolean isServiceExists(TreeItem<ServiceIntent> rootNode, Service serv) {
		ObservableList<TreeItem<ServiceIntent>> itemTemp = rootNode.getChildren();
		for (TreeItem<ServiceIntent> item : itemTemp) {
			ServiceIntent intent = item.getValue();
			if (intent.getServiceName().equals(serv.getDisplayName())
					&& intent.getServiceId().endsWith(serv.getServiceId())) {
				return true;
			}
		}
		return false;
	}

	public void sendEvent() {

		TreeItem<ServiceIntent> selItem = serviceTree.getSelectionModel().getSelectedItem();
		if (selItem != null) {
			ServiceIntent serviceIntent = selItem.getValue();
			for (Service service : services) {
				if (service.getDisplayName().equals(serviceIntent.getServiceName())
						&& service.getServiceId().equals(serviceIntent.getServiceId())) {
					setChanged();
					notifyObservers(service);
					return;
				}
			}

		} else {

			if (services.size() > 0) {
				ServiceIntent servNode = new ServiceIntent(1, services.get(0).getDisplayName(), "", myServiceIcon,
						services.get(0).getServiceId(), "", null);
				Image serviceIcon = new Image(getClass().getResourceAsStream(servNode.getImageName()));
				TreeItem<ServiceIntent> depNode = new TreeItem<ServiceIntent>(servNode, new ImageView(serviceIcon));
				setChanged();
				notifyObservers(services.get(0));

			} else {
				setChanged();
				notifyObservers(null);
			}
		}
	}

	public void handleBackgroundRefresh() {
		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				while (true) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							refresh();
							sendEvent();
						}
					});
					Thread.sleep(5000);
				}
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
