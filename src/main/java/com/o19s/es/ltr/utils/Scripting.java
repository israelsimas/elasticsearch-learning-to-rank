/*
 * Copyright [2017] Wikimedia Foundation
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
package com.o19s.es.ltr.utils;

import java.io.IOException;
import java.util.Collections;
import org.elasticsearch.script.DoubleValuesScript;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.ScriptType;

public class Scripting {
  private static ScriptService scriptService = null;

  private Scripting() {}

  public static void initScriptService(ScriptService scriptService) {
    Scripting.scriptService = scriptService;
  }

  public static DoubleValuesScript compile(String scriptSource) throws IOException {
    if (Scripting.scriptService == null) {
      throw new IOException("Script service not initialized.");
    }

    Script script =
        new Script(ScriptType.INLINE, "expression", scriptSource, Collections.emptyMap());
    return scriptService.compile(script, DoubleValuesScript.CONTEXT).newInstance();
  }
}
