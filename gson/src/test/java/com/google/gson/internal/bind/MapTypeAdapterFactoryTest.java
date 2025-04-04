/*
 * Copyright (C) 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal.bind;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;

public class MapTypeAdapterFactoryTest {

  private Gson createGsonWithComplexMapSerialization() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.emptyMap(), true, Collections.emptyList());
    MapTypeAdapterFactory factory = new MapTypeAdapterFactory(constructorConstructor, true);
    return new GsonBuilder().registerTypeAdapterFactory(factory).create();
  }

  @Test
  public void testWriteAsObjectWithStringKeys() {
    // Create a map with string keys.
    Map<String, String> map = new LinkedHashMap<>();
    map.put("name", "Alice");
    map.put("city", "Wonderland");

    Gson gson = createGsonWithComplexMapSerialization();
    String json = gson.toJson(map, new TypeToken<Map<String, String>>() {}.getType());
    assertEquals("{\"name\":\"Alice\",\"city\":\"Wonderland\"}", json);
  }

  @Test
  public void testWriteAsObjectWithIntegerKeys() {
    // Create a map with integer keys.
    Map<Integer, String> map = new LinkedHashMap<>();
    map.put(10, "ten");
    map.put(20, "twenty");

    Gson gson = createGsonWithComplexMapSerialization();
    String json = gson.toJson(map, new TypeToken<Map<Integer, String>>() {}.getType());
    assertEquals("{\"10\":\"ten\",\"20\":\"twenty\"}", json);
  }

  @Test
  public void testWriteAsObjectWithBooleanKeys() {
    // Create a map with boolean keys.
    Map<Boolean, String> map = new LinkedHashMap<>();
    map.put(true, "yes");
    map.put(false, "no");

    Gson gson = createGsonWithComplexMapSerialization();
    String json = gson.toJson(map, new TypeToken<Map<Boolean, String>>() {}.getType());
    assertEquals("{\"true\":\"yes\",\"false\":\"no\"}", json);
  }
}
