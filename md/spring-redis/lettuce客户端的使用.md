### lettuce
  
#### 2.1 简介

Lettuce是可拓展的`线程安全`的Redis客户端，提供`同步`、`异步`和`响应式`APIs。

如果避免使用阻塞和事务性操作（例如，BLPOP和MULTI/EXEC）,多个线程可以共享一个连接。

Nttey的nio框架对多个连接提供了有效的管理。

支持Redis的高级特性，例如`哨兵（Sential）`、`集群（Cluster）`以及Redis数据结构。

#### 2.2 连接
Lettuce中RedisURI是创建连接的关键，那么，接下来，看看RedisURI是怎么创建的。

#####（1）构建方式：
- 使用uri： `RedisURI.create("redis://localhost/")`;
- 使用Builder：`RedisURI.Builder.redis("localhost",6379).auth("password").database(1).build()`;
- 直接使用构造方法：`new RedisURI("localhost", 6379, 60, TimeUnit.SECONDS)`;
#####（2）RedisURI句式：
 Redis standalone模式：
 ```
redis :// [: password@] host [: port] [/ database][? [timeout=timeout[d|h|m|s|ms|us|ns]] [&_database=database_]]`
```
Redis standalone模式 (SSL)：
```
redis:// [: password@] host [: port] [/ database][? [timeout=timeout[d|h|m|s|ms|us|ns]] [&_database=database_]]
```

Redis哨兵模式：
```
redis-sentinel :// [: password@] host1[: port1] [, host2[: port2]] [, hostN[: portN]] [/ database][?[timeout=timeout[d|h|m|s|ms|us|ns]] 
[&_sentinelMasterId=sentinelMasterId_] [&_database=database_]
```

#### 2.3 基本用法
```
RedisClient client = RedisClient.create(redisURI);          (1)
StatefulRedisConnection<String, String> connection = client.connect(); (2)
RedisCommands<String, String> commands = connection.sync();            (3)
String value = commands.get("key");                                    (4)
...
connection.close();                                                    (5)
client.shutdown();                                                     (6)
```

（1）根据给出的redisURI（创建方法在2.2节）创建Redis客户端（默认连接6379端口）；

（2）打开Redis standalone模式（模式由给出的RedisURI决定的，即redis、rediss和redis-sentinel的区别）连接。

（3）获得同步执行的命令API；

（4）发出一个GET命令获取“foo”对应的值；

（5）关闭连接；

（6）关掉客户端。

#### 2.4 同步与异步
#####（1）RedisFuture<T>和CompleteableFuture<T>简介

每一个异步的API命令的调用都会创建一个可以取消、等待和订阅的`RedisFuture<T>`。而一个`RedisFuture<T>`或`CompleteableFuture<T>`是一个指向值计算尚未完成的最初未知结果的指针。
一个`RedisFuture<T>`提供异步和链接的操作。

异步通过`RedisFuture<T>`进行操作，同步直接通过同步执行命令进行操作。

##### （2）创建RedisFuture<T>（Lettuce中）
- 获取同步执行命令API
```
//根据redisURI创建客户端
RedisClient client = RedisClient.create(redisURI); 
//创建连接        
StatefulRedisConnection<String, String> connection = client.connect(); 
//获取同步执行命令
RedisCommands<String, String> sync = connection.sync(); 
//发送get请求，获取值         
String value = sync.get("key");
... 
//关闭连接
connection.close();
//关掉客户端
client.shutdown();
```
- 获取异步执行命令API
``` 
RedisClient client = RedisClient.create(redisURI);
//创建连接
StatefulRedisConnection<String, String> connection = client.connect(); 
//获取异步执行命令api
RedisAsyCommands<String, String> commands = connection.async();  
//获取RedisFuture<T>
RedisFuture<String> future = commands.get("key");
```
#### 2.5 消费RedisFuture<T>
- 没有设置超时（拉模式）
```
RedisFuture<String> future = commands.get("key");
//使用拉模式调用get方法，阻塞调用线程，直到计算结果完成，
//最坏的情况是线程一直阻塞
String value = future.get();
System.out.println(value);
```

- 设置超时
```
//获取异步执行api
RedisAsyncCommands<String, String> async = client.connect().async();
//发送set请求
RedisFuture<String> set = async.set("key", "value");
//发送get请求
RedisFuture<String> get = async.get("key");
//设置set超时
set.await(1, SECONDS) == true
set.get() == "OK"
//设置get超时
get.get(1, TimeUnit.MINUTES) == "value"
```
        
#### 2.6 使用消费者监听器
- 非阻塞

```
//发送get请求
RedisFuture<String> future = commands.get("key");
//设置监听器
future.thenAccept(new Consumer<String>() {
//该方法将会在future.complete()方法执行后，自动执行
    @Override
    public void accept(String value) {
        ...
    }
});
```

- 阻塞
```
try {
    RedisFuture<String> future = commands.get("key");
    //设置超时
    String value = future.get(1, TimeUnit.MINUTES);
    System.out.println(value);
} catch (Exception e) {
    //超时后，将抛出TimeoutException
    e.printStackTrace();
}
```
- 推模式
```
RedisFuture<String> future = commands.get("key");
//在future完成执行（即future.complete()）时，将触发该方法
//这样的执行流程就是推模式
future.thenAccept(new Consumer<String>() {
    @Override
    public void accept(String value) {
        System.out.println(value);
    }
});
```
#### 2.7 发布订阅（Pub/Sub）
- 同步订阅
```
//创建发布订阅连接
StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub()
//添加监听器
connection.addListener(new RedisPubSubListener<String, String>() { ... })
//获取同步发布订阅执行命令API
RedisPubSubCommands<String, String> sync = connection.sync();
//订阅channel通道信息
sync.subscribe("channel");
//向channel通道发送message信息,
//暂时没找到该命令，等后期补充
...
//下面是自定义的业务代码
...
```
- 异步订阅
```
StatefulRedisPubSubConnection<String, String> connection = client.connectPubSub()
connection.addListener(new RedisPubSubListener<String, String>() { ... })
//获取异步发布订阅执行命令API
RedisPubSubAsyncCommands<String, String> async = connection.async();
//获取向通道channel订阅的future
RedisFuture<Void> futureSub = async.subscribe("channel");
//获取向通道channel发布message的future
RedisFuture<Void> futurePub = async.push("channel","message");
//自定义业务代码业务代码
```
- Redis Cluster发布订阅
```
//创建Redis Cluster发布订阅连接
StatefulRedisClusterPubSubConnection<String, String> connection = clusterClient.connectPubSub()
//向连接中添加监听器
connection.addListener(new RedisPubSubListener<String, String>() { ... })
//获取发布订阅同步执行代码
RedisPubSubCommands<String, String> sync = connection.sync();
//向连接中订阅channel通道信息
sync.subscribe("channel");
//自定义业务代码
...
```
#### 2.8 事务（Transaction）
  Lettuce通过WATCH, UWATCH,EXEC, MULTI 和DISCARD来控制事务（Transaction），
  同时允许同步、异步、响应式和集群使用事务。那么，下面将分析同步和异步事务。
  
- 同步使用事务
```
//第一段代码
//创建同步连接
RedisCommands<String, String> redis = client.connect().sync();
//开启事务
redis.multi() //成功，则返回值为"OK"
redis.set(key, value) //未执行，返回为null
redis.exec() //执行事务，返回list("OK")

//第二段代码
RedisCommands<String, String> redis = client.connect().sync();
//开启事务
redis.multi() //成功，返回"OK"
redis.set(key1, value) //未执行，返回null
redis.set(key2, value) //未执行， 返回null
redis.exec() //事务执行成功，返回list("OK", "OK")
```  

-   异步使用事务
```
//获取异步执行命令API
RedisAsyncCommands<String, String> async = client.connect().async();
//获取发送开启事务的future
RedisFuture<String> multi = async.multi();
//执行future的set命令设置值
RedisFuture<String> set = async.set("key", "value");
//获取提交执行事务的future
RedisFuture<List<Object>> exec = async.exec();
//获取执行事务的结果
List<Object> objects = exec.get();
//获取set的执行结果
String setResult = set.get();
//测试事务操作与set操作结果是否一致
//即事务操作是否成功 
objects.get(0) == setResult
```

- 响应式使用事务
```
//创建响应式连接
RedisReactiveCommands<String, String> reactive = client.connect().reactive();
//开启事务
reactive.multi().subscribe(multiResponse -> {
    //编写事务操作
    reactive.set("key", "1").subscribe();
    reactive.incr("key").subscribe();
    //提交执行事务
    reactive.exec().subscribe();
});
```
#### 2.9 主从复制（Master/Replica）
##### （1）Redis standalone模式
```
//创建客户端
RedisClient redisClient = RedisClient.create();
//创建主从连接
StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave.connect(redisClient, new Utf8StringCodec(),
        RedisURI.create("redis://localhost"));
//从ReadFrom.MASTER_PREFERRED中读取并复制，
//ReadFrom.MASTER_PREFERRED是一个ReadFromMasterPreferred类实例的引用
connection.setReadFrom(ReadFrom.MASTER_PREFERRED);
System.out.println("Connected to Redis");
//关闭连接
connection.close();
//关掉客户端
redisClient.shutdown();
```

##### （2）Redis哨兵模式

```
RedisClient redisClient = RedisClient.create();
//创建哨兵模式主从复制连接
StatefulRedisMasterSlaveConnection<String, String> connection = MasterSlave.connect(redisClient, new Utf8StringCodec(),
        RedisURI.create("redis-sentinel://localhost:26379,localhost:26380/0#mymaster"));
//从ReadFrom.MASTER_PREFERRED中读取并进行复制
connection.setReadFrom(ReadFrom.MASTER_PREFERRED);

System.out.println("Connected to Redis");

connection.close();
redisClient.shutdown();
```
####  2.10  集群
##### （1）lettuce对集群的支持

- 支持所有的CLUSTER命令；
- 基于命令见hash slot的命令路由；
- 所选集群命令的高级抽象；
- 多集群节点的命令操作；
- 通过slot和host/port获取集群节点的直接连接
- SSL和身份验证；
- 周期性灯芯集群拓扑图；
- 发布/订阅。

#####（2）使用NodeSelection API
```
//创建Redis集群的高级异步连接
RedisAdvancedClusterAsyncCommands<String, String> async = clusterClient.connect().async();
//使用NodeSelection API连接所有副本
AsyncNodeSelection<String, String> replicas = connection.slaves();
//从所有副本中获取所有的keys（密钥）
AsyncExecutions<List<String>> executions = replicas.commands().keys("*");
//遍历得到的keys
executions.forEach(result -> result.thenAccept(keys -> System.out.println(keys)));
```

##### (3) 连接到一个集群

```
RedisURI redisUri = RedisURI.Builder.redis("localhost").withPassword("authentication").build();
//创建集群客户端
RedisClusterClient clusterClient = RedisClusterClient.create(rediUri);
//创建连接
StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
//获取同步执行命令api
RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
...
connection.close();
clusterClient.shutdown();
```

##### （4）连接到多个子节点的Redis集群
```
RedisURI node1 = RedisURI.create("node1", 6379);
RedisURI node2 = RedisURI.create("node2", 6379);
//创建拥有多个子节点的集群客户端
RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2));
//创建连接
StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
//获取同步执行命令api
RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
...
connection.close();
clusterClient.shutdown();
```

##### (5) 开启周期性拓扑图更新

```
RedisClusterClient clusterClient = RedisClusterClient.create(RedisURI.create("localhost", 6379));
//创建周期性拓扑图更新操作的配置操作
ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                                .enablePeriodicRefresh(10, TimeUnit.MINUTES)
                                .build();
//向客户端设置刚才配置好的操作
clusterClient.setOptions(ClusterClientOptions.builder()
                                .topologyRefreshOptions(topologyRefreshOptions)
                                .build());
...

clusterClient.shutdown();
```

##### (6) 开启自适应拓扑图更新
```
RedisURI node1 = RedisURI.create("node1", 6379);
RedisURI node2 = RedisURI.create("node2", 6379);

RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2));
//配置自适应拓扑图更新操作
ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                                .enableAdaptiveRefreshTrigger(RefreshTrigger.MOVED_REDIRECT, RefreshTrigger.PERSISTENT_RECONNECTS)
                                .adaptiveRefreshTriggersTimeout(30, TimeUnit.SECONDS)
                                .build();
//向集群客户端中设置刚才配置好的操作
clusterClient.setOptions(ClusterClientOptions.builder()
                                .topologyRefreshOptions(topologyRefreshOptions)
                                .build());
...

clusterClient.shutdown();
```
##### （7）获取一个节点
```
RedisURI node1 = RedisURI.create("node1", 6379);
RedisURI node2 = RedisURI.create("node2", 6379);
//创建集群客户端
RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(node1, node2));
//创建连接
StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
//获取指定节点的同步执行命令api
RedisClusterCommands<String, String> node1 = connection.getConnection("host", 7379).sync();
...
//不需要关闭节点连接
connection.close();
clusterClient.shutdown();
```