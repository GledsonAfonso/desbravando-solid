package cotuba.md;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import cotuba.domain.Chapter;

public class HtmlRender {
  public List<Chapter> render(Path markdownPath) {
    return this.getMarkdownPaths(markdownPath)
        .stream()
        .map(markdownFile -> {
          var nodeVisitor = new NodeVisitor();
          var document = this.getDocument(markdownFile, nodeVisitor);
          var html = this.getHtml(markdownFile, document);

          var chapter = nodeVisitor.getChapter();
          chapter.setHtml(html);

          return chapter;
        })
        .toList();
  }

  private List<Path> getMarkdownPaths(Path directoryPath) {
    try {
      var matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
      return Files.list(directoryPath).filter(matcher::matches).sorted().toList();
    } catch (IOException exception) {
      throw new IllegalStateException(
          "Error while trying to find the markdown files in " + directoryPath.toAbsolutePath(), exception);
    }
  }

  private Node getDocument(Path markdownFile, Visitor visitor) {
    try {
      var parser = Parser.builder().build();
      var document = parser.parseReader(Files.newBufferedReader(markdownFile));
      document.accept(visitor);

      return document;
    } catch (Exception exception) {
      throw new IllegalStateException("Error while trying to parse the file " + markdownFile, exception);
    }
  }

  private String getHtml(Path markdownFile, Node document) {
    try {
      var renderer = HtmlRenderer.builder().build();
      return renderer.render(document);
    } catch (Exception exception) {
      throw new IllegalStateException("Error while trying to render the file to HTML. File " + markdownFile,
          exception);
    }
  }
}
