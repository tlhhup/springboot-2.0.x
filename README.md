# springboot-2.0.x
springboot2.0.x学习整理

- chapter-1：初识springboot 
- chapter-2：外化配置(多环境配置)
- chapter-3：日志
- chapter-4：web应用开发
- chapter-5：数据库事务应用
- chapter-6：redis缓存配置
- chapter-7：oauth 2
- chapter-8：quartz
- chapter-9：ImportBeanDefinitionRegistrar实现bean的动态注册(模拟rpc客户端创建过程)
- chapter-10：log4j2应用
- chapter-11：并发工具的应用，通过CyclicBarrier来获取所有线程都开始运行的时间，CountDownLatch设置为1来获取第一个线程执行结束时间

### 说明
1. 集成h2的console，其使用的URL地址为: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`