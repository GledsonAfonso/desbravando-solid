package cotuba.md;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Text;

import cotuba.domain.Chapter;

public class NodeVisitor extends AbstractVisitor {
  private Chapter chapter;

  public NodeVisitor() {
    super();
    this.chapter = new Chapter();
  }

  @Override
  public void visit(Heading heading) {
    if (heading.getLevel() == 1) {
      String title = ((Text) heading.getFirstChild()).getLiteral();
      chapter.setTitle(title);
    } else if (heading.getLevel() == 2) {// section
    } else if (heading.getLevel() == 3) {// title
    }
  }

  public Chapter getChapter() {
    return chapter;
  }
}
