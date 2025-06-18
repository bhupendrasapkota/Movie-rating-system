# Use official Tomcat base image
FROM tomcat:9.0-jdk17

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file into Tomcat webapps as ROOT.war
COPY target/MovieRatingSystem.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (default)
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
