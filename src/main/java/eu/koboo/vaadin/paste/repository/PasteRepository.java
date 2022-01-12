package eu.koboo.vaadin.paste.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasteRepository extends MongoRepository<Paste, String> {

}
