package com.swiss.bank.user.service.services.impl;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.FileDocument;
import com.swiss.bank.user.service.repositories.FileDocumentRepository;
import com.swiss.bank.user.service.services.FileDocumentService;

import reactor.core.publisher.Mono;

@Service
public class FileDocumentServiceImpl implements FileDocumentService{

	FileDocumentRepository fileDocumentRepository;
	
	FileDocumentServiceImpl(FileDocumentRepository fileDocumentRepository){
		this.fileDocumentRepository = fileDocumentRepository;
	}
	
	@Override
	public Mono<FileDocument> saveDocument(FileDocument fileDocument) {
		return fileDocumentRepository.save(fileDocument);
	}

}
