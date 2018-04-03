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

import org.onosproject.orchestrator.dismi.primitives.Aggregate;
import org.onosproject.orchestrator.dismi.primitives.BandwidthConstraint;
import org.onosproject.orchestrator.dismi.primitives.Connection;
import org.onosproject.orchestrator.dismi.primitives.Constraint;
import org.onosproject.orchestrator.dismi.primitives.DelayConstraint;
import org.onosproject.orchestrator.dismi.primitives.HighAvailabilityConstraint;
import org.onosproject.orchestrator.dismi.primitives.IPSelector;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Mesh;
import org.onosproject.orchestrator.dismi.primitives.Multicast;
import org.onosproject.orchestrator.dismi.primitives.Path;
import org.onosproject.orchestrator.dismi.primitives.SDWAN;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint;
import org.onosproject.orchestrator.dismi.primitives.Selector;
import org.onosproject.orchestrator.dismi.primitives.Subject;
import org.onosproject.orchestrator.dismi.primitives.Tree;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ASController {

	@FXML
	private TextArea info;
	@FXML
	private Label optionNo;
	@FXML
	private Button prev;
	@FXML
	private Button next;

	// private IntentList ;
	private List<Intent> intentList = new ArrayList<Intent>();
	private int current = 0;

	// the initialize method is automatically invoked by the FXMLLoader
	public void initialize() {
		prev();
		next();
		info.setEditable(false);
		prev.setDisable(true);
		setData(current);
	}

	public void setIntentList(List<Intent> intents) {
		for (Intent intent : intents) {
			intentList.add(intent);
		}
		current = 0;
		setData(current);
	}

	private void prev() {
		prev.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				current--;
				setData(current);
				if (current <= 0) {
					prev.setDisable(true);
					next.setDisable(false);
					return;

				} else {
					next.setDisable(false);
				}
			}
		});
	}

	private void next() {
		next.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				current++;
				setData(current);
				if (current >= intentList.size() - 1) {
					next.setDisable(true);
					prev.setDisable(false);
					return;
				} else {
					prev.setDisable(false);
				}
			}
		});
	}
	private void setData(int cur) {
		if (cur >= intentList.size()) {
			return;
		}
		info.setText(intent2String(intentList.get(cur)));
		optionNo.setText("" + (cur + 1));
	}

	private String intent2String(Intent intent) {
		return parseIntents(intent);
	}

	// -------------------------------------
	private String parseIntents(Intent intentTemp) {
		StringBuffer stringBuffer = new StringBuffer();
		String tabs = tabs(1);
		String nl = "\n";
		stringBuffer.append(tabs + "Intent Name: " + intentTemp.getDisplayName() + nl);
		if (intentTemp.getAction() instanceof Path) {
			Path path = (Path) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Path" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof Connection) {
			Connection connection = (Connection) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Connection" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof Tree) {
			Tree tree = (Tree) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Tree" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof Mesh) {
			Mesh mesh = (Mesh) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Mesh" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof Multicast) {
			Multicast multicast = (Multicast) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Multicast" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof Aggregate) {
			Aggregate aggregate = (Aggregate) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : Aggregate" + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		} else if (intentTemp.getAction() instanceof SDWAN) {
			SDWAN sdwan = (SDWAN) intentTemp.getAction();
			stringBuffer.append(tabs + "Action Type : SDWAN" + nl);
			stringBuffer.append(tabs + "Provider : " + intentTemp.getProviderName() + nl);
			stringBuffer.append(selectorsToString("Intent Selector(s)", intentTemp.getSelectors(), 1));
			stringBuffer.append(constraintsToString("Intent Constraint(s)", intentTemp.getConstraints(), 1));
		}
		return stringBuffer.toString();
	}

	private String subjectToString(String title, Subject subject, int level) {
		StringBuffer stringBuffer = new StringBuffer();
		String tabs = tabs(level);
		String nl = "\n";
		stringBuffer.append(tabs + title + ":" + nl);
		stringBuffer.append(tabs + "  Name:" + subject.getConnectionPoint().getName() + nl);
		stringBuffer
				.append(tabs + "  " + selectorsToString("Subject Level Selector", subject.getSelectors(), level + 1));
		stringBuffer.append(
				tabs + "  " + constraintsToString("Subject Level Constraint(s)", subject.getConstraints(), level + 1));
		return stringBuffer.toString();
	}

	private String subjectsToString(String title, List<Subject> subjects, int level) {
		StringBuffer stringBuffer = new StringBuffer();
		String tabs = tabs(level);
		String nl = "\n";
		stringBuffer.append(tabs + title + ":" + nl);
		int count = 1;
		for (Subject subject : subjects) {
			stringBuffer.append(tabs + "  -" + "No. " + count + nl);
			stringBuffer.append(tabs + "  Name:" + subject.getConnectionPoint().getName() + nl);
			stringBuffer.append(
					tabs + "  " + selectorsToString("Subject Level Selector", subject.getSelectors(), level + 1) + nl);
			stringBuffer.append(tabs + "  "
					+ constraintsToString("Subject Level Constraint(s)", subject.getConstraints(), level + 1) + nl);
			count++;
		}
		return stringBuffer.toString();
	}

	public String tabs(int level) {
		String tabs = "";
		for (int i = 0; i < level; i++)
			tabs = tabs + "  ";
		return tabs;
	}

	private String selectorsToString(String title, List<Selector> intSelector, int level) {

		StringBuffer stringBuffer = new StringBuffer();
		String tabs = tabs(level);
		String nl = "\n";
		stringBuffer.append(tabs + title + ":");
		if (intSelector.size() <= 0) {
			stringBuffer.append(tabs + "  " + "<Selector not specified>" + nl);
			return stringBuffer.toString();
		}
		stringBuffer.append(nl);
		for (Selector iSelect : intSelector) {
			if (iSelect instanceof IPSelector) {
				IPSelector ipSelector = (IPSelector) iSelect;
				stringBuffer.append(tabs + "  " + "IPSelector:" + nl);
				if (null != ipSelector.getIpDestAddr()) {
					stringBuffer.append(tabs + "  " + "  " + "Destination Address:" + ipSelector.getIpDestAddr() + nl);
				}

			}
		}
		return stringBuffer.toString();
	}

	private String constraintsToString(String title, List<Constraint> intConstraints, int level) {

		StringBuffer stringBuffer = new StringBuffer();
		String tabs = tabs(level);
		String nl = "\n";
		stringBuffer.append(tabs + title + ":");
		if (intConstraints.size() <= 0) {
			stringBuffer.append(tabs + "  " + "<Constraints not specified>" + nl);
			return stringBuffer.toString();
		}
		stringBuffer.append(nl);
		stringBuffer.append(nl);
		for (Constraint iConst : intConstraints) {
			if (iConst instanceof DelayConstraint) {
				DelayConstraint delayConstraint = (DelayConstraint) iConst;
				stringBuffer.append(tabs + "  " + "DelayConstraint:" + nl);
				if (null != delayConstraint.getLatency()) {
					stringBuffer.append(tabs + "  " + "  " + "Latency:" + delayConstraint.getLatency() + nl);
				}
				if (null != delayConstraint.getJitter()) {
					stringBuffer.append(tabs + "  " + "  " + "Jitter:" + delayConstraint.getJitter() + nl);
				}
			} else if (iConst instanceof BandwidthConstraint) {
				BandwidthConstraint bandwidthConstraint = (BandwidthConstraint) iConst;
				stringBuffer.append(tabs + "  " + "BandwidthConstraint:" + nl);
				if (null != bandwidthConstraint.getBitrate()) {
					stringBuffer.append(tabs + "  " + "  " + "Bandwodth:" + bandwidthConstraint.getBitrate() + nl);
				}
			} else if (iConst instanceof SecurityConstraint) {
				SecurityConstraint securityConstraint = (SecurityConstraint) iConst;
				stringBuffer.append(tabs + "  " + "SecurityConstraint:" + nl);
				if (securityConstraint.getEncryption()) {
					stringBuffer.append(
							tabs + "  " + "  " + "Encryption [" + securityConstraint.getEncStrength() + "]" + nl);
				} else {
					stringBuffer.append(tabs + "  " + "  " + "No Encryption" + nl);
				}
				if (securityConstraint.getIntegrity()) {
					stringBuffer
							.append(tabs + "  " + "  " + "Integrity[" + securityConstraint.getIntStrength() + "]" + nl);

				} else {
					stringBuffer.append(tabs + "  " + "  " + "No Integrity" + nl);
				}
			} else if (iConst instanceof HighAvailabilityConstraint) {
				HighAvailabilityConstraint availabilityConstraint = (HighAvailabilityConstraint) iConst;
				stringBuffer.append(tabs + "  " + "High Availability:" + nl);
				if (availabilityConstraint.getAvailability()) {
					stringBuffer.append(tabs + "  " + "  " + "Required" + nl);
				} else {
					stringBuffer.append(tabs + "  " + "  " + "Not Required" + nl);
				}
			}
		}
		return stringBuffer.toString();
	}

	public Intent getSelected() {
		return intentList.get(current);
	}
}