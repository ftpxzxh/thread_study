package cn.itcast.heima2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  第一次读的时候，上读锁，使得写的线程不能进来；
但是第一次读的时候发现cacheValid是false，即还没有缓存，所以就先把读锁释放，然后上写锁；
如果读锁切换成写锁的过程中，缓存cacheValid还是false，表示没有其他线程缓存过，这时候就在加写锁的情况下进行数据的缓存，缓存完成后，就把缓存标志位变为true，最后上读锁，并把写锁释放；
然后就可以使用缓存数据了，使用完成后，就把读锁释放掉
 * @author Administrator
 *
 */
public class CacheDemo {

	private Map<String, Object> cache = new HashMap<String, Object>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private ReadWriteLock rwl = new ReentrantReadWriteLock();
	public  Object getData(String key){
		rwl.readLock().lock();
		Object value = null;
		try{
			value = cache.get(key);
			if(value == null){
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				try{
					if(value==null){//必需再判斷一下
						value = "aaaa";//实际失去queryDB();
					}
				}finally{
					rwl.writeLock().unlock();
				}
				rwl.readLock().lock();
			}
		}finally{
			rwl.readLock().unlock();
		}
		return value;
	}
}
