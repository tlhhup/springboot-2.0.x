1 .环境搭建
  
   - 继承spring boot的parent
  
        <parent>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>2.0.5.RELEASE</version>
        </parent>
  - 如果项目中还有其它的parent，则可以通过导入的方式引入springboot
  
        <dependencyManagement>
        		<dependencies>
        		<dependency>
        			<!-- Import dependency management from Spring Boot -->
        			<groupId>org.springframework.boot</groupId>
        			<artifactId>spring-boot-dependencies</artifactId>
        			<version>2.0.5.RELEASE</version>
        			<type>pom</type>
        			<scope>import</scope>
        		</dependency>
        	</dependencies>
        </dependencyManagement>
  
2 .devtools
    
  - 在classpath下又任何文件的修改都会自动触发应用的自动重启，在开发阶段建议添加该依赖，在生产环境下会自动忽略掉。
   
	       <dependencies>
	        <dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-devtools</artifactId>
	            <optional>true</optional>
	        </dependency>
	       </dependencies>
  - 可以通过在springboot的配置文件中添加
        
    	spring.devtools.restart.enabled＝flase
  - 其实现的原理依赖与两个类加载器BaseClassloader(加载不变的class)和RestartClassloader。可以在配置文件中添加相应的属性来声明那些需要重启加载的：
   
	    restart.exclude.companycommonlibs=/mycorp-common-[\\w-]+\.jar # 正则表达式
	    restart.include.projectcommon=/mycorp-myproj-[\\w-]+\.jar
  - 当资源文件(界面)devtools借助内嵌的LiveReload server可以让浏览器自动刷新，可以通过以下属性关闭：
   
    	spring.devtools.livereload.enabled

3 .自定义banner，可以在classpath下添加banner.txt文件用于定制自己的banner内容或者通过spring.banner.location属性指定文本文件路径;同时也可以添加banner.gif、banner.jpg、banner.png图片作为banner或者通过spring.banner.image.location指定图片地址。
 
 - 通过在配置文件的属性来设置banner的模式

	 	 spring:
		  main:
		    banner-mode: "console" # 控制台 
		    				"log"  # 日志
		    				"off"  ＃关闭

4 .监听器
  
  - 监听器的创建优先于ApplicationContext的创建，所以**不能**通过@Bean注解来创建，可以通过SpringApplication和SpringApplicationBuilder中的方法来**手动**添加监听器；同时可以在META-INF/spring.factories文件中通过key为org.springframework.context.ApplicationListener来配置**自动**创建的监听器 
  - 常用的监听器
  	- ApplicationStartingEvent：启动前调用
  	- ApplicationEnvironmentPreparedEvent：在context创建之前，enviroment创建之后
  	- ApplicationPreparedEvent：在所有的bean定义之后，开始refresh时
  	- ApplicationStartedEvent：初始化完成之后，application 和commandline runners调用之前
  	- ApplicationReadyEvent：application 和commandline runners调用之后，准备提供请求
  	- ApplicationFailedEvent：启动失败