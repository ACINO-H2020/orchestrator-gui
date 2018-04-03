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

import java.util.HashSet;
import java.util.Set;

import org.onosproject.orchestrator.dismi.primitives.ConnectionPoint;
import org.onosproject.orchestrator.dismi.primitives.EndPoint;
import org.onosproject.orchestrator.dismi.primitives.EthEndPoint;
import org.onosproject.orchestrator.dismi.primitives.FiberEndPoint;
import org.onosproject.orchestrator.dismi.primitives.IPEndPoint;
import org.onosproject.orchestrator.dismi.primitives.LambdaEndPoint;
import org.onosproject.orchestrator.dismi.primitives.extended.ConnectionPointExtended;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ConnectionPointController {

	@FXML
	private ComboBox<String> ncpType;
	@FXML
	private Label ncpLab1;
	@FXML
	private Label ncpLab2;
	@FXML
	private Label ncpLab3;
	@FXML
	private Label ncpLab4;
	@FXML
	private TextField ncpText1;
	@FXML
	private TextField ncpText2;
	@FXML
	private TextField ncpText3;
	@FXML
	private TextField ncpText4;
	@FXML
	private Button ncpSave;
	@FXML
	private ListView<EndPoint> ncpListView;
	
	// the initialize method is automatically invoked by the FXMLLoader
	public void initialize() {
		ncpType.getItems().add(SelectionEndPoint.IP_ENDPOINT.value);
		ncpType.getItems().add(SelectionEndPoint.FIBER_ENDPOINT.value);
		ncpType.getItems().add(SelectionEndPoint.LAMBDA_ENDPOINT.value);
		ncpType.getItems().add(SelectionEndPoint.ETHERNET_ENDPOINT.value);
		ncpType.getSelectionModel().select(SelectionEndPoint.IP_ENDPOINT.value);
		// Default Option : Init form and set to get value for IP End point
		setVisiabilityAll(true);
		setClearAll();
		ncpLab1.setText("In Address:");
		ncpLab2.setText("Subnet mask:");
		ncpLab3.setText("Router URL");
		ncpLab4.setText("Port ID");
		ncpSave.addEventHandler(MouseEvent.MOUSE_CLICKED, new SaveBtnHandler());
		ncpType.getSelectionModel().selectedItemProperty().addListener(new ValChangedListner());
		ncpListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EpListHandler());
	}

	private void setVisiabilityAll(boolean status) {
		ncpLab1.setVisible(status);
		ncpLab2.setVisible(status);
		ncpLab3.setVisible(status);
		ncpLab4.setVisible(status);
		ncpText1.setVisible(status);
		ncpText2.setVisible(status);
		ncpText3.setVisible(status);
		ncpText4.setVisible(status);

	}

	private void setClearAll() {
		ncpLab1.setText("");
		ncpLab2.setText("");
		ncpLab3.setText("");
		ncpLab3.setText("");
		ncpText1.setText("");
		ncpText2.setText("");
		ncpText3.setText("");
		ncpText4.setText("");
	}

	public enum SelectionEndPoint {

		IP_ENDPOINT("IP Endpoint"), FIBER_ENDPOINT("Fiber Endpoint"), LAMBDA_ENDPOINT(
				"Lambda Endpoint"), ETHERNET_ENDPOINT("Ethernet Endpoint");

		private String value;

		SelectionEndPoint(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}

	private EndPoint createEndPoint() {
		EndPoint endPoint = null;
		String selectedItem = ncpType.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return endPoint;
		}

		if (selectedItem.equalsIgnoreCase(SelectionEndPoint.IP_ENDPOINT.value)) {
			IPEndPoint ipEndPoint = new IPEndPoint();
			ipEndPoint.setInAddr(ncpText1.getText());
			ipEndPoint.setSubnets(ncpText2.getText());
			ipEndPoint.setRouterId(ncpText3.getText());
			ipEndPoint.setPortId(Integer.parseInt(ncpText4.getText()));
			endPoint = ipEndPoint;

		} else if (selectedItem.equalsIgnoreCase(SelectionEndPoint.FIBER_ENDPOINT.value)) {
			FiberEndPoint fiberEndPoint = new FiberEndPoint();
			fiberEndPoint.setSwitchId(ncpText1.getText());
			fiberEndPoint.setPortId(Integer.parseInt(ncpText2.getText()));
			endPoint = fiberEndPoint;

		} else if (selectedItem.equalsIgnoreCase(SelectionEndPoint.LAMBDA_ENDPOINT.value)) {
			LambdaEndPoint lambdaEndPoint = new LambdaEndPoint();
			lambdaEndPoint.setSwitchId(ncpText1.getText());
			lambdaEndPoint.setPortId(Integer.parseInt(ncpText2.getText()));
			lambdaEndPoint.setLambda(Double.parseDouble(ncpText3.getText()));
			lambdaEndPoint.setWitdh(Double.parseDouble(ncpText4.getText()));
			endPoint = lambdaEndPoint;

		} else if (selectedItem.equalsIgnoreCase(SelectionEndPoint.ETHERNET_ENDPOINT.value)) {
			EthEndPoint ethEndPoint = new EthEndPoint();
			ethEndPoint.setSwitchId(ncpText1.getText());
			ethEndPoint.setPortId(Integer.parseInt(ncpText2.getText()));
			ethEndPoint.setMacAddress(ncpText3.getText());
			endPoint = ethEndPoint;
		}
		return endPoint;
	}

	private void fillFiledsWithEP(EndPoint endPoint) {

		if (endPoint == null) {
			return;
		}

		if (endPoint instanceof IPEndPoint) {
			ncpType.getSelectionModel().select(SelectionEndPoint.IP_ENDPOINT.value);
			IPEndPoint ipEndPoint = (IPEndPoint) endPoint;
			ncpText1.setText(ipEndPoint.getInAddr());
			ncpText2.setText(ipEndPoint.getSubnets());
			ncpText3.setText(ipEndPoint.getRouterId());
			ncpText4.setText("" + ipEndPoint.getPortId());		

		} else if (endPoint instanceof FiberEndPoint) {
			ncpType.getSelectionModel().select(SelectionEndPoint.FIBER_ENDPOINT.value);
			FiberEndPoint fiberEndPoint = (FiberEndPoint) endPoint;
			ncpText1.setText(fiberEndPoint.getSwitchId());
			ncpText2.setText("" + fiberEndPoint.getPortId());			

		} else if (endPoint instanceof LambdaEndPoint) {
			ncpType.getSelectionModel().select(SelectionEndPoint.LAMBDA_ENDPOINT.value);
			LambdaEndPoint lambdaEndPoint = (LambdaEndPoint) endPoint;
			ncpText1.setText(lambdaEndPoint.getSwitchId());
			ncpText2.setText("" + lambdaEndPoint.getPortId());
			ncpText3.setText("" + lambdaEndPoint.getLambda());
			ncpText4.setText("" + lambdaEndPoint.getWitdh());			

		} else if (endPoint instanceof EthEndPoint) {
			ncpType.getSelectionModel().select(SelectionEndPoint.ETHERNET_ENDPOINT.value);
			EthEndPoint ethEndPoint = (EthEndPoint) endPoint;
			ncpText1.setText(ethEndPoint.getSwitchId());
			ncpText2.setText("" + ethEndPoint.getPortId());
			ncpText3.setText(ethEndPoint.getMacAddress());			
		}
	}

	class ValChangedListner implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> arg0, String arg1, String newSelected) {
			if (newSelected != null) {
				if (newSelected.equalsIgnoreCase(SelectionEndPoint.IP_ENDPOINT.value)) {
					setVisiabilityAll(true);
					setClearAll();
					ncpLab1.setText("In Address:");
					ncpLab2.setText("Subnet mask:");
					ncpLab3.setText("Router URL");
					ncpLab4.setText("Port ID");
				} else if (newSelected.equalsIgnoreCase(SelectionEndPoint.FIBER_ENDPOINT.value)) {
					setVisiabilityAll(false);
					setClearAll();
					ncpLab1.setText("Switch ID:");
					ncpLab2.setText("Port ID:");
					ncpLab1.setVisible(true);
					ncpLab2.setVisible(true);
					ncpText1.setVisible(true);
					ncpText2.setVisible(true);

				} else if (newSelected.equalsIgnoreCase(SelectionEndPoint.LAMBDA_ENDPOINT.value)) {
					setVisiabilityAll(true);
					setClearAll();
					ncpLab1.setText("Switch ID:");
					ncpLab2.setText("Port ID:");
					ncpLab3.setText("Lambda");
					ncpLab4.setText("Width");
				} else if (newSelected.equalsIgnoreCase(SelectionEndPoint.ETHERNET_ENDPOINT.value)) {
					setVisiabilityAll(true);
					ncpText4.setVisible(false);
					ncpLab4.setVisible(false);
					setClearAll();
					ncpLab1.setText("Switch ID:");
					ncpLab2.setText("Port ID:");
					ncpLab3.setText("MAC Address");
				}
			}
		}
	}

	class SaveBtnHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			EndPoint ep = createEndPoint();
			if (null != ep) {
				ncpListView.getItems().add(ep);
				setClearAll();
				ncpType.getSelectionModel().select(SelectionEndPoint.IP_ENDPOINT.value);
			}
		}
	}

	class EpListHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {

			if (event.getClickCount() == 2) {
				EndPoint endPoint = ncpListView.getSelectionModel().getSelectedItem();
				if (null == endPoint) {
					return;
				}
				setClearAll();
				fillFiledsWithEP(endPoint);
				ncpListView.getItems().remove(ncpListView.getSelectionModel().getSelectedIndex());
			}
		}
	}
	public void setConnectionPoint(ConnectionPoint connectionPoint){
		ConnectionPointExtended  connectionPointExtended = (ConnectionPointExtended) connectionPoint;
		ObservableList<EndPoint > np = FXCollections.observableArrayList(); 
		Set<EndPoint> setNP = connectionPointExtended.getEndpoints();
		for(EndPoint endpoint : setNP )
			np.add(endpoint);
		ncpListView.setItems(np);
	}
	
	public void setSaveButtonTitle(String title){
		ncpSave.setText(title);
	}
	
	public ConnectionPoint getConnectionPoint(){
		ConnectionPointExtended  connectionPointExtended = new ConnectionPointExtended();
		ObservableList<EndPoint > np = ncpListView.getItems();
		Set<EndPoint> setNP = new HashSet<EndPoint>();
		for(EndPoint endpoints : np)
			setNP.add(endpoints);
		connectionPointExtended.setEndpoints(setNP);
		return connectionPointExtended;
	}
	
}
