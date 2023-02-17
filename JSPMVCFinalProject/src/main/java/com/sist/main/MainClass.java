package com.sist.main;

import java.lang.reflect.Method;

class A
{
	public void aaa()
	{
		System.out.println("aaa() call...");
		
	}
}
public class MainClass {

	public static void main(String[] args) {
		A a=new A();
		a.aaa();
		try
		{
			//아래 코드는 A a=new A()와 같음
			Class clsName=Class.forName("com.sist.main.A");
			Object obj=clsName.getDeclaredConstructor().newInstance();
			
			Method[] methods=clsName.getDeclaredMethods();
			methods[0].invoke(obj, null); //aaa()메소드 호출
		}catch(Exception ex) {}
	}

}
