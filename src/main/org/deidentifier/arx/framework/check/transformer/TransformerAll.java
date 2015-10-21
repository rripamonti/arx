/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2015 Florian Kohlmayer, Fabian Prasser
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

package org.deidentifier.arx.framework.check.transformer;

import org.deidentifier.arx.ARXConfiguration.ARXConfigurationInternal;
import org.deidentifier.arx.framework.check.distribution.IntArrayDictionary;
import org.deidentifier.arx.framework.data.GeneralizationHierarchy;

/**
 * The class TransformerAll.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class TransformerAll extends AbstractTransformer {

    /**
     * Instantiates a new transformer.
     *
     * @param data the data
     * @param hierarchies the hierarchies
     * @param otherValues
     * @param dictionarySensValue
     * @param dictionarySensFreq
     * @param config
     */
    public TransformerAll(final int[][] data,
                          final GeneralizationHierarchy[] hierarchies,
                          final int[][] otherValues,
                          final IntArrayDictionary dictionarySensValue,
                          final IntArrayDictionary dictionarySensFreq,
                          final ARXConfigurationInternal config) {
        super(data, hierarchies, otherValues, dictionarySensValue, dictionarySensFreq, config);
    }

    @Override
    protected void processAll() {
        final int dimensions = data[0].length;
        for (int i = startIndex; i < stopIndex; i++) {
            intuple = data[i];
            outtuple = buffer[i];
            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                outtuple[d] = map[d][intuple[d]][state];
            }

            // Call
            delegate.callAll(outtuple, i);
        }
    }

    @Override
    protected void processGroupify() {

        int elements = stopIndex - startIndex;
        while (element != null && elements > 0) {

            intuple = data[element.representative];
            outtuple = buffer[element.representative];
            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                outtuple[d] = map[d][intuple[d]][state];
            }

            // Call
            delegate.callGroupify(outtuple, element);

            // Next element
            elements--;
            element = element.nextOrdered;
        }
    }

    @Override
    protected void processSnapshot() {

        startIndex *= ssStepWidth;
        stopIndex *= ssStepWidth;

        for (int i = startIndex; i < stopIndex; i += ssStepWidth) {
            intuple = data[snapshot[i]];
            outtuple = buffer[snapshot[i]];
            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                outtuple[d] = map[d][intuple[d]][state];
            }

            // Call
            delegate.callSnapshot(outtuple, snapshot, i);
        }
    }
}
