package ch.idsia.agents.controllers;
import java.util.Random;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.tools.EvaluationInfo;


public class RandomLearningAgent extends RandomAgent implements LearningAgent{

  boolean[] action;
  
  public RandomLearningAgent()
  {
      super();
      reset();
  }

  private Random R = null;

  public void reset()
  {
      // Dummy reset, of course, but meet formalities!
      R = new Random();
  }
  


    
    @Override
    public boolean[] getAction() {
      boolean[] ret = new boolean[Environment.numberOfKeys];

      for (int i = 0; i < Environment.numberOfKeys; ++i)
      {
          // Here the RandomAgent is encouraged to move more often to the Right and make long Jumps.
          boolean toggleParticularAction = R.nextBoolean();
          toggleParticularAction = (i == 0 && toggleParticularAction && R.nextBoolean()) ? R.nextBoolean() : toggleParticularAction;
          toggleParticularAction = (i == 1 || i > 3 && !toggleParticularAction) ? R.nextBoolean() : toggleParticularAction;
          toggleParticularAction = (i > 3 && !toggleParticularAction) ? R.nextBoolean() : toggleParticularAction;
//              toggleParticularAction = (i == 4 && !toggleParticularAction ) ? R.nextBoolean() :  toggleParticularAction;
          ret[i] = toggleParticularAction;
      }
      if (ret[1])
          ret[0] = false;
      return ret;
    }

    @Override
    public void integrateObservation(Environment environment) {
      // TODO Auto-generated method stub
      action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = 
            environment.isMarioAbleToJump() || !environment.isMarioOnGround();
      
    }

    @Override
    public void giveIntermediateReward(float intermediateReward) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow,
        int egoCol) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public String getName() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public void setName(String name) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void learn() {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void giveReward(float reward) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void newEpisode() {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void setLearningTask(LearningTask learningTask) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void setEvaluationQuota(long num) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public Agent getBestAgent() {
      // TODO Auto-generated method stub
      return this;
    }

    @Override
    public void init() {
      // TODO Auto-generated method stub
      action = new boolean[6];
      action[Mario.KEY_RIGHT] = true;
    }

  }
