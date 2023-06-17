# JEP Café #12

Launching 10 millions virtual threads with Loom -
[JEP Café episode 12](https://www.youtube.com/watch?v=UVoGE0GZZPI)

## VM Options to run examples

https://stackoverflow.com/a/53647605

```
# Has to be added when compiling (javac) and when running (java) the code :
--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED
```

To compile/run examples :

* Project Structure > Project > SDK : 19
* Project Structure > Project > Language level : 19 (Preview)
* Maven compile plugin configuration :

``` xml
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>${maven.compiler.source}</source>
          <target>${maven.compiler.target}</target>
          <compilerArgs>
            <arg>--enable-preview</arg>
            <arg>--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED</arg>
          </compilerArgs>
        </configuration>
      </plugin>
```

* For example C add VM Option : `--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED` to run configuration