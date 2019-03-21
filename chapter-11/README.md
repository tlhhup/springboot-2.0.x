### 动态改变log4j2的文件中的参数(Lookups)
1. 具体支持的配置可以查看官网的说明：
	1. lookups说明： http://logging.apache.org/log4j/2.x/manual/configuration.html#PropertySubstitution 
	2. 使用方式：http://logging.apache.org/log4j/2.x/manual/lookups.html
2. 使用：
 	1. 再log4j2.xml文件中先进行声明并引用Lookups中的参数

	 		<Properties>
	 			 <!-- 使用Lookups中的变量 -->
		        <Property name="filename">target/$${date:MM-dd-yyyy}.log</Property>
		        <Property name="filename1">target/$${ctx:fileName}</Property>
		        <Property name="filename2">target/${sys:fileName}</Property>
	    	</Properties>
 	2. 设置Lookups参数

	 		ThreadContext.put("fileName", "logs/file.txt");
	        System.setProperty("fileName","detail.txt");
 	3. 使用声明的变量 

 			<File name="MyFile" fileName="${filename2}">
	            <PatternLayout>
	                <Pattern>%d %p ${filename1} %c{1.} [%t] %m%n</Pattern>
	            </PatternLayout>
        	</File> 	