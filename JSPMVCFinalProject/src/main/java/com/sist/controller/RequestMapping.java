package com.sist.controller;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD) // 메소드 찾기
//주의점 : 같은 이름을 가진 어노테이션이 있는 경우 에러 발생 (+같은 어노테이션 쓸 때 매개변수는 달라야 함)
public @interface RequestMapping {
	public String value(); //@RequestMappping("value")
}
