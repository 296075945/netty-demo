package com.wy.nio.im.msg;

import java.io.Serializable;

import com.wy.nio.im.common.User;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:15:11
 */
public abstract class AbstractMsg implements Msg,Serializable {

	private static final long serialVersionUID = 1L;
	User sender;
	MsgType type;

	public User getSender() {
		return sender;
	}

	public MsgType getType() {
		return type;
	}
	
	@Override
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("msg:{sender:").append(sender.getUuid()).append(",type:")
		.append(type).append("}");
		return  buff.toString();
	}

}
