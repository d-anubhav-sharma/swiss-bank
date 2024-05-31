package com.swiss.bank.user.service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.swiss.bank.user.service.entities.FileDocument;

public interface FileDocumentRepository extends ReactiveMongoRepository<FileDocument, String> {

}
