### 一、Redis简介
 
https://my.oschina.net/u/3343218/blog/2989564

官方给出的定义是：

Redis 是一个开源（BSD许可）的，内存中的数据结构存储系统，它可以用作数据库、缓存和消息中间件。

它支持多种类型的数据结构，如 
- [字符串（strings）](http://www.redis.cn/topics/data-types-intro.html#strings)， 
- [散列（hashes）](http://www.redis.cn/topics/data-types-intro.html#hashs)， 
- [列表（lists)](http://www.redis.cn/topics/data-types-intro.html#lists)， 
- [集合（sets）](http://www.redis.cn/topics/data-types-intro.html#sets)， 
- [有序集合（sorted sets） 与范围查询](http://www.redis.cn/topics/data-types-intro.html#sorted-sets)， 
- bitmaps， 
- hyperloglogs 
- 地理空间（geospatial） 索引半径查询。

Redis 内置了 
- 复制（replication），
- LUA脚本（Lua scripting）， 
- LRU驱动事件（LRU eviction），
- 事务（transactions）
提供不同级别的 `磁盘持久化（persistence)`， 并通过 
`Redis哨兵（Sentinel）`和`自动 分区（Cluster）`提供高可用性（high availability）
    那么接下来，逐步分析官方给出的定义的重要概念。
    
####1.1 数据结构的操作

下面简单介绍常见命令：

##### （1）字符串（strings）：

- 添加：set key value
- 取值：get key
- (整型)递增： incr key
- (多值)添加：mset  key1 val1 key2 val2 key3 val3
- (多值)取值：mget key1 key2 key3
- 修改：set key newVal
- 查询（是否存在）：exists key
- 删除：del key
- 查询类型：type key
- (创建值后)设置超时（time时间后将key对应值删除）：expire key time
- (创建值时)设置超时：set  key  val  ex  time
- 去除超时：persist key
- 查看超时剩余时间：ttl key

#####（2）散列（hashes）：
- 添加多值：hmset  yourhash  field val  [field val ...]
- 添加单值：hset  yourhash  field  val
- 取多值：hmget  yourhash  field  [field ...]
- 取单值：hget  yourhash field
- 取全值： hgetall  yourhash
- 删除值：hdel  yourhash  field  [field ...]

#####（3）列表（lists）：

- 链表左边添加：lpush list val
- 链表右边添加：rpush list val
- 范围内取值：lrange list index_start  index_end
- 截取范围内值：ltrim list index_start index_end
- 添加多值：rpush list val1 val2 val3 val4
- 左边删除：lpop list
- 右边删除:   rpop list
- 阻塞式访问左删除：blpop list    或者   blpop list1 list2 list3
- 阻塞式访问右删除：brpop list    或者   brpop list1 list2 list3
- 原子性地返回并移除存储在 list1 的列表的最后一个元素（列表尾部元素）， 并把该元素放入存储在 list2的列表的第一个元素位置（列表头部） : rpoplpush list1 list2
- 阻塞版RPOPLPUSH：brpoplpush list1 list2

#####（4）集合（sets）： String 的无序排列 ， 适合用于表示对象间的关系 。

- 添加： sadd myset val1 val2 val3
- 查询元素：smembers  myset
- 查询特定元素： sismember  myset  val
- 删除（随机）：spop myset

#####（5）有序集合（sorted sets）：

- 添加（更新）：zadd mysortedset  score  val [score  val ...]
- 范围内取值：zrange mysortedset  score_begin score_end
- 取索引值：zscore mysortedset val
- 删除索引值最大的值： zpopmax  mysortedset
- 删除索引值最小的值： zpopmin  mysortedset

#####（6）bitmaps：不是实际的数据结构，而是一个字符串类型定义的面向比特的集合。

- 添加值：setbit key offset [offset ...]
- 取值：getbit  key  offset
#####（7）hyperloglogs：是一种概率的数据结构，用于计算唯一的数据。

- 添加：pfadd key element [element ...]
- 合并： pfmerge key  key1 key2

##### more 想详细了解可通过点击下面链接：
- redis官方命令大全:  https://redis.io/commands

#### 1.2 重要概念分析
##### (1）复制（基于主从结构）
##### （2）事务
**保证机制：**

事务中所有命令都被序列化并会按序执行，并不可打断；
Readis事物是原子性的，若执行则全部执行，若失败则全部失败。
    
**使用：**

- MULTI 命令开启事务；
- EXEC命令执行事务。
##### （3）持久化
**持久化方式：**

- RDB持久化方式能够在指定的时间间隔进行数据集快照存储；

- AOF持久化方式会记录每一个sever的写操作，当server重启时，将重新执行记录的写操作构建原始数据集；

如果只希望数据存在于server运行时，可以不进行持久化；

可以结合RDB和AOF进行持久化。
##### （4）哨兵（Sentinel）
**功能**

- 监控（monitoring）：Sentinel不断检查master和slave以确保他们按预期运行；

- 提醒（notification）：当被监控的Redis出现问题时，将会通过api向用户或者另一个程序发送通知；

- 自动故障转移（automatic failover）：当master失效时，Sentinel会开启故障转移处理，将一个slave提升为master，原依附于故障master的slaves将重新配置依附于新的master，并在使用Redis server的应用连接时，告知其使用master的新地址；

- 配置提供者（Configuretion provider）：Sentinel充当客户端（clilent）服务发现的根据来源，客户端连接Sentinel，为一个指定的服务请求当前Redis master响应的地址（若发生故障转移，则会向所有连接Sentinel的客户端告知master的新地址）。
##### （5）分区（Partitioning）
- 目的：

使用多个计算机提供的内存总和来构建更大的数据库（若没有分区，用户将只能使用单台计算机提供的内存量）；
拓展计算能力到多核和多计算机，将网络带宽拓展到多计算机和多适配器。

- 方式：

客户端分区：客户端直接在正确的节点读取或写入key（大部分客户端已实现）；
代理分区：客户端向代理端请求数据，由代理端访问正确的节点；
查询路由：客户端向一个随机实例请求查询，该实例会将请求转移到正确节点上。    

### 二、Redis客户端

  现在用得比较多的我比较关注的Redis Java客户端是Jedis和Lettuce，
  而在Spring Data框架以及Spring Boot中也是使用这两种客户端。
  因此，我将关注与Spring Data是如何整合Redis（实现Redis的操作和功能）
  以及Spring Boot中是如何实现自动配置的（包括提供的配置项）。
  在Jedis和Lettuce中，我对Lettuce更感兴趣（可能由于我比较喜欢吃生菜吧），
  
  所以，接下来的内容，将围绕者Lettuce客户端进行分析，以及之后的框架整合主要也是以lettuce为主。

见 [lettuce客户端的使用](./lettuce客户端的使用.md)

### 三、Spring Data Redis操作

- 3.1  简介

- 3.2 配置Lettuce连接

- 3.3 主写从读模式配置

- 3.4 Redis哨兵模式（Sentinel）

- 3.5 发布订阅（Pub/Seb）

- 3.6  事务（Transaction）

- 3.7 流水线（Pipelining）

- 3.8 集群（Cluster）

- 3.9 序列化与反序列化

### 四、源码浅析

- 4.1 RedisTemplate

- 4.2 Operations类和Commands类

- 4.3 数据结构

- 4.4 SessionCallback接口

- 4.5 RedisCallbacke接口

- 4.6 总结

### 五、Spring Boot整合Redis

- 5.1 pom.xml文件

- 5.2 spring-boot-autoconfigure配置

- 5.3 RedisProperties类

- 5.4 LettuceConnectionConfiguration类

- 5.5 RedisAutoConfiguration类

### 六、总结

### 七、附录 相关网址