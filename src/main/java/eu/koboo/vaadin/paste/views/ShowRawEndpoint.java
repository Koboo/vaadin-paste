package eu.koboo.vaadin.paste.views;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import eu.koboo.vaadin.paste.repository.Paste;
import eu.koboo.vaadin.paste.repository.PasteService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AnonymousAllowed
public class ShowRawEndpoint {

  @Autowired
  PasteService service;

  @RequestMapping(path = "raw", method = RequestMethod.GET)
  public ResponseEntity<String> showRawPaste(@RequestParam(name = "p") String pasteId) {

    Optional<Paste> optional = service.getRepository().findById(pasteId);
    if (!optional.isPresent()) {
      return ResponseEntity
          .notFound()
          .build();
    }
    Paste paste = optional.get();
    String text = new String(Base64.getDecoder().decode(paste.getText()), StandardCharsets.UTF_8);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(text.length())
        .contentType(MediaType.TEXT_PLAIN)
        .body(text);
  }

}
