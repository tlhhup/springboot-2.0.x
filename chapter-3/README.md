###日志配置
提供java util logging、log4j2、logback

1. 默认情况下使用starter来配置的日志使用的Logback,默认为打印在console中
2. 默认情况下在控制台打印的日志级别为`info`以上的，可以通过以下方式修改日志的级别

		java -jar demo.jar --debug 或者 application文件中设置debug=true
3. 日志级别：fatal、error、warn、info、debug、trace
4. 设置日志显示的颜色，可以在配置文件中添加如下属性，开起日志的颜色

		spring:
		  output:
		    ansi:
		      enabled: always
5. 将日志记录到文件中，可以通过配置logging.file或logging.path来指定日志保持的文件和路径,日志文件在到达默认10M之后会重新创建新文件用于记录新的日志

		logging.file=my.log #将日志记录到指定的文件中，可以为绝对路径的名称或相对于当前路径纪录
		logging.path=/var/log #将以spring.log为文件名记录在该路径中
		logging.file.max-size #设置日志文件大小
		logging.file.max-history #最大日志文件保存的个数
6. 定义日志级别

		logging.level.<logger-name>=<level>
7. 对于不同的日志框架，springboot提供不同的配置文件用于扩展不同的日志框架（建议通过带-spring的名称的扩展日志的配置）
	
		Logback：logback-spring.xml, logback-spring.groovy, logback.xml, or logback.groovy
		Log4j2：log4j2-spring.xml or log4j2.xml
		JDK (Java Util Logging)：logging.properties
8. 可以在日志配置文件中获取application文件中定义的变量

		#application文件
		fluentHost：myapp.fluentd.host
		＃日志配置文件
		<appender name="FLUENT" class="ch.qos.logback.more.appenders.DataFluentAppender">
			<remoteHost>${fluentHost}</remoteHost>
			...
		</appender>
9. 通过elk来记录日志：https://yq.aliyun.com/articles/316648