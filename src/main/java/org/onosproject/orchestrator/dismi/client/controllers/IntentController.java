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

import javax.ws.rs.core.Response;

import org.onosproject.orchestrator.dismi.client.cps.ListItem;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.restcli.ServicesOperations;
import org.onosproject.orchestrator.dismi.client.utils.GenericStatusObserver;
import org.onosproject.orchestrator.dismi.client.utils.ServiceUtils;
import org.onosproject.orchestrator.dismi.client.utils.ServiceUtils.ACTION_TYPE;
import org.onosproject.orchestrator.dismi.primitives.Aggregate;
import org.onosproject.orchestrator.dismi.primitives.AvailabilityConstraint;
import org.onosproject.orchestrator.dismi.primitives.BandwidthConstraint;
import org.onosproject.orchestrator.dismi.primitives.Connection;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.Constraint;
import org.onosproject.orchestrator.dismi.primitives.DelayConstraint;
import org.onosproject.orchestrator.dismi.primitives.HighAvailabilityConstraint;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Mesh;
import org.onosproject.orchestrator.dismi.primitives.Multicast;
import org.onosproject.orchestrator.dismi.primitives.Path;
import org.onosproject.orchestrator.dismi.primitives.SDWAN;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint;
import org.onosproject.orchestrator.dismi.primitives.Service;
import org.onosproject.orchestrator.dismi.primitives.Tree;
import org.onosproject.orchestrator.dismi.primitives.VPN;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class IntentController extends GenericStatusObserver  {

	@FXML
	private TextField intentName;
	@FXML
	private ComboBox<String> actionType;
	@FXML
	private ListView<ListItem> source;
	@FXML
	private ListView<ListItem> destination;
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
	private Button pEdit;
	
	@FXML
	private Button save;
	
	@FXML
	private Button submitService;
	
	@FXML
	private Button clearService;
	
	@FXML
	private Button closeService;
	
	@FXML
	private TableView intentTable;
	
	@FXML
	private CheckBox negotiable;
	
	@FXML
	private CheckBox highAva;
	
	@FXML
	private ComboBox<String> avail;
	
	@FXML
	private CheckBox availChk;
	
	@FXML
	private Label dstlab;
	
	
	private List<Intent> intetns = new ArrayList<Intent>();
	private IntentTableHandler intentTableHandler = null;
	private ServiceUtils serviceUtils = new ServiceUtils();

	/**
	 * @the initialize method is automatically invoked by the FXMLLoader
	 */
	public void initialize() {
		actionType.setItems(serviceUtils.getActionTypes());
		actionType.getSelectionModel().select(0);
		serviceUtils.findCpOptions(actionType.getSelectionModel().getSelectedItem(), source, destination, dstlab);
		actionType.getSelectionModel().selectedItemProperty().addListener(new ActionTypeHandler());
		source.setItems(serviceUtils.getAllConnectionPoints());
		destination.setItems(serviceUtils.getAllConnectionPoints());
		submitService.addEventHandler(ActionEvent.ACTION, new SubmitActionHandler());
		closeService.addEventHandler(ActionEvent.ACTION, new CloseActionHandler());
		save.addEventHandler(ActionEvent.ACTION, new SaveActionHandler());
		bwUnit.setItems(serviceUtils.getBandwidthUnits());
		bwUnit.getSelectionModel().select(0);
		lUnit.setItems(serviceUtils.getTimeUnits());
		lUnit.getSelectionModel().select(0);
		security.setItems(serviceUtils.getSecurityOptions());
		security.getSelectionModel().select(0);
		bandwidth.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		latency.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		intentTableHandler = new IntentTableHandler(intentTable);
		
		bandwidth.textProperty().addListener((observable, oldValue, newValue) -> {
			dataValidation(bandwidth, oldValue, newValue);
		});
		latency.textProperty().addListener((observable, oldValue, newValue) -> {
			dataValidation(latency, oldValue, newValue);
		});
		
		avail.setItems(serviceUtils. getAvailability());
		avail.getSelectionModel().select(0);
		availChk.selectedProperty().set(false);
		avail.setDisable(true);
		availChk.setOnAction((event) -> {
		    boolean selected = availChk.isSelected();
		    avail.setDisable(!(selected));		    
		});
	}
	
	private void dataValidation(TextField temp, String oldValue, String newValue){
		try{
			if(newValue.endsWith("d") || newValue.endsWith("D")){
				temp.setText(oldValue);
				temp.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
				return;
			}
			if(newValue.length()==0){
				temp.setText("0");
				temp.setStyle("-fx-border-color: white ; -fx-border-width: 1px ;");
				return;
			}
			Double.parseDouble(newValue);
			temp.setStyle("-fx-border-color: white ; -fx-border-width: 1px ;");
			
		}catch(Exception exp){
			temp.setText(oldValue);
			temp.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
		}
	}

	class ActionTypeHandler implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String newSelected) {
			String action = actionType.getSelectionModel().getSelectedItem();
			if (null == action) {
				return;
			}
			serviceUtils.findCpOptions(action, source, destination, dstlab);
		}
	}

	class SubmitActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Service service = new Service();
			List<Intent> intentsList = intentTableHandler.getCollectedIntetns();
			if(intentsList.size() == 0){
				sendEvent("1:Service's intent list is empty !");
				return;
			}
			DismiControllerManager controllerManager = new DismiControllerManager();
			String srn = controllerManager.getDismiServiceController().getServiceTitle();
			service.setDisplayName(srn);
			service.setIntents(intentsList);
			
			String basicPath = "service";
			ServicesOperations sp = new ServicesOperations(basicPath);
			ReturnedStatus returnedStatus = sp.servicePost(service);
			if (returnedStatus.getStatus() == STATUS.SUCCESS) {
				Response response = (Response) returnedStatus.getInfomration();
				if (response.getStatus() == 200) {
					controllerManager.getDismiServiceController().displayIntentTableWindow();
				}
			} else {
				sendEvent("1:Problems when submitting Dismi service. Please check log file for more inforamtion !");
			}
			DismiServiceController.ACTIVE_WINDOW = 1;
		}
	}

	class CloseActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Service service = new Service();
			List<Intent> intentsList = intentTableHandler.getCollectedIntetns();
			DismiControllerManager controllerManager = new DismiControllerManager();
			controllerManager.getDismiServiceController().displayIntentTableWindow();
			DismiServiceController.ACTIVE_WINDOW = 1;
		}
	}

	class SaveActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			
			Intent intent = createDismiIntent();
			intentTableHandler.setIntent(intent);
			// clear filed and prepare for next intent
			actionType.getSelectionModel().select(0);
			serviceUtils.findCpOptions(actionType.getSelectionModel().getSelectedItem(), source, destination, dstlab);
			source.setItems(serviceUtils.getAllConnectionPoints());
			destination.setItems(serviceUtils.getAllConnectionPoints());
			bwUnit.setItems(serviceUtils.getBandwidthUnits());
			bwUnit.getSelectionModel().select(0);
			lUnit.setItems(serviceUtils.getTimeUnits());
			lUnit.getSelectionModel().select(0);
			security.setItems(serviceUtils.getSecurityOptions());
			security.getSelectionModel().select(0);
			bandwidth.setText("");
			latency.setText("");
			intentName.setText("");
			negotiable.selectedProperty().set(false);
			avail.getSelectionModel().select(0);
			avail.setDisable(true);
			availChk.setSelected(false);
		}
	}

	private Intent createDismiIntent() {
		ACTION_TYPE selectedActionType = serviceUtils
				.selectActionTypes(actionType.getSelectionModel().getSelectedItem());
		List<ConnectionPoint> sourceCps = null;
		List<ConnectionPoint> destCps = null;
		Intent intent = new Intent();
		intent.setDisplayName(intentName.getText());
		intent.setIsNegotiatable(negotiable.selectedProperty().getValue());
		switch (selectedActionType) {
		case PATH:
			Path path = new Path();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, false);
			path.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			path.setDestination(serviceUtils.createSubject(destCps.get(0)));
			intent.setAction(path);
			break;
		case SDWAN:
			SDWAN sdwan = new SDWAN();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, false);
			sdwan.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			sdwan.setDestination(serviceUtils.createSubject(destCps.get(0)));
			intent.setAction(sdwan);
			break;
		case VPN:
			VPN vpn = new VPN();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, false);
			vpn.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			vpn.setDestination(serviceUtils.createSubject(destCps.get(0)));
			intent.setAction(vpn);
			break;
		case CONNECTION:
			Connection connection = new Connection();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, false);
			connection.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			connection.setDestination(serviceUtils.createSubject(destCps.get(0)));
			intent.setAction(connection);
			break;
		case TREE:
			Tree tree = new Tree();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, true);
			tree.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			tree.setDestination(serviceUtils.createSubjects(destCps));
			intent.setAction(tree);
			break;
		case MESH:
			Mesh mesh = new Mesh();
			System.out.println("Mesh intents !");
			sourceCps = getSelConnectionPoints(source, true);
			mesh.setSource(serviceUtils.createSubjects(sourceCps));
			intent.setAction(mesh);
			break;
		case AGGREGATE:
			Aggregate aggregate = new Aggregate();
			sourceCps = getSelConnectionPoints(source, true);
			destCps = getSelConnectionPoints(destination, false);
			aggregate.setSource(serviceUtils.createSubjects(sourceCps));
			aggregate.setDestination(serviceUtils.createSubject(destCps.get(0)));
			intent.setAction(aggregate);
			break;
		case MULTICAST:
			Multicast multicast = new Multicast();
			sourceCps = getSelConnectionPoints(source, false);
			destCps = getSelConnectionPoints(destination, true);
			multicast.setSource(serviceUtils.createSubject(sourceCps.get(0)));
			multicast.setDestination(serviceUtils.createSubjects(destCps));
			intent.setAction(multicast);
			break;
		}
		intent.setConstraints(buildConstraint());
		return intent;
	}

	private List<Constraint> buildConstraint() {
		List<Constraint> constraints = new ArrayList<Constraint>();
		String bw = bandwidth.getText();
		if (!(bw == null && bw.length() == 0)) {
			String selBwUnit = bwUnit.getSelectionModel().getSelectedItem();
			BandwidthConstraint bandwidthConstraint = new BandwidthConstraint();
			bandwidthConstraint.setBitrate(bw + selBwUnit);
			constraints.add(bandwidthConstraint);
		}
		String lat = latency.getText();
		if (lat != null && lat.length() != 0) {
			String selLUnit = lUnit.getSelectionModel().getSelectedItem();
			DelayConstraint delayConstraint = new DelayConstraint();
			delayConstraint.setLatency(lat + selLUnit);
			constraints.add(delayConstraint);
		}
		SecurityConstraint securityConstraint = buildSecurityConstraint();
		if (null != securityConstraint) {
			constraints.add(securityConstraint);
		}
		if (availChk.isSelected()){
			AvailabilityConstraint availabilityConstraint = new AvailabilityConstraint();
			availabilityConstraint.availability(avail.getSelectionModel().selectedItemProperty().getValue());
			constraints.add(availabilityConstraint);
		}
		
		HighAvailabilityConstraint availabilityConstraint = new HighAvailabilityConstraint();
		availabilityConstraint.setAvailability(highAva.selectedProperty().getValue());
		constraints.add(availabilityConstraint);
		return constraints;
	}

	private SecurityConstraint buildSecurityConstraint() {
		SecurityConstraint securityConstraint;
		String selSecurity = security.getSelectionModel().getSelectedItem();
		securityConstraint = serviceUtils.buildSecurityConstraint(selSecurity);
		return securityConstraint;
	}

	private List<ConnectionPoint> getSelConnectionPoints(ListView<ListItem> list, boolean isCheckBoxes) {
		List<ConnectionPoint> cps = new ArrayList<ConnectionPoint>();
		if (!isCheckBoxes) {
			ConnectionPoint point = new ConnectionPoint();
			point.setName(list.getSelectionModel().getSelectedItem().getName());
			cps.add(point);
		} else {
			List<ListItem> items = list.getItems();
			for (ListItem item : items) {
				if (item.isOn()) {
					ConnectionPoint point = new ConnectionPoint();
					point.setName(item.getName());
					cps.add(point);
				}
			}
		}
		return cps;
	}	
}