#!/bin/bash

# Download and extract Tomcat
curl -O https://downloads.apache.org/tomcat/tomcat-9/v9.0.85/bin/apache-tomcat-9.0.85.tar.gz
tar -xzf apache-tomcat-9.0.85.tar.gz
mv apache-tomcat-9.0.85 tomcat

# Clean default Tomcat apps
rm -rf tomcat/webapps/*

# Build the WAR using Maven
./mvnw clean package

# Copy the generated WAR file into Tomcat
cp target/*.war tomcat/webapps/ROOT.war

# Start Tomcat server
tomcat/bin/catalina.sh run
