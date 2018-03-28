package com.eastrobot.robotdev.nuskin.utils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 支持容量、超时时间设置及泛型， 本机测试处理速度：约4万条数据每秒
 * 
 * @author Tom
 * @param <K>
 * @param <V>
 */
public class LRUTCache<K, V> {
	private final ReentrantReadWriteLock theLock = new ReentrantReadWriteLock();
	private final Lock writeLock = theLock.writeLock();
	private final Lock readLock = theLock.readLock();
	private int timeToLiveSeconds;
	private int cacheSize;
	private LinkedHashMap<K, LRUObje> map;

	public LRUTCache(int cacheSize, int timeToLiveSeconds) {
		this.cacheSize = cacheSize;
		this.timeToLiveSeconds = timeToLiveSeconds;
		this.map = new LinkedHashMap<K, LRUObje>(cacheSize, 1f, true) {
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(Map.Entry<K, LRUObje> eldest) {
				boolean sizeOut = size() > LRUTCache.this.cacheSize - 1;
				boolean timeOut = true;
				if(eldest==null || eldest.getValue()==null){
					timeOut = true;
				}else{
					timeOut = !eldest.getValue().isLiving();
				}
				return sizeOut || timeOut;
			}
		};
	}

	public LRUTCache(int cacheSize) {
		this.cacheSize = cacheSize;
		this.map = new LinkedHashMap<K, LRUObje>(cacheSize, 1f, true) {
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(Map.Entry<K, LRUObje> eldest) {
				return size() > LRUTCache.this.cacheSize - 1;
			}
		};
	}

	public V get(K key) {
		LRUObje lruObje = null;
		V returnObj = null;
		boolean timeOut = false;
		readLock.lock();
		try {
			lruObje = map.get(key);
			if (lruObje != null) {
				if (lruObje.isLiving()) {
					returnObj = lruObje.value;
				} else {// time out
					timeOut = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}
		if (timeOut) {
			remove(key);
		}
		return returnObj;
	}

	public V put(K key, V value) {
		LRUObje lruObje = new LRUObje(value);
		writeLock.lock();
		try {
			LRUObje putRet = map.put(key, lruObje);
			return (putRet == null ? null : putRet.value);
		} finally {
			writeLock.unlock();
		}
	}

	public V remove(K key) {
		writeLock.lock();
		try {
			LRUObje removeRet = map.remove(key);
			return (removeRet == null ? null : removeRet.value);
		} finally {
			writeLock.unlock();
		}
	}

	public int size() {
		readLock.lock();
		try {
			return map.size();
		} finally {
			readLock.unlock();
		}
	}

	public String printDetail() {
		StringBuffer str = new StringBuffer("[");
		for (Map.Entry<K, LRUObje> entry : map.entrySet()) {
			str.append(entry.getKey()).append("=").append(entry.getValue().value).append(";");
		}
		str.append("]");
		return str.toString();
	}

	private class LRUObje {
		long pTime;
		V value;

		private boolean isLiving() {
			return timeToLiveSeconds <= 0 || (timeToLiveSeconds > 0 && (System.currentTimeMillis() - pTime) <= timeToLiveSeconds * 1000);
		}

		private LRUObje(V obj) {
			pTime = System.currentTimeMillis();
			value = obj;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}
	
	public void clear(){
		writeLock.lock();
		try{
			this.map.clear();	
		}finally{
			writeLock.unlock();
		}
	}

	public int getTimeToLiveSeconds() {
		return timeToLiveSeconds;
	}

	public void setTimeToLiveSeconds(int timeToLiveSeconds) {
		this.timeToLiveSeconds = timeToLiveSeconds;
	}
	
	public static void main(String[] args) {
		LRUTCache<String, String> t = new LRUTCache<String, String>(10,10);
		
		for (int i = 0; i <=20; i++) {
			
			//System.out.println("befor="+t.printDetail());
			t.put(""+i, "value-"+i);
			//System.out.println("after="+t.printDetail());
			//System.out.println("--------------------------------");
			
			try {
				Thread.sleep(2*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("after="+t.printDetail());
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
}
