package cotuba;

public class Orchestrator {
  public void execute(Cli cli) {
    var htmlRender = new HtmlRender();
    var chapters = htmlRender.render(cli.getMarkdownPath());

    var ebook = new Ebook();
    ebook.setFormat(cli.getFormat());
    ebook.setOutputPath(cli.getOutputPath());
    ebook.setChapters(chapters);

    if ("pdf".equals(cli.getFormat())) {
      var pdfGenerator = new PdfGenerator();
      pdfGenerator.generate(ebook);
    } else if ("epub".equals(cli.getFormat())) {
      var epubGenerator = new EpubGenerator();
      epubGenerator.generate(ebook);
    } else {
      throw new IllegalArgumentException("Invalid ebook format: " + cli.getFormat());
    }
  }
}
