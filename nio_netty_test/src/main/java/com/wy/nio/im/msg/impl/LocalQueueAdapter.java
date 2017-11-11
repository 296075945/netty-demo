package com.wy.nio.im.msg.impl;

import java.util.concurrent.LinkedBlockingQueue;

import com.wy.nio.im.msg.Msg;
import com.wy.nio.im.msg.MsgQueue;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午4:09:43
 */
public class LocalQueueAdapter extends LinkedBlockingQueue<Msg> implements MsgQueue{

	private static final long serialVersionUID = 1L;


	
}
