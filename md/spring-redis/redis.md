
### 参考资料
-  [redis推荐参数设置](https://segmentfault.com/a/1190000020362579?utm_source=tag-newest)
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

##### `RedisCustomConversions`实例是重写了jar里面的吗，把自己定义的写进去
RedisCustomConversions的代码里，其实我并不是替换，而是把自己的Converter追加了进去。
#### Jedis客户端的使用
- Jedis不是线程安全的
- JedisPool用于获取Jedis实例

```
  /**
     *  config JedisPoolConfig
      * @return
     */
    @Bean
    @ConfigurationProperties("redis")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }


    /**
     * config JedisPool
     * @param host
     * @return
     */
    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(@Value("${redis.host}") String host) {
        return new JedisPool(jedisPoolConfig(), host);
    }
```
##### Jedis client参数设置
```
redis.host=localhost
# 最大连接数, 默认8个
redis.maxTotal=5
# 最大空闲连接数, 默认8个
redis.maxIdle=5
# 在获取连接的时候检查有效性
redis.testOnBorrow=true 
```
#### redis的哨兵和集群模式及在Jedis中的体现

#### Lettuce客户端的使用
- spring-boot-starter-data-redis中默认就是lettuce,jedis需要我们自己来引入
- LettucePoolingClientConfiguration

#### RedisTemplate
#####  每多一个实体类,就要配一个RedistTemplate<XX,XX>,是不是这样？
这是一个便捷的辅助模板类，用来方便你和Redis交互，其中封装了一些序列化和反序列化逻辑，如果你放Redis里的类型不多，可以配一下，如果多的话，
我建议你还是自己做JSON序列化和反序列化操作，直接通过StringRedisTemplate来做操作。
##### 
#### Repository对redis的支持
RedisJta
- @RedisHash
#####    @RedisHash的value值是redis中的一个key? 这么多key都能指定这条记录吗？
不是KEY，可以看看这个注解的注释，这里的value指的是The prefix to distinguish between domain types，
其实是个前缀。明白了这点，你的第二个问题也解决了。

#### @RedisHash设置过期时间
RedisHash里加了timeToLive的

#### Spring Cache



##### 