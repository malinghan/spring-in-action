### 学习资源
  
- [IBM DOC:使用 Reactor 进行反应式编程](https://www.ibm.com/developerworks/cn/java/j-cn-with-reactor-response-encode/index.html)
- [projectreactor 官方文档](https://projectreactor.io/docs/core/release/reference/#about-doc)
- [响应式Spring的道法术器（Spring WebFlux 快速上手 + 全面介绍](https://blog.51cto.com/liukang/2090163)
### 基本概念
- 迭代器（Iterator）
- 发布者-订阅者模式
- 负压（backpressure）

### Reactor
- Reactor 则是完全基于反应式流规范设计和实现的库,没有 RxJava 那样的历史包袱，在使用上更加的直观易懂
- Reactor 也是 Spring 5 中反应式编程的基础

```xml
 <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
 </dependency>
```

### Flux 和 Mono
Flux 和 Mono 是 Reactor 中的两个基本概念。

Flux 表示的是包含 0 到 N 个元素的异步序列。
在该序列中可以包含三种不同类型的消息通知：
正常的包含元素的消息、
序列结束的消息和序列出错的消息。
当消息通知产生时，订阅者中对应的方法 onNext(), onComplete()和 onError()会被调用。

Mono 表示的是包含 0 或者 1 个元素的异步序列。
该序列中同样可以包含与 Flux 相同的三种类型的消息通知。
Flux 和 Mono 之间可以进行转换。
对一个 Flux 序列进行计数操作，得到的结果是一个 Mono<Long>对象。
把两个 Mono 序列合并在一起，得到的是一个 Flux 对象。

- 创建 Flux
- 创建 Mono


- just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
- fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
- empty()：创建一个不包含任何元素，只发布结束消息的序列。
- error(Throwable error)：创建一个只包含错误消息的序列。
- never()：创建一个不包含任何消息通知的序列。
- range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
- interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
- intervalMillis(long period)和 intervalMillis(long delay, long period)：与 interval()方法的作用相同，只不过该方法通过毫秒数来指定时间间隔和延迟时间。

### 操作符
  - buffer 和 bufferTimeout
    - 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素
  - filter
  - window
    - window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>
  - zipWith
    - zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并
  - take
    - take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种。
  - reduce 和 reduceWith
    - reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
  - merge 和 mergeSequential
    - merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。   
  - flatMap 和 flatMapSequential
    - flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。  
  - concatMap
    - concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
  - combineLatest
    - combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
          
### 消息处理
- subscribe
- switchOnError
- onErrorResumeWith
- retry 

### 调度器
- 当前线程，通过 Schedulers.immediate()方法来创建
- 单一的可复用的线程，通过 Schedulers.single()方法来创建
- 使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理
- 使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
- 使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建
- 从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。

### 测试
在对使用 Reactor 的代码进行测试时，需要用到 io.projectreactor.addons:reactor-test 库。
- StepVerifier
- TestPublisher

### 调试
- 启用调试模式    Hooks.onOperator
- 使用检查点   .checkpoint("test")
- 日志记录

### “冷”与“热”序列
之前的代码清单中所创建的都是冷序列。冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。

通过 publish()方法把一个 Flux 对象转换成 ConnectableFlux 对象