package com.kakao.actionbase.test.migration;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MigratedFrom {
  String value() default "";

  String description() default "";
}
