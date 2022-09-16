package cotuba.pdf;

import java.nio.file.Files;
import java.util.List;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.AreaBreakType;

import cotuba.domain.Ebook;

public class PdfGenerator {
  public void generate(Ebook ebook) {
    var outputPath = ebook.getOutputPath();

    try (
        var writer = new PdfWriter(Files.newOutputStream(outputPath));
        var pdf = new PdfDocument(writer);
        var pdfDocument = new Document(pdf)) {

      for (var chapter : ebook.getChapters()) {
        var html = chapter.getHtml();
        List<IElement> convertToElements = HtmlConverter.convertToElements(html);
        for (IElement element : convertToElements) {
          pdfDocument.add((IBlockElement) element);
        }
  
        if (!ebook.isLastChapter(chapter)) {
          pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }
      };
    } catch (Exception exception) {
      throw new IllegalStateException("Error while trying to create the PDF file: " + outputPath.toAbsolutePath(), exception);
    }
  }
}
