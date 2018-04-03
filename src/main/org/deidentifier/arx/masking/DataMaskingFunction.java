/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2018 Fabian Prasser and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deidentifier.arx.masking;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import org.deidentifier.arx.framework.data.DataColumn;

/**
 * This class implements data masking functions
 * 
 * @author Fabian Prasser
 */
public abstract class DataMaskingFunction implements Serializable {

    /**
     * Generates a random alphanumeric string
     * 
     * @author Fabian Prasser
     */
    public static class DataMaskingFunctionRandomAlphanumericString extends DataMaskingFunction {
        
        /** SVUID*/
        private static final long serialVersionUID = 918401877743413029L;
        
        /** Characters*/
        private static final char[] CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        
        /** Random */
        private final Random random;
        
        /** Buffer */
        private final char[] buffer;
        
        /**
         * Creates a new instance
         * @param ignoreMissingData
         * @param length 
         */
        public DataMaskingFunctionRandomAlphanumericString(boolean ignoreMissingData, int length) {
            super(ignoreMissingData, false);
            this.buffer = new char[length];
            this.random = new SecureRandom();
        }

        @Override
        public void apply(DataColumn column) {
            for (int row = 0; row < column.getNumRows(); row++) {
                column.set(row, getRandomAlphanumericString());
            }
        }

        @Override
        public DataMaskingFunction clone() {
            return new DataMaskingFunctionRandomAlphanumericString(super.isIgnoreMissingData(), buffer.length);
        }

        /**
         * Creates a random string
         * @return
         */
        private String getRandomAlphanumericString() {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = CHARACTERS[random.nextInt(CHARACTERS.length)];
            }
            return new String(buffer);
        }
    }

    /** SVUID */
    private static final long serialVersionUID = -5605460206017591293L;

    /** Ignore missing data */
    private final boolean     ignoreMissingData;

    /** Preserves data types */
    private final boolean     typePreserving;

    /**
     * Creates a new instance
     * @param ignoreMissingData
     * @param typePreserving
     */
    private DataMaskingFunction(boolean ignoreMissingData, boolean typePreserving) {
        this.ignoreMissingData = ignoreMissingData;
        this.typePreserving = typePreserving;
    }
    
    /**
     * Applies the function to the given column
     * @param column
     */
    public abstract void apply(DataColumn column);

    /** Clone*/
    public abstract DataMaskingFunction clone();

    /**
     * Returns whether the function ignores missing data
     * @return
     */
    public boolean isIgnoreMissingData() {
        return this.ignoreMissingData;
    }

    /**
     * Returns whether the function is type preserving
     * @return
     */
    public boolean isTypePreserving() {
        return this.typePreserving;
    }
}
