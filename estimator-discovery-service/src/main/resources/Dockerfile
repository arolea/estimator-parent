FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD estimator-discovery-service-0.0.1-SNAPSHOT.jar estimator-discovery.jar
RUN sh -c 'touch /estimator-discovery.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /estimator-discovery.jar" ]