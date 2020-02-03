package one.chest

import groovy.transform.CompileStatic

@CompileStatic
class MavenPublishExtension {

    MavenPublishRepository repository = new MavenPublishRepository()
    private String groupId, artifactId, version

    void repository(@DelegatesTo(MavenPublishRepository) Closure<?> closure) {
        closure.delegate = repository
        closure()
    }

    void groupId(String groupId) {
        this.groupId = groupId
    }

    String getGroupId() {
        groupId
    }

    void artifactId(String artifactId) {
        this.artifactId = artifactId
    }

    String getArtifactId() {
        artifactId
    }

    void version(String version) {
        this.version = version
    }

    String getVersion() {
        version
    }
}
