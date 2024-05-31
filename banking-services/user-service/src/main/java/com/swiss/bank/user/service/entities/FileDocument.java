package com.swiss.bank.user.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FileDocument {

	@Id
	private String fileId;
	private String fileName;
	private String fileType;
	private String fileCategory;
	private String fileSubCategory;
	@Nonnull
	private String username;
	private byte[] fileData;
	
}
