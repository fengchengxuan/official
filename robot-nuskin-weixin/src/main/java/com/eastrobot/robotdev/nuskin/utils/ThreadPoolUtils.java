package com.eastrobot.robotdev.nuskin.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolUtils {
	public static ExecutorService pool = Executors.newCachedThreadPool();
}
