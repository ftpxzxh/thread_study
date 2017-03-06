package cn.itcast.heima2;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用两个具有一个空间的队列可以实现同步通知的功能
 * @author Administrator
 *
 */
public class BlockingQueueCommunication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final Business business = new Business();
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
					
						for(int i=1;i<=50;i++){
							business.sub(i);
						}
						
					}
				}
		).start();
		
		for(int i=1;i<=50;i++){
			business.main(i);
		}
		
	}

	 static class Business {
		 
		 
		  BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<Integer>(1);
		  BlockingQueue<Integer> queue2 = new ArrayBlockingQueue<Integer>(1);
		  
		  //匿名构造方法，在任何构造方法运行之前；调用类的任意一个有名字的构造方法之前，程序都会执行匿名的构造方法
		  {
			  Collections.synchronizedMap(null);
			  try {
				  System.out.println("xxxxxdfsdsafdsa");
				queue2.put(1);//保证队列2不是第一个先运行，可以实现队列1可以先运行
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  
		  public  void sub(int i){
			  	try {
					queue1.put(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int j=1;j<=10;j++){
					System.out.println("sub thread sequece of " + j + ",loop of " + i);
				}
				try {
					queue2.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
		  
		  public  void main(int i){//注意这里不能加synchronized，否则第一次先进入main时，因为匿名构造方法中队列2已经满了，会阻塞在这里，锁一直不能释放，导致sub一直进不去了
			  	try {
					queue2.put(1);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for(int j=1;j<=100;j++){
					System.out.println("main thread sequece of " + j + ",loop of " + i);
				}
				try {
					queue1.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	  }

}
