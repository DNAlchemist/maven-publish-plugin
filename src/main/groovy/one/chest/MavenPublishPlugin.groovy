/*
 * Copyright 2020 - Ruslan Mikhalev <mikhalev.ruslan@gmail.com>
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
package one.chest

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication

@CompileStatic
class MavenPublishPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin: "maven-publish"
        project.extensions.create("publish", MavenPublishExtension)

        project.afterEvaluate {
            configurePublishing(project);
        }
    }

    @CompileDynamic
    void configurePublishing(Project project) {
        project.with {
            // GRADLE DSL
            publishing {
                repositories {
                    maven {
                        String usernameStr = extractUsername(project)
                        String passwordStr = extractPassword(project)
                        if (usernameStr && passwordStr) {
                            credentials {
                                username usernameStr
                                password passwordStr
                            }
                        }
                        url = extractUrl(project)
                        if (project.extensions.publish.repository.insecure) {
                            allowInsecureProtocol = project.extensions.publish.repository.insecure as boolean
                        }
                        mavenContent {
                            releasesOnly()
                        }
                    }
                }

                publications {
                    maven(MavenPublication) {
                        groupId = project.extensions.publish.groupId ?: project.group
                        artifactId = project.extensions.publish.artifactId ?: project.name
                        version = project.extensions.publish.version ?: project.version
                        if (version == "unspecified") {
                            throw new GradleException("Version unspecified")
                        }
                        from components.java
                    }
                }
            }
        }
    }

    static String fetchProperty(Project project, String propertyName) {
        try {
            return project.property(propertyName)
        } catch (MissingPropertyException ignored) {
            return null
        }
    }

    @CompileDynamic
    static String extractUsername(Project project) {
        project.extensions.publish.repository.username ?: fetchProperty(project, "publish.repository.username")
    }

    @CompileDynamic
    static String extractPassword(Project project) {
        project.extensions.publish.repository.password ?: fetchProperty(project, "publish.repository.password")
    }

    @CompileDynamic
    static String extractUrl(Project project) {
        project.extensions.publish.repository.url ?: fetchProperty(project, "publish.repository.url")
    }
}
