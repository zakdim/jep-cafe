# Refactoring Java 8 code with Java 17 new features - JEP Café #9

[JEP Café #9](https://www.youtube.com/watch?v=wW7uzc61tZ8&list=PLX8CzqL3ArzV4BpOzLanxd4bZr46x5e87&index=14&t=930s)

## Java Microbenchmark Harness (JMH)

[JMH GitHub](https://github.com/openjdk/jmh)

### Command Line Usage

1. Generate project :
 
```shell
mvn archetype:generate \
  -DinteractiveMode=false \
  -DarchetypeGroupId=org.openjdk.jmh \
  -DarchetypeArtifactId=jmh-java-benchmark-archetype \
  -DgroupId=org.github.zakdim \
  -DartifactId=jep-cafe-09 \
  -Dversion=1.0
```

2. Build project

```shell
mvn clean verify
```

3. Run the benchmarks

```shell
java -jar target/benchmarks.jar
```