package com.wy.nio.im.common;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author wy
 * @version 创建时间：2017年11月7日 下午3:00:08
 */
public class User implements Serializable{
	private static final long serialVersionUID = -971712068351941518L;
	String uuid;

	public User() {
		this(UUID.randomUUID().toString());
	}
	public User(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

}
