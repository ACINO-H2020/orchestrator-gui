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

package org.onosproject.orchestrator.dismi.client.cps;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.onosproject.orchestrator.dismi.client.restcli.ConnectionPointOperations;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.utils.CpUtils;
import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.EndPoint;
import org.onosproject.orchestrator.dismi.primitives.EthEndPoint;
import org.onosproject.orchestrator.dismi.primitives.FiberEndPoint;
import org.onosproject.orchestrator.dismi.primitives.IPEndPoint;
import org.onosproject.orchestrator.dismi.primitives.LambdaEndPoint;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointExtended;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class ViewConnectionPoint {

	public enum SELECTION {
		SINGLE, MULTIPLE
	};

	private ObservableList<ConnectionPoint> cps = FXCollections.observableArrayList();
	private List<ConnectionPoint> selectedItems = new ArrayList<ConnectionPoint>();
	private SELECTION selectionMode;

	public ViewConnectionPoint(ObservableList<ConnectionPoint> cps) {
		this.cps = cps;
		this.selectionMode = selectionMode;
	}

	public void init(CpUtils cpUtils) {
		ListView<ConnectionPoint> cpsView = new ListView<>();
		for (ConnectionPoint item : cps) {
			cpsView.getItems().add(item);
		}
		ListView<String> reactionLog = new ListView<>();

		cpsView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				reactionLog.getItems().clear();
				ConnectionPointExtended cpe = (ConnectionPointExtended) cpsView.getSelectionModel().getSelectedItem();
				String basicPath = "connectionpoint";
				ConnectionPointOperations connectionPointOperations = new ConnectionPointOperations(basicPath);
				ReturnedStatus returnedStatus = connectionPointOperations.getConnectionPointByName(cpe.getName());// postConnectionPoint(connectionPointExtended);
				if (returnedStatus.getStatus() == STATUS.SUCCESS) {
					cpe = (ConnectionPointExtended) returnedStatus.getInfomration();
					Set<EndPoint> endpoints = cpe.getEndpoints();
					for (EndPoint ep : endpoints) {
						ObservableList<String> temp = endpointDescription(ep);
						for (String anItem : temp) {
							reactionLog.getItems().add(anItem);
						}
					}

				}else{					
					cpUtils.sendEvent("1:"+returnedStatus.getInfomration().toString());
				}

			}
		});

		// -----------------------------
		reactionLog.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> p) {

				ListCell<String> cell = new ListCell<String>() {
					@Override
					protected void updateItem(String t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t);							
							if (t.contains("<<"))
								setStyle("-fx-control-inner-background: green");
							else
								setStyle("-fx-control-inner-background: white");
						} else {
							setText("");
						}
					}

				};
				return cell;
			}
		});
		// ----------------------------

		HBox layout = new HBox(10, cpsView, reactionLog);
		layout.setPrefSize(550, 150);
		layout.setPadding(new Insets(10));
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Connection Points");
		alert.setHeaderText("Detail of Connection Point(s)");

		alert.getDialogPane().setContent(layout);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.setGraphic(ImageHandler.CP_ICON);
		Optional<ButtonType> result = alert.showAndWait();
	}

	private ObservableList<String> endpointDescription(EndPoint endPoint) {
		ObservableList<String> epDesc = FXCollections.observableArrayList();
		String str = "";
		if (endPoint instanceof IPEndPoint) {
			IPEndPoint ep = (IPEndPoint) endPoint;
			epDesc.add("<< IPEndPoint >>");
			epDesc.add("Router URL:");
			epDesc.add("  " + ep.getRouterId());
			epDesc.add("Port Id:");
			epDesc.add("  " + ep.getPortId());
			epDesc.add("Address:");
			epDesc.add("  " + ep.getInAddr());
			epDesc.add("Subnet-mask:");
			epDesc.add("  " + ep.getSubnets());

		} else if (endPoint instanceof FiberEndPoint) {
			FiberEndPoint ep = (FiberEndPoint) endPoint;
			epDesc.add("<< FiberEndPoint >>");
			epDesc.add("Switch Id:");
			epDesc.add("  " + ep.getSwitchId());
			epDesc.add("Port Id:");
			epDesc.add("  " + ep.getPortId());

		} else if (endPoint instanceof LambdaEndPoint) {
			LambdaEndPoint ep = (LambdaEndPoint) endPoint;
			epDesc.add("<< LambdaEndPoint >>");
			epDesc.add("Switch Id:");
			epDesc.add("  " + ep.getSwitchId());
			epDesc.add("Port Id:");
			epDesc.add("  " + ep.getPortId());
			epDesc.add("Lambda:");
			epDesc.add("  " + ep.getLambda());
			epDesc.add("Witdh:");
			epDesc.add("  " + ep.getWitdh());

		} else if (endPoint instanceof EthEndPoint) {
			EthEndPoint ep = (EthEndPoint) endPoint;
			epDesc.add("<< EthEndPoint >>");
			epDesc.add("Switch Id:");
			epDesc.add("  " + ep.getSwitchId());
			epDesc.add("Port Id:");
			epDesc.add("  " + ep.getPortId());
			epDesc.add("MAC Address:");
			epDesc.add("  " + ep.getMacAddress());

		} else {
			epDesc.add("No end point found !");
		}
		return epDesc;
	}

}