mvn clean test -Dspring.profiles.active=jdk-proxy -o
mvn clean test -Paop-proxy -Dspring.profiles.active=aop-proxy -o
mvn clean test -Pcache-aspect -Dspring.profiles.active=cache-aspect -o

cd ~/workspace/labs/jcache/spring-jcache
