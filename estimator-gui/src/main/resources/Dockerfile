FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD estimator-gui-0.0.1-SNAPSHOT.jar estimator-gui.jar
RUN sh -c 'touch /estimator-gui.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /estimator-gui.jar" ]