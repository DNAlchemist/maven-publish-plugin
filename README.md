# Maven Publish Plugin

**Simple plugin to upload java components to Maven repository**

You can specify url as a file or a Maven repository

## Plugin setup

### via gradle properties

    publish.repository.url=https://nexus.example.com/repository/maven-releases
    publish.repository.username=admin
    publish.repository.password=password

### via project settings

    project.group = 'com.example'
    project.name = 'application'
    project.version = '0.1'

### via build.gradle extension

    publish {
        repository {
            url 'https://nexus.example.com/repository/maven-releases'
            username 'admin'
            password 'admin'
        }
        groupId 'com.example'
        artifactId 'application'                         
        version '0.1'
    }
    
**Any parameter declared in extension has the highest priority**
    