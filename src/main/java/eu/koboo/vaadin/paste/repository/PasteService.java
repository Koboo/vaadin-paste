package eu.koboo.vaadin.paste.repository;

import eu.koboo.vaadin.paste.utility.Date;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PasteService {

  private static final Logger logger = Logger.getLogger(PasteService.class.getName());

  @Getter
  PasteRepository repository;

  @Value("${paste.days}")
  private String pasteDays;

  public PasteService(PasteRepository repository) {
    this.repository = repository;
  }

  @Scheduled(fixedRate = 86_400_000)
  public void execute() {
    try {
      logger.log(Level.INFO,
          "clear routine started..");
      int pasteDaysInt = Integer.parseInt(pasteDays);
      LocalDate now = LocalDate.now();
      int deletion = 0;
      int total = 0;
      for (Paste paste : repository.findAll()) {
        total += 1;
        LocalDate validUntilDate = LocalDate.parse(paste.getCreatedAt(), Date.FORMATTER)
            .plusDays(pasteDaysInt);
        if (validUntilDate.isBefore(now)) {
          repository.delete(paste);
          deletion += 1;
        }
      }
      logger.log(Level.INFO,
          "clear routine deleted " + deletion + " of " + total + " total pastes..");
    } catch (NumberFormatException e) {
      logger.log(Level.WARNING, "couldn't execute clear routine. property \"paste.days\" is wrong! (value=\"" + pasteDays + "\", must be integer!");
    }
  }

}
