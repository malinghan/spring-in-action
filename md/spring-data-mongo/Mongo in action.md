
### 菜鸟教程
https://www.runoob.com/mongodb/mongodb-tutorial.html

### 简单命令
#### 创建数据库、切换数据库
use  springbucks
#### 创建用户和角色
```
    db.createUser{
       "user":"springbucks",
       "pwd":"springbucks",
       "roles":[
       {"role":"readWrite","db":"springbucks"}
       ]
    }
```

#### 显示数据库
```
show dbs
```
#### 显示用户
```
show  users
```

### 查询数据  
文档数据 -> 数据库表
#### 显示文档集合  
```
show collections
```
#### 查询文档数据
```
db.coffee.find();
```

#### 删除文档数据
```
db.coffee.remove({"name","aaa"})
```

#### 更新文档数据
```
db.coffee.update
```