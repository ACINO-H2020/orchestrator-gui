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

package org.onosproject.orchestrator.dismi.primitives;

/**
 * Container for ACINO App Store Actions
 */
public abstract class ServiceAction extends org.onosproject.orchestrator.dismi.primitives.Action {

    protected org.onosproject.orchestrator.dismi.primitives.Subject source;
    protected org.onosproject.orchestrator.dismi.primitives.Subject destination;

    public org.onosproject.orchestrator.dismi.primitives.Subject getSource() {
        return source;
    }

    public org.onosproject.orchestrator.dismi.primitives.Subject getDestination() {
        return destination;
    }
}
