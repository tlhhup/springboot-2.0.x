1. spring boot属性的加载顺序为（从大到小）：
	1. Devtools的全局设置(~/.spring-boot-devtools.properties该文件中配置的内容)
	2. @TestPropertySource在测试中使用该注解
	3. @SpringBootTest#properties在测试中使用该注解，注意#为SpEL
	4. 命令行参数
	5. `SPRING_APPLICATION_JSON`json格式数据中获取的属性 
	6. ServletConfig初始化参数
	7. ServletContext初始化参数
	8. JNDI属性
	9. Java的系统属性(`System.getProperties()`)
	10. 操作系统环境变量
	11. 随机属性，通过random.*赋值
	12. jar外部的profile文件(application-{profile}.properties或者yaml文件)
	13. jar内部的profile文件(application-{profile}.properties或者yaml文件)
	14. 外部的属性文件(application.properties或者yaml文件)
	15. 内部的属性文件(application.properties或者yaml文件)
	16. 在配置类中属性@PropertySource注解指定的
	17. 通过`SpringApplication.setDefaultProperties`方法设置的默认属性
2. `SPRING_APPLICATION_JSON`：通过命令行指定外部的json数据
	1. `SPRING_APPLICATION_JSON='{"acme":{"name":"test"}}' java -jar myapp.jar` 
	2. `java -Dspring.application.json='{"name":"test"}' -jar myapp.jar`
	3. `java -jar myapp.jar --spring.application.json='{"name":"test"}`
3. 	命令行参数，通过在参数名前添加`--`设置命令行参数
	1. `java -jar demo.jar --server.port=9000`
	2. 禁用：
	
			SpringApplication.setAddCommandLineProperties(false).
4. Application property Files加载路径(优先级从低到高，及优先级越高越先读取，及3的属性会覆盖4中的属性)
	1. 当前路径下的/config文件夹
	2. 当前路径
	3. classpath下的/config包
	4. classpath根目录 
5. Profiles
		
		# 指定profile
		spring:
		  profiles:
		    active: development
		
		# 默认的profile配置，default
		person:
		  address: 192.168.1.100
		---
		spring:
		  profiles: development
		person:
		  address: 127.0.0.1
		---
		spring:
		  profiles: production
		person:
		  address: 192.168.1.120