Example dashboard tool
------------------------------------------


Usage
-----

Have your AWS creds in $HOME/.aws . Then:

```
docker run -ti --rm -v "$PWD:/src" -v "$HOME/.ivy2":/root/.ivy2 -v "$HOME/.aws":/root/.aws -p 9000:9000 sjs-dashboard  bash -c "cd /src ; sbt run"
```


Then open your browser at localhost:9000

Build
-----

```
docker build -t sjs-dashboard .
```


P.S.
----

A good ScalaJS-React example:

https://github.com/malaman/scala-weather-app

Not related, but really wanted to save the link somewhere:

https://stackoverflow.com/questions/17965059/what-is-lifting-in-scala
