package com.wy.nio.im.common;
/**
 * @author wy
 * @version 创建时间：2017年11月8日 上午9:21:36
 */
public interface Manager<T> {
	void start();
	void stop();
	T getManager();
}
