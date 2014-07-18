package io.github.m1enkrafftman.safeguard2.utils;

public class Timer {
	
	private long myLastTime;
	
	public Timer() {
		myLastTime = -1L;
	}
	
	public boolean canCheck(float deltaMillis) {
		return getCurrentTime() - myLastTime >= deltaMillis ? getReturn(true) : getReturn(false);
	}
	
	public float diffMillis() {
		return getCurrentTime() - myLastTime;
	}
	
	private boolean getReturn(boolean b) {
		if(b == true) {
			myLastTime = getCurrentTime();
			return true;
		}else {
			return false;
		}
	}
	
	private static long getCurrentTime() {
		long john; //silver
		john = (System.nanoTime()/1000000);
		return john;
	}
}