package cn.itcast.heima2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalTest {

	private static ThreadLocal<Integer> x = new ThreadLocal<Integer>();
	private static ThreadLocal<MyThreadScopeData> myThreadScopeData = new ThreadLocal<MyThreadScopeData>();
	public static void main(String[] args) {
		for(int i=0;i<2;i++){
			new Thread(new Runnable(){
				@Override
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() 
							+ " has put data :" + data);
					x.set(data);
/*					MyThreadScopeData myData = new MyThreadScopeData();//这种方式，需要每个线程自己去创建与之相关的线程共享对象，烂透了的代码
					myData.setName("name" + data);
					myData.setAge(data);
					myThreadScopeData.set(myData);*/
					MyThreadScopeData.getThreadInstance().setName("name" + data);
					MyThreadScopeData.getThreadInstance().setAge(data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	static class A{
		public void get(){
			int data = x.get();
			System.out.println("A from " + Thread.currentThread().getName() 
					+ " get data :" + data);
/*			MyThreadScopeData myData = myThreadScopeData.get();;
			System.out.println("A from " + Thread.currentThread().getName() 
					+ " getMyData: " + myData.getName() + "," +
					myData.getAge());*/
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("A from " + Thread.currentThread().getName() 
					+ " getMyData: " + myData.getName() + "," +
					myData.getAge());
		}
	}
	
	static class B{
		public void get(){
			int data = x.get();			
			System.out.println("B from " + Thread.currentThread().getName() 
					+ " get data :" + data);
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("B from " + Thread.currentThread().getName() 
					+ " getMyData: " + myData.getName() + "," +
					myData.getAge());			
		}		
	}
}
//线程范围内共享的对象的设计，这个对象是专门与线程绑定而存在的。
//用户不需要特别的为每个线程去创建一个对象，而是有这个共享对象内部实现。
//只要调用一下这个类的某个方法就可以得到与线程绑定的共享对象
class MyThreadScopeData{
	private MyThreadScopeData(){}
	//获取线程间的共享的MyThreadScopeData对象
	public static /*synchronized*/ MyThreadScopeData getThreadInstance(){//不需要加锁
		MyThreadScopeData instance = map.get();//每个线程拿的是各自的对象，所以可以不加同步锁
		if(instance == null){//线程内共享的对象为空，则创建，并把共享对象放入各自的线程中
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		return instance;
	}
	//private static MyThreadScopeData instance = null;//new MyThreadScopeData();
	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();
	
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
