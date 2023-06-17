# JEP Café #12

Launching 10 millions virtual threads with Loom -
[JEP Café episode 12](https://www.youtube.com/watch?v=UVoGE0GZZPI)

## VM Options to run examples

https://stackoverflow.com/a/53647605

```
# Has to be added when compiling (javac) and when running (java) the code :
--add-exports=java.base/jdk.internal.vm=ALL-UNNAMED
```