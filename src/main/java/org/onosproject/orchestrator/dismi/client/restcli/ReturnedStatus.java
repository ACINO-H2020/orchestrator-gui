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

package org.onosproject.orchestrator.dismi.client.restcli;

public class ReturnedStatus {
	
	public enum STATUS {SUCCESS, FAIL};
	private STATUS status;
	private Object infomration;
	
	public ReturnedStatus(STATUS status, Object infomration){
		this.status = status;
		this.infomration = infomration;
	}
	public STATUS getStatus() {
		return status;
	}
	public void setStatus(STATUS status) {
		this.status = status;
	}
	public Object getInfomration() {
		return infomration;
	}
	public void setInfomration(Object infomration) {
		this.infomration = infomration;
	}
	
	

}
