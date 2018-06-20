package com.weedahm.ontmanager;

import java.sql.Timestamp;

public class Log {
	static void d(String tag, String msg) {
		System.out.println("["+new Timestamp(System.currentTimeMillis())+"] " + tag + " : " + msg);
	}
}
