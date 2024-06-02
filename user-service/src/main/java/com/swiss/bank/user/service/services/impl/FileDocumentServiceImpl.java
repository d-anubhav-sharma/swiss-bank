package com.swiss.bank.user.service.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.FileDocument;
import com.swiss.bank.user.service.repositories.FileDocumentRepository;
import com.swiss.bank.user.service.services.FileDocumentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FileDocumentServiceImpl implements FileDocumentService {

	FileDocumentRepository fileDocumentRepository;

	FileDocumentServiceImpl(FileDocumentRepository fileDocumentRepository) {
		this.fileDocumentRepository = fileDocumentRepository;
	}

	@Override
	public Mono<FileDocument> saveDocument(FileDocument fileDocument) {
		return fileDocumentRepository.save(fileDocument);
	}

	@Override
	public Mono<FileDocument> findDocumentByid(String documentId) {
		return fileDocumentRepository.findById(documentId);
	}

	@Override
	public Flux<FileDocument> fetchFileDocumentsByIdList(List<String> documentsIds) {
		return fileDocumentRepository.findAllById(documentsIds);
	}

	@Override
	public Mono<Map<String, FileDocument>> fetchFileDocumentsByTitleIdMap(Map<String, String> titleDocumentIdMap) {
		Map<String, Mono<FileDocument>> fileDocumentMap = new HashMap<>();
		titleDocumentIdMap
			.entrySet()
			.forEach(titleDocumentEntry -> fileDocumentMap.put(titleDocumentEntry.getKey(),
				fileDocumentRepository.findById(titleDocumentEntry.getValue())));
		return Flux.fromIterable(fileDocumentMap.entrySet())
                .flatMap(entry -> entry.getValue().map(fileDocument -> Map.entry(entry.getKey(), fileDocument)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);

	}

}
