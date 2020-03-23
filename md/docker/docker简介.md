### 概览
- docker的官网介绍、安装、加速镜像
- 阿里云对docker的支持
- docker的菜鸟教程
- docker中使用mongodb镜像
- docker中使用redis镜像


### 镜像

- 官网 https://www.docker.com
- 国内加速镜像 https://www.docker-cn.com

### 菜鸟教程
https://www.runoob.com/docker/docker-tutorial.html  


### 使用阿里云的镜像
https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors

### mongodb
```
docker run -itd --name mongo -p 27017:27017 mongo --auth
```

```
$ docker exec -it mongo mongo admin
# 创建一个名为 admin，密码为 123456 的用户。
>  db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'}]});
# 尝试使用上面创建的用户信息进行连接。
> db.auth('admin', '123456')
```

```
mongo -u  admin -p 123456 
```

### redis
-  https://docker.com/_/redis
```
docker pull redis
docker run  --name redis -d -p 6379:6379 redis
```