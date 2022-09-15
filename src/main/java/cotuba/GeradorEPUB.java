package cotuba;

import java.io.IOException;
import java.nio.file.Files;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class GeradorEPUB {
  public void gera(Ebook ebook) {
    var epub = new Book();
    var arquivoDeSaida = ebook.getArquivoDeSaida();

    for (var capitulo : ebook.getCapitulos()) {
      var html = capitulo.getConteudoHTML();
      var titulo = capitulo.getTitulo();
      epub.addSection(titulo, new Resource(html.getBytes(), MediatypeService.XHTML));
    };

    var epubWriter = new EpubWriter();

    try {
      epubWriter.write(epub, Files.newOutputStream(arquivoDeSaida));
    } catch (IOException ex) {
      throw new IllegalStateException("Erro ao criar arquivo EPUB: " + arquivoDeSaida.toAbsolutePath(), ex);
    }
  }
}
