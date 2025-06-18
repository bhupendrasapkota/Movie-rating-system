# -------- Stage 1: Build WAR with Maven --------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Package the WAR
RUN mvn clean package -DskipTests

# -------- Stage 2: Run WAR with Tomcat --------
FROM tomcat:10.1-jdk17-temurin

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy WAR from the build stage
COPY --from=build /app/target/MovieRatingSystem.war /usr/local/tomcat/webapps/ROOT.war
