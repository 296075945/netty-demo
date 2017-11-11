package com.wy.nio.im.msg;
/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:03:47
 */

import com.wy.nio.im.common.User;

public interface Msg {
	User getSender();

	User getAccept();

	MsgType getType();

	String getMsg();

}
