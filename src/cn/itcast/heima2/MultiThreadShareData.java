package cn.itcast.heima2;

public class MultiThreadShareData {

	private static ShareData1 data1 = new ShareData1();
	
	public static void main(String[] args) {
		ShareData1 data2 = new ShareData1();
		new Thread(new MyRunnable1(data2)).start();//用多个Runnable对象，两个线程用的是同一个共享对象。
		new Thread(new MyRunnable2(data2)).start();
		
		final ShareData1 data1 = new ShareData1();
		new Thread(new Runnable(){
			@Override
			public void run() {//Runnable主动去取共享数据
				data1.decrement();
				
			}
		}).start();
		new Thread(new Runnable(){
			@Override
			public void run() {
				data1.increment();
				
			}
		}).start();

	}

}
	
	class MyRunnable1 implements Runnable{
		private ShareData1 data1;
		public MyRunnable1(ShareData1 data1){
			this.data1 = data1;
		}
		public void run() {
			data1.decrement();
			
		}
	}
	
	class MyRunnable2 implements Runnable{
		private ShareData1 data1;
		public MyRunnable2(ShareData1 data1){
			this.data1 = data1;
		}
		public void run() {
			data1.increment();
		}
	}

	class ShareData1 /*implements Runnable*/{
/*		private int count = 100;//执行的代码相同，可以用同一个Runnable对象
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				count--;
			}
		}*/
		
		
		private int j = 0;
		public synchronized void increment(){
			j++;
		}
		
		public synchronized void decrement(){
			j--;
		}
	}