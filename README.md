# star_java_frame

### brief introduction
以前，假如我们要实现user表的增删改查功能，我需要写UserController,UserService,UserDao,UserMapper...   
然后写一个insertUser接口,需要在Controller校验参数，取出session，处理service给出的是Result是success还是fail，处理service的异常避免外泄给前端。  
在UserService组装UserDO，加入session中的用户名作为createUser，再交给UserDao写入DB，然后删除缓存等  
这样流水账式的代码在逻辑简单的时候好像没啥问题，逻辑清晰，但是随着业务的变化，当后人不断地在上面叠加新的逻辑时，会使代码复杂度增加、逻辑分支越来越多，最终造成bug或者没人敢重构的历史包袱。...  

star_java_frame 框架就是按照DDD的分层思想，分离出独立的Interface接口层，负责处理网络协议相关的逻辑，拆除后可以统一的处理Json数据响应或者Exception。  
Interface接口层的最大职责就是网络协议转化，这里的协议泛指http.dubbo.kafka...或者其他中间件的数据传输协议。看个例子把

```
@Component
@OpenAPI
public class UserService {
    
    @Autowired
    private UserDao userDao;
  
    @OpenAPIMethod(methodName = "author/createUser")
    public User createUser(@Validate User user){
        UserSession session = TokenService.getSession();
        user.setCreateUser(session.getName());
        retrun userDao.insert(user);
    }

}
```
很明显，框架把之前controller干的事做了，UserService不用关心调用自己的是http请求或者Kafka的消息，不用处理响应，也不用处理异常，框架会统一处理、记录log上传监控平台等，框架收到异常会返回400，否则都是200成功。下面换个协议，以kafka来举例
```
    @KafkaConsumerHandlerMethod(topic = "data_user")
	public void dataUser(ConsumerRecord<?, String> record) throws Exception {
		if (record != null && record.value() != null) {
			
			UserWrapper user = JSONUtilsEx.deserialize(record.value(), UserWrapper.class);
			
			userService.createUser(user);
		} 
	}
```
这里还抽象不够好，理论上@KafkaConsumerHandlerMethod和@OpenAPIMethod应该是一样的用法，都是把不同的协议中的数据转成service层需要的对象。


### high light
- 减少代码量，更佳的实践DDD。一个包满足Java后端开发的方方面面
- interface层封装支持http/kafka（0.10.0），标准化输入输出和异常处理、统一异常处理，traceId追踪、session上下文管理、分页参数管理
- 封装AsyncHttpClient
- 多数据源路由，实践读写分离
- 各种工具包，BeanUtils、DateTimeUtils、JsonUtils

### implementation

##### @KafkaConsumerHandlerMethod的实现
```
 kafka
│   │   │   ├── KafkaConsumerHandler.java
│   │   │   ├── KafkaConsumerHandlerMethod.java
│   │   │   ├── KafkaConsumerRunner.java
│   │   │   ├── KafkaConsumerStarter.java
│   │   │   └── KafkaProducerStarter.java


```
##### @OpenAPI的实现
```
servlet
│   │   │   ├── OpenAPI.java
│   │   │   ├── OpenAPIMethod.java
│   │   │   └── OpenAPIMethodProto.java
│   │   └── json
│   │       ├── MJSONResultEntity.java
│   │       └── OpenAPIJsonServlet.java

	    exception
    │   │   ├── ServiceException.java
    │   │   └── ServiceWarn.java
    │   ├── local
    │   │   └── LocalAttributeHolder.java
    │   ├── pageLimit
    │   │   ├── PageLimit.java
    │   │   └── PageLimitHolderFilter.java
    │   ├── servlet
    │   │   ├── ServletHolderFilter.java
    │   │   └── ServletInfo.java



```

##### @DynamicDataSource(key = "")动态路由切换的实现
```
dataSource
    │   │   ├── DynamicDataSource.java
    │   │   ├── DynamicDataSourceEnum.java
    │   │   ├── DynamicDataSourceInterceptor.java
    │   │   ├── DynamicDataSourceManager.java
    │   │   └── DynamicDataSourceTransactionManager.java
    
```

### performance
```
TODO 目前已经在生产环境大量使用，可以在CAT监控看
```

### deployment
```
git clone https://github.com/chenyyyang/star_java_frame
mvn install 
打成jar后，deploy 到私服或者 <system>标签引入依赖

```


### Q&A

