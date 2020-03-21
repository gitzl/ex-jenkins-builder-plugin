# ex-jenkins-plugin

The plugin extends Builder implements SimpleBuildStep , allows you deploy image to cloud

Requirement:
- Java8
- mave3+

## Table of Contents

- [Config](#config)
- [Install](#install)
- [Start](#start)
- [Show Demo](#show-demo)
- [Develop Plugin steps](#develop-Plugin-steps)


## Config
add the content to :  ~/.m2/settings.xml  

```$xslt

<settings>
  <pluginGroups>
    <pluginGroup>org.jenkins-ci.tools</pluginGroup>
  </pluginGroups>

  <profiles>
    <!-- Give access to Jenkins plugins -->
    <profile>
      <id>jenkins</id>
      <activation>
        <activeByDefault>true</activeByDefault> <!-- change this to false, if you don't like to have it on per default -->
      </activation>
      <repositories>
        <repository>
          <id>repo.jenkins-ci.org</id>
          <url>https://repo.jenkins-ci.org/public/</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>repo.jenkins-ci.org</id>
          <url>https://repo.jenkins-ci.org/public/</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>
  <mirrors>
    <mirror>
      <id>repo.jenkins-ci.org</id>
      <url>https://repo.jenkins-ci.org/public/</url>
      <mirrorOf>m.g.o-public</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

## Install 

make sure the project can build success
````
$ cd ex-jenkins-builder-plugin 
$ mvn verify 

````


## Start

```
$ mvn hpi:run 

```
If Jenkins is fully up and running ,visiting http://localhost:8080/jenkins in your browser

## Show Demo

![](demo.png)


## Develop-Plugin-Steps

Develop the Plugin steps and detail , at the blog : https://gitzl.github.io/2019/02/17/jenkinsPlugin/



