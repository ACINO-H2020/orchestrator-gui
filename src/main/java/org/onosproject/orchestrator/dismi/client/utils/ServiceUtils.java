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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.onosproject.orchestrator.dismi.client.controllers.DismiControllerManager;
import org.onosproject.orchestrator.dismi.client.cps.ListItem;
import org.onosproject.orchestrator.dismi.client.restcli.ConnectionPointOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint.EncStrengthEnum;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint.IntStrengthEnum;
import org.onosproject.orchestrator.dismi.primitives.Subject;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ServiceUtils {

	public enum ACTION_TYPE {
		PATH, CONNECTION, TREE, MESH, MULTICAST, AGGREGATE, SDWAN, VPN
	};

	public void handleNewServiceGui(DismiControllerManager dismiControllerManager, VBox contentContainer) {
		try {
			Parent rootIntent = dismiControllerManager.loadIntentWindow();//FXMLLoader.load(getClass().getResource("/icons/intentVBox.fxml"));
			contentContainer.getChildren().add(rootIntent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ObservableList<String> getActionTypes() {
		ObservableList<String> actionTypes = FXCollections.observableArrayList();
		actionTypes.add("Path");
		actionTypes.add("Tree");
		actionTypes.add("Mesh");
		actionTypes.add("Multicast");
		actionTypes.add("Aggregate");
		actionTypes.add("Connection");
        actionTypes.add("SDWAN");
		actionTypes.add("VPN");
		return actionTypes;
	}

	public ACTION_TYPE selectActionTypes(String action) {

		if (action.equals("Path")) {
			return ACTION_TYPE.PATH;
		}
		if (action.equals("SDWAN")) {
			return ACTION_TYPE.SDWAN;
		}
		if (action.equals("VPN")) {
			return ACTION_TYPE.VPN;
		}

		if (action.equals("Tree")) {
			return ACTION_TYPE.TREE;
		}
		if (action.equals("Mesh")) {
			return ACTION_TYPE.MESH;
		}
		if (action.equals("Multicast")) {
			return ACTION_TYPE.MULTICAST;
		}
		if (action.equals("Aggregate")) {
			return ACTION_TYPE.AGGREGATE;
		}
		if (action.equals("Connection")) {
			return ACTION_TYPE.CONNECTION;
		} else
			return null;
	}

	public ObservableList<String> getBandwidthUnits() {
		ObservableList<String> bwUnits = FXCollections.observableArrayList();
		bwUnits.add("bps");
		bwUnits.add("kbps");
		bwUnits.add("mbps");
		bwUnits.add("gbps");
		bwUnits.add("tbps");
		return bwUnits;
	}
	
	public ObservableList<String> getAvailability() {
		ObservableList<String> availability= FXCollections.observableArrayList();
		availability.add("99.9999%");
		availability.add("99.9990%");
		availability.add("99.9900%");
		availability.add("99.9000%");
		availability.add("99.8000%");
		availability.add("99.7000%");
		availability.add("99.6000%");
		availability.add("99.5000%");
		availability.add("99.0000%");
		availability.add("98.5000%");
		availability.add("98.0000%");
		availability.add("97.5000%");
		availability.add("97.0000%");
		availability.add("96.5000%");
		availability.add("96.0000%");
		availability.add("95.5000%");
		availability.add("95.0000%");
		availability.add("94.5000%");
		availability.add("94.0000%");
		availability.add("93.5000%");
		availability.add("93.0000%");
		availability.add("92.5000%");
		availability.add("92.0000%");
		availability.add("91.5000%");
		availability.add("91.0000%");
		availability.add("90.5000%");
		availability.add("90.0000%");
		
		return availability;
	}
	

	public ObservableList<String> getTimeUnits() {
		ObservableList<String> timeUnits = FXCollections.observableArrayList();
		timeUnits.add("us");
		timeUnits.add("ms");
		timeUnits.add("s");
		timeUnits.add("m");
		timeUnits.add("h");
		timeUnits.add("d");
		timeUnits.add("w");
		return timeUnits;
	}

	public ObservableList<String> getSecurityOptions() {
		ObservableList<String> timeUnits = FXCollections.observableArrayList();
		timeUnits.add("No Security");
		timeUnits.add("Encryption Only");
		timeUnits.add("Encryption (AES128)");
		timeUnits.add("Encryption (AES256)");
		timeUnits.add("Integrity Only");
		timeUnits.add("Integrity (SHA256)");
		timeUnits.add("Integrity (SHA384)");
		timeUnits.add("Encryption(128) & Integrity(256)");
		timeUnits.add("Encryption(128) & Integrity(384)");
		timeUnits.add("Encryption(256) & Integrity(256)");
		timeUnits.add("Encryption(256) & Integrity(384)");
		return timeUnits;
	}

	public ObservableList<ListItem> getAllConnectionPoints() {
		ObservableList<ListItem> cps = FXCollections.observableArrayList();
		try {

			String basicPath = "connectionpoint";
			ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
			ReturnedStatus returnedStatus = connectionPointOperations.getAllConnectionPoints();// postConnectionPoint(connectionPointExtended);
			if (returnedStatus.getStatus() == STATUS.SUCCESS) {
				List<ConnectionPoint> connectionPoints = (List<ConnectionPoint>) returnedStatus.getInfomration();
				for (int i = 0; i < connectionPoints.size(); i++) {
					ListItem li = new ListItem(connectionPoints.get(i).getName(), false);
					cps.add(li);
				}
			}else {
				Alert alert1 = new Alert(AlertType.ERROR);
				alert1.setTitle("Dismi Error");
				alert1.setHeaderText("Dismi Service Connection");
				alert1.setContentText(returnedStatus.getInfomration().toString());
				alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert1.showAndWait();
			}
		} catch (Exception exp) {

		}
		return cps;
	}

	public void findCpOptions(String action, ListView<ListItem> source, ListView<ListItem> destination, Label dstlab) {
		if (action.equals("Path")) {
			source.setVisible(true);
			destination.setVisible(true);
			dstlab.setVisible(true);
			setSimpleOption4Source(source);
			setSimpleOption4Source(destination);
			return;
		}
        if (action.equals("SDWAN")) {
            source.setVisible(true);
            destination.setVisible(true);
            dstlab.setVisible(true);
            setSimpleOption4Source(source);
            setSimpleOption4Source(destination);
            return;
        }
		if (action.equals("Tree")) {
			source.setVisible(true); // One source 
			destination.setVisible(true); // Multiple options
			dstlab.setVisible(true);
			setCheckBoxOption4Source(destination);
			return;
		}
		if (action.equals("Mesh")) {
			source.setVisible(true); // Multiple options
			destination.setVisible(false); // No destinations
			dstlab.setVisible(false);
			setCheckBoxOption4Source(source);
			return;
		}
		if (action.equals("Multicast")) {
			source.setVisible(true); // One source 
			destination.setVisible(true); // Multiple options
			dstlab.setVisible(true);
			setSimpleOption4Source(source);
			setCheckBoxOption4Source(destination);
			return;
		}
		if (action.equals("Aggregate")) {
			source.setVisible(true); // Multiple options
			destination.setVisible(true); // One source
			dstlab.setVisible(true);
			setCheckBoxOption4Source(source);
			setSimpleOption4Source(destination);
			
			return;
		}
		if (action.equals("Connection")) {
			source.setVisible(true);
			destination.setVisible(true);
			dstlab.setVisible(true);
			setSimpleOption4Source(source);
			setSimpleOption4Source(destination);
			return;
		} else {
			source.setVisible(true);
			destination.setVisible(true);
			dstlab.setVisible(true);
			setSimpleOption4Source(source);
			setSimpleOption4Source(destination);
			return;
		}
	}

	private void setCheckBoxOption4Source(ListView<ListItem> listView) {
		listView.setCellFactory(CheckBoxListCell.forListView(new Callback<ListItem, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(ListItem item) {
				return item.onProperty();
			}
		}));
		listView.refresh();
	}

	private void setSimpleOption4Source(ListView<ListItem> listView) {
		listView.setCellFactory(null);
		listView.refresh();
	}

	public List<Subject> createSubjects(List<ConnectionPoint> connectionPoint) {
		List<Subject> subjects = new ArrayList<Subject>();

		for (ConnectionPoint point : connectionPoint) {
			Subject subject = new Subject();
			subject.setConnectionPoint(point);
			subjects.add(subject);
		}
		return subjects;
	}

	public Subject createSubject(ConnectionPoint connectionPoint) {
		Subject subject = new Subject();
		subject.setConnectionPoint(connectionPoint);
		return subject;
	}

	public SecurityConstraint buildSecurityConstraint(String selOption) {
		SecurityConstraint constraint = new SecurityConstraint();

		if (selOption.equals("No Security")) {
			return null;
		} else if (selOption.equals("Encryption Only") || selOption.equals("Encryption (AES128)")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES128);
		} else if (selOption.equals("Encryption (AES256")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES256);
		} else if (selOption.equals("Integrity Only") || selOption.equals("Integrity (SHA256)")) {
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA256);
		} else if (selOption.equals("Integrity (SHA384)")) {
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA384);
		} else if (selOption.equals("Encryption(128) & Integrity(256)")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES128);
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA256);
		} else if (selOption.equals("Encryption(128) & Integrity(384)")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES128);
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA384);
		} else if (selOption.equals("Encryption(256) & Integrity(256)")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES256);
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA256);
		} else if (selOption.equals("Encryption(256) & Integrity(384)")) {
			constraint.setEncryption(true);
			constraint.setEncStrength(EncStrengthEnum.AES256);
			constraint.setIntegrity(true);
			constraint.setIntStrength(IntStrengthEnum.SHA384);
		}
		return constraint;
	}
}
