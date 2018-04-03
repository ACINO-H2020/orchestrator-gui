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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LokalLogger {

	final static Logger logger = Logger.getLogger(LokalLogger.class);

	private LokalLogger() {

	}
	public static Logger getLogger(Class clientClass){
		
		return logger;
	}
	
	public static void main(String[] args) {
		
		LokalLogger obj = new LokalLogger();
		obj.runMe("mkyong");

	}

	private void runMe(String parameter) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("This is debug : " + parameter);
		}
		if (logger.isInfoEnabled()) {
			logger.info("This is info : " + parameter);
		}

		logger.warn("This is warn : " + parameter);
		logger.error("This is error : " + parameter);
		logger.fatal("This is fatal : " + parameter);

	}
	public void setLevel(Level level) {
		logger.setLevel(level);
	}

	// Create log folder
	static {
		File logDir = new File("log");
		if (!logDir.exists())
			logDir.mkdir();

		File logFile = new File("log/application.log");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
			}
		}
	}

}
