/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dsl.jbang.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.camel.dsl.jbang.core.commands.CamelJBangMain;

/**
 * Main class for the Camel JBang Fat-Jar Launcher.
 * <p>
 * This launcher provides a self-contained executable JAR that includes all dependencies required to run Camel JBang
 * without the need for the JBang two-step process.
 * <p>
 * System properties can be pre-configured at build time via the camel-launcher.properties file.
 */
public class CamelLauncher {

    private static final String LAUNCHER_PROPERTIES = "/camel-launcher.properties";

    /**
     * Main entry point for the Camel JBang Fat-Jar Launcher.
     *
     * @param args command line arguments to pass to Camel JBang
     */
    public static void main(String... args) {
        // Load and set system properties from embedded configuration
        loadLauncherProperties();

        CamelJBangMain.run(args);
    }

    /**
     * Loads properties from the embedded camel-launcher.properties file and sets them as system properties. Only sets
     * properties that are not already defined in system properties (command line takes precedence).
     */
    private static void loadLauncherProperties() {
        try (InputStream is = CamelLauncher.class.getResourceAsStream(LAUNCHER_PROPERTIES)) {
            if (is == null)
                return;

            Properties props = new Properties();
            props.load(is);

            props.forEach((key, value) -> System.getProperties().putIfAbsent(key, value));
        } catch (IOException e) {
            System.err.println("Warning: Failed to load launcher properties: " + e.getMessage());
        }
    }
}
