### 地址
https://github.com/geektime-geekbang/geektime-spring-family

### 打包执行

```
打包 跳过测试
mvn clean package -Dmaven.test.skip
执行
java -jar helloworld-0.0.1-SNAPSHOT.jar
```

### Jar和war的区别

Spring Boot默认会打可执行Jar包，这个Jar的结构和普通的是不一样的，里面会包含了所依赖的各种Jar，同时也会有Web容器，因此你不再需要容器，直接就能运行。当然，你也可以选择打普通的War包，然后放在外置的容器里运行。在后续Spring Boot的章节中我们会去聊这个打包的话题，详细讲解可执行Jar包的。

### 在pom文件中如果不使用spring-boot自带的parent节点要怎么处理


1. 第一步: 在dependencyManagement中引入spring-boot-dependencies
```xml
<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-dependencies</artifactId>
				<version>2.1.1.RELEASE</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>
```
2. 第二步:在dependency中加入所需的spring-boot-starter
```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

```
3. 第三步:使用spring-boot-maven-plugin来repackage整个project
```xml
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<version>2.1.1.RELEASE</version>
				<executions>
					<execution>
						<goals>
							<goal>repackage</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```

4. spring-boot-actuator 健康检查命令

第一步: 引入spring-boot-starter-actuator
```xml
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
第二步: 启动spring-boot容器，执行curl
``` 
curl localhost:8080/actuator/health
```
返回 `{"status":"UP"}`


5. spring-boot-maven-plugin中的repackage的含义
 - repackage 用来告诉Maven什么时候执行我这个plugin的动作
 - 以SpringBoot的spring-boot-starter-parent作为POM的parent时，就可以不用自己配置repackage，如果没有用这个parent，就需要自己来配置了

