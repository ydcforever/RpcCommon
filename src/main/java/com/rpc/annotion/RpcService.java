package com.rpc.annotion;

import java.lang.annotation.*;

/**
 * Created by T440 on 2019/4/14.
 * 服务端和客户端服务名约束
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcService {
    String value() default "";
}
