package com.wy.nio.im.server;
/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:24:14
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wy.nio.im.common.User;

import io.netty.channel.Channel;

public class OnlineUser {
	static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
	public static Channel get(User user){
		return map.get(user.getUuid());
	}
	public static void add(User user,Channel channel){
		map.put(user.getUuid(), channel);
	}
	public static void remove(User user){
		map.remove(user.getUuid());
	}
}
