package eu.koboo.vaadin.paste.repository;

import eu.koboo.vaadin.paste.utility.Date;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasteService {

  private static final Logger logger = Logger.getLogger(PasteService.class.getName());

  @Getter
  PasteRepository repository;

  @Scheduled(fixedRate = 86_400_000)
  public void execute() {
    LocalDate now = LocalDate.now();
    int deletion = 0;
    int total = 0;
    for (Paste paste : repository.findAll()) {
      total += 1;
      LocalDate validUntilDate = LocalDate.parse(paste.getCreatedAt(), Date.FORMATTER).plusDays(7);
      if(validUntilDate.isAfter(now)) {
        repository.delete(paste);
        deletion += 1;
      }
    }
    logger.log(Level.INFO, "clear routine deleted " + deletion + " of " + total + " total pastes..");
  }

}
