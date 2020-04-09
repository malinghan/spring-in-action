### 学习资源
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Data Redis](https://spring.io/projects/spring-data-redis)
- [redis:reactive](https://docs.spring.io/spring-data/redis/docs/2.2.6.RELEASE/reference/html/#redis:reactive)
- [spring-data-mongodb](https://docs.spring.io/spring-data/mongodb/docs/2.2.6.RELEASE/reference/html)
- [spring-data-r2dbc](https://spring.io/projects/spring-data-r2dbc)
- [学习mongodb的资源站点](https://docs.spring.io/spring-data/mongodb/docs/2.2.6.RELEASE/reference/html/#get-started:first-steps:nosql)


### 概览
- 基本概念
- reactive-mongodb
- reactive-redis
- reactive-r2dbms
### 基本概念

### reactive-mongodb
- [mongo.reactive.support](https://docs.spring.io/spring-data/mongodb/docs/2.2.6.RELEASE/reference/html/#mongo.reactive)
- [mongodb.reactive.repositories](https://docs.spring.io/spring-data/mongodb/docs/2.2.6.RELEASE/reference/html/#mongo.reactive.repositories)

```java
@Document
public class Person {

  private String id;
  private String name;
  private int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
  }
}
```
```java
public class ReactiveMongoApp {

  private static final Logger log = LoggerFactory.getLogger(ReactiveMongoApp.class);

  public static void main(String[] args) throws Exception {

    CountDownLatch latch = new CountDownLatch(1);

    ReactiveMongoTemplate mongoOps = new ReactiveMongoTemplate(MongoClients.create(), "database");

    mongoOps.insert(new Person("Joe", 34))
          .flatMap(p -> mongoOps.findOne(new Query(where("name").is("Joe")), Person.class))
          .doOnNext(person -> log.info(person.toString()))
          .flatMap(person -> mongoOps.dropCollection("person"))
          .doOnComplete(latch::countDown)
          .subscribe();

    latch.await();
  }
}
```

```
@Configuration
public class AppConfig {

  /*
   * Use the Reactive Streams Mongo Client API to create a com.mongodb.reactivestreams.client.MongoClient instance.
   */
   public @Bean MongoClient reactiveMongoClient()  {
       return MongoClients.create("mongodb://localhost");
   }
}
```
- ReactiveMongoClientFactoryBean
```
@Configuration
public class AppConfig {

    /*
     * Factory bean that creates the com.mongodb.reactivestreams.client.MongoClient instance
     */
     public @Bean ReactiveMongoClientFactoryBean mongoClient() {

          ReactiveMongoClientFactoryBean clientFactory = new ReactiveMongoClientFactoryBean();
          clientFactory.setHost("localhost");

          return clientFactory;
     }
}
```
### reactive-redis

- [redis:reactive](https://docs.spring.io/spring-data/redis/docs/2.2.6.RELEASE/reference/html/#redis:reactive)


```
@Bean
public ReactiveRedisConnectionFactory lettuceConnectionFactory() {

  LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
    .useSsl().and()
    .commandTimeout(Duration.ofSeconds(2))
    .shutdownTimeout(Duration.ZERO)
    .build();

  return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379), clientConfig);
}
```

```
@Configuration
class RedisConfiguration {

  @Bean
  ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
    return new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());
  }
}   
```

```java
public class Example {

  @Autowired
  private ReactiveRedisTemplate<String, String> template;

  public Mono<Long> addLink(String userId, URL url) {
    return template.opsForList().leftPush(userId, url.toExternalForm());
  }
}
```

```java
@Configuration
class RedisConfiguration {

  @Bean
  ReactiveStringRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
    return new ReactiveStringRedisTemplate<>(factory);
  }
}
```
### reactive-r2dbms   