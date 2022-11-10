Spring Boot HTTP组件预热Starter
====

[![Maven Central](https://img.shields.io/maven-central/v/io.github.shelltea/warmup-spring-boot-starter.svg)](https://central.sonatype.dev/namespace/io.github.shelltea) [![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/shelltea/warmup-spring-boot-starter/blob/main/LICENSE)

## 简介

Spring Boot的应用程序在启动时，JVM需要加载各种类进行初始化，导致初期的HTTP请求往往响应时间较长，这也就是我们经常遇到的冷启动问题。通过`warmup-spring-boot-starter`可以在应用程序对外提供服务之前预热**HTTP相关的组件**，从而降低HTTP请求响应时间。

通过在业务服务中使用对比测试，可有效降低响应时间达**30%~90%**。

此项目受到[warm-me-up](https://github.com/steinsag/warm-me-up)项目启发，将预热功能包装成Spring Boot Starter以方便使用。

如果希望在启动时对其他组件（数据库连接、缓存加载等）进行预热，可以参考：

```java
@Component
public class WarmUpListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 对其他组件进行预热
    }
}
```

## 使用方法

1. 增加依赖；

```xml
<dependency>
    <groupId>io.github.shelltea</groupId>
    <artifactId>warmup-spring-boot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

2. 正常启动服务，如果日志中输出以下内容表示预热完成。

```
Completed warm up application
```

### 配置参数

| 参数名                                | 类型      | 默认值  | 说明       |
|------------------------------------|---------|------|----------|
| management.endpoint.warm-up.enable | boolean | true | 开启预热     |
| management.endpoint.warm-up.times  | int     | 5    | 预热接口请求次数 |

## 兼容性

| Spring Boot 版本 | 兼容  |
|----------------|-----|
| 2.7.x          | √   |
| 2.6.x          | √   |
| 2.5.x          | √   |
| 2.4.x          | √   |
| 2.3.x.RELEASE  | √   |
| 2.2.x.RELEASE  | √   |
| 2.1.x.RELEASE  | √   |
| 2.0.x.RELEASE  | √   |

## 参考

1. https://stackoverflow.com/questions/54759652/spring-boot-cold-start/70711103#70711103
2. https://github.com/steinsag/warm-me-up