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

public class DismitestBase {
    
    public static String baiscURL = "http://localhost:8181/onos/v1/orchestrator/api/";
    private String baiscPath = "";

    public DismitestBase (){        
    }
    public DismitestBase (String baiscPath ){
        this.baiscPath = baiscPath ;
    }
  
    public String getBaiscPath() {
        return baiscPath;
    }

    public void setBaiscPath(String baiscPath) {
        this.baiscPath = baiscPath;
    }

    public String getBaiscURL() {
        return baiscURL;
    }

    public void setBaiscURL(String baiscURL) {
        this.baiscURL = baiscURL;
    }
}
