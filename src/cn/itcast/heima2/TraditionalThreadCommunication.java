package cn.itcast.heima2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 子线程循环10次，接着主线程循环100，接着又回到子线程循环10次，接着再回到主线程又循环100，如此循环50次
 * @author Administrator
 *
 */
public class TraditionalThreadCommunication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final Business business = new Business();
		
		//子线程运行
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
					
						for(int i=1;i<=50;i++){//大循环是50次
							business.sub(i);
						}
						
					}
				}
		).start();
		
		//主线程运行
		for(int i=1;i<=50;i++){//大循环是50次
			business.main(i);
		}
		
	}

}
  class Business {
	  private boolean bShouldSub = true;//如果是轮到子线程运行，则为true；如果是轮到主线程运行，就改为false
	  public synchronized void sub(int i){
		  //如果不是轮到子类运行，那么子线程就等待
		  while(!bShouldSub){
			  try {
				//要用到同步监视器对象去调用wait方法
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  //如果是子线程运行，那么子线程就运行10次
			for(int j=1;j<=10;j++){
				System.out.println("sub thread sequence of " + j + ",loop of " + i);
			}
			
		  //子线程运行完成后，就轮到主线程运行了（把标记位改为主线程运行，并唤醒正在等待运行的主线程）
		  bShouldSub = false;
		  this.notify();
	  }
	  
	  public synchronized void main(int i){
		    //如果不是轮到主类运行，那么主线程就等待
		  	while(bShouldSub){//这里用while，比起用if更加的好，用while可以防止伪唤醒的导致的意外情况的发生
		  		try {
		  		//要用到同步监视器对象去调用wait方法
					this.wait();
					//当子线程执行完成后，唤醒主线程，这时候bShouldSub标志位已经被改成false了，所以就执行下面主线程的内容
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	}
		  	
		  	//如果是主线程运行，那么主线程就运行100次
			for(int j=1;j<=100;j++){
				System.out.println("main thread sequence of " + j + ",loop of " + i);
			}
			
			//主线程运行完成后，就轮到子线程运行了（把标记位改为子线程运行，并唤醒正在等待运行的子线程）
			bShouldSub = true;
			this.notify();
	  }
  }
