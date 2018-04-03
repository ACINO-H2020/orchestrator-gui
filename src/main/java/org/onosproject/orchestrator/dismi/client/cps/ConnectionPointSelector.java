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

import org.onosproject.orchestrator.dismi.client.utils.ImageHandler;
import org.onosproject.orchestrator.dismi.client.utils.ServiceUtils;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class ConnectionPointSelector {

	public enum SELECTION {SINGLE,MULTIPLE};
	private ObservableList<ListItem> cps = FXCollections.observableArrayList();
	private List <ListItem> selectedItems = new ArrayList<ListItem>();
	private SELECTION selectionMode; 
	public ConnectionPointSelector(ObservableList<ListItem> cps) {
		this.cps = cps;
		this.selectionMode =selectionMode;
	}

	public void init() {
		ListView<ListItem> cpsView = new ListView<>();
		for (ListItem item : cps) {
			item.onProperty().addListener((obs, wasOn, isNowOn) -> {
				if (!wasOn){
					selectedItems.add(item); 
				}
				else{
					if (selectedItems.contains(item)){
						selectedItems.remove(item);
					}
				}
			});
			cpsView.getItems().add(item);
		}
		
		cpsView.setCellFactory(CheckBoxListCell.forListView(new Callback<ListItem, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(ListItem item) {
				
				return item.onProperty();
			}
		}));
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Connection Points");
		alert.setHeaderText("Select Connection Point(s)");
		
		alert.getDialogPane().setContent(cpsView);
		alert.setGraphic(ImageHandler.CP_ICON);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

		} else {

		}
	}

	
	public List <ListItem> getSelectedItems(){
		return selectedItems; 
	}
	public static void main(String arg[]){
		ServiceUtils serviceUtils = new ServiceUtils();
		ConnectionPointSelector connectionPointSelector = new ConnectionPointSelector(serviceUtils.getAllConnectionPoints());
		connectionPointSelector .init();
	}
}