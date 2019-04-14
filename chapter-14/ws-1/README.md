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
		
			<!-- 配置基于注解处理器映射器和处理器适配器 -->
		    <!-- 开启注解驱动，
		    	EndpointMapping 使用PayloadRootAnnotationMethodEndpointMapping来处理请求映射,表示使用@PayloadRoot来定义处理器 
	    		EndpointAdapter 使用基于注解的处理器适配器，及添加了@Endpoint注解的表示为处理器
	    	-->
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
7. [借助trang可以将xml文件自动生成对应的xsd文件](http://www.displayobject.fr/2010/03/08/generate-xsd-from-xml-with-trang/)
	1. 准备xml文件

			<HolidayRequest xmlns="http://mycompany.com/hr/schemas">
			    <Holiday>
			        <StartDate>2006-07-03</StartDate>
			        <EndDate>2006-07-07</EndDate>
			    </Holiday>
			    <Employee>
			        <Number>42</Number>
			        <FirstName>Arjen</FirstName>
			        <LastName>Poutsma</LastName>
			    </Employee>
			</HolidayRequest> 	
	2. 使用tang生成xsd文件

			java -jar tang.jar hr.xml hr.xsd
	3. 修改生成的xsd文件(简化数据类型的定义)
		1. 生成的xsd文件

				<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
				        elementFormDefault="qualified"
				        targetNamespace="http://mycompany.com/hr/schemas"
				        xmlns:hr="http://mycompany.com/hr/schemas">
				    <xs:element name="HolidayRequest">
				        <xs:complexType>
				            <xs:sequence>
				                <xs:element ref="hr:Holiday"/>
				                <xs:element ref="hr:Employee"/>
				            </xs:sequence>
				        </xs:complexType>
				    </xs:element>
				    <xs:element name="Holiday">
				        <xs:complexType>
				            <xs:sequence>
				                <xs:element ref="hr:StartDate"/>
				                <xs:element ref="hr:EndDate"/>
				            </xs:sequence>
				        </xs:complexType>
				    </xs:element>
				    <xs:element name="StartDate" type="xs:NMTOKEN"/>
				    <xs:element name="EndDate" type="xs:NMTOKEN"/>
				    <xs:element name="Employee">
				        <xs:complexType>
				            <xs:sequence>
				                <xs:element ref="hr:Number"/>
				                <xs:element ref="hr:FirstName"/>
				                <xs:element ref="hr:LastName"/>
				            </xs:sequence>
				        </xs:complexType>
				    </xs:element>
				    <xs:element name="Number" type="xs:integer"/>
				    <xs:element name="FirstName" type="xs:NCName"/>
				    <xs:element name="LastName" type="xs:NCName"/>
				</xs:schema>
		2. 修改后的xsd文件 

				<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
				        xmlns:hr="http://mycompany.com/hr/schemas"
				        elementFormDefault="qualified"
				        targetNamespace="http://mycompany.com/hr/schemas">
				    <xs:element name="HolidayRequest">
				        <xs:complexType>
				            <xs:all>
				                <xs:element name="Holiday" type="hr:HolidayType"/> 
				                <xs:element name="Employee" type="hr:EmployeeType"/> 
				            </xs:all>
				        </xs:complexType>
				    </xs:element>
				    <xs:complexType name="HolidayType">
				        <xs:sequence>
				            <xs:element name="StartDate" type="xs:date"/> 
				            <xs:element name="EndDate" type="xs:date"/> 
				        </xs:sequence>
				    </xs:complexType>
				    <xs:complexType name="EmployeeType">
				        <xs:sequence>
				            <xs:element name="Number" type="xs:integer"/>
				            <xs:element name="FirstName" type="xs:string"/> 
				            <xs:element name="LastName" type="xs:string"/> 
				        </xs:sequence>
				    </xs:complexType>
				</xs:schema>

### 核心接口

1. 处理请求流程

![](https://docs.spring.io/spring-ws/docs/3.0.7.RELEASE/reference/images/sequence.png)

2. 核心接口
	1.	WebServiceMessage
	2. WebServiceMessageFactory
	3. MessageContext
	4. TransportContext
	5. MessageDispatcher：入口
	6. EndpointInterceptor：拦截器
	7. EndpointAdapter：处理器适配器
	8. EndpointMapping：处理器映射器
	9. EndpointExceptionResolver：异常处理器
	10. WsConfigurerAdapter：基于Java config来定制ws的行为
	11. @EnableWs：基于Java config方式来配置ws
	12. **流程**：
		1. client端通过将消息发送到server的服务器(**发送的地址为MessageDispatcher对外暴露的地址**)
		2. server接受到请求后，根据请求中的命名空间和请求调用的方法找到对应的endpoint
		3. 由adapter负责endpoint的执行
3. 处理器支持的接受的数据类型
	1. RequestPayload：将request payload绑定到方法参数中

		<table class="tableblock frame-all grid-all spread">
		<colgroup>
		<col style="width: 25%;">
		<col style="width: 25%;">
		<col style="width: 25%;">
		<col style="width: 25%;">
		</colgroup>
		<thead>
		<tr>
		<th class="tableblock halign-left valign-top">Name</th>
		<th class="tableblock halign-left valign-top">Supported parameter types</th>
		<th class="tableblock halign-left valign-top"><code>@RequestPayload</code> required?</th>
		<th class="tableblock halign-left valign-top">Additional notes</th>
		</tr>
		</thead>
		<tbody>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">TrAX</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>javax.xml.transform.Source</code> and sub-interfaces (<code>DOMSource</code>, <code>SAXSource</code>, <code>StreamSource</code>, and <code>StAXSource</code>)</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">W3C DOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.w3c.dom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">dom4j</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.dom4j.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when dom4j is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">JDOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.jdom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when JDOM is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">XOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>nu.xom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when XOM is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">StAX</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>javax.xml.stream.XMLStreamReader</code> and <code>javax.xml.stream.XMLEventReader</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when StAX is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">XPath</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Any boolean, double, <code>String</code>, <code>org.w3c.Node</code>, <code>org.w3c.dom.NodeList</code>, or type that can be converted from a <code>String</code> by a Spring 3 <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/validation.html#core-convert">conversion service</a>, and that is annotated with <code>@XPathParam</code>.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">No</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default, see <a href="#server-xpath-param">the section called <code>XPathParam</code></a>.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Message context</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.springframework.ws.context.MessageContext</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">No</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">SOAP</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.springframework.ws.soap.SoapMessage</code>, <code>org.springframework.ws.soap.SoapBody</code>, <code>org.springframework.ws.soap.SoapEnvelope</code>, <code>org.springframework.ws.soap.SoapHeader</code>, and <code>org.springframework.ws.soap.SoapHeaderElement`s when used in combination with the `@SoapHeader</code> annotation.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">No</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">JAXB2</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Any type that is annotated with <code>javax.xml.bind.annotation.XmlRootElement</code>, and <code>javax.xml.bind.JAXBElement</code>.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when JAXB2 is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">OXM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Any type supported by a Spring OXM <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/oxm.html#d0e26164"><code>Unmarshaller</code></a>.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when the <code>unmarshaller</code> attribute of <code>&lt;sws:annotation-driven/&gt;</code> is specified.</p></td>
		</tr>
		</tbody>
		</table>
	
	2. XPathParam：将满足该xpath数据绑定到方法参数中,其支持绑定的参数有
		1. boolean or Boolean
		2. double or Double
		3. String
		4. Node
		5. NodeList 	
3. 处理器支持的返回数据类型
	1. ResponsePayload：将响应放到responsebody中

		<table class="tableblock frame-all grid-all spread">
		<colgroup>
		<col style="width: 25%;">
		<col style="width: 25%;">
		<col style="width: 25%;">
		<col style="width: 25%;">
		</colgroup>
		<thead>
		<tr>
		<th class="tableblock halign-left valign-top">Name</th>
		<th class="tableblock halign-left valign-top">Supported return types</th>
		<th class="tableblock halign-left valign-top"><code>@ResponsePayload</code> required?</th>
		<th class="tableblock halign-left valign-top">Additional notes</th>
		</tr>
		</thead>
		<tbody>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">No response</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>void</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">No</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">TrAX</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>javax.xml.transform.Source</code> and sub-interfaces (<code>DOMSource</code>, <code>SAXSource</code>, <code>StreamSource</code>, and <code>StAXSource</code>)</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">W3C DOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.w3c.dom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled by default</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">dom4j</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.dom4j.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when dom4j is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">JDOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>org.jdom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when JDOM is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">XOM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock"><code>nu.xom.Element</code></p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when XOM is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">JAXB2</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Any type that is annotated with <code>javax.xml.bind.annotation.XmlRootElement</code>, and <code>javax.xml.bind.JAXBElement</code>.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when JAXB2 is on the classpath.</p></td>
		</tr>
		<tr>
		<td class="tableblock halign-left valign-top"><p class="tableblock">OXM</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Any type supported by a Spring OXM <a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/oxm.html#d0e26096"><code>Marshaller</code></a>.</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Yes</p></td>
		<td class="tableblock halign-left valign-top"><p class="tableblock">Enabled when the <code>marshaller</code> attribute of <code>&lt;sws:annotation-driven/&gt;</code> is specified.</p></td>
		</tr>
		</tbody>
		</table>
	2. d


### 日志处理
1. org.springframework.ws.server.MessageTracing 日志级别设置为debug和tracing即可
2. org.springframework.ws.server.MessageTracing.sen 记录发送的数据日志
3. org.springframework.ws.server.MessageTracing.received 记录接受的数据日志