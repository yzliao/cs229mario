package edu.stanford.cs229.agents;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;

/**
 * Class to evaluate performance.
 * 
 * @author zheyang@stanford.edu (Zhe Yang)
 */
public class Evaluation {
  
  public static enum Mode {
    DEBUG,
    DEMO,
    EVAL;
    
    static Mode getMode(String mode) {
      for (Mode m : Mode.values()) {
        if (m.name().equalsIgnoreCase(mode)) {
          return m;
        }
      }
      return Mode.DEMO;
    }
  }
  
  private Mode mode;
  
  private MarioAIOptions marioAIOptions;
  private MarioRLAgent agent;
  
  private float averageScore = 0;
  private float wins = 0;
  private float averageKills = 0;
  private float averageDistance = 0;
  private float averageTimeSpent = 0;
  
  public Evaluation(Mode mode) {
    this.mode = mode;
    
    agent = new MarioRLAgent();
    
    marioAIOptions = new MarioAIOptions();
    marioAIOptions.setAgent(agent);
    marioAIOptions.setVisualization(false);
    marioAIOptions.setFPS(24);
    
    agent.setOptions(marioAIOptions);
    agent.setLearningTask(new LearningTask(marioAIOptions));
  }
  
  private void reset() {
    averageScore = 0;
    wins = 0;
    averageKills = 0;
    averageDistance = 0;
    averageTimeSpent = 0;
  }

  public float evaluate(String dumpFilename) {
    if (mode == Mode.DEBUG) {
      marioAIOptions.setVisualization(true);
      LearningParams.DEBUG = 2;
    }
    
    reset();
    
    agent.learn();

    if (mode == Mode.DEMO) {
      marioAIOptions.setVisualization(true);
    }

    BasicTask basicTask = new BasicTask(marioAIOptions);
    
    Logger.println(0, "*************************************************");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*              Starting Evaluation              *");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*************************************************");
    
    System.out.println("Task = " + basicTask);
    System.out.println("Agent = " + agent);

    for (int i = 0; i < LearningParams.NUM_EVAL_ITERATIONS; i++) {
      // Set to a different seed for evaluation.
      if (LearningParams.USE_DIFFERENT_SEED_FOR_EVAL) {
        marioAIOptions.setLevelRandSeed(Utils.getSeed());
      }
         
      // Make evaluation on the same episode once.
      int failedCount = 0;
      while (!basicTask.runSingleEpisode(1)) {
        System.err.println("MarioAI: out of computational time per action?");
        failedCount++;
        if (failedCount >= 3) {
          System.err.println("Exiting.. =(");
          System.exit(0);
        }
      }

      EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();
      accumulateEvalInfo(evaluationInfo);

      System.out.println(evaluationInfo.toString());
    }
    
    computeFinalEvalInfo(dumpFilename);
    return averageScore;
  }
  
  private void computeFinalEvalInfo(String dumpFilename) {
    averageScore /= LearningParams.NUM_EVAL_ITERATIONS;
    wins /= LearningParams.NUM_EVAL_ITERATIONS;
    averageKills /= LearningParams.NUM_EVAL_ITERATIONS;
    averageDistance /= LearningParams.NUM_EVAL_ITERATIONS;
    averageTimeSpent /= LearningParams.NUM_EVAL_ITERATIONS;
    
    Utils.dump(dumpFilename, String.format("%f\n%f\n%f\n%f\n%f",
        averageScore, wins, averageKills, averageDistance, averageTimeSpent));
  }
  
  private void accumulateEvalInfo(EvaluationInfo evaluationInfo) {
    averageScore += evaluationInfo.computeWeightedFitness();
    wins += evaluationInfo.marioStatus == Mario.STATUS_WIN ? 1 : 0;
    averageKills += 1.0 *
        evaluationInfo.killsTotal / evaluationInfo.totalNumberOfCreatures;
    averageDistance += 1.0 *
        evaluationInfo.distancePassedCells / evaluationInfo.levelLength;
    averageTimeSpent += evaluationInfo.timeSpent;
  }
  
  public static String getParam(String[] args, String name) {
    for (int i = 0; i < args.length; i++) {
      String s = args[i];
      if (s.startsWith("-") && s.substring(1).equals(name)) {
        if (i + 1 < args.length) {
          String v = args[i + 1];
          if (!v.startsWith("-")) {
            return v;
          }
        }
        return "";
      }
    }
    return null;
  }
  
  public static boolean isNullOrEmpty(String v) {
    return v == null || v.isEmpty();
  }
  
  public static int getIntParam(String[] args, String name, int defaultValue) {
    String v = getParam(args, name);
    return isNullOrEmpty(v) ? defaultValue : Integer.valueOf(v);
  }
  
  public static boolean getBooleanParam(String[] args, String name) {
    String v = getParam(args, name);
    return v != null;
  }

  public static void main(String[] args) {
    Mode mode = Mode.getMode(getParam(args, "m"));
    int numRounds = getIntParam(args, "n", 1);

    LearningParams.NUM_MODES_TO_TRAIN =
        getIntParam(args, "nm", LearningParams.NUM_MODES_TO_TRAIN);
    LearningParams.NUM_SEEDS_TO_TRAIN =
        getIntParam(args, "ns", LearningParams.NUM_SEEDS_TO_TRAIN);
    LearningParams.NUM_TRAINING_ITERATIONS =
        getIntParam(args, "i", LearningParams.NUM_TRAINING_ITERATIONS);
    LearningParams.NUM_EVAL_ITERATIONS =
        getIntParam(args, "ei", LearningParams.NUM_EVAL_ITERATIONS);
    
    LearningParams.LOAD_QTABLE = getBooleanParam(args, "l");
    
    Evaluation eval = new Evaluation(mode);

    for (int i = 0; i < numRounds; i++) {
      System.out.println("~ Round " + i + " ~");
      float finalScore = eval.evaluate(String.format("eval.%d.txt", i));
      System.out.println("Final Score = " + finalScore + "\n");
    }

    System.exit(0);
  }
}
