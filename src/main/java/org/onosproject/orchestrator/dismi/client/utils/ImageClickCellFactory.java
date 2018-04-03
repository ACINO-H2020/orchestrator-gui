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

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class ImageClickCellFactory implements Callback<TableColumn, TableCell> {

    public ImageClickCellFactory() {
        
    }

    
    public TableCell call(TableColumn p) {
        TableCell<Object, StatusImage> cell
                = new TableCell<Object, StatusImage>() {

                    private final VBox vbox;
                    private final ImageView imageview;

                    // Constructor
                    {
                        vbox = new VBox();
                        vbox.setAlignment(Pos.CENTER);
                        imageview = new ImageView();
                        imageview.setFitHeight(16);
                        imageview.setFitWidth(16);
                        imageview.setVisible(true);
                        imageview.setCache(true);
                        vbox.getChildren().addAll(imageview);
                        setGraphic(vbox);
                    }

                    @Override
                    protected void updateItem(StatusImage item,
                            boolean empty) {

                        // calling super here is very important - don't skip this!
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            Image image = new Image(
                                    getClass().getResourceAsStream(
                                            item.getStatusImage()));

                            imageview.setImage(image);
                            setGraphic(vbox);
                        }
                    }
                };
        return cell;
    }
}