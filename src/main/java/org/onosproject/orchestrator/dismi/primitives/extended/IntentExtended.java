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
import org.onosproject.orchestrator.dismi.primitives.Intent;
import org.onosproject.orchestrator.dismi.store.InternalIntentState;

import java.util.Objects;


@ApiModel(description = "IntentExtended adds to an Intent the capability to remember its state " +
        "in the Finite State Machine")
@javax.annotation.Generated(value = "Created by hand by RISE ACREO AB developers",
        date = "2016-12-02T16:10:00.000Z")
public class IntentExtended extends Intent {
    // IntentExtended are created during the validation process
    private InternalIntentState internalIntentState = InternalIntentState.Submit;


    /**
     **/
    public Intent internalIntentState(InternalIntentState internalIntentState) {
        this.internalIntentState = internalIntentState;
        return this;
    }

    @ApiModelProperty(required = true, value = "internalIntentState")
    @JsonProperty("internalIntentState")
    public InternalIntentState getInternalIntentState() {
        return internalIntentState;
    }

    public void setInternalIntentState(InternalIntentState internalIntentState) {
        this.internalIntentState = internalIntentState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntentExtended intent = (IntentExtended) o;
        return Objects.equals(this.internalIntentState, intent.internalIntentState) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalIntentState, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IntentExtended {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    internalIntentState: ").append(toIndentedString(internalIntentState)).append("\n");
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
