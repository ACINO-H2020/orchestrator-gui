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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ListItem {
	private final StringProperty name = new SimpleStringProperty();
	private final BooleanProperty on = new SimpleBooleanProperty();

	public ListItem(String name, boolean on) {
		setName(name);
		setOn(on);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}
	
	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final BooleanProperty onProperty() {
		return this.on;
	}

	public final boolean isOn() {
		return this.onProperty().get();
	}

	public final void setOn(final boolean on) {
		this.onProperty().set(on);
	}

	@Override
	public String toString() {
		return getName();
	}
}
