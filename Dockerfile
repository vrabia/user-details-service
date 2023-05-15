FROM maven:3.8.1-openjdk-17-slim
# Since we want to execute the mvn command with RUN (and not when the container gets started),
# we have to do here some manual setup which would be made by the maven's entrypoint script
RUN mkdir -p /root/.m2 \
    && mkdir /root/.m2/repository
# Copy maven settings, containing repository configurations
COPY settings.xml /root/.m2

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn clean install

CMD ["mvn", "spring-boot:run"]
