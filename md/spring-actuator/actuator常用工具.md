### 查看监控情况
```
curl localhost:8080/actuator/health
```

### 查看Beans注入情况
```
curl localhost:8080/actuator/beans
```

### 前提条件
```
management.endpoints.web.exposure.include=*
```