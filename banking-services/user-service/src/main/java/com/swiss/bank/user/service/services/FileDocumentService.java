package com.swiss.bank.user.service.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.FileDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface FileDocumentService {

	Mono<FileDocument> saveDocument(FileDocument build);

	Mono<FileDocument> findDocumentByid(String documentId);

	Flux<FileDocument> fetchFileDocumentsByIdList(List<String> documentsIds);

	Mono<Map<String, FileDocument>> fetchFileDocumentsByTitleIdMap(Map<String, String> titleDocumentIdMap);

}
