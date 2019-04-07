#### Spring Security
1. 核型模块
	1. core:提供认证和访问控制等接口
	2. remoting:提供集成Spring Remoting
	3. web:提供对web的支持，提供内置的过滤器
	4. config:提供Spring xml命名空间和java config的配置
	5. ldap：提供对ldap的支持
	6. oauth2.0 core：对oauth2和 Openid的支持
	7. oauth2.0 client：对oauth2和 Openid的客户端支持
	8. oauth2.0 jose：对jose(Javascript object signing and encryption)的支持
		1. jwt: json web token
		2. jws: json web signing
		3. jwe：json web encryption
		4. jwk：json web key 	
	9. ACL：对对象的访问控制
	10. cas:提供对CAS单点登录的支持
	11. openid:提供对openid的支持
2. CSRF支持
	1. 如果使用thymeleaf作为模版引擎或者Spring mvc的标签，将在退出的时候自动添加该支持

			<!DOCTYPE html>
			<html lang="en" xmlns:th="http://www.thymeleaf.org">
			<head>
			    <meta charset="UTF-8">
			    <title>自定义登陆界面</title>
			</head>
			<body>
			    <form th:action="@{/customLogin}" method="post">
			        <table>
			            <tr>
			                <td>用户名：</td>
			                <td>
			                    <input name="username">
			                </td>
			            </tr>
			            <tr>
			                <td>密码：</td>
			                <td>
			                    <input name="password" type="password">
			                </td>
			            </tr>
			            <tr>
			                <td colspan="2">
			                    <input type="submit" value="登陆">
			                    <input type="reset" value="重置">
			                </td>
			            </tr>
			        </table>
			    </form>
			</body>
			</html>
	2. 其他情况可以通过在logou的form表单中添加隐藏域来实现csrf token的添加,CsrfFilter过滤器中会在request中添加这两个属性

			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
3. 核心接口
	1. UserDetailsService：用户的详细信息，认证和权限数据
	2. HttpSecurity：定义认证的规则(哪些URL地址需要认证和需要什么权限才能访问)以及认证的方式(form表单认证等)
		1. 采用表单登录时Spring security会自动的生成一个登陆界面，如果需要自定义，可以通过HttpSecurity中的formlogin方法指定登陆界面的地址
		2. **登陆请求的默认地址为/login** 请求方式为post请求

		
				protected void configure(HttpSecurity http) throws Exception {
					http
						.authorizeRequests()//通过匹配器设置URL的访问规则                                                                
							.antMatchers("/resources/**", "/signup", "/about").permitAll() //所有的用户都能访问                 
							.antMatchers("/admin/**").hasRole("ADMIN")   //只有拥有ROLE_ADMIN的用户才能访问，如果使用  hasRole方法可以省略 ROLE_前缀                                   
							.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")  //通过表达式来制定访问控制          
							.anyRequest().authenticated()                                                   
							.and()
						// ...
						.formLogin();
				}
		3. 退出(**LogoutFilter**)：
			1. 请求的URL地址：/logout
			2. 流程：清除session->请求rememberMe->请求SecurityContextHolder->重定向到/login?logout(登陆界面)
			3. 自定义

					protected void configure(HttpSecurity http) throws Exception {
						http
							.logout()                                                                
								.logoutUrl("/my/logout")//设置退出的地址                                                 
								.logoutSuccessUrl("/my/index")        //调整地址                                   
								.logoutSuccessHandler(logoutSuccessHandler)    //退出成功的回调，如果指定则 logoutSuccessUrl将无效                         
								.invalidateHttpSession(true)               //使用清除session                              
								.addLogoutHandler(logoutHandler)          //添加退出的回调                               
								.deleteCookies(cookieNamesToClear)        //请求cookie的名称                               
								.and()
							...
					}
				1. 一般只需要定制LogoutHandler和logoutSuccessHandler即可(将在LogoutFilter中处理)
					1. LogoutHandler: 触发退出请求的时候将调用该方法(流程的1、2、3)	 	
					2. logoutSuccessHandler：退出成功之后调用的方法

					
							public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
								throws IOException, ServletException {
							.....
							if (requiresLogout(request, response)) {
							//获取认证信息
								Authentication auth = SecurityContextHolder.getContext().getAuthentication();
								//调用LogoutHandler的方法处理退出逻辑
								this.handler.logout(request, response, auth);
								//执行logoutSuccessHandler的方法
								logoutSuccessHandler.onLogoutSuccess(request, response, auth);
					
								return;
							}
					
							chain.doFilter(request, response);
							}
	3. AccessDecisionManager：访问控制，用于访问控制(鉴权)处理
	4. AuthenticationManager：认证管理器,用户认证处理
4. 认证方式
	1. 基于内存数据的认证InMemoryUserDetailsManager(实则暴露UserDetailsService对象) 
	2. 基于JDBC的认证
	3. 基于LDAP(Lightweight Directory Access Protocol)的认证
	4. 自定义AuthenticationProvider处理
	5. 自定义UserDetailsService处理
5. **方法级别的权限控制**
	1. 开启：在配置类添加EnableGlobalMethodSecurity注解  
	2. 使用：通过Secured注解标示需要的权限，其使用[表达式](https://docs.spring.io/spring-security/site/docs/5.0.8.RELEASE/reference/htmlsingle/#el-access)来处理
	3. JSR-250提供的注解：PreAuthorize、PostAuthorize，在配置类添加注解

			@EnableWebSecurity
			@EnableGlobalMethodSecurity(jsr250Enabled = true,prePostEnabled = true)
			public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
			}

### 认证
主要通过认证管理器处理AuthenticationManager

1. 源码分析
	1. 入口:FilterChainProxy

			public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			if (currentPosition == size) {
				.....

				originalChain.doFilter(request, response);
			}
			else {
				currentPosition++;
				//遍历得到所有的filter，执行相应的逻辑
				Filter nextFilter = additionalFilters.get(currentPosition - 1);

				.....
				nextFilter.doFilter(request, response, this);
			}
		}
	2. AbstractAuthenticationProcessingFilter

			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
				throws IOException, ServletException {
	
			//判断是否需要认证-->根据HttpSecurity中的配置
			if (!requiresAuthentication(request, response)) {
				chain.doFilter(request, response);
	
				return;
			}
	
				//发起认证
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					// return immediately as subclass has indicated that it hasn't completed
					// authentication
					return;
				}
				sessionStrategy.onAuthentication(authResult, request, response);
			}
			catch (InternalAuthenticationServiceException failed) {
				
				unsuccessfulAuthentication(request, response, failed);
	
				return;
			}
			catch (AuthenticationException failed) {
				// Authentication failed
				unsuccessfulAuthentication(request, response, failed);
	
				return;
			}
	
			// Authentication success
			if (continueChainBeforeSuccessfulAuthentication) {
				chain.doFilter(request, response);
			}
			// 认证成功
			successfulAuthentication(request, response, chain, authResult);
		}
					
	3. UsernamePasswordAuthenticationFilter

			public Authentication attemptAuthentication(HttpServletRequest request,
				HttpServletResponse response) throws AuthenticationException {
				if (postOnly && !request.getMethod().equals("POST")) {
					throw new AuthenticationServiceException(
							"Authentication method not supported: " + request.getMethod());
				}
				//获取用户名、密码
				String username = obtainUsername(request);
				String password = obtainPassword(request);
		
				if (username == null) {
					username = "";
				}
		
				if (password == null) {
					password = "";
				}
		
				username = username.trim();
				//创建token对象
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
						username, password);
		
				// Allow subclasses to set the "details" property
				setDetails(request, authRequest);
				//通过认证管理器进行认证
				return this.getAuthenticationManager().authenticate(authRequest);
			}
	4. AbstractAuthenticationProcessingFilter

			protected void successfulAuthentication(HttpServletRequest request,
					HttpServletResponse response, FilterChain chain, Authentication authResult)
					throws IOException, ServletException {
		
				//存储认证信息
				SecurityContextHolder.getContext().setAuthentication(authResult);
		
				rememberMeServices.loginSuccess(request, response, authResult);
		
				// Fire event
				if (this.eventPublisher != null) {
					eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
							authResult, this.getClass()));
				}
				//通过successHandler处理，跳转到成功界面
				successHandler.onAuthenticationSuccess(request, response, authResult);
			}
		1. AuthenticationSuccessHandler
			1. ForwardAuthenticationSuccessHandler：如果配置了successForwardUrl将使用该处理器处理，通过转发的方式实现跳转，请求方式还是为POST，所以需要编写一个controller进行特殊处理
			2. SavedRequestAwareAuthenticationSuccessHandler：默认采用的方式，调整到根路径，界面对应index 	
		
### 授权

1. 源码分析
	1. AffirmativeBased

			public void decide(Authentication authentication, Object object,
				Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
				int deny = 0;
		
				for (AccessDecisionVoter voter : getDecisionVoters()) {
				    //通过投票机制处理
					int result = voter.vote(authentication, object, configAttributes);
		
					if (logger.isDebugEnabled()) {
						logger.debug("Voter: " + voter + ", returned: " + result);
					}
		
					switch (result) {
					case AccessDecisionVoter.ACCESS_GRANTED:
						return;
		
					case AccessDecisionVoter.ACCESS_DENIED:
						deny++;
		
						break;
		
					default:
						break;
					}
				}
		
				if (deny > 0) {
					throw new AccessDeniedException(messages.getMessage(
							"AbstractAccessDecisionManager.accessDenied", "Access is denied"));
				}
		
				// To get this far, every AccessDecisionVoter abstained
				checkAllowIfAllAbstainDecisions();
			}
	2. WebExpressionVoter

			public int vote(Authentication authentication, FilterInvocation fi,
				Collection<ConfigAttribute> attributes) {
				assert authentication != null;
				assert fi != null;
				assert attributes != null;
		
				WebExpressionConfigAttribute weca = findConfigAttribute(attributes);
		
				if (weca == null) {
					return ACCESS_ABSTAIN;
				}
		
				EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication,
						fi);
				ctx = weca.postProcess(ctx, fi);
				//解析表达式
				return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED
						: ACCESS_DENIED;
			}