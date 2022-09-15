package cotuba;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {
  private Path markdownPath;
  private String format;
  private Path outputPath;
  private boolean verboseModeEnabled;

  public Cli(String args[]) {
    try {
      verboseModeEnabled = false;
      var options = new Options();

      var markdownPathOption = new Option("d", "dir", true,
          "Directory where the markdown files are located. Default: current directory.");
      options.addOption(markdownPathOption);

      var ebookFormatOption = new Option("f", "format", true,
          "Ebook output format. It can be: pdf or epub. Default: pdf");
      options.addOption(ebookFormatOption);

      var outputPathOption = new Option("o", "output", true,
          "Ebook output path. Default: book.{format}.");
      options.addOption(outputPathOption);

      var verboseModeOption = new Option("v", "verbose", false,
          "Enables verbose mode.");
      options.addOption(verboseModeOption);

      CommandLineParser cmdParser = new DefaultParser();
      var help = new HelpFormatter();
      CommandLine cmd;

      try {
        cmd = cmdParser.parse(options, args);
      } catch (ParseException exception) {
        System.err.println(exception.getMessage());
        help.printHelp("cotuba", options);
        System.exit(1);
        return;
      }

      String markdownDirectory = cmd.getOptionValue("dir");

      if (markdownDirectory != null) {
        markdownPath = Paths.get(markdownDirectory);
        if (!Files.isDirectory(markdownPath)) {
          throw new IllegalArgumentException(markdownDirectory + " is not a directory.");
        }
      } else {
        markdownPath = Paths.get("");
      }

      String ebookFormat = cmd.getOptionValue("format");

      if (ebookFormat != null) {
        format = ebookFormat.toLowerCase();
      } else {
        format = "pdf";
      }

      String ebookOutputPath = cmd.getOptionValue("output");
      if (ebookOutputPath != null) {
        outputPath = Paths.get(ebookOutputPath);
      } else {
        outputPath = Paths.get("book." + format.toLowerCase());
      }
      if (Files.isDirectory(outputPath)) {
        // delete files in the directory recursively
        Files.walk(outputPath).sorted(Comparator.reverseOrder())
            .map(Path::toFile).forEach(File::delete);
      } else {
        Files.deleteIfExists(outputPath);
      }

      verboseModeEnabled = cmd.hasOption("verbose");
    } catch (IOException exception) {
      throw new IllegalArgumentException(exception);
    }
  }

  public Path getMarkdownPath() {
    return markdownPath;
  }

  public String getFormat() {
    return format;
  }

  public Path getOutputPath() {
    return outputPath;
  }

  public boolean isVerboseModeEnabled() {
    return verboseModeEnabled;
  }
}
