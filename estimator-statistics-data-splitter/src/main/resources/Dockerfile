FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD estimator-statistics-data-splitter-0.0.1-SNAPSHOT.jar estimator-data-splitter.jar
RUN sh -c 'touch /estimator-data-splitter.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /estimator-data-splitter.jar" ]