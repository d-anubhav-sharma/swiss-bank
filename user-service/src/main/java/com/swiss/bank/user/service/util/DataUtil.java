package com.swiss.bank.user.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.swiss.bank.user.service.entities.Role;

public class DataUtil{

	public static <T> T getOrDefault(T currentValue, T defaultValue) {
		if(currentValue==null) return defaultValue;
		return currentValue;
	}

	public static <T> T getOrDefault(boolean condition, T currentValue, T defaultValue) {
		if(condition) return defaultValue;
		return currentValue;
	}
	
	public static List<SimpleGrantedAuthority> getGrantedAuthoritiesFromRoles(List<Role> roles){
		if(roles==null) return new ArrayList<>();
		return roles
				.stream()
				.flatMap(role -> role.getPrivileges().parallelStream())
				.map(SimpleGrantedAuthority::new)
				.toList();
	}
	
	public static byte[] extractBytesFromDataBuffer(DataBuffer dataBuffer) {
		try(InputStream inputStream = dataBuffer.asInputStream(true)) {
			return inputStream.readAllBytes();
		}
		catch(IOException ex) {
			return new byte[] {};
		}
	}
	
	public static byte[] combineBytes(List<byte[]> byteArrays) {
        int totalLength = byteArrays.stream().mapToInt(byteArray -> byteArray.length).sum();
        byte[] combined = new byte[totalLength];
        int start = 0;
        for (byte[] byteArray : byteArrays) {
            System.arraycopy(byteArray, 0, combined, start, byteArray.length);
            start += byteArray.length;
        }
        return combined;
    }
	
	private DataUtil() {}
}
