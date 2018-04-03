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

import org.onosproject.orchestrator.dismi.primitives.DismiIntentState;


public enum InternalIntentState implements InternalIntentStateIface {

    Submit {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING;
        }

        @Override
        public boolean onSubmitForValidation() { return true; }
    },

    Validating {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING;
        }

        @Override
        public boolean onValidationSuccess() { return true; }

        @Override
        public boolean onValidationFailure() { return true; }
    },

    ValidationFailed {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING_FAILED;
        }

        @Override
        public boolean onUpdateRequest() { return true; }

    },

    Validated {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING;
        }

        @Override
        public boolean onSubmitForConversion() { return true; }
    },

    Converting2Aci {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING;
        }

        @Override
        public boolean onConversionSuccess() { return true; }

        @Override
        public boolean onConversionFailure() { return true; }
    },

    ConversionFailed {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING_FAILED;
        }

        @Override
        public boolean onUpdateRequest() { return true; }

    },

    Converted2Aci {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.PROCESSING;
        }

        @Override
        public boolean onSubmitForInstallation() { return true; }
    },

    Installing {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.INSTALLING;
        }

        @Override
        public boolean onInstallationSuccess() { return true; }

        @Override
        public boolean onInstallationFailure() { return true; }
    },

    Failed {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.FAILED;
        }

        @Override
        public boolean onUpdateRequest() { return true; }

        @Override
        public boolean onSubmitForInstallation() { return true; }
    },

    Installed {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.INSTALLED;
        }

        @Override
        public boolean onUpdateRequest() { return true; }

        @Override
        public boolean onInstallationFailure() { return true; }

        @Override
        public boolean onSubmitForWithdrawal() { return true; }
    },

    Withdrawing {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.WITHDRAWING;
        }

        @Override
        public boolean onWithdrawalSuccess() { return true; }
    },

    Withdrawn {
        @Override
        public DismiIntentState getUserState() {
            return DismiIntentState.WITHDRAWN;
        }
    };

    public boolean onUpdateRequest() { return false; }

    public boolean onSubmitForValidation() { return false; }

    public boolean onValidationSuccess() { return false; }

    public boolean onValidationFailure() { return false; }

    public boolean onSubmitForConversion() { return false; }

    public boolean onConversionSuccess() { return false; }

    public boolean onConversionFailure() { return false; }

    public boolean onSubmitForInstallation() { return false; }

    public boolean onInstallationSuccess() { return false; }

    public boolean onInstallationFailure() { return false; }

    public boolean onSubmitForWithdrawal() { return false; }

    public boolean onWithdrawalSuccess() { return false; }

    public DismiIntentState getUserState() {return DismiIntentState.UNKNOWN;}

}
