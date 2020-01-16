/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
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

package mergepolicies;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.merge.MergingCreationTime;
import com.hazelcast.spi.merge.MergingHits;
import com.hazelcast.spi.merge.MergingValue;
import com.hazelcast.spi.merge.SplitBrainMergePolicy;

/**
 * Merge policy which shows composition of required merge types.
 *
 * @param <V> the (deserialized) type of the merging value
 * @param <T> the type of the required merging value
 * @see com.hazelcast.spi.merge.SplitBrainMergeTypes
 */
public class ComposedHitsAndCreationTimeMergePolicy<V, T extends MergingValue<V> & MergingHits & MergingCreationTime>
        implements SplitBrainMergePolicy<V, T, Object> {

    @Override
    public Object merge(T mergingValue, T existingValue) {
        if (existingValue == null) {
            return mergingValue.getRawValue();
        }
        System.out.println("========================== Merging value " + mergingValue.getValue() + "..."
                + "\n    mergingValue creation time: " + mergingValue.getCreationTime()
                + "\n    existingValue creation time: " + existingValue.getCreationTime()
                + "\n    mergingValue hits: " + mergingValue.getHits()
                + "\n    existingValue hits: " + existingValue.getHits()
        );
        // the merging value wins, if it's older and has more hits
        if (mergingValue.getCreationTime() < existingValue.getCreationTime()
                && mergingValue.getHits() > existingValue.getHits()) {
            return mergingValue.getRawValue();
        }
        return existingValue.getRawValue();
    }

    @Override
    public void writeData(ObjectDataOutput out) {
    }

    @Override
    public void readData(ObjectDataInput in) {
    }
}
