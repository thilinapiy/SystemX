#
# docker build -t thilina/licensemanager:$(date '+%Y%m%d%H%M') .
#
#
FROM williamyeh/java8

MAINTAINER Thilina Piyasundara 

RUN useradd -c 'app user' -m -d /home/appuser -s /bin/bash appuser
#RUN adduser -D -u 1000 appuser
#RUN chown -R appuser:appuser /home/appuser
ENV HOME /home/appuser
ENV MAVEN_CONFIG /home/appuser/.m2 
ENV MAVEN_HOME /opt/maven
ENV M2_HOME /opt/maven
ENV M3_HOME /opt/maven
ENV PATH="/opt/maven/bin:${PATH}"

COPY m2 /home/appuser/.m2

ADD . /home/appuser/

ADD apache-maven-3.3.9-bin.tar.gz /opt
RUN chown -R appuser:appuser /home/appuser && ln -s /opt/apache-maven-3.3.9 /opt/maven && rm -rf /home/appuser/m2 /home/appuser/apache-maven-3.3.9-bin.tar.gz
#RUN rm -rf /home/appuser/LicenceAgent/target /home/appuser/ServerX/dist/target

EXPOSE 8081

USER appuser
CMD ["java", "-jar", "/home/appuser/LicenceManager/target/LicenceManager-0.1-jar-with-dependencies.jar"]

