#!/bin/bash

# 1. Download Tomcat 9
curl -O https://downloads.apache.org/tomcat/tomcat-9/v9.0.85/bin/apache-tomcat-9.0.85.tar.gz
tar -xzf apache-tomcat-9.0.85.tar.gz
mv apache-tomcat-9.0.85 tomcat

# 2. Remove default apps
rm -rf tomcat/webapps/*

# 3. Build the WAR using Maven Wrapper
./mvnw clean package

# 4. Copy the WAR to Tomcat's webapps folder
cp target/*.war tomcat/webapps/ROOT.war

# 5. Start Tomcat
tomcat/bin/catalina.sh run
