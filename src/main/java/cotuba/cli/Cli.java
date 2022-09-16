package cotuba.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {
  private Path markdownPath;
  private String format;
  private Path outputPath;
  private boolean verboseMode;

  public Cli(String args[]) {
    try {
      var options = this.getOptions();
      var cmd = this.getCommandLine(options, args);
      
      this.markdownDirectorySetup(cmd);
      this.setFormat(cmd);
      this.setOutputPath(cmd);
      this.setVerboseMode(cmd);
      this.cleanDirectory();
    } catch (IOException exception) {
      throw new IllegalArgumentException(exception);
    }
  }

  private void setVerboseMode(CommandLine cmd) {
    verboseMode = cmd.hasOption("verbose") ? true : false;
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

  public boolean isVerboseMode() {
    return verboseMode;
  }

  private Options getOptions() {
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

    return options;
  }

  private CommandLine getCommandLine(Options options, String[] args) {
    var cmdParser = new DefaultParser();
    var help = new HelpFormatter();
    CommandLine cmd;

    try {
      cmd = cmdParser.parse(options, args);
    } catch (ParseException exception) {
      help.printHelp("cotuba", options);
      throw new IllegalArgumentException("Invalid option!", exception);
    }

    return cmd;
  }

  private void markdownDirectorySetup(CommandLine cmd) {
    String markdownDirectory = cmd.getOptionValue("dir");

    if (markdownDirectory != null) {
      markdownPath = Paths.get(markdownDirectory);
      if (!Files.isDirectory(markdownPath)) {
        throw new IllegalArgumentException(markdownDirectory + " is not a directory.");
      }
    } else {
      markdownPath = Paths.get("");
    }
  }

  private void setFormat(CommandLine cmd) {
    String ebookFormat = cmd.getOptionValue("format");

    if (ebookFormat != null) {
      this.format = ebookFormat.toLowerCase();
    } else {
      this.format = "pdf";
    }
  }

  private void setOutputPath(CommandLine cmd) {
    String ebookOutputPath = cmd.getOptionValue("output");

    if (ebookOutputPath != null) {
      this.outputPath = Paths.get(ebookOutputPath);
    } else {
      this.outputPath = Paths.get("book." + format.toLowerCase());
    }
  }

  private void cleanDirectory() throws IOException {
    if (Files.isDirectory(this.outputPath)) {
      // delete files in the directory recursively
      Files.walk(this.outputPath).sorted(Comparator.reverseOrder())
          .map(Path::toFile).forEach(File::delete);
    } else {
      Files.deleteIfExists(this.outputPath);
    }
  }
}
