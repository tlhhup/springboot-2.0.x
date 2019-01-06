### 基于oauth2的认证服搭建
1. 对于oauth2及其认证的流程可以查看以下资源
	1. [理解OAuth 2.0](http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html) 
	2. [认证流程](https://my.oschina.net/liuyuantao/blog/804880)
2. 基于内存的认证中心搭建(token采用jwt进行处理)
	1. 添加依赖 	

			<dependencies>
		        <!-- 注意是starter,自动配置 -->
		        <dependency>
		            <groupId>org.springframework.boot</groupId>
		            <artifactId>spring-boot-starter-web</artifactId>
		        </dependency>
		        <dependency>
		            <groupId>org.springframework.boot</groupId>
		            <artifactId>spring-boot-starter-security</artifactId>
		        </dependency>
		        <!-- 不是starter,手动配置 -->
		        <dependency>
		            <groupId>org.springframework.security.oauth</groupId>
		            <artifactId>spring-security-oauth2</artifactId>
		            <version>2.3.2.RELEASE</version>
		        </dependency>
		        <dependency>
		            <groupId>org.springframework.security</groupId>
		            <artifactId>spring-security-jwt</artifactId>
		            <version>1.0.9.RELEASE</version>
		        </dependency>
		    </dependencies>
	2. 添加认证管理器，继承AuthorizationServerConfigurerAdapter配置client
的信息和token处理方式

			@Configuration
			@EnableAuthorizationServer
			public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
			
			    private static final String ORDER = "order";
			    private static final String PRODUCT = "product";
			
			    @Autowired
			    AuthenticationManager authenticationManager;
			
			    @Autowired
			    private ClientDetailsService clientDetailsService;
			
			    @Override
			    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			        //允许表单认证
			        security.allowFormAuthenticationForClients();
			        //RemoteTokenServices 暴露/oauth/check_token端点
			        security.tokenKeyAccess("permitAll()")
			                .checkTokenAccess("permitAll()");
			    }
			
			    @Override
			    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
			        //配置两个客户端,一个用于password认证一个用于client认证
			        //一个客户端只有order的资源，另一个客户端拥有order和product
			        clients.inMemory().withClient("client_1")
			                .resourceIds(ORDER)//
			                .authorizedGrantTypes("client_credentials", "refresh_token")
			                .scopes("select")
			                .authorities("oauth2")
			                .secret(finalSecret)
			                .and().withClient("client_2")
			                .resourceIds(ORDER,PRODUCT)
			                .authorizedGrantTypes("password", "refresh_token")
			                .scopes("select")
			                .authorities("oauth2")
			                .secret(finalSecret).and().withClient("client")//该用户用于服务进行token的校验
			                .secret(finalSecret);
			    }
			
			    @Override
			    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			        endpoints
			                .tokenStore(tokenStore())
			                .tokenServices(tokenServices())
			                .accessTokenConverter(jwtAccessTokenConverter())
			                .authenticationManager(authenticationManager)
			                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
			    }
			
			    @Bean
			    public TokenStore tokenStore() {
			        return new JwtTokenStore(jwtAccessTokenConverter());
			    }
			
			    @Bean
			    public JwtAccessTokenConverter jwtAccessTokenConverter() {
			        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
			        jwtAccessTokenConverter.setSigningKey("cola-cloud");
			        return jwtAccessTokenConverter;
			    }
			
			    @Bean
			    @Primary
			    public DefaultTokenServices tokenServices() {
			        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			        defaultTokenServices.setTokenStore(tokenStore());
			        defaultTokenServices.setClientDetailsService(clientDetailsService);
			        defaultTokenServices.setSupportRefreshToken(true);
			        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
			        return defaultTokenServices;
			    }
	
	
			}
	3. 添加spring security的配置

			@Configuration
			@EnableWebSecurity
			public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
			
			    @Bean
			    @Override
			    protected UserDetailsService userDetailsService() {
			        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			        String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("123456");
			        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			        manager.createUser(User.withUsername("user_1").password(finalPassword).authorities("USER").build());
			        manager.createUser(User.withUsername("user_2").password(finalPassword).authorities("USER").build());
			        return manager;
			    }
			
			    @Bean
			    PasswordEncoder passwordEncoder() {
			        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
			    }
			
			    @Bean
			    @Override
			    public AuthenticationManager authenticationManagerBean() throws Exception {
			        AuthenticationManager manager = super.authenticationManagerBean();
			        return manager;
			    }
			
			    @Override
			    protected void configure(HttpSecurity http) throws Exception {
			        http
			                .requestMatchers().anyRequest()
			                .and()
			                .authorizeRequests()
			                //去掉oauth的端点
			                .antMatchers("/oauth/**").permitAll();
			    }
			}
3. 添加两个资源服务器
	1. 添加依赖
	
			<dependency>
	            <groupId>org.springframework.boot</groupId>
	            <artifactId>spring-boot-starter-web</artifactId>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework.security.oauth</groupId>
	            <artifactId>spring-security-oauth2</artifactId>
	            <version>2.3.2.RELEASE</version>
	        </dependency>
	        <dependency>
	            <groupId>org.springframework.security</groupId>
	            <artifactId>spring-security-jwt</artifactId>
	            <version>1.0.9.RELEASE</version>
	        </dependency>
	
	2. 配置资源服务器

			@Configuration
			@EnableResourceServer
			public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
			
			    public static final String CHECK_TOKEN_ENDPOINT_URL = "http://localhost:8080/oauth/check_token";
			
			    @Override
			    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			        //设置资源ID
			        resources.resourceId("order").stateless(true);
			    }
			
			    @Override
			    public void configure(HttpSecurity http) throws Exception {
			        http.authorizeRequests().anyRequest().authenticated();
			    }
			
			    @Bean
			    public ResourceServerTokenServices tokenService(){
			    	//配置tokenservice，用于向授权服务器进行token的校验
			        RemoteTokenServices tokenServices=new RemoteTokenServices();
			        tokenServices.setCheckTokenEndpointUrl(CHECK_TOKEN_ENDPOINT_URL);
			        tokenServices.setAccessTokenConverter(jwtAccessTokenConverter());
			        // 设置客户端信息,该配置需要在授权服务器进行配置
			        tokenServices.setClientId("client");
			        tokenServices.setClientSecret("123456");
			        return tokenServices;
			    }
			
			    @Bean
			    public JwtAccessTokenConverter jwtAccessTokenConverter() {
			        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
			        jwtAccessTokenConverter.setSigningKey("cola-cloud");
			        return jwtAccessTokenConverter;
			    }
	
			}
	3. 添加受保护资源 		

			@RestController
			public class OrderController {
			
			    @GetMapping("/order/{id}")
			    public String getOrder(@PathVariable String id) {
			        //for debug
			        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			        return "order id : " + id;
			    }
			
			}
4. 访问：
	1.	获取token
		1. password模式：http://localhost:8080/oauth/token?username=user_1&password=123456&grant_type=password&scope=select&client_id=client_2&client_secret=123456
		2. client模式：http://localhost:8080/oauth/token?grant_type=client_credentials&scope=select&client_id=client_1&client_secret=123456
	2. 访问受保护的资源
		1. password模式两个资源都可以正常访问
		2. client模式只能访问order资源：http://localhost:8082/order/1?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXIiXSwidXNlcl9uYW1lIjoidXNlcl8xIiwic2NvcGUiOlsic2VsZWN0Il0sImV4cCI6MTU0Njc0MDMzNiwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJqdGkiOiI1NDI0MTczNi1iMGVmLTQ2MmYtOTk1NC04NmViNjFhMTNmNGYiLCJjbGllbnRfaWQiOiJjbGllbnRfMiJ9.XvaLVdhcBliFAlo_UquWjWvWNqt5g8T_9KfeHrs3F9Q
	3. 在请求头添加token信息
		1. 设置请求头：Authorization  值为：bearer+token
5. 基于数据库的认证服务器搭建
	1. 初始化数据库表结构
	2. 添加依赖 
6. 源码分析
	1. 认证(token的创建)
	2. 授权   	
