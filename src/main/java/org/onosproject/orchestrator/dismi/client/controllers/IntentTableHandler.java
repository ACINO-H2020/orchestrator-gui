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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.onosproject.orchestrator.dismi.api.ApiResponseMessage;
import org.onosproject.orchestrator.dismi.client.IntentEntity;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus;
import org.onosproject.orchestrator.dismi.client.restcli.ReturnedStatus.STATUS;
import org.onosproject.orchestrator.dismi.client.restcli.ServicesOperations;
import org.onosproject.orchestrator.dismi.client.utils.ImageClickCellFactory;
import org.onosproject.orchestrator.dismi.client.utils.StatusImage;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.extended.IntentList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class IntentTableHandler {
	private static String name = "IntentTableHandler";
	private final Logger log = Logger.getLogger(name);
	private TableView<IntentEntity> intentTable;
	private ObservableList<IntentEntity> dataList = FXCollections.observableArrayList();
	private Hashtable<String, Intent> intents = new Hashtable<String, Intent>();

	private boolean isIntentTableActive = false;
	public IntentTableHandler(TableView intentTable) {
		this.intentTable = intentTable;
		ObservableList<TableColumn> columns = intentTable.getColumns();
		int i = 0;
		for (TableColumn tc : columns) {
			//
			if (tc.getText().equalsIgnoreCase("status")) {
				tc.setCellValueFactory(new PropertyValueFactory<IntentEntity, StatusImage>(IntentEntity.ids().get(i)));
				ImageClickCellFactory cellFactory = new ImageClickCellFactory();
				tc.setCellFactory(cellFactory);

			} else {
				tc.setCellValueFactory(new PropertyValueFactory<IntentEntity, String>(IntentEntity.ids().get(i)));
			}
			i++;
		}
		// Add event on Row
		intentTable.setRowFactory(tv -> {
			TableRow<IntentEntity> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				// intentTable.getParent().setVisible(false);
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					IntentEntity rowData = row.getItem();
				}
			});
			return row;
		});
		this.intentTable.setRowFactory((tableView) -> {
			return new TooltipTableRow<IntentEntity>((IntentEntity intentEntity) -> {
				return intentEntity.toString();
			});
		});
		intentTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new CreateStatusClickMouseEventHandler());

	}

	public int showDilaog(Parent root) {

		ButtonType cancelButtonType = new ButtonType("Cancel", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
		ButtonType selectButtonType = new ButtonType("Select", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
		ButtonType deleteButtonType = new ButtonType("Delete", javafx.scene.control.ButtonBar.ButtonData.FINISH);
		javafx.scene.control.Dialog<ButtonType> dialog = new javafx.scene.control.Dialog<>();
		final DialogPane grid = new DialogPane();
		grid.setContent(root);
		dialog.setDialogPane(grid);
		dialog.setTitle("Select Alternative Solution");
		dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, cancelButtonType, deleteButtonType );
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == selectButtonType) {
			return 1;
		}
		if (result.get() == deleteButtonType) {
			return 2;
		}
		return 0;

	}
	// This class implement the mouse event handler
	private class CreateStatusClickMouseEventHandler implements EventHandler<MouseEvent> {
		public void handle(MouseEvent event) {
			if (event.getClickCount() == 2) {
				IntentEntity intentEntity = intentTable.getSelectionModel().getSelectedItem();
				if(intentEntity == null){
					return;
				}
				if (intentEntity.getStatus().equals("NEGOTIATION") ) {
					String intntId = intentEntity.getIntent_Id();

					List<Intent> asIntents = displayAlternativeSolutions(intntId);
					if(null == asIntents){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Alternative Solutions");
						alert.setHeaderText(null);
						alert.setContentText("Coluld not find alternative solution(s) for selected intent !");
						alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
						alert.showAndWait();
						return;
					}
					DismiControllerManager dismiControllerManager = new DismiControllerManager();
					Parent root = null;
					try {
						root = dismiControllerManager.loadIntentASSelector();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ASController asController = dismiControllerManager.getAsController();
					asController.setIntentList(asIntents);
					int result = showDilaog(root);					
					if (result == 1){
						String intentId = intentEntity.getIntent_Id();
						String updatedintent_id = asController.getSelected().getIntentId();
						String basicPath = "service";
						ServicesOperations servicesOperations = new ServicesOperations(basicPath);
						servicesOperations.serviceUpdateIntentPut(intentId, updatedintent_id);

					} else if (result == 2){
						String intentId = intentEntity.getIntent_Id();
						String serviceid = extractServiceId(intentEntity.getIntent_Id());
						String basicPath = "service";
						ServicesOperations servicesOperations = new ServicesOperations(basicPath);
						servicesOperations.serviceUpdateIntentDelete(serviceid, intentId);
					}  
				}
			}
		}
	}
	  public String extractServiceId(String dismiIntentId){
	        String serviceId = null;
	        if (null != dismiIntentId && dismiIntentId.lastIndexOf("-") >= 0) {
	            serviceId = dismiIntentId.substring(0, dismiIntentId.lastIndexOf("-"));
	        }
	        return serviceId;
	    }

	public void setItems(ObservableList<Intent> intentList) {
		if(intentList.size()==0){
			dataList = FXCollections.observableArrayList();
		}
		if (!this.dataList.isEmpty()) {
			dataList = FXCollections.observableArrayList();
		}
		this.intents = new Hashtable<String, Intent>();
		int i = 1;
		for (Intent anIntent : intentList) {
			intents.put("" + i, anIntent);
			IntentEntity intentEntity = new IntentEntity(i, anIntent);
			dataList.add(intentEntity);
			i++;
		}

		intentTable.setItems(dataList);
		intentTable.refresh();
	}

	public void setIntent(Intent intent) {
		intents.put("" + this.intents.size() + 1, intent);
		IntentEntity intentEntity = new IntentEntity(this.intents.size(), intent);
		intentTable.getItems().add(intentEntity);
		intentTable.refresh();
	}

	public List<Intent> getCollectedIntetns() {
		Collection<Intent> intCol = intents.values();
		List<Intent> tempIntentList = new ArrayList<Intent>();
		for (Intent temp : intCol) {
			tempIntentList.add(temp);
		}
		return tempIntentList;
	}

	private List<Intent> displayAlternativeSolutions(String intentId) {
		return fetchAlternativeSolutions(intentId);
	}

	private List<Intent> fetchAlternativeSolutions(String intentId) {
		String basicPath = "service";
		ServicesOperations sp = new ServicesOperations(basicPath);
		ReturnedStatus returnedStatus = sp.fetchAlternativeSolutions(intentId);
		if (returnedStatus.getStatus() == STATUS.SUCCESS) {
			IntentList intentList = (IntentList) returnedStatus.getInfomration();
			if(intentList.size() == 0 ){
				return null;
			}
			List<Intent> intents = new ArrayList<>();
			for(int i=0; i< intentList.size(); i++){
				intents.add(intentList.get(i));
			}
			return intentList;
		} else{
			if (returnedStatus.getInfomration() instanceof ApiResponseMessage){
				ApiResponseMessage apiResponseMessage = (ApiResponseMessage) returnedStatus.getInfomration() ;
				log.error(apiResponseMessage .getMessage());
			}
			if (returnedStatus.getInfomration() instanceof Exception){
				Exception exception = (Exception) returnedStatus.getInfomration() ;
				log.error(exception.getMessage());
			}
		}
		return null;
	}
}
