package com.swiss.bank.user.service.controllers;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.FileDocument;
import com.swiss.bank.user.service.services.FileDocumentService;
import com.swiss.bank.user.service.util.DataUtil;

import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/file")
public class FileDocumentController {

	FileDocumentService fileDocumentService;

	public FileDocumentController(FileDocumentService fileDocumentService) {
		this.fileDocumentService = fileDocumentService;
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Mono<FileDocument>> saveFileDocument(@RequestPart FilePart file,
			@RequestParam(value = "category", required = true) String category,
			@RequestParam(value = "subCategory", required = false) String subCategory,
			Principal principal) {
		Mono<FileDocument> fileDocumentMono = file.content()
			.map(DataUtil::extractBytesFromDataBuffer)
			.collectList()
			.map(DataUtil::combineBytes)
			.map(fileContent -> FileDocument
				.builder()
				.fileData(fileContent)
				.fileCategory(category)
				.fileSubCategory(subCategory)
				.fileName(file.filename())
				.fileType(file.headers().getContentType().toString())
				.username(principal.getName())
				.build())
			.flatMap(fileDocument -> fileDocumentService.saveDocument(fileDocument));
		return ResponseEntity.ok(fileDocumentMono);
	}
}
