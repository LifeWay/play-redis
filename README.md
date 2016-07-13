[![Build Status](https://travis-ci.org/lifeway/play-redis.svg?branch=master)](https://travis-ci.org/lifeway/play-redis)

# Redis Plugin

This plugin provides support for [Redis](http://redis.io/) using the best Java driver [Jedis](https://github.com/xetorthio/jedis) and the corresponding Scala wrapper [Sedis](https://github.com/pk11/sedis). Also implements play's internal [Caching] (https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/cache/Cache.scala#L9) interface  

### Notice
This plugin is a fork of the redis plugin from https://github.com/playframework/play-plugins. That repository is no longer actively maintained, and the goal
is to provide a redis plugin for play framework with more responsive updates to PR's and new play releases.

For Play releases prior to 2.5.x, please refer to the original repo.

# Features

###  Provides a Redis-based Cache API (supported types: String, Int, Long, Boolean and Serializable) ie.

```java
//java
String f = (String) play.cache.Cache.get("mykey");
```

and 

```scala
//scala
val o = play.api.cache.Cache.getAs[String]("mykey")
```

#### Configurable

* Point to your Redis server using configuration settings  ```redis.host```, ```redis.port```,  ```redis.password``` and ```redis.database``` (defaults: ```localhost```, ```6379```, ```null``` and ```0```)
* Alternatively, specify a URI-based configuration using ```redis.uri``` (for example: ```redis.uri="redis://user:password@localhost:6379"```).
* Set the timeout in milliseconds using ```redis.timeout``` (default is 2000).
* Configure any aspect of the connection pool. See [the documentation for commons-pool2 ```GenericObjectPoolConfig```](https://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/impl/GenericObjectPoolConfig.html), the underlying pool implementation, for more information on each setting.
    * redis.pool.maxIdle
    * redis.pool.minIdle
    * redis.pool.maxTotal
    * redis.pool.maxWaitMillis
    * redis.pool.testOnBorrow
    * redis.pool.testOnReturn
    * redis.pool.testWhileIdle
    * redis.pool.timeBetweenEvictionRunsMillis
    * redis.pool.numTestsPerEvictionRun
    * redis.pool.minEvictableIdleTimeMillis
    * redis.pool.softMinEvictableIdleTimeMillis
    * redis.pool.lifo
    * redis.pool.blockWhenExhausted


#### Allows direct access to Jedis and Sedis:
Because the underlying Sedis Pool was injected for the cache module to use, you can just inject the sedis Pool yourself, something like this:

```scala
//scala
import javax.inject.Inject
import org.sedis.Pool

class TryIt @Inject()(sedisPool: Pool) extends Controller {
   val directValue: String = sedisPool.withJedisClient(client => client.get("someKey"))
}
```

```java
//java
import javax.inject.Inject
import redis.clients.jedis.JedisPool

class TryIt extends Controller {
   
   //The JedisPool will be injected for you from the module
   @Inject JedisPool jedisPool;

   ...
}
```

The module also supports compile time DI via RedisCacheComponents. Mix this in with your custom application loader just like you would if you were using EhCacheComponents from the reference cache module.

# How to install

```"com.lifeway" %% "play-modules-redis" % "2.5.1"``` to your dependencies

* The default cache module (EhCache) will be used for all non-named cache UNLESS this module (RedisModule) is the only cache module that was loaded. If this module is the only cache module being loaded, it will work as expected on named and non-named cache. To disable the default cache module so that this Redis Module can be the default cache you must put this in your configuration:

 ```
 play.modules.disabled = ["play.api.cache.EhCacheModule"]
 ```

* This module supports play 2.5 NamedCaches through key namespacing on a single Sedis pool. To add additional namepsaces besides the default (play), the configuration would look like such:

 ```
 play.cache.redis.bindCaches = ["db-cache", "user-cache", "session-cache"]
 ```




## Licence

This software is licensed under the Apache 2 license, quoted below.

Copyright 2016 LifeWay Christian Resources (http://www.lifeway.com).  
Copyright 2012 Typesafe (http://www.typesafe.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.