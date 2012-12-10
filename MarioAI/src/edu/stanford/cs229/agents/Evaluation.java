package edu.stanford.cs229.agents;

import java.util.Date;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
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
  
  private MarioAIOptions marioAIOptions;
  private LearningTask learningTask;
  private LearningAgent learningAgent;
  
  public Evaluation(
      MarioAIOptions marioAIOptions, LearningAgent learningAgent) {
    this.marioAIOptions = marioAIOptions;
    this.learningAgent = learningAgent;
    this.learningTask = new LearningTask(marioAIOptions);

    learningAgent.setLearningTask(learningTask);
    learningAgent.init();
    
    train();
  }
  
  private void train() {
    //marioAIOptions.setEnemies("off");

    learningAgent.setEvaluationQuota(
        LearningParams.NUM_TRAINING_ITERATIONS /
        LearningParams.NUM_MODES_TO_TRAIN / LearningParams.NUM_SEEDS_TO_TRAIN);

    for (int i = 0; i < LearningParams.NUM_MODES_TO_TRAIN; i++) {
      marioAIOptions.setMarioMode(i);
      learningAgent.learn();
      for (int j = 1; j < LearningParams.NUM_SEEDS_TO_TRAIN; j++) {
        marioAIOptions.setLevelRandSeed(getSeed());
        learningAgent.learn();
      }
    }
  }

  public int getSeed() {
    return (int)(new Date().getTime() / 1000);
  }

  public float evaluate() {
    Agent agent = learningAgent.getBestAgent();

    marioAIOptions.setVisualization(true);
    marioAIOptions.setAgent(agent);
    
    BasicTask basicTask = new BasicTask(marioAIOptions);
    
    Logger.println(0, "*************************************************");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*              Starting Evaluation              *");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*************************************************");
    
    System.out.println("Task = " + basicTask);
    System.out.println("Agent = " + agent);

    int averageScore = 0;
    for (int i = 0; i < LearningParams.NUM_EVAL_ITERATIONS; i++) {
      // Set to a different seed for evaluation.
      if (LearningParams.USE_DIFFERENT_SEED_FOR_EVAL) {
        marioAIOptions.setLevelRandSeed(getSeed());
        //basicTask.setOptionsAndReset(marioAIOptions);
      }
         
      // Make evaluation on the same episode once.
      if (!basicTask.runSingleEpisode(1)) {
        System.err.println("MarioAI: out of computational time per action!");
        System.exit(0);
      }

      EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();
      int score = evaluationInfo.computeWeightedFitness();

      System.out.println("Intermediate SCORE = " + score + ";");
      System.out.println("Details: " + evaluationInfo.toString());
      
      averageScore += score;
    }
    
    averageScore =
        (int)((1.0 * averageScore) / LearningParams.NUM_EVAL_ITERATIONS);
    System.out.println("Average: " + averageScore);

    return averageScore;
  }
  
  public static void main(String[] args) {
    MarioAIOptions marioAIOptions = new MarioAIOptions(args);
    LearningAgent learningAgent = (LearningAgent) marioAIOptions.getAgent();
    System.out.println("Learning Agent = " + learningAgent);

    float finalScore = new Evaluation(marioAIOptions, learningAgent).evaluate();
    
    System.out.println("Final Score = " + finalScore);
    System.exit(0);
  }
}
