package com.swiss.bank.user.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class PathPrivilegeMapper {

	@Id
	private String id;
	private String urlPattern;
	private String privilegeName;
	private String category;
	private String method;
	private boolean enabled;
	
}
