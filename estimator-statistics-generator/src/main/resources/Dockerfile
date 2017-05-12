FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD estimator-statistics-generator-0.0.1-SNAPSHOT.jar estimator-statistics-generator.jar
RUN sh -c 'touch /estimator-statistics-generator.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /estimator-statistics-generator.jar" ]