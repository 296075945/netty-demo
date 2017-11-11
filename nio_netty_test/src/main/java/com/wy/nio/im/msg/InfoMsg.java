package com.wy.nio.im.msg;

import com.wy.nio.im.common.User;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午4:40:23
 */
public class InfoMsg extends AbstractMsg {

	private static final long serialVersionUID = 1L;
	User accept;
	String msg;
	
	public InfoMsg(User sender,User accept,String msg){
		type = MsgType.info;
		this.sender = sender;
		this.accept = accept;
		this.msg = msg;
	}
	

	public User getAccept() {
		return accept;
	}


	public String getMsg() {
		return msg;
	}

}
