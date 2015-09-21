package com.miqian.mq.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.List;

/**
 * Description:
 * 
 * @author Jackie
 * @created 2014-7-25 下午4:24:58
 */

public class JsonUtil {

	public static <T>T parseObject(String httpString, Class<T> clazz) {
		try {
			return JSON.parseObject(httpString, clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T> List<T> parseArray(String httpString, Class<T> clazz) {
		try {
			return JSON.parseArray(httpString, clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T>T parseObject(String httpString, TypeReference<T> type) {
		try {
			return JSON.parseObject(httpString, type);
		} catch (Exception e) {
			return null;
		}
	}
}
