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

import org.gradle.internal.impldep.com.google.common.io.Files
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class MavenPublishPluginFunctionalTest extends Specification {

    def "setup gradle.properties and publish to folder"() {
        given:
        def tmpDir = Files.createTempDir().toPath()

        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle").text = ""
        new File(projectDir, "gradle.properties").text = """
            publish.repository.url=${tmpDir}
""".stripIndent()

        new File(projectDir, "build.gradle").text = """
            plugins {
                id('java')
                id('one.chest.maven-publish-plugin')
            }
            group = "rocks.mango"
            version = "0.1"
""".stripIndent()

        when:
        def result = runTask(projectDir, "publish")

        then:
        result.output.contains("BUILD SUCCESSFUL")
        assertArtifact(tmpDir, "rocks.mango", "functionalTest", "0.1")
    }

    def "setup extension and publish to folder"() {
        given:
        def tmpDir = Files.createTempDir().toPath()

        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle").text = ""
        new File(projectDir, "gradle.properties").text = ""

        new File(projectDir, "build.gradle").text = """
            plugins {
                id('java')
                id('one.chest.maven-publish-plugin')
            }
            
            publish {
                repository {
                    url "${tmpDir}"
                }
                groupId "rocks.mango"
                artifactId "example"
                version "0.1"
            }
""".stripIndent()

        when:
        def result = runTask(projectDir, "publish")

        then:
        result.output.contains("BUILD SUCCESSFUL")
        assertArtifact(tmpDir, "rocks.mango", "example", "0.1")
    }

    def "setup insecure"() {
        given:
        def tmpDir = Files.createTempDir().toPath()

        def projectDir = new File("build/functionalTest")
        projectDir.mkdirs()
        new File(projectDir, "settings.gradle").text = ""
        new File(projectDir, "gradle.properties").text = ""

        new File(projectDir, "build.gradle").text = """
            plugins {
                id('java')
                id('one.chest.maven-publish-plugin')
            }
            
            publish {
                repository {
                    url "${tmpDir}"
                    insecure true
                }
                groupId "rocks.mango"
                artifactId "example"
                version "0.1"
            }
""".stripIndent()

        when:
        def result = runTask(projectDir, "publish")

        then:
        result.output.contains("BUILD SUCCESSFUL")
        assertArtifact(tmpDir, "rocks.mango", "example", "0.1")
    }

    def runTask(File projectDir, String task) {
        def runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments(task)
        runner.withProjectDir(projectDir)
        return runner.build()
    }

    def assertArtifact(Path artifactDir, String groupId, String artifactId, String artifactVersion) {
        def groupPath = Paths.get(groupId.replace(".", File.separator))
        def jarFilename = "${artifactId}-${artifactVersion}.jar"

        def group = artifactDir.resolve(groupPath)
        def name = group.resolve(artifactId)
        def version = name.resolve(artifactVersion)
        def artifact = version.resolve(jarFilename)

        assert group.toFile().exists(): /"${groupPath}" not found. File list: ${artifactDir.toFile().listFiles()*.name}/
        assert name.toFile().exists(): /"${artifactId}" not found. File list: ${group.toFile().listFiles()*.name}/
        assert version.toFile().exists(): /"${artifactVersion}" not found. File list: ${name.toFile().listFiles()*.name}/
        assert artifact.toFile().exists(): /"${jarFilename}" not found. File list: ${version.toFile().listFiles()*.name}/
        true
    }
}
