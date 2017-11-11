package com.wy.nio.im.msg;

import com.wy.nio.im.common.User;

/**
 * @author wy
 * @version 创建时间：2017年11月8日 上午10:14:02
 */
public class ServerMsg extends AbstractMsg{

	private static final long serialVersionUID = 1L;

	public static final ServerMsg UN_ONLINE = new ServerMsg("收件人不在线");
	String msg;
	private ServerMsg(String msg){
		this.sender = new User("server");
		this.msg = msg;
	}
	public User getAccept() {
		return null;
	}

	public String getMsg() {
		return msg;
	}

}
