package cn.itcast.heima2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//模拟线程间共享数据的问题引入
//Thread-0 has put data :-1035858983
//Thread-1 has put data :1090502417
//A from Thread-0 get data :1090502417
//A from Thread-1 get data :1090502417
//B from Thread-0 get data :1090502417
//B from Thread-1 get data :1090502417
//启动了两个线程，线程0和线程1；分别往线程里面放数据；然后在两个模块中取数据，发现不同的线程取到的数据不是该线程放入的数据。
//比如线程0放入的是-1035858983，但是A from Thread-0 get data :1090502417，B from Thread-0 get data :1090502417，线程0中的AB模块取到的是线程1的数据
public class ThreadScopeShareDataHasMistake {

	private static int data = 0;
	public static void main(String[] args) {
		for(int i=0;i<2;i++){//启动两个线程
			new Thread(new Runnable(){
				@Override
				public void run() {
					data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() 
							+ " has put data :" + data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	static class A{
		public void get(){
			System.out.println("A from " + Thread.currentThread().getName() 
					+ " get data :" + data);
		}
	}
	
	static class B{
		public void get(){
			System.out.println("B from " + Thread.currentThread().getName() 
					+ " get data :" + data);
		}		
	}
}
