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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test for the CamelLauncher properties loading mechanism.
 */
public class CamelLauncherPropertiesTest {

    @Test
    public void testLauncherPropertiesFileExists() throws IOException {
        // Verify the properties file is bundled in the JAR
        try (InputStream is = CamelLauncher.class.getResourceAsStream("/camel-launcher.properties")) {
            assertNotNull(is, "camel-launcher.properties should be available in classpath");

            Properties props = new Properties();
            props.load(is);

            // Verify expected properties are present
            assertNotNull(props.getProperty("camel.jbang.quarkusGroupId"),
                    "Should contain camel.jbang.quarkusGroupId property");
            assertNotNull(props.getProperty("camel.jbang.quarkusArtifactId"),
                    "Should contain camel.jbang.quarkusArtifactId property");
            assertNotNull(props.getProperty("camel.jbang.quarkusVersion"),
                    "Should contain camel.jbang.quarkusVersion property");
            assertNotNull(props.getProperty("camel.jbang.camelSpringBootVersion"),
                    "Should contain camel.jbang.camelSpringBootVersion property");
            assertNotNull(props.getProperty("camel.jbang.repos"),
                    "Should contain camel.jbang.repos property");
        }
    }

    @Test
    public void testDefaultPropertyValues() throws IOException {
        // Verify default values match expected configuration
        try (InputStream is = CamelLauncher.class.getResourceAsStream("/camel-launcher.properties")) {
            assertNotNull(is, "camel-launcher.properties should be available in classpath");

            Properties props = new Properties();
            props.load(is);

            // Check default Quarkus configuration
            assertEquals("com.redhat.quarkus.platform", props.getProperty("camel.jbang.quarkusGroupId"),
                    "Default quarkusGroupId should be com.redhat.quarkus.platform");
            assertEquals("quarkus-bom", props.getProperty("camel.jbang.quarkusArtifactId"),
                    "Default quarkusArtifactId should be quarkus-bom");

            // Check Maven repositories are configured
            String repos = props.getProperty("camel.jbang.repos");
            assertNotNull(repos, "Maven repositories should be configured");
        }
    }
}
