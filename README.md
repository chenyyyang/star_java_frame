# star_java_frame

## brief introduction
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
这里抽象的不够好，理论上@KafkaConsumerHandlerMethod和@OpenAPIMethod应该是一样的用法，都是把收到的CQE对象动态dispatch到Service处理。


## high light
- 减少代码量，更佳的实践DDD。一个包满足Java后端开发的方方面面
- interface层封装支持http/kafka（0.10.0），标准化请求参数和响应格式、统一异常处理，session上下文管理、分页参数管理
- 封装AsyncHttpClient
- 多数据源路由，实现读写分离
- 各种工具包，BeanUtils、DateTimeUtils、JsonUtils

## implementation

##### @KafkaConsumerHandlerMethod的实现
```
 kafka
│   │   │   ├── KafkaConsumerHandler.java
│   │   │   ├── KafkaConsumerHandlerMethod.java
│   │   │   ├── KafkaConsumerRunner.java
│   │   │   ├── KafkaConsumerStarter.java
│   │   │   └── KafkaProducerStarter.java
依赖
kafka-clients 0.10.0.0

消费者初始化：
    @PostConstruct 
    public void initConsumer()throws Exception {
        // 初始化消费者线程
        KafkaConsumerStarter.init(brokerAddress, consumerGroupName, 0,0,0);
    }
    
    @PreDestroy
    public void destroy(){
        // 销毁消费者线程
        KafkaConsumerStarter.destroy();
    }
消费者 绑定事件处理函数到某个topic:
  
    //消费逻辑无需try catch异常，框架代码会catch。如果有显式的异常，直接在方法名后面抛出
    @KafkaConsumerHandlerMethod(topic = "topicName") 
    public void topicName(ConsumerRecord<?, String> record) throws xxxException {
        //消费逻辑
     }
在@PostConstruct方法中调用KafkaConsumerStarter.init()方法，初始化consumer配置和consumer线程
init方法的主要参数：
brokerAddress                 broker地址,逗号分隔
consumerGroupName      consumerGroupName 
sessionTimeOutMs           session超时时间，默认30s,0.10版本的kafka心跳线程和poll线程在一起，session超时broker就收不到心跳
maxPollRecords               每次拉取消息条数，默认30条，请务必确保30秒内30条一定能消费完，否则会触发kafka broker rebalance，引发性能问题
consumerThreadNum      consumer实例个数，既consumer线程数，默认为6。一个consumer对于一个或多个分区
消费方法中加上@KafkaConsumerHandlerMethod(topic = "topicName")注解。消费方法无需捕获异常，框架层已经捕获并打印了异常，并且发送至cat。
注意消费方法一定要实现幂等，即同一条消息消费一次和消费多次的结果一致。目前consumer的实现策略只保证每条消息至少被消费一次，不保证exactly once。

生产者初始化：
KafkaProducerStarter.init(brokerAddress)

生产者发送消息到某个topic:
KafkaProducerStarter.send(topic, message)

3.发送顺序消息
KafkaProducerStarter.send(key, topic, message)
注: 相同key的消息会被发送到同一个分区，以保证相同业务上产生消息的顺序性。应该使用如：订单id等属性作为key，以此来保证同一个订单产生的消息之间的先后顺序，避免出现“先产生的消息后消费”这种问题。默认情况下，消息队列Kafka版为了提升可用性，并不保证单个分区内绝对有序，在升级或者宕机时，会发生少量消息乱序（某个分区挂掉后把消息Failover到其它分区）。

    
```
##### @OpenAPIMethod的实现
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
    │   │   └── ServletInfo.java

这里依赖原生servlet-api。
初始化openAPI的servlet和管理session以及分页参数的Filter
@Bean
public ServletRegistrationBean openAPIJsonServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new OpenAPIJsonServlet(), "/api/*");
        servletRegistrationBean.setName("xxx");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("rateLimit", "150");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }
@Bean
public FilterRegistrationBean sessionFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SessionHolderFilter());
        registrationBean.setUrlPatterns(Arrays.asList(new String[]{"*.do"}));
        registrationBean.setOrder(6);
        return registrationBean;
    }

然后在代码中即可即可使用，使用类似SpringMVC的 @RequestMapping。获取分页则使用
PageLimit pageLimit = PageLimitHolderFilter.getContext();
list = pageLimit.limitList(list);
非常方便。
实现方式可以看OpenAPIJsonServlet类的源代码，本质上还是一个普通servlet类，servlet.init时候把被@openAPI注解的bean加载到内存cache，拦截到请求后到开始经过一系列处理，在运行时动态dispatch到对应的bean以及方法，invoke方法。这里的难点是把通过request中的参数准确dispatch到对应的方法。invoke后接收返回值组装Response对象，序列化和设置响应code。如果捕获到异常，判断是逻辑处理异常还是未预料到的异常，分别进行处理。
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
对于@DynamicDataSource(key = "db1")这样的注解 。
首先是利用AOP代理被注解的方法，把key=db1放入DynamicDataSourceManager，这里面有个ThreadLocal存放这个key。

DynamicDataSourceManager继承自AbstractRoutingDataSource
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements InitializingBean {

AbstractRoutingDataSource是spring支持数据源切换的路由数据源抽象，而且继承自AbstractDataSource，实现InitializingBean是为了再bean加载完成时，根据key="db1"/“db2”  value=DruidDataSource的形式将多个数据源注入该bean作为属性。

在DAO决定使用哪个数据源的时候会调用determineCurrentLookupKey，而这个方法已经被DynamicDataSourceManager重写了，会从ThreadLocal种取出这个key对应的DataSource，如"db1"。
确定数据源后就可以determineTargetDataSource().getConnection()了。


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

