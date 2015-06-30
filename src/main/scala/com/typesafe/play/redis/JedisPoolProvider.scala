package com.typesafe.play.redis

import java.net.URI
import javax.inject.{Provider, Inject, Singleton}

import org.apache.commons.lang3.builder.ReflectionToStringBuilder
import play.api.inject.ApplicationLifecycle
import play.api.{Logger, Configuration}
import redis.clients.jedis.{JedisPool, JedisPoolConfig}

import scala.concurrent.Future

@Singleton
class JedisPoolProvider @Inject()(config: Configuration, lifecycle: ApplicationLifecycle) extends Provider[JedisPool]{

  lazy val logger = Logger("redis.module")
  lazy val get: JedisPool = {
    val jedisPool = {
      val redisUri = config.getString("redis.uri").map(new URI(_))

      val host = config.getString("redis.host")
        .orElse(redisUri.map(_.getHost))
        .getOrElse("localhost")

      val port = config.getInt("redis.port")
        .orElse(redisUri.map(_.getPort).filter(_ != -1))
        .getOrElse(6379)

      val password = config.getString("redis.password")
        .orElse(redisUri.map(_.getUserInfo).filter(_ != null).filter(_ contains ":").map(_.split(":", 2)(1)))
        .orNull

      val timeout = config.getInt("redis.timeout")
        .getOrElse(2000)

      val database = config.getInt("redis.database")
        .getOrElse(0)

      val poolConfig = createPoolConfig(config)
      Logger.info(s"Redis Plugin enabled. Connecting to Redis on $host:$port to $database with timeout $timeout.")
      Logger.info("Redis Plugin pool configuration: " + new ReflectionToStringBuilder(poolConfig).toString)


      new JedisPool(poolConfig, host, port, timeout, password, database)
    }

    logger.info("Starting Jedis Pool Provider")

    lifecycle.addStopHook(() => Future.successful {
      logger.info("Stopping Jedis Pool Provider")
      jedisPool.destroy()
    })

    jedisPool
  }

  private def createPoolConfig(config: Configuration): JedisPoolConfig = {
    val poolConfig: JedisPoolConfig = new JedisPoolConfig()
    config.getInt("redis.pool.maxIdle").foreach(poolConfig.setMaxIdle)
    config.getInt("redis.pool.minIdle").foreach(poolConfig.setMinIdle)
    config.getInt("redis.pool.maxTotal").foreach(poolConfig.setMaxTotal)
    config.getLong("redis.pool.maxWaitMillis").foreach(poolConfig.setMaxWaitMillis)
    config.getBoolean("redis.pool.testOnBorrow").foreach(poolConfig.setTestOnBorrow)
    config.getBoolean("redis.pool.testOnReturn").foreach(poolConfig.setTestOnReturn)
    config.getBoolean("redis.pool.testWhileIdle").foreach(poolConfig.setTestWhileIdle)
    config.getLong("redis.pool.timeBetweenEvictionRunsMillis").foreach(poolConfig.setTimeBetweenEvictionRunsMillis)
    config.getInt("redis.pool.numTestsPerEvictionRun").foreach(poolConfig.setNumTestsPerEvictionRun)
    config.getLong("redis.pool.minEvictableIdleTimeMillis").foreach(poolConfig.setMinEvictableIdleTimeMillis)
    config.getLong("redis.pool.softMinEvictableIdleTimeMillis").foreach(poolConfig.setSoftMinEvictableIdleTimeMillis)
    config.getBoolean("redis.pool.lifo").foreach(poolConfig.setLifo)
    config.getBoolean("redis.pool.blockWhenExhausted").foreach(poolConfig.setBlockWhenExhausted)
    poolConfig
  }
}
