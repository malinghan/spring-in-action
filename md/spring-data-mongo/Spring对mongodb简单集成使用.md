### mongodb 文档型数据库

www.mongodb.com

### spring对mongodb的支持

- Spring Data Mongodb

- MongoTemplate

- Repository


#### 注解

@Document

@Id

#### MongoTemplate

- save /remove

- Criteria / Query / Update


#### Repository
- 开启Repository支持 `@EnableMongoRepositories` 

- interface
```
-   MongoRepository<T,ID>
- PagingAndSortingRepository<T,ID>
- CrudRepository<T,ID>
```


### MongoAutoConfiguration
- MongoClient
- MongoProperties

