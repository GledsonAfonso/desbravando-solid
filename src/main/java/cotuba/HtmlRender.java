package cotuba;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class HtmlRender {
  public List<Chapter> render(Path markdownPath) {
    List<Chapter> chapters = new ArrayList<>();

    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
    try (Stream<Path> markdownFiles = Files.list(markdownPath)) {
      markdownFiles
          .filter(matcher::matches)
          .sorted()
          .forEach(markdownFile -> {
            Chapter chapter = new Chapter();
            Parser parser = Parser.builder().build();
            Node document = null;
            try {
              document = parser.parseReader(Files.newBufferedReader(markdownFile));
              document.accept(new AbstractVisitor() {
                @Override
                public void visit(Heading heading) {
                  if (heading.getLevel() == 1) {
                    // chapter
                    String title = ((Text) heading.getFirstChild()).getLiteral();
                    chapter.setTitle(title);
                  } else if (heading.getLevel() == 2) {
                    // section
                  } else if (heading.getLevel() == 3) {
                    // title
                  }
                }

              });
            } catch (Exception exception) {
              throw new IllegalStateException("Error while trying to parse the file " + markdownFile, exception);
            }

            try {
              HtmlRenderer renderer = HtmlRenderer.builder().build();
              String html = renderer.render(document);

              chapter.setHtml(html);
              chapters.add(chapter);
            } catch (Exception exception) {
              throw new IllegalStateException("Error while trying to render the file to HTML. File " + markdownFile, exception);
            }
          });
    } catch (IOException exception) {
      throw new IllegalStateException("Error while trying to find the markdown files in " + markdownPath.toAbsolutePath(), exception);
    }

    return chapters;
  }
}
