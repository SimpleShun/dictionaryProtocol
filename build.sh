#! /bin/env bash
set -e
mvn clean package && java -cp mariadb-java-client-3.5.3.jar:target/dictionaryProtocol-1.0-SNAPSHOT.jar sixth.sem.App
