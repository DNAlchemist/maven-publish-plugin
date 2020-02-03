package one.chest

import groovy.transform.CompileStatic

@CompileStatic
class MavenPublishRepository {
    private String username, password, url

    void username(String username) {
        this.username = username
    }

    String getUsername() {
        username
    }

    void password(String password) {
        this.password = password
    }

    String getPassword() {
        password
    }

    void url(String url) {
        this.url = url
    }

    String getUrl() {
        url
    }
}
