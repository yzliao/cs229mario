/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of the Mario AI nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.scenarios.champ;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.MarioAIOptions;
import edu.stanford.cs229.agents.LearningParams;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey at idsia dot ch
 * Date: Mar 17, 2010 Time: 8:34:17 AM
 * Package: ch.idsia.scenarios
 */

/**
 * Class used for agent evaluation in Learning track
 * http://www.marioai.org/learning-track
 */

public class LearningTrack {
  final static long numberOfTrials = 1000;
  final static boolean scoring = false;
  final static int populationSize = 100;

  private static int evaluateSubmission(
      MarioAIOptions marioAIOptions, LearningAgent learningAgent) {
 
    // Provides the level.
    LearningTask learningTask = new LearningTask(marioAIOptions);
    // Limits the number of evaluations per run for LearningAgent.
    learningAgent.setEvaluationQuota(LearningTask.getEvaluationQuota());
    // Gives LearningAgent access to evaluator via method
    // LearningTask.evaluate(Agent).
    learningAgent.setLearningTask(learningTask);
    learningAgent.init();
    
    // Launches the training process. numberOfTrials happen here.
    //marioAIOptions.setEnemies("off");
    //marioAIOptions.setMarioInvulnerable(true);
    //marioAIOptions.setMarioMode(0);
    //learningAgent.learn();
    //marioAIOptions.setLevelRandSeed((int)(new Date().getTime() / 1000));
    //marioAIOptions.setMarioMode(1);
    //learningAgent.learn();
    marioAIOptions.setMarioMode(2);
    learningAgent.learn();

    Agent agent = learningAgent.getBestAgent(); // this agent will be evaluated
    System.out.println("LearningTrack best agent = " + agent);

    // Perform the gameplay task on the same level.
    marioAIOptions.setVisualization(true);
    marioAIOptions.setAgent(agent);
    
    // Set to a different seed for eval.
    marioAIOptions.setArgs("-ls 7801");
    
    BasicTask basicTask = new BasicTask(marioAIOptions);
    basicTask.setOptionsAndReset(marioAIOptions);
    System.out.println("basicTask = " + basicTask);
    System.out.println("agent = " + agent);

    boolean verbose = true;

    System.out.println("\n****************************************\n");
    System.out.println("Starting evaluation!!");

    int averageScore = 0;
    for (int i = 0; i < LearningParams.NUM_EVAL_ITERATIONS; i++) {
      // Make evaluation on the same episode once.
      if (!basicTask.runSingleEpisode(1)) {
        System.out.println("MarioAI: out of computational time per action! Agent disqualified!");
        System.exit(0);
      }

      EvaluationInfo evaluationInfo = basicTask.getEvaluationInfo();
      int f = evaluationInfo.computeWeightedFitness();

      if (verbose) {
        System.out.println("Intermediate SCORE = " + f + ";\n Details: " + evaluationInfo.toString());
      }
      averageScore += f;
    }
    
    averageScore =
        (int)((1.0 * averageScore) / LearningParams.NUM_EVAL_ITERATIONS);
    System.out.println("Average: " + averageScore);

    return averageScore;
  }

  public static void main(String[] args) {
    // set up parameters
    MarioAIOptions marioAIOptions = new MarioAIOptions(args);
    // read agent from commadline
    LearningAgent learningAgent = (LearningAgent) marioAIOptions.getAgent();
    System.out.println("main.learningAgent = " + learningAgent);

    marioAIOptions.setMarioMode(2);
    marioAIOptions.setLevelDifficulty(0);
    marioAIOptions.setDeadEndsCount(true);
    marioAIOptions.setTubesCount(true);
    marioAIOptions.setBlocksCount(true);
    marioAIOptions.setGapsCount(false);
    marioAIOptions.setCannonsCount(false);
    marioAIOptions.setGreenMushroomMode(0);
//        Level 0
    float finalScore = LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);

//        Level 1
//    marioAIOptions = new MarioAIOptions(args);
//    marioAIOptions.setAgent(learningAgent);
//    marioAIOptions.setArgs("-lco off -lb on -le off -lhb off -lg on -ltb on -lhs off -lca on -lde on -ld 5 -ls 133829");
//    finalScore += LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);

//        Level 2
//    marioAIOptions = new MarioAIOptions(args);
//    marioAIOptions.setArgs("-lde on -i on -ld 30 -ls 133434");
//    finalScore += LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);
//
////        Level 3
//    marioAIOptions = new MarioAIOptions(args);
//    marioAIOptions.setArgs("-lde on -i on -ld 30 -ls 133434 -lhb on");
//    finalScore += LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);
//
////        Level 4
//    marioAIOptions = new MarioAIOptions(args);
//    marioAIOptions.setArgs("-lla on -le off -lhs on -lde on -ld 5 -ls 1332656");
//    finalScore += LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);
//
//    // Level 5 (bonus level)
//    marioAIOptions = new MarioAIOptions(args);
//    marioAIOptions.setArgs("-le off -lhs on -lde on -ld 5 -ls 1332656");
//    finalScore += LearningTrack.evaluateSubmission(marioAIOptions, learningAgent);

    System.out.println("finalScore = " + finalScore);
    System.exit(0);
  }
}
