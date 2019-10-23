package com.rpc.annotion;

import java.lang.annotation.*;

/**
 * Created by T440 on 2019/4/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcClient {
    String value() default "";
}
