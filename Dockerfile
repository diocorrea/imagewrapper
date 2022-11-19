FROM openjdk:17
COPY target/imagewrapper-0.0.1-SNAPSHOT.jar imagewrapper-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/imagewrapper-0.0.1-SNAPSHOT.jar"]