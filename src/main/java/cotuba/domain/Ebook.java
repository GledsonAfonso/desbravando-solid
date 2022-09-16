package cotuba.domain;

import java.nio.file.Path;
import java.util.List;

public class Ebook {
  private String format;
  private Path outputPath;
  private List<Chapter> chapters;

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public Path getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(Path outputPath) {
    this.outputPath = outputPath;
  }

  public List<Chapter> getChapters() {
    return chapters;
  }

  public void setChapters(List<Chapter> chapters) {
    this.chapters = chapters;
  }

  public boolean isLastChapter(Chapter chapter) {
    return this.chapters.get(this.chapters.size() - 1).equals(chapter);
  }
}
