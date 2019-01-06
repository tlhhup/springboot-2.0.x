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

			-- 创建用户表
			drop table if exists users;
			create table users(
			  id int primary key auto_increment,
			  username varchar(100),
			  password varchar(200),
			  realname varchar(20),
			  usertype int,
			  enabled boolean
			);
			-- 创建oauth_client_details
			drop table if exists oauth_client_details;
			create table oauth_client_details(
				client_id varchar(100) primary key comment '客户端ID',
			    client_secret varchar(200) comment '密钥',
			    resource_ids varchar(5000) comment '资源ID，采用，隔开',
			    scope varchar(200) comment '角色，采用，隔开',
			    authorized_grant_types varchar(150) comment '授权类型，采用，隔开',
			    web_server_redirect_uri varchar(200) comment '重定向URL地址',
			    authorities varchar(500) comment '权限，采用,隔开',
			    access_token_validity int comment 'access token 有效时间',
			    refresh_token_validity int comment 'refresh token 有效时间',
			    autoapprove boolean comment '是否需要授权',
			    additional_information varchar(500) comment '自定义数据，json格式'
			);
	2. 添加依赖 

			<dependencies>
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
		        <dependency>
		            <groupId>mysql</groupId>
		            <artifactId>mysql-connector-java</artifactId>
		        </dependency>
		        <dependency>
		            <groupId>org.springframework.boot</groupId>
		            <artifactId>spring-boot-starter-jdbc</artifactId>
		        </dependency>
		        <dependency>
		            <groupId>org.projectlombok</groupId>
		            <artifactId>lombok</artifactId>
		        </dependency>
		        <dependency>
		            <groupId>org.springframework.boot</groupId>
		            <artifactId>spring-boot-starter-test</artifactId>
		            <scope>test</scope>
		        </dependency>
		    </dependencies>
    3. 定义UserDetailService加载用户信息

    		@Slf4j
			@Service
			public class AuthUserDetailService implements UserDetailsService {
			
			    private static final String DEF_USER_EXISTS_SQL = "select username,password,enabled from users where username = ?";
			
			    @Autowired
			    private JdbcTemplate jdbcTemplate;
			
			    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
			
			    @Override
			    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			        List<UserDetails> users = this.jdbcTemplate.query(this.DEF_USER_EXISTS_SQL,
			                new String[]{username}, new RowMapper<UserDetails>() {
			                    @Override
			                    public UserDetails mapRow(ResultSet rs, int rowNum)
			                            throws SQLException {
			                        String username = rs.getString(1);
			                        String password = rs.getString(2);
			                        boolean enabled = rs.getBoolean(3);
			                        return new User(username, password, enabled, true, true, true,
			                                AuthorityUtils.NO_AUTHORITIES);
			                    }
			
			                });
			        if (users.size() == 0) {
			            log.debug("Query returned no results for user '" + username + "'");
			
			            throw new UsernameNotFoundException(
			                    this.messages.getMessage("JdbcDaoImpl.notFound",
			                            new Object[] { username }, "Username {0} not found"));
			        }
			        UserDetails user = users.get(0);
			        return user;
			    }
			}
	4. 认证服务器配置，和之前的配置一致，只是client的信息通过数据库存储

			@Configuration
			@EnableAuthorizationServer
			public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
				....
			
			    @Autowired
			    private PasswordEncoder passwordEncoder;
			
			    @Autowired
			    private DataSource dataSource;
				....
			
			    @Override
			    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
			    }
			
			    ......
	
			}
6. 源码分析
	1. 认证(token的创建)
		1. [获取token](http://blog.didispace.com/spring-security-oauth2-xjf-2/) 
	2. 授权   	
		1. 资源服务器通过remoteTokenService调用认证服务器的check_token端点，进行token的校验，并返回该client所有的资源ID
		2. OAuth2AuthenticationManager#authenticate该方法中

				public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			
					// 1.通过tokenservice校验token(这个使用RemoteTokenService)
					String token = (String) authentication.getPrincipal();
					OAuth2Authentication auth = tokenServices.loadAuthentication(token);
					if (auth == null) {
						throw new InvalidTokenException("Invalid token: " + token);
					}
					//2. 获取该client拥有的所有资源ID
					Collection<String> resourceIds = auth.getOAuth2Request().getResourceIds();
					if (resourceId != null && resourceIds != null && !resourceIds.isEmpty() && !resourceIds.contains(resourceId)) {
						throw new OAuth2AccessDeniedException("Invalid token does not contain resource id (" + resourceId + ")");
					}
					//3. 补救，对scope进行校验，如果该位置成功则说明该资源该client可以访问了
					checkClientDetails(auth);
			
					if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
						OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
						// Guard against a cached copy of the same details
						if (!details.equals(auth.getDetails())) {
							// Preserve the authentication details from the one loaded by token services
							details.setDecodedDetails(auth.getDetails());
						}
					}
					auth.setDetails(authentication.getDetails());
					auth.setAuthenticated(true);
					return auth;
	
			}	
	3. 说明
		1. **资源服务器的resourceID**，对于认证服务器和资源服务器分离的，资源服务器对器受保护的资源必须添加resourceID,并且一个资源服务器之后有一个(设置多个，后面的有效)

				private void checkClientDetails(OAuth2Authentication auth) {
					// 此时该对象为空，及出现受保护的资源不再受保护(可以设置，或扩展clientDetailsService接口)
					if (clientDetailsService != null) {
						ClientDetails client;
						try {
							client = clientDetailsService.loadClientByClientId(auth.getOAuth2Request().getClientId());
						}
						catch (ClientRegistrationException e) {
							throw new OAuth2AccessDeniedException("Invalid token contains invalid client id");
						}
						// 此处为补救措施
						Set<String> allowed = client.getScope();
						for (String scope : auth.getOAuth2Request().getScope()) {
							if (!allowed.contains(scope)) {
								throw new OAuth2AccessDeniedException(
										"Invalid token contains disallowed scope (" + scope + ") for this client");
							}
						}
					}
				}