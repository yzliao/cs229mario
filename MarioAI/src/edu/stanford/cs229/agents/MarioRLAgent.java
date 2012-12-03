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

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;

public class MarioRLAgent implements LearningAgent {

  private static final int ATTACK = 0;
  private static final int GO = 1;
  private static final int AVOID = 2;
  private static final int COLLECT = 3;
  private static final int SEARCH = 4;

  // Evaluation task and quota
  LearningTask learningTask;
  
  int learningQuota;
  
  // Fields for the Mario Agent
  public MarioState currentState;

  // Associated Qtable for the agent. Used for RL training.
  public ActionQtable actionTable;

  // Evaluation info for previous tick. Used to calculate reward
  EvaluationInfo prevEva = new EvaluationInfo();

  // The type of phase the Agent is in.
  // INIT: initial phase
  // LEARN: accumulatively update the Qtable
  enum Phase {
    INIT, LEARN
  };

  Phase currentPhase;

  public String name;

  // Constructor
  public MarioRLAgent() {
    setName("Super Mario 229");
    
    currentState = new MarioState();

    /*
     * Number of different key combinations: there are 6 keys. "UP" not used, so
     * 2^5 = 32 key combinations 0: left 1: right 2: down 3: jump 4: speed and
     * the index of one action in the table is simple the decimal representation
     */
    int numActions = 32;
    actionTable = new ActionQtable(numActions);
  }

  private boolean getBit(int number, int i) {
    return (number & (1 << i)) != 0;
  }
  
  @Override
  public boolean[] getAction() {
    // Transforms the best action number to action array.
    int actionNumber = actionTable.getNextAction(currentState.getStateNumber());
    boolean[] actionToReturn = new boolean[6];
    actionToReturn[Mario.KEY_LEFT] = getBit(actionNumber, 0);
    actionToReturn[Mario.KEY_RIGHT] = getBit(actionNumber, 1);
    actionToReturn[Mario.KEY_JUMP] = getBit(actionNumber, 2);
    actionToReturn[Mario.KEY_SPEED] = getBit(actionNumber, 3);
    actionToReturn[Mario.KEY_UP] = false;
    actionToReturn[Mario.KEY_DOWN] = false;
    return actionToReturn;
  }

  /*
   * Importance of this function: the scene observation is THE RESULT after
   * performing some action given the previous state. Therefore we could get
   * informaion on: 1. prev state x prev action -> current state 2. get the
   * reward for prev state, prev action pair
   * 
   * the reward function, however, is not provided and has to be customized
   */
  @Override
  public void integrateObservation(Environment environment) {
    // update the current state
    currentState.update(environment);
    // update the Qvalue entry in the Qtable
    actionTable.updateQvalue(
        calculateReward(environment), currentState.getStateNumber());
  }

  // calculate the reward got from prev and current environmets
  private int calculateReward(Environment currentEnv) {
    if (currentPhase == Phase.INIT) {
      currentPhase = Phase.LEARN;
      return 0;
    } else {
      // First try: reward is given as the distance traveled.
      EvaluationInfo currentEva = currentEnv.getEvaluationInfo();

      int reward =
          - LearningParams.TIME_PENALTY +
          currentEva.computeWeightedFitness(LearningParams.REWARD_PARAMS) -
          prevEva.computeWeightedFitness(LearningParams.REWARD_PARAMS);
      
      prevEva = currentEnv.getEvaluationInfo().clone();

      if (LearningParams.DEBUG) {
        System.out.println("Reward = " + reward + "\n");
      }

      return reward;
    }
  }

  /*
   * calculate or estimate how the state changes if a certain action is
   * performed could be hard.
   */
  private int getNextState(int actionNumber) {
    return currentState.getStateNumber();
  }

  @Override
  public void learn() {
    System.out.println(learningQuota + " is the learning quota.");
    int numIterationsToTrain =
        Math.min(LearningParams.NUM_TRAINING_ITERATIONS, learningQuota);
    for (int i = 0; i < numIterationsToTrain; ++i) {
      System.out.println("\n---------------------------------");
      System.out.println("Trial: " + i);

      learningTask.runSingleEpisode(1);

      EvaluationInfo evaluationInfo =
          learningTask.getEnvironment().getEvaluationInfo();
      System.out.println(
          "Intermediate SCORE = " + evaluationInfo.computeWeightedFitness() +
          "\nDetails: " + evaluationInfo.toStringSingleLine());
    }
  }

  @Override
  // The first episode run initializes the agent.
  public void newEpisode() {
    switch (currentPhase) {
      case INIT:
        currentPhase = Phase.LEARN;
        break;
    }
  }

  /**
   *  Gives access to the evaluator through learningTask.evaluate(Agent).
   */
  @Override
  public void setLearningTask(LearningTask learningTask) {
    // TODO Auto-generated method stub
    this.learningTask = learningTask;
  }

  /**
   * Defines the max number of trials to learn. Currently it is 100000.
   */
  @Override
  public void setEvaluationQuota(long num) {
    // TODO Auto-generated method stub
    this.learningQuota = (int)num;
  }

  @Override
  public Agent getBestAgent() {
    return this;
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

  @Override
  public void reset() {
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

  @Override
  public void init() {
  }
}
