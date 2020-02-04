# Maven Publish Plugin

**Simple plugin to upload java components to Maven repository**

You can specify url as a file or a Maven repository

## Applying plugin

Using the plugins DSL:

    plugins {
        id 'one.chest.maven-publish-plugin' version '0.0.1'
    }

Using legacy plugin application:

    buildscript {
        repositories {
            maven {
                url 'https://plugins.gradle.org/m2/'
            }
        }
        dependencies {
            classpath 'gradle.plugin.one.chest:maven-publish-plugin:0.0.1'
        }
    }

    apply plugin: 'one.chest.maven-publish-plugin'

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

*Any parameter declared in extension has the highest priority*

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
    
    
