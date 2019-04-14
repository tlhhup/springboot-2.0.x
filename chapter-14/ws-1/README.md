### Spring Web Service

Spring对webservice的支持，其开发流程类似Spring mvc的开发流程.

1. 添加依赖

		<dependencies>
	        <dependency>
	            <groupId>org.springframework.ws</groupId>
	            <artifactId>spring-ws-core</artifactId>
	            <version>3.0.0.RELEASE</version>
	        </dependency>
	        <dependency>
	            <groupId>org.jdom</groupId>
	            <artifactId>jdom2</artifactId>
	            <version>2.0.6</version>
	        </dependency>
	        <dependency>
	            <groupId>jaxen</groupId>
	            <artifactId>jaxen</artifactId>
	            <version>1.1</version>
	        </dependency>
	        <dependency>
	            <groupId>wsdl4j</groupId>
	            <artifactId>wsdl4j</artifactId>
	            <version>1.6.3</version>
	        </dependency>
	    </dependencies>

2. 配置web.xml文件，添加ws的处理器(入口)-->角色和Spring MVC的前端控制器一致

		<servlet>
	        <servlet-name>spring-ws</servlet-name>
	        <servlet-class>org.springframework.ws.transport.http.MessageDispatcherServlet</servlet-class>
	        <init-param>
	            <param-name>transformWsdlLocations</param-name>
	            <param-value>true</param-value>
	        </init-param>
	    </servlet>
	
	    <servlet-mapping>
	        <servlet-name>spring-ws</servlet-name>
	        <url-pattern>/*</url-pattern>
	    </servlet-mapping>
3. 编写ws的配置文件-->等价于Spring mvc的配置文件(其默认命名规则也和Spring mvc配置文件命名规则一致servletName-servlet.xml)

		<?xml version="1.0" encoding="UTF-8"?>
		<beans xmlns="http://www.springframework.org/schema/beans"
		       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		       xmlns:sws="http://www.springframework.org/schema/web-services"
		       xmlns:context="http://www.springframework.org/schema/context"
		       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		       http://www.springframework.org/schema/web-services http://www.springframework.org/schema/web-services/web-services-2.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
		
		    <context:component-scan base-package="org.tlh.ws"/>
		
			<!-- 配置处理器映射器 -->
		    <!-- 开启注解驱动，EndpointMapping 使用PayloadRootAnnotationMethodEndpointMapping来处理请求映射,表示使用@PayloadRoot来定义处理器 -->
		    <sws:annotation-driven/>
		
		    <!-- 自动生成wsdl文件 -->
		    <sws:dynamic-wsdl id="holiday"
		                      portTypeName="HumanResource"
		                      locationUri="/holidayService/"
		                      targetNamespace="http://mycompany.com/hr/definitions">
		        <sws:xsd location="/WEB-INF/hr.xsd"/>
		    </sws:dynamic-wsdl>
		
		
		</beans>
4. 编写Endpoint--->等价于Spring MVC的控制器

		@Endpoint
		public class HolidayEndpoint {
		
		    private static final String NAMESPACE_URI = "http://mycompany.com/hr/schemas";
		
		    private XPathExpression<Element> startDateExpression;
		
		    private XPathExpression<Element> endDateExpression;
		
		    private XPathExpression<Element> firstNameExpression;
		
		    private XPathExpression<Element> lastNameExpression;
		
		    private HumanResourceService humanResourceService;
		
		    @Autowired
		    public HolidayEndpoint(HumanResourceService humanResourceService) throws JDOMException {
		        this.humanResourceService = humanResourceService;
		
		        Namespace namespace = Namespace.getNamespace("hr", NAMESPACE_URI);
		        XPathFactory xPathFactory = XPathFactory.instance();
		        startDateExpression = xPathFactory.compile("//hr:StartDate", Filters.element(), null, namespace);
		        endDateExpression = xPathFactory.compile("//hr:EndDate", Filters.element(), null, namespace);
		        firstNameExpression = xPathFactory.compile("//hr:FirstName", Filters.element(), null, namespace);
		        lastNameExpression = xPathFactory.compile("//hr:LastName", Filters.element(), null, namespace);
		    }
		
		    //通过指定命名空间和part来限定需要处理的请求
		    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HolidayRequest")
		    public void handleHolidayRequest(@RequestPayload Element holidayRequest) throws Exception {
		        Date startDate = parseDate(startDateExpression, holidayRequest);
		        Date endDate = parseDate(endDateExpression, holidayRequest);
		        String name = firstNameExpression.evaluateFirst(holidayRequest).getText() + " " + lastNameExpression.evaluateFirst(holidayRequest).getText();
		
		        humanResourceService.bookHoliday(startDate, endDate, name);
		    }
		
		    private Date parseDate(XPathExpression<Element> expression, Element element) throws ParseException {
		        Element result = expression.evaluateFirst(element);
		        if (result != null) {
		            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		            return dateFormat.parse(result.getText());
		        } else {
		            throw new IllegalArgumentException("Could not evaluate [" + expression + "] on [" + element + "]");
		        }
		    }
		
		}	
5. 集成tomcat插件，启动项目

		<build>
	        <finalName>ws-1</finalName>
	        <plugins>
	            <plugin>
	                <artifactId>maven-compiler-plugin</artifactId>
	                <configuration>
	                    <source>1.8</source>
	                    <target>1.8</target>
	                </configuration>
	            </plugin>
	            <plugin>
	                <groupId>org.codehaus.mojo</groupId>
	                <artifactId>tomcat-maven-plugin</artifactId>
	                <version>1.1</version>
	            </plugin>
	        </plugins>
    	</build>	
    	
6. 借助spring-ws-archetype来创建项目，可以自动配置ws的基本组件	