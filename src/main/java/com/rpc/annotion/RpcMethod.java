package com.rpc.annotion;

import java.lang.annotation.*;

/**
 * Created by T440 on 2019/4/15.
 * 服务端和客户端方法名约束
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {
    String value() default "";
}
