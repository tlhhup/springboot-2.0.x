###开发web应用
1. 可以通过引入以下的包来开发web应用
	1. spring-boot-starter-web:集成springmvc
	2. spring-boot-starter-webflux：开发响应式web应用 
2. springmvc在springboot中自动配置保护的内容
	1. ContentNegotiatingViewResolver和BeanNameViewResolver视图解析器
	2. 静态资源和WebJars（将web资源打包成jar文件）
	3. 转换器和各式话器
	4. HttpMessageConverters
	5. MessageCodesResolver
	6. index.html
	7. 图标
	8. ConfigurableWebBindingInitializer
3. 自定义json序列化和反序列化器，可以通过@JsonComponent注解来定义，将被自动注入到ApplicationContext中

		@JsonComponent
		public class Example {
		
			＃包含序列号和方序列化器，同时也可以分别定义
			public static class Serializer extends JsonSerializer<SomeObject> {
				// ...
			}
		
			public static class Deserializer extends JsonDeserializer<SomeObject> {
				// ...
			}
		
			} 
	同时在springboot中预制了两个抽象类JsonObjectSerializer和JsonObjectDeserializer作为基类 	
4. 静态资源，通过ResourceHttpRequestHandler进行处理，默认的静态资源的路径为：/static (or /public or /resources or /META-INF/resources)
	1. 同时可以通过webjar将静态资源打包成jar文件(https://www.webjars.org/)
5. 错误界面定义
	1. 根据错误码自定义错误界面

			src/
			 +- main/
			     +- java/
			     |   + <source code>
			     +- resources/
			         +- public/
			             +- error/
			             |   +- 404.html
			             +- <other public assets> 
6. 跨域处理(springmvc中4.2开始支持CORS：Cross-origin resource sharing)

		@Configuration
		public class MyConfiguration {
		
			@Bean
			public WebMvcConfigurer corsConfigurer() {
				return new WebMvcConfigurer() {
					@Override
					public void addCorsMappings(CorsRegistry registry) {
						registry.addMapping("/api/**");
					}
				};
			}
		}	
7. WebFlux:在spring5.0新增的不依赖于servlet API的异步非阻塞的响应式web框架
8. 定义web环境的servlet、filters、listeners
	1. 使用内嵌的servlet容器是添加了@WebServlet, @WebFilter, and @WebListener可以通过@ServletComponentScan来开启自动扫描
	2. 可以通过在spring容器中定义ServletRegistrationBean, FilterRegistrationBean, 和 ServletListenerRegistrationBean 类型的bean来完成对servlet、filter和listener的注册	