package com.wy.nio.im.msg;

import com.wy.nio.im.common.User;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:13:31
 */
public class LoginMsg extends AbstractMsg{

	private static final long serialVersionUID = 1L;

	public LoginMsg() {
		this.type = MsgType.login;
	}
	public LoginMsg(User sender){
		this.type = MsgType.login;
		this.sender = sender;
	}
	public User getAccept() {
		return null;
	}

	public String getMsg() {
		// TODO Auto-generated method stub
		return null;
	}

}
