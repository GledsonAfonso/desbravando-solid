package cotuba.epub;

import java.io.IOException;
import java.nio.file.Files;

import cotuba.domain.Ebook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class EpubGenerator {
  public void generate(Ebook ebook) {
    var epub = new Book();
    var outputPath = ebook.getOutputPath();

    for (var chapter : ebook.getChapters()) {
      var html = chapter.getHtml();
      var title = chapter.getTitle();
      epub.addSection(title, new Resource(html.getBytes(), MediatypeService.XHTML));
    };

    var epubWriter = new EpubWriter();

    try {
      epubWriter.write(epub, Files.newOutputStream(outputPath));
    } catch (IOException exception) {
      throw new IllegalStateException("Error while trying to create the EPUB file: " + outputPath.toAbsolutePath(), exception);
    }
  }
}
