package org.dangcat.boot.security.annotation;

import org.dangcat.boot.security.impl.LoginServiceBase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendSecurity
{
    Class<? extends LoginServiceBase> value();
}