package org.tlh.rpc.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/11
 * <p>
 * Github: https://github.com/tlhhup
 */
@Slf4j
public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(beanDefinitionRegistry){

            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }

        };
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        scanner.setResourceLoader(resourceLoader);

        //获取扫描的包
        Set<String> basePackages = getBasePackages(metadata);
        //注册bean对象
        try {
            for (String basePackage : basePackages) {
                Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
                for (BeanDefinition candidateComponent : candidateComponents) {
                    if (candidateComponent instanceof AnnotatedBeanDefinition) {
                        AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                        String className = annotationMetadata.getClassName();

                        Class aClass = Class.forName(className);

                        //创建bean对象,通过回调来创建对象
                        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(aClass,()->{
                            Object proxy=Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    log.info(method.getDeclaringClass().getSimpleName());
                                    log.info(method.getName());
                                    log.info(Arrays.toString(args));
                                    return null;
                                }
                            });
                           return proxy;
                        });

                        BeanDefinitionHolder holder = new BeanDefinitionHolder(definition.getBeanDefinition(), className, null);
                        BeanDefinitionReaderUtils.registerBeanDefinition(holder, beanDefinitionRegistry);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableRpcClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader=resourceLoader;
    }
}
