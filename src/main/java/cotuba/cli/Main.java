package cotuba.cli;

import cotuba.application.Orchestrator;

public class Main {
  public static void main(String[] args) {
    boolean verboseModeEnabled = false;

    try {
      var cli = new Cli(args);
      verboseModeEnabled = cli.isVerboseModeEnabled();

      var orchestrator = new Orchestrator();
      orchestrator.execute(cli);

      System.out.println("File created with success: " + cli.getOutputPath());

    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      if (verboseModeEnabled) {
        exception.printStackTrace();
      }
      System.exit(1);
    }
  }
}
