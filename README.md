BigBang problems investigation helper tool
------------------------------------------


Usage
-----

```
docker run -ti --rm -v "$PWD:/src" -v "$HOME/.ivy2":/root/.ivy2 -v "$HOME/.aws":/root/.aws -p 9000:9000 internal.hub.polyverse.io/bigbang-dashboard  bash -c "cd /src ; sbt run"
```


Build
-----

```
docker build -t internal.hub.polyverse.io/bigbang-dashboard .
```


P.S.
----



Not related, but really wanted to save the link somewhere:

https://stackoverflow.com/questions/17965059/what-is-lifting-in-scala
