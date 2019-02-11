package org.tlh.rpc.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 离歌笑tlh/hu ping on 2019/2/11
 * <p>
 * Github: https://github.com/tlhhup
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {
}
