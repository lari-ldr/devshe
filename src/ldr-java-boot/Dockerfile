FROM openjdk:18-jdk
RUN mkdir /opt/ldr-java-boot
COPY ./target/ldr-java-boot.jar /opt/ldr-java-boot
COPY ./wait /opt/ldr-java-boot
RUN chmod +x /opt/ldr-java-boot/wait
WORKDIR /opt/ldr-java-boot
ENTRYPOINT ./wait && java -jar ldr-java-boot.jar

