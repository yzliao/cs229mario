/* Some thoughts on the Mario implementation. What is to be gained from training?
 * 1. Should the states be rough and abstract? Yes. How abstract? It should be 
 *    abstract enough that the number of state-action pairs is exceedingly large.
 *    But if it is too abstract, we may lose the approximate Markov property. 
 *    (e.g. if the defined Mario state lacks "Mario's position", then suppose
 *    two original scenes, one with Mario on high platform, the other wiht Mario 
 *    on low platform, and other parameters the same. They have the same abstract
 *    state S. But S x Action A -> undetermined for the two scenes.
 *       With that said, we hope given many trials and a large state space the
 *    effect is not affecting us.
 *  
 * 2. Learning for specific actions (keystrokes) or movement preferences?
 *    Learning for keystrokes seems to be hard, but can be tolerated. Consider we
 *    can first hard-code the preferences, and modify the reward function to "unit
 *    learn" the keystroke combo. For example, we could define first learning unit
 *    to be "advance", and set reward to be large for every step going rightward.
 *    Then we train the "search" unit, etc.
 *      After the units complete, we face the problem that given a scene, what is
 *    the task to carry out. This can be completed using a higher-level QTable, or
 *    simply estimate the reward given by carrying out each task, and pick the
 *    best-rewarded.
 *        I think the latter approach is easier, but possibly contain bugs. Let's see
 *    whether is will become a problem.
 * 
 * 3. How to let Mario advance?
 *    -given a scene, abstract to Mario state
 *    -construct a QTable AdvanceTable, containing State, Action pairs
 *    -each Action is a combination of keystrokes
 *    -the MDP is also learned, not predetermined?
 *    -the reward function: the number of steps rightward taken
 *    -possible problem: how to let Mario jump through gaps, platforms and enemies?
 *        -jump until necessary? could give negative rewards for unnecessary jumps
 *    -the Mario state should contain "complete" information about the scene
 *        -idea: "poles", where the Mario should be jumping off and how far?
 * 
 * */

package edu.stanford.cs229.agents;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;

public class MarioRLAgent implements LearningAgent {

  private String name;
  
  // Evaluation task and quota
  private LearningTask learningTask;
  private int learningQuota;
  
  // Fields for the Mario Agent
  private MarioState currentState;

  // Associated Qtable for the agent. Used for RL training.
  private ActionQtable actionTable;

  // The type of phase the Agent is in.
  // INIT: initial phase
  // LEARN: accumulatively update the Qtable
  private enum Phase {
    INIT, LEARN, EVAL
  };
  private Phase currentPhase = Phase.INIT;
  
  private int learningTrial = 0;
  
  private List<Integer> scores =
      new ArrayList<Integer>(LearningParams.NUM_TRAINING_ITERATIONS);
  
  public MarioRLAgent() {
    setName("Super Mario 229");
    
    currentState = new MarioState();
    actionTable = new ActionQtable(MarioAction.TOTAL_ACTIONS);
    
    if (LearningParams.LOAD_QTABLE) {
      actionTable.loadQtable(LearningParams.FINAL_QTABLE_NAME);
    }
    
    Logger.println(0, "*************************************************");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*                Super Mario 229                *");
    Logger.println(0, "*                 Agent created!                *");
    Logger.println(0, "*                                               *");
    Logger.println(0, "*************************************************");
  }
  
  @Override
  public boolean[] getAction() {
    // Transforms the best action number to action array.
    int actionNumber = actionTable.getNextAction(currentState.getStateNumber());
    
    Logger.println(2, "Next action: " + actionNumber + "\n");
    
    return MarioAction.getAction(actionNumber);
  }

  /**
   * Importance of this function: the scene observation is THE RESULT after
   * performing some action given the previous state. Therefore we could get
   * information on:
   *     1. prev state x prev action -> current state.
   *     2. get the reward for prev state, prev action pair.
   * 
   * The reward function, however, is not provided and has to be customized.
   */
  @Override
  public void integrateObservation(Environment environment) {
    if (currentPhase == Phase.INIT && environment.isMarioOnGround()) {
      // Start learning after Mario lands on the ground.
      Logger.println(1, "============== Learning Phase =============");
      currentPhase = Phase.LEARN;
      currentState.update(environment);
    } else {
      // Update the current state.
      currentState.update(environment);
      
      // Update the Qvalue entry in the Qtable.
      actionTable.updateQvalue(
          currentState.calculateReward(), currentState.getStateNumber());
    }
  }

  @Override
  public void learn() {
    for (int i = 0; i < learningQuota; ++i) {
      Logger.println(1, "================================================");
      Logger.println(0, "Trial: %d.%d", learningTrial, i);

      learningTask.runSingleEpisode(1);

      EvaluationInfo evaluationInfo =
          learningTask.getEnvironment().getEvaluationInfo();
      
      int score = evaluationInfo.computeWeightedFitness();
      Logger.println(1, "Intermediate SCORE = " + score);
      Logger.println(1, evaluationInfo.toStringSingleLine());
      
      scores.add(score);

      // Dump the info of the most visited states into file.
      if (LearningParams.DUMP_INTERMEDIATE_QTABLE) {
        actionTable.dumpQtable(
            String.format(LearningParams.QTABLE_NAME_FORMAT, learningTrial, i));
      }
    }

    learningTrial++;
  }
  
  @Override
  public void reset() {
    if (currentPhase != Phase.EVAL) {
      Logger.println(1, "=================== Init =================");
      currentPhase = Phase.INIT;
    }
    currentState = new MarioState();
  }
  
  private void setUpForEval() {
    Logger.println(1, "=========== Dumping Results ===========");
    // Dump final Qtable.
    actionTable.dumpQtable(LearningParams.FINAL_QTABLE_NAME);
    // Dump training scores.
    dumpScores(LearningParams.SCORES_NAME);
    
    // Entering EVAL phase.
    Logger.println(1, "============== Eval Phase =============");
    currentPhase = Phase.EVAL;
    LearningParams.DEBUG = LearningParams.EVAL_DEBUG;

    // Set learning and exploration chance for evaluations.
    actionTable.learningRate = LearningParams.EVAL_LEARNING_RATE;
    actionTable.explorationChance = LearningParams.EVAL_EXPLORATION_CHANCE;
    
    //LearningParams.DEBUG = 2;
  }
  
  private void dumpScores(String logfile) {
    Logger.println(1, "** Dumping scores to " + logfile + " **");
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(logfile));
      writer.write(Utils.join(scores.toArray(), "\n"));
      writer.close();
    } catch (Exception x) {
      System.err.println("Failed to write scores.");
    }
  }

  /**
   *  Gives access to the evaluator through learningTask.evaluate(Agent).
   */
  @Override
  public void setLearningTask(LearningTask learningTask) {
    this.learningTask = learningTask;
  }

  /**
   * Defines the max number of trials to learn..
   */
  @Override
  public void setEvaluationQuota(long num) {
    this.learningQuota = (int)num;
  }

  @Override
  public Agent getBestAgent() {
    setUpForEval();
    return this;
  }
  
  @Override
  public void setObservationDetails(
      int rfWidth, int rfHeight, int egoRow, int egoCol) {
  }

  @Override
  public String getName() {
    return name;
  }
  

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Deprecated
  @Override
  public void init() {
  }

  @Deprecated
  @Override
  public void newEpisode() {
  }

  @Deprecated
  @Override
  public void giveReward(float reward) {
  }
  
  // This function is completely bogus! intermediateReward is not properly given
  // either modify the intermediate reward calculation or ignore this function
  // and do reward update elsewhere. forexample when integrating observation.
  @Deprecated
  @Override
  public void giveIntermediateReward(float intermediateReward) {
    // TODO Auto-generated method stub
  }
}
