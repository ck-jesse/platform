# platform

#### 介绍
包含通用模块和基础服务，通用模块是为了形成一种标准化规约和公共工具类库。

#### 一、通用模块
`common-dto` - 通用DTO模块 

> 1、标准化Rest风格的微服务通信协议。
>
> 2、统一业务异常
>
> 3、统一异常编码

`common-util` - 通用工具类模块 

> 主要常用工具类。


#### 二、基础服务
注：后续会将基础服务中的各个服务规划为独立的库。

`base-service` 模块为基础服务模块。

`id-generator-service` 产号服务。

具体见：https://gitee.com/ckcoy/id-generator-service

##### 后续规划

`sms-service` 短信服务

> 1、支持多短信通道切换
>
> 2、支持各种限流规则
>
> 同一手机号一天最多发送短信数量
>
> 同一手机号5分钟内发送短信数量
>
> 短信有效期

`pay-service` 支付服务

> 1、支持多支付通道
>
> 2、支持微信、支付宝支付
>
> 3、支持简单快速的接入其他支付通道
>
> 4、支付回调处理成功后，通过MQ与具体业务解耦

`account-service` 核心账户服务

> 用于作为积分账户体系，用户余额账户体系。