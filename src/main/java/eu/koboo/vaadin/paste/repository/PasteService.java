package eu.koboo.vaadin.paste.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasteService {

  @Getter
  PasteRepository repository;

}
