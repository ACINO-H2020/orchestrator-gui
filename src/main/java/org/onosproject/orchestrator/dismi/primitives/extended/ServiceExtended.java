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

package org.onosproject.orchestrator.dismi.primitives.extended;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.onosproject.orchestrator.dismi.primitives.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@ApiModel(description = "This class extends Service to support IntentExtended, which are Intents after Resolution")
@javax.annotation.Generated(value = "Manually by Stephane", date = "2016-12-12T17:41:00.000Z")
public class ServiceExtended extends Service {
    private List<IntentExtended> intentsExtended = new ArrayList<>();


    public ServiceExtended intentsExtended(List<IntentExtended> intentExtended) {
        this.intentsExtended = intentsExtended;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("intents_extended")
    public List<IntentExtended> getIntentsExtended() {
        return intentsExtended;
    }

    public void setIntentsExtended(List<IntentExtended> intentsExtended) {
        this.intentsExtended = intentsExtended;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceExtended service = (ServiceExtended) o;
        return Objects.equals(intentsExtended, service.intentsExtended) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intentsExtended, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceExtended {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    intentsExtended: ").append(toIndentedString(intentsExtended.toString())).append("\n");
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
}
