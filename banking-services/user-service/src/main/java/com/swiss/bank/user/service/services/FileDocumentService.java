package com.swiss.bank.user.service.services;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.FileDocument;

import reactor.core.publisher.Mono;

@Service
public interface FileDocumentService {

	Mono<FileDocument> saveDocument(FileDocument build);

}
