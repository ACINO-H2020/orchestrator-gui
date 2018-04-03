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

package org.onosproject.orchestrator.dismi.client;

import java.util.ArrayList;
import java.util.List;

import org.onosproject.orchestrator.dismi.client.utils.StatusImage;
import org.onosproject.orchestrator.dismi.primitives.Aggregate;
import org.onosproject.orchestrator.dismi.primitives.AvailabilityConstraint;
import org.onosproject.orchestrator.dismi.primitives.BandwidthConstraint;
import org.onosproject.orchestrator.dismi.primitives.Connection;
import org.onosproject.orchestrator.dismi.primitives.Constraint;
import org.onosproject.orchestrator.dismi.primitives.DelayConstraint;
import org.onosproject.orchestrator.dismi.primitives.DismiIntentState;
import org.onosproject.orchestrator.dismi.primitives.HighAvailabilityConstraint;
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Mesh;
import org.onosproject.orchestrator.dismi.primitives.Multicast;
import org.onosproject.orchestrator.dismi.primitives.Path;
import org.onosproject.orchestrator.dismi.primitives.SDWAN;
import org.onosproject.orchestrator.dismi.primitives.SecurityConstraint;
import org.onosproject.orchestrator.dismi.primitives.Subject;
import org.onosproject.orchestrator.dismi.primitives.Tree;
import org.onosproject.orchestrator.dismi.primitives.VPN;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class IntentEntity {

	private final StringProperty noProperty = new SimpleStringProperty();
	private final StringProperty intent_idProperty = new SimpleStringProperty();
	private final StringProperty nameProperty = new SimpleStringProperty();
	private final StringProperty typeProperty = new SimpleStringProperty();
	private final StringProperty sourceProperty = new SimpleStringProperty();
	private final StringProperty destinationProperty = new SimpleStringProperty();
	private final StringProperty constraintProperty = new SimpleStringProperty();
	// --------------------------------------------
	private String installingIcon = "/icons/installing.png";
	private String installedIcon = "/icons/installed.png";
	private String withdrawingIcon = "/icons/withdrawing.png";
	private String witchdrawnIcon = "/icons/withdrawn.png";
	private String processingIcon = "/icons/processing.png";
	private String failedIcon = "/icons/failed.png";
	private String errorIcon = "/icons/installation_failed.png";
	private String unknownIcon = "/icons/unknown.png";
	private String negotiationIcon = "/icons/star.png";
	// --------------------------------------------

	public static List<String> ids = new ArrayList<String>();

	private final ObjectProperty<StatusImage> statusImageProperty = new SimpleObjectProperty();

	private final SimpleStringProperty statusProperty;

	public IntentEntity(int no, Intent intent) {
		setNo("" + no);
		this.statusProperty = new SimpleStringProperty(this, "status", intent.getIntentStatus().name());
		setName(intent.getDisplayName());
		setIntent_Id(intent.getIntentId());
		parseIntents(intent);
		hasStatusImage(getStatus());
	}

	public IntentEntity(String no, DismiIntentState status, String name, String type, String source, String destination,
			String constraints, String intentId) {
		setNo(no);
		this.statusProperty = new SimpleStringProperty(this, "status", status.toString());
		setName(name);
		setIntent_Id(intentId);
		setType(type);
		setSource(source);
		setDestination(destination);
		setConstraint(constraints);
		hasStatusImage(getStatus());
	}

	public Object getStatusImage() {
		return this.statusImageProperty.get();
	}

	public ObjectProperty<StatusImage> statusImageProperty() {
		return statusImageProperty;
	}

	public void setStatusImage(StatusImage statusImage) {
		this.statusImageProperty.set(statusImage);
	}

	public void hasStatusImage(String image) {
		if (image.equalsIgnoreCase("FAILED")) {
			setStatusImage(new StatusImage(failedIcon));
		} else if (image.equalsIgnoreCase("INSTALLED")) {
			setStatusImage(new StatusImage(installedIcon));
		} else if (image.equalsIgnoreCase("INSTALLING")) {
			setStatusImage(new StatusImage(installingIcon));
		} else if (image.equalsIgnoreCase("PROCESSING")) {
			setStatusImage(new StatusImage(processingIcon));
		} else if (image.equalsIgnoreCase("PROCESSING_FAILED")) {
			setStatusImage(new StatusImage(errorIcon));
		} else if (image.equalsIgnoreCase("UNKNOWN")) {
			setStatusImage(new StatusImage(unknownIcon));
		} else if (image.equalsIgnoreCase("WITHDRAWING")) {
			setStatusImage(new StatusImage(withdrawingIcon));
		} else if (image.equalsIgnoreCase("WITHDRAWN")) {
			setStatusImage(new StatusImage(witchdrawnIcon));
		} else if (image.equalsIgnoreCase("NEGOTIATION")) {
			setStatusImage(new StatusImage(negotiationIcon));
		}
	}

	public String getStatus() {
		return this.statusProperty.get();
	}

	public SimpleStringProperty statusProperty() {
		return statusProperty;
	}

	public void setStatus(String status) {
		this.statusProperty.set(status);
		hasStatusImage(getStatus());
	}

	public String getNo() {
		return noProperty.get();
	}

	public void setNo(String no) {
		this.noProperty.set(no);
	}

	public String getIntent_Id() {
		return intent_idProperty.get();
	}

	public void setIntent_Id(String intentid) {
		this.intent_idProperty.set(intentid);
	}

	public String getName() {
		return nameProperty.get();
	}

	public void setName(String name) {
		this.nameProperty.set(name);
	}

	public String getType() {
		return typeProperty.get();
	}

	public void setType(String type) {
		this.typeProperty.set(type);
	}

	public String getSource() {
		return sourceProperty.get();
	}

	public void setSource(String source) {
		this.sourceProperty.set(source);
	}

	public String getConstraint() {
		return constraintProperty.get();
	}

	public void setConstraint(String constraints) {
		this.constraintProperty.set(constraints);
	}

	public String getDestination() {
		return destinationProperty.get();
	}

	public void setDestination(String destination) {
		this.destinationProperty.set(destination);
	}

	public StringProperty noProperty() {
		return noProperty;
	}

	public StringProperty nameProperty() {
		return nameProperty;
	}

	public StringProperty typeProperty() {
		return typeProperty;
	}

	public StringProperty sourceProperty() {
		return sourceProperty;
	}

	public StringProperty destinationProperty() {
		return destinationProperty;
	}

	public StringProperty constraintsProperty() {
		return constraintProperty;
	}

	public static List<String> ids() {
		ids.add("no");
		ids.add("statusImage");
		ids.add("name");
		ids.add("type");
		ids.add("source");
		ids.add("destination");
		ids.add("constraint");
		return ids;
	}

	private void parseIntents(Intent intentTemp) {
		if (intentTemp.getAction() instanceof Path) {
			Path path = (Path) intentTemp.getAction();
			setType("Path");
			setSource(path.getSource().getConnectionPoint().getName());
			setDestination(path.getDestination().getConnectionPoint().getName());
		}
		if (intentTemp.getAction() instanceof SDWAN) {
			SDWAN path = (SDWAN) intentTemp.getAction();
			setType("SDWAN");
			setSource(path.getSource().getConnectionPoint().getName());
			setDestination(path.getDestination().getConnectionPoint().getName());
		}
		if (intentTemp.getAction() instanceof VPN) {
			VPN path = (VPN) intentTemp.getAction();
			setType("VPN");
			setSource(path.getSource().getConnectionPoint().getName());
			setDestination(path.getDestination().getConnectionPoint().getName());
		}
		if (intentTemp.getAction() instanceof Connection) {
			Connection connection = (Connection) intentTemp.getAction();
			setType("Connection");
			setSource(connection.getSource().getConnectionPoint().getName());
			setDestination(connection.getDestination().getConnectionPoint().getName());
		}
		if (intentTemp.getAction() instanceof Tree) {
			Tree tree = (Tree) intentTemp.getAction();
			setType("Tree");
			setSource(tree.getSource().getConnectionPoint().getName());
			setDestination(subjectsToString(tree.getDestination()));
		}
		if (intentTemp.getAction() instanceof Mesh) {
			Mesh mesh = (Mesh) intentTemp.getAction();
			setSource(subjectsToString(mesh.getSource()));
		}
		if (intentTemp.getAction() instanceof Multicast) {
			Multicast multicast = (Multicast) intentTemp.getAction();
			setType("Multicast");
			setSource(multicast.getSource().getConnectionPoint().getName());
			setDestination(subjectsToString(multicast.getDestination()));
		}
		if (intentTemp.getAction() instanceof Aggregate) {
			Aggregate aggregate = (Aggregate) intentTemp.getAction();
			setType("Aggregate");
			setSource(subjectsToString(aggregate.getSource()));
			setDestination(aggregate.getDestination().getConnectionPoint().getName());
		}
		setStatus(intentTemp.getIntentStatus().name());
		setConstraint(constraintsToString(intentTemp.getConstraints()));
	}

	private String subjectsToString(List<Subject> subjects) {
		String strSub = "";
		for (Subject subject : subjects) {
			strSub = strSub + subject.getConnectionPoint().getName() + ",";
		}
		if (strSub.endsWith(",")) {
			strSub = strSub.substring(0, strSub.length() - 1);
		}
		return strSub;
	}

	private String constraintsToString(List<Constraint> intConstraints) {
		String strConsts = "";
		for (Constraint iConst : intConstraints) {
			if (iConst instanceof DelayConstraint) {
				DelayConstraint delayConstraint = (DelayConstraint) iConst;
				StringBuffer buffer = new StringBuffer();
				buffer.append("Delay(");
				String constTemp = "";
				if (null != delayConstraint.getLatency()) {
					buffer.append("lat:" + delayConstraint.getLatency() + " , ");
				}
				if (null != delayConstraint.getJitter()) {
					buffer.append("jit:" + constTemp + delayConstraint.getJitter());
				}
				constTemp = buffer.toString();
				if (constTemp.endsWith(", ")) {
					constTemp = constTemp.substring(0, constTemp.length() - 2);
				}
				strConsts = strConsts + constTemp + ")";
			} else if (iConst instanceof BandwidthConstraint) {
				BandwidthConstraint bandwidthConstraint = (BandwidthConstraint) iConst;
				if (null != bandwidthConstraint.getBitrate())
					strConsts = strConsts + "BW(" + bandwidthConstraint.getBitrate() + ") ,";
			} else if (iConst instanceof SecurityConstraint) {
				SecurityConstraint securityConstraint = (SecurityConstraint) iConst;
				String secStatus = "";
				if (securityConstraint.getEncryption()) {
					secStatus = "Encryption[" + securityConstraint.getEncStrength() + "], ";
				} else {
					secStatus = "No Encryption, ";
				}
				if (securityConstraint.getIntegrity()) {
					secStatus = "Integrity[" + securityConstraint.getIntStrength() + "], ";
				} else {
					secStatus = "No Integrity, ";
				}
				strConsts = strConsts + "Security(" + secStatus + ") ,";
			} else if (iConst instanceof HighAvailabilityConstraint) {
				
				HighAvailabilityConstraint highAvailabilityConstraint = (HighAvailabilityConstraint) iConst;
				strConsts = strConsts + ", HA(" + highAvailabilityConstraint.getAvailability() + "),";
				
			} else if (iConst instanceof AvailabilityConstraint) {
				AvailabilityConstraint availabilityConstraint= (AvailabilityConstraint) iConst;
				String avStatus = "";
				avStatus  = "Availability[" + availabilityConstraint.getAvailability() + "], ";
				strConsts = strConsts + "Av(" + avStatus  + ") ,";
			}
		}
		if (strConsts.endsWith(",")) {
			strConsts = strConsts.substring(0, strConsts.length() - 1);
		}
		return strConsts;
	}

	public String toString() {
		return "Name: " + nameProperty.getValue() + "\n" + "Type: " + typeProperty.getValue() + "\n" + "Source: "
				+ sourceProperty.getValue() + "\n" + "Destination: " + destinationProperty.getValue() + "\n"
				+ "Constraint(s): " + constraintProperty.getValue() + "\n" + "Status: " + getStatus();
	}
}