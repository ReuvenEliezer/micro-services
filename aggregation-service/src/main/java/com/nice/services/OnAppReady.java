package com.nice.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class OnAppReady implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LogManager.getLogger(OnAppReady.class);

    private final ApplicationContext appContext;

    public OnAppReady(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent readyEvent) {
        printAppLoadingInfo(readyEvent);
    }

    private void printAppLoadingInfo(ApplicationReadyEvent readyEvent) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Application Name: ").append(appContext.getApplicationName()).append(System.lineSeparator())
                .append("Application Id: ").append(appContext.getId()).append(System.lineSeparator())
                .append("Application started in: ").append(readyEvent.getTimeTaken()).append(System.lineSeparator())
                .append("Total loading classes: ").append(readyEvent.getSpringApplication().getClassLoader().getDefinedPackages().length).append(System.lineSeparator())
                .append("Number of threads: ").append(Thread.activeCount()).append(System.lineSeparator())
//                .append("Active Group Count: ").append(Thread.currentThread().getThreadGroup().activeGroupCount()).append(System.lineSeparator())
//                .append("Current Thread Group: ").append(Thread.currentThread().getThreadGroup().getName()).append(System.lineSeparator())
                .append("Total Number of threads: ").append(ManagementFactory.getThreadMXBean().getThreadCount()).append(System.lineSeparator())
        ;

        sb.append("Singleton Beans: ").append(getSingletonNames().size()).append(System.lineSeparator());
//        getSingletonNames().forEach(beanName -> sb.append(beanName).append(System.lineSeparator()));
        sb.append("BeanDefinitionCount: ").append(appContext.getBeanDefinitionCount()).append(System.lineSeparator());
//        sb.append("Beans: ").append(System.lineSeparator());
//        Arrays.stream(appContext.getBeanDefinitionNames()).forEach(beanName -> sb.append(beanName).append(System.lineSeparator()));
        collectMemoryUsage(sb);
        logger.info(sb);
    }

    private List<String> getSingletonNames() {
        AutowireCapableBeanFactory autowireCapableBeanFactory = appContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory instanceof SingletonBeanRegistry) {
            return List.of(((SingletonBeanRegistry) autowireCapableBeanFactory).getSingletonNames());
        }
        return new ArrayList<>();
    }

    private static void collectMemoryUsage(StringBuilder sb) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        double totalUsedPercentage = ((double) usedMemory / totalMemory) * 100;
        sb.append("Used Memory: ").append(usedMemory / 1024 / 1024).append(" MB").append(System.lineSeparator())
                .append("Free Memory: ").append(freeMemory / 1024 / 1024).append(" MB").append(System.lineSeparator())
                .append("Total Memory: ").append(totalMemory / 1024 / 1024).append(" MB. ").append(System.lineSeparator())
                .append("Total Memory Used Percentage: ").append(String.format("%.2f", totalUsedPercentage)).append("%").append(System.lineSeparator())
                .append("Max Memory: ").append(maxMemory / 1024 / 1024).append(" MB").append(System.lineSeparator());
    }

}
