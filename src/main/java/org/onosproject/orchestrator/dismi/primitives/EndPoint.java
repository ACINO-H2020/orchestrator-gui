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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.annotations.ApiModel;


/**
 * An EndPoints refers to a specific point in the ACINO network that can ingress and egress customer traffic.
 * For the ACINO use cases, the main type of EndPoint is a point on an IP router, but other types are
 * possible too, such as ports on layer 2 switches, OTN time slots, lambdas or fiber connections.
 **/

@ApiModel(description = "An EndPoints refers to a specific point in the ACINO network that can ingress " +
        "and egress customer traffic (A point on an IP router or on a layer 2 switch, ...).")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen",
        date = "2016-03-22T15:29:51.886Z")
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "end_point")

public class EndPoint {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), "end_point");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EndPoint {\n");
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

