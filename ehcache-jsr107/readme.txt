maven profiles:
- ehcache
- infinispan

mvn eclipse:eclipse -Pehcache -o
mvn eclipse:eclipse -Pinfinispan -o
mvn eclipse:eclipse -Pehcache,infinispan -o

mvn clean test -Dtest=FirstCache -Pehcache -o
mvn clean test -Dtest=FirstCache -Pinfinispan -o
mvn clean test -Dtest=FirstCache -Pehcache,infinispan -o

cd ~/workspace/labs/jcache/ehcache-jsr107