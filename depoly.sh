#!/bin/bash
mvn clean compile jar:jar
mvn deploy -DaltDeploymentRepository=pygzx-mvn-repo::default::file:/Users/pygzx/maven-repo/repository
