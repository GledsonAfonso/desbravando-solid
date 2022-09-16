package cotuba.cli;

import cotuba.application.Orchestrator;

public class Main {
  public static void main(String[] args) {
    boolean verboseMode = false;

    try {
      var cli = new Cli(args);
      verboseMode = cli.isVerboseMode();

      var orchestrator = new Orchestrator();
      orchestrator.execute(cli);

      System.out.println("File created with success: " + cli.getOutputPath());

    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      if (verboseMode) {
        exception.printStackTrace();
      }
      System.exit(1);
    }
  }
}
