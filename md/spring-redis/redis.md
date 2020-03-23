### 概览
- redis简介
- Spring对redis的支持有哪些
- Jedis客户端的使用
- redis的哨兵和集群模式及在Jedis中的体现
- Lettuce客户端的使用、读写分离
- RedisTemplate
- Repository的支持
- Spring Cache

#### redis简介

REmote DIctionary Server(Redis) 是一个由Salvatore Sanfilippo写的key-value存储系统。
Redis是一个开源的使用ANSI C语言编写、遵守BSD协议、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API。
它通常被称为数据结构服务器，因为值（value）可以是 字符串(String), 哈希(Hash), 列表(list), 集合(sets) 和 有序集合(sorted sets)等类型。

- [菜鸟教程](https://www.runoob.com/redis/redis-tutorial.html)

#### Spring对redis的支持有哪些
- spring-data-redis
- spring-data-jta
- RedisTemplate
- Repository
- Spring Cache
- Jedis
- Lettuce

#### Jedis客户端的使用
- Jedis不是线程安全的
- JedisPool用于获取Jedis实例
#### redis的哨兵和集群模式及在Jedis中的体现

#### Lettuce客户端的使用、读写分离

#### RedisTemplate

#### Repository对redis的支持

#### Spring Cache