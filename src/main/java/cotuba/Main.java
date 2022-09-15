package cotuba;

import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {
    Path markdownPath;
    String format;
    Path outputPath;
    boolean verboseModeEnabled = false;

    try {
      var cli = new Cli(args);
      markdownPath = cli.getMarkdownPath();
      format = cli.getFormat();
      outputPath = cli.getOutputPath();
      verboseModeEnabled = cli.isVerboseModeEnabled();

      var render = new HtmlRender();
      var chapters = render.render(markdownPath);

      var ebook = new Ebook();
      ebook.setFormat(format);
      ebook.setOutputPath(outputPath);
      ebook.setChapters(chapters);

      if ("pdf".equals(format)) {
        var pdfGenerator = new PdfGenerator();
        pdfGenerator.generate(ebook);
      } else if ("epub".equals(format)) {
        var epubGenerator = new EpubGenerator();
        epubGenerator.generate(ebook);
      } else {
        throw new IllegalArgumentException("Invalid ebook format: " + format);
      }

      System.out.println("File created with success: " + outputPath);

    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      if (verboseModeEnabled) {
        exception.printStackTrace();
      }
      System.exit(1);
    }
  }
}
