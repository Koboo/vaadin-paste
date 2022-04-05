package eu.koboo.vaadin.paste.views;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.f0rce.ace.enums.AceTheme;
import eu.koboo.vaadin.paste.repository.Paste;
import eu.koboo.vaadin.paste.repository.PasteService;
import eu.koboo.vaadin.paste.utility.Date;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AnonymousAllowed
public class CreatePostEndpoint {

  @Autowired
  PasteService service;

  @RequestMapping(path = "post", method = RequestMethod.POST)
  public ResponseEntity<String> postNewPaste(@RequestBody String body) {

    String response;

    if(body == null) {
      return response("No text submitted!");
    }

    if(body.trim().length() == 0 || body.equalsIgnoreCase("")) {
      return response("Submitted text is empty!");
    }

    String id = UUID.randomUUID().toString();
    Paste paste = new Paste(
        id,
        Base64.getEncoder().encodeToString(body.getBytes()),
        AceTheme.cobalt,
        LocalDate.now().format(Date.FORMATTER)
    );
    service.getRepository().save(paste);

    response = paste.getPasteId();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(response.length())
        .contentType(MediaType.TEXT_PLAIN)
        .body(response);
  }

  private ResponseEntity<String> response(String text) {
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
