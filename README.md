# Kafka topic流式连接

------
### 1 创建配置文件
```
 Properties props = new Properties();
```
### 2 添加配置文件参数
```
        props.put(StreamsConfig.APPLICATION_ID_CONFIG,"stream-pipe"); // which gives the unique identifier of your Streams application to distinguish itself with other applications talking to the same Kafka cluster
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); // which specifies a list of host/port pairs to use for establishing the initial connection to the Kafka cluster
```
### 3 创建数据流并绑定topic
```
    final StreamsBuilder builder = new StreamsBuilder();
    KStream<String,String> source = builder.stream("streams-plaintext-input");
```
### 4 输出流到指定topic
```
     source.to("streams-pipe-output");
```
### 5 构建拓扑结构
```
    final Topology topology = builder.build();
```
`这一步构建拓扑结构将两个topic和一个流封装成一个对象`
### 6 创建KafkaStream流对象并绑定拓扑结构和kafka服务
```
    final KafkaStreams streams = new KafkaStreams(topology,props);
```
### 7 利用CountDownLatch阻塞主线程等待source流全部导入到指定topic
```
    final CountDownLatch latch = new CountDownLatch(1);
```
#### [CountDownLatch详解](http://www.importnew.com/15731.html)
---- 
```
By calling its start() function we can trigger the execution of this client. The execution won't stop until close() is called on this client. We can, for example, add a shutdown hook with a countdown latch to capture a user interrupt and close the client upon terminating this program
```
## 常见问题
```
    Error: javac: 源版本　1.6 需要目标版本　1.6 
    javac: source release 1.6 requires target release 1.6 
```
`构建maven的时候指定的jdk版本错误才会产生报错`
### 解决办法：
```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <configuration>
            <source>1.6</source>
            <target>1.6</target>
        </configuration>
    </plugin>
```
------
参考资料：
[Maven生成可以直接运行的jar包的多种方式](http://blog.csdn.net/xiao__gui/article/details/47341385)
------
作者 [@jonyhy](https://weibo.com/u/5991880963)
