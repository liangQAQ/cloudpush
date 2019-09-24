![1566284996878](README.assets/1566284996878.png)

# cloudpush- 分布式推送实战
cloudpush是我对于springcloud微服务框架的一个学习实践，旨在学习微服务架构的同时，实现一个基于websocket协议，可平滑水平拓展的分布式推送产品。
# 应用架构图
![1565533924343](README/assets/1565533924343.png)

# 项目用到的技术
目前核心的技术栈采用的是SpringBoot2.0.1.RELEASE

## 前端使用的技术
管理台页面待开发。。

## 后端使用的技术
后端的主要架构是基于springboot.

* SpringCloud
* Netty
* Nacos
* Redis
* RocketMQ


# 项目模块说明

| tools  工具包                                | 压缩包 | 项目，或者测试中需要用的到中间件包，直接用docker更方便。。             |
| ------------------------------------------------------------ | --------- | ---------------------------------------------------- |
| cloudpush-api 公共组件部分                                    | jar       | 公共组件，很多地方都有引用         |
| cloudpush-eureka 注册中心                           | web项目  | 其他组件的注册中心，方便平滑的上下线服务                      |
| cloudpush-portal 接口服务                  | web项目   | 提供http接口服务                                             |
| cloudpush-websocket 推送服务                | web项目  | 由springboot启动的一个netty服务，对外的netty端口提供websocket的握手，消息的收发服务                    |
| cloudpush-gateway 统一网关    | web项目   | 统一入口,便于未来鉴权，现在用作根据各个cloudpush-websocket实例上所维持的客户端数量，生成ws连接的动态路由规则（优先分配给连接数较少的实例）|
| wiki                                                         |           | 安装帮助文档           |

# 项目开发进度

## 前台项目整体的规划有

* 提供管理后台页面

![1565235690613](README.assets/1565235690613.png)

## 后台规划

* 结合客户端收到消息的回执和在推送流程中的埋点，接入ELK组件实现对消息送达率，客户端会话时长的统计

# 效果图


# 项目架构图







# 如何贡献

非常欢迎您对cloudpush的开发作出贡献



# 技术交流及问题解答


>  作者的个人博客

