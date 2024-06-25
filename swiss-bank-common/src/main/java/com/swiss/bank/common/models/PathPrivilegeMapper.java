package com.swiss.bank.common.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathPrivilegeMapper {

	private String urlPattern;
	private String privilegeName;
	private String category;
	private String method;
	private boolean enabled;
	
}
