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

package com.o19s.es.ltr;

import java.util.Collections;
import java.util.Set;
import org.elasticsearch.index.query.SearchExecutionContext;

/** LTR queryShardContext used to track information needed for building lucene queries */
public class LtrQueryContext {
  private final SearchExecutionContext queryShardContext;
  private final Set<String> activeFeatures;

  public LtrQueryContext(SearchExecutionContext context) {
    this(context, Collections.emptySet());
  }

  public LtrQueryContext(SearchExecutionContext context, Set<String> activeFeatures) {
    this.queryShardContext = context;
    this.activeFeatures = activeFeatures;
  }

  public SearchExecutionContext getSearchExecutionContext() {
    return queryShardContext;
  }

  public boolean isFeatureActive(String featureName) {
    return activeFeatures == null
        || activeFeatures.isEmpty()
        || activeFeatures.contains(featureName);
  }

  public Set<String> getActiveFeatures() {
    return activeFeatures == null
        ? Collections.emptySet()
        : Collections.unmodifiableSet(activeFeatures);
  }
}
