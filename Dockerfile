FROM tomcat:10.1-jdk17-temurin
# Remove default ROOT app from Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file and rename to ROOT.war
COPY target/MovieRatingSystem.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080
EXPOSE 8080

# Tomcat startup command is already baked in the tomcat base image
