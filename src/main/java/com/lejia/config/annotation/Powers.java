package com.lejia.config.annotation;


import com.lejia.global.PowerConsts;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Powers {
	PowerConsts[] value();
}

