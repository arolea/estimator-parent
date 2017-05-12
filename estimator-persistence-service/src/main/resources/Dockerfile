FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD estimator-persistence-service-0.0.1-SNAPSHOT.jar estimator-persistence-service.jar
RUN sh -c 'touch /estimator-persistence-service.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /estimator-persistence-service.jar" ]