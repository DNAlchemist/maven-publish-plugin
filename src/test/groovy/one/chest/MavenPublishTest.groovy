package one.chest

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class MavenPublishTest extends Specification {
    def "plugin registers task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply("java")
        project.plugins.apply("one.chest.maven-publish-plugin")

        then:
        project.extensions.publish
    }
}
