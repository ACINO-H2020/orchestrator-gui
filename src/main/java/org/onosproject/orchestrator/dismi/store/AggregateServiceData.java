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

package org.onosproject.orchestrator.dismi.store;

import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.primitives.Service;
import org.onosproject.orchestrator.dismi.primitives.Tracker;
import org.onosproject.orchestrator.dismi.primitives.extended.IntentExtended;
import org.onosproject.orchestrator.dismi.primitives.extended.ServiceExtended;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import org.slf4j.Logger;

//import static org.slf4j.LoggerFactory.getLogger;

public class AggregateServiceData implements Serializable {

    private String serviceId;
    private String displayName;
    private List<Intent> originalIntentList;
    private List<IntentExtended> resolvedIntentList;
    private Tracker tracker;

    private AggregateServiceData updateRequest;

//    private final Logger log = getLogger(getClass());

    public AggregateServiceData() {
        serviceId = "";
        displayName = "";
        originalIntentList = new ArrayList<>() ;
        resolvedIntentList = null;
        tracker = null;

        updateRequest = null;
    }

    public Service getOriginalService() {
        Service service = new Service();

        service.setServiceId(serviceId);
        service.setDisplayName(displayName);

        if (originalIntentList.size() == 0) {
            return service;
        }

        // No Extended Intents yet?
        if ((null == resolvedIntentList) || (resolvedIntentList.size() == 0)) {
            if (originalIntentList.size()>0) {
                service.setIntents(originalIntentList);
            }
        }

        // Same number of requested and extended intents? Otherwise we are in trouble...
        if (originalIntentList.size() == resolvedIntentList.size()) {
            Intent ito;
            IntentExtended ite;
            for (int i=0; i<originalIntentList.size(); i++) {
                ito = originalIntentList.get(i);
                ite = resolvedIntentList.get(i);
                ite.setIntentStatus(ito.getIntentStatus());
            }
            service.setIntents(originalIntentList);

            if (null != tracker) {
                service.getServerInfo();
            }
        }

        return service;
    }

    public Service getOriginalServiceUpdate() {
        if (null == updateRequest) {
            return new Service();
        }
        return updateRequest.getOriginalService();
    }

    public boolean setOriginalService(Service service) {
        if (isOriginalServiceValid()) {
            //  We already have an original Service
            return false;
        }

        if (null == service) {
            return false;
        }

        String id = service.getServiceId();
        List<Intent> intentList = service.getIntents();
        if ((null == id) || (id.length() == 0) || (null == intentList) || (intentList.size() == 0)) {
            return false;
        }

        serviceId = id;
        displayName = service.getDisplayName();
        originalIntentList = intentList;
        return true;
    }

    public Service getResolvedService() {
        Service service = new Service();

        service.setServiceId(serviceId);
        service.setDisplayName(displayName);

        if (null != resolvedIntentList) {
            List<Intent> list = new ArrayList<>();
            for (IntentExtended intent : resolvedIntentList) {
                list.add((Intent) intent);
            }
            service.setIntents(list);
        }
        return service;
    }


    public boolean setResolvedService(ServiceExtended service, Tracker t) {
        if ((! isOriginalServiceValid()) ||
                (null == service) ||
                (null == t) ||
                (null == service.getIntents()) ||
                (service.getIntents().size() == 0)) {
            return false;
        }

        resolvedIntentList = service.getIntentsExtended();

        if ( (null == resolvedIntentList) || (resolvedIntentList.size() == 0)) {
            resolvedIntentList = null;
            return false;
        }

        tracker = t;
        return true;
    }

    public boolean setServiceUpdate(Service service) {
        if (    (null == service) ||
                (null == tracker) ||
                (null == resolvedIntentList) ||
                (null != updateRequest) ) {
            return false;
        }

        if (! isOriginalServiceValid() ){
            return false;
        }

        updateRequest = new AggregateServiceData();

        updateRequest.setOriginalService(service);

        if (    (! updateRequest.isOriginalServiceValid()) ||
                (serviceId.compareTo(updateRequest.serviceId) != 0) ||
                (displayName.compareTo(updateRequest.displayName) != 0) ) {
            return false;
        }

        //  Check that all the Intent update requests have IDs that exist in the originals
        Intent intent;
        for (Intent it:updateRequest.originalIntentList) {
            intent = getOriginalIntent(it.getIntentId());
            if (null == intent) {
                traceMe("Intent \"" + it.getIntentId() + "\" does not belog to original Service \"" + serviceId + "\"");
                return false;
            }
        }

        return true;
    }

    public boolean setResolvedServiceUpdate(ServiceExtended service, Tracker tracker) {
        if (    (null == updateRequest) ||
                (! updateRequest.isOriginalServiceValid()) ||
                (null == resolvedIntentList) ||
                (null == service) ||
                (null == tracker) ||
                (null == service.getIntents()) ||
                (service.getIntents().size() == 0)) {
            return false;
        }

        if (null != updateRequest.resolvedIntentList) {
            //  There is already a list of update requests!
            return false;
        }

        updateRequest.resolvedIntentList = new ArrayList<>();

        // Compare each resolvedIntentUpdate with the original resolvedUpdate
        List<IntentExtended> intentList = service.getIntentsExtended();
        IntentExtended iExtended;
        for (Intent it:intentList) {
            iExtended = getResolvedIntent(it.getIntentId());
            if (null != iExtended) {
                if (iExtended.getInternalIntentState().onUpdateRequest()) {
                    // Update of the Intent is permitted
                    updateRequest.resolvedIntentList.add(iExtended);
                }
            }
        }

        //  Did we get anything in?
        if (updateRequest.resolvedIntentList.size() == 0) {
            updateRequest.resolvedIntentList = null;
            return false;
        }

        updateRequest.tracker = tracker;
        return true;
    }

    public List<IntentExtended> getResolvedIntents() {
        if (null == resolvedIntentList ) {
            return new ArrayList<IntentExtended>();
        }
            return resolvedIntentList;
    }

    public List<IntentExtended> getResolvedIntentUpdates() {
        return updateRequest.getResolvedIntents();
    }

    /*
    private String serviceId;
    private String displayName;
    private List<Intent> originalIntentList;
    private List<IntentExtended> extendedIntentList;
    private Tracker tracker;

    */
    public boolean isOriginalServiceValid() {
        if (    (serviceId.length() == 0) ||
                (null == originalIntentList) ||
                (originalIntentList.size() == 0) ) {
            return false;
        }
        return true;
    }

    public Intent getOriginalIntent (String intentId) {
        if (null == originalIntentList) {
            return null;
        }

        for (Intent intent:originalIntentList) {
            if (intentId.compareTo(intent.getIntentId()) == 0) {
                return intent;
            }
        }
        return null;
    }

    public IntentExtended getResolvedIntent(String intentId) {
        if (null == resolvedIntentList) {
            return null;
        }

        for (IntentExtended intent:resolvedIntentList) {
            if (intentId.compareTo(intent.getIntentId()) == 0) {
                return intent;
            }
        }
        return null;
    }


    /*
        private String serviceId;
        private String displayName;
        private List<Intent> originalIntentList;
        private List<IntentExtended> extendedIntentList;
        private Tracker tracker;

        private AggregateServiceData updateRequest;

     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AggregateServiceData {\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    originalIntentList: ").append(toIndentedString(originalIntentList)).append("\n");
        sb.append("    extendedIntentList: ").append(toIndentedString(resolvedIntentList)).append("\n");
        sb.append("    tracker: ").append(toIndentedString(tracker)).append("\n");
        sb.append("    updateRequest: ").append(toIndentedString(updateRequest)).append("\n");
        sb.append("}");
        return sb.toString();
    }


    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    private void traceMe(String s) {
        //log.error(s);
    }
}
