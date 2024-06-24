package com.swiss.bank.common.models;

import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathPrivilegeMapper {

	private String urlPattern;
	private String privilege;
	private String category;
	private HttpMethod method;
	private boolean enabled;
	
}
