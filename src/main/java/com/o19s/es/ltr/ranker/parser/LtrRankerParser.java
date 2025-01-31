/*
 * Copyright [2017] Wikimedia Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.o19s.es.ltr.ranker.parser;

import com.o19s.es.ltr.feature.FeatureSet;
import com.o19s.es.ltr.ranker.LtrRanker;

/** A model parser (don't have to be thread-safe) */
public interface LtrRankerParser {

  /**
   * Parse the model with the given {@link FeatureSet}
   *
   * @param set the feature-set to use to parse the model
   * @param model the model name
   * @return the {@link LtrRanker} parsed from the given model and feature-set
   */
  LtrRanker parse(FeatureSet set, String model);
}
