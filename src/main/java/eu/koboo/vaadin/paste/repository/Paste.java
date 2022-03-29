package eu.koboo.vaadin.paste.repository;

import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Paste {

  @Id
  @EqualsAndHashCode.Include
  @NotNull
  private final String pasteId;

  @NotNull
  private final String text;

  @NotNull
  private final AceTheme theme;

  @NotNull
  private final String createdAt;

}
