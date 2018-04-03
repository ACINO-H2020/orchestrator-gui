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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class DismiLogController {
	
	@FXML
	private TextArea log;
	
	private final Logger loger = Logger.getLogger(DismiLogController.class.getName());
	
	// the initialize method is automatically invoked by the FXMLLoader
	public void initialize() {		
		Enumeration e = Logger.getRootLogger().getAllAppenders();
	    while ( e.hasMoreElements() ){
	      Appender app = (Appender)e.nextElement();
	      if ( app instanceof FileAppender ){
	        File file = new File(((FileAppender)app).getFile());
	        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	            	log.appendText(line+"\n\n");       
	            }
	        } catch (Exception e1) {			
			}
	      }
	    }
	}
}
