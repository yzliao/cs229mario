package edu.stanford.cs229.agents;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Abstract representation of the game environment.
 * @author kunyi@stanford.edu
 */
public class MarioState {

  /**
   * Screen is divided into 5 regions as shown below. For each region, we set
   * whether an enemy is present in that region.
   *  ______________________________
   * |        |       |             |
   * |        | Above | Front Above |
   * |        |_______|_____________|
   * | Behind | Mario |   Front     |
   * |        |_______|_____________|
   * |        |       Below         |
   * |________|_____________________|
   */
  public static class Region {
    public static final int ABOVE = 0;
    public static final int FRONT_ABOVE = 1;
    public static final int FRONT = 2;
    public static final int BELOW = 3;
    public static final int BEHIND = 4;
  }

  // [2 bits] 0-small, 1-big, 2-fire.
  int marioMode;
  // [2 bits] = 0~3.
  int jumpHeight;

  // [5 bits] Whether enemies are present in each region.
  boolean[] enemies = new boolean[5];
  
  // [4 bits] Whether obstacles are in front of Mario.
  //   | 3
  //   | 2 
  //   | 1
  // M | 0
  boolean[] obstacles = new boolean[4];
  
  // [2 bits] Whether there are gaps under or in front of Mario.
  //  M |
  // ---|---
  //  0 | 1
  boolean[] gaps = new boolean[2];
  
  // Total: 15 bits.
  
  private static final int MARIO_X = 9;
  private static final int MARIO_Y = 9;

  /**
   * Helper method to convert boolean array to an integer in binary
   * representation. The first element is treated as most significant bit.
   */
  private int array2Int(boolean[] array) {
    int decInt = 0;
    for (int i = 0; i < array.length; i++) {
      decInt *= 2;
      decInt += array[i] ? 1 : 0;
    }
    return decInt;
  }

  /**
   * Returns a unique number to identify each different state.
   */
  public int getStateNumber() {
    return
        marioMode +
        (jumpHeight << 2) +
        (array2Int(enemies) << 4) +
        (array2Int(obstacles) << 9) +
        (array2Int(gaps) << 13);
  }

  private int getRegion(int x, int y) {
    if (x < 9) {
      return Region.BEHIND;
    } else if (y > 9) {
      return Region.BELOW;
    } else if (x == 9) {
      return Region.ABOVE;
    } else if (y == 9) {
      return Region.FRONT;
    } else {
      return Region.FRONT_ABOVE;
    }
  }
  
  /**
   * Computes the distance from Mario to the ground. This method will return -1
   * if there's no ground below Mario.
   */
  private int getDistanceToGround(byte[][] scene, int x) {
    for (int y = MARIO_Y + 1; y < scene.length; y++) {
      if (isGround(scene, x, y)) {
        return y - MARIO_Y - 1;
      }
    }
    return -1;
  }
  
  private String printArray(boolean[] array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(" ");
      }
      sb.append(array[i] ? "1" : "0");
    }
    return sb.toString();
  }
  
  private boolean isObstacle(byte[][] scene, int x, int y) {
    switch(scene[y][x]) {
      case 0:
      case GeneralizerLevelScene.COIN_ANIM:
      case GeneralizerLevelScene.PRINCESS:
      case GeneralizerLevelScene.BORDER_HILL:
        return false;
    }
    return true;
  }
  
  private boolean isGround(byte[][] scene, int x, int y) {
    return isObstacle(scene, x, y) ||
        scene[y][x] == GeneralizerLevelScene.BORDER_HILL;
  }
  
  private void resetArray(boolean[] array) {
    for (int i = 0; i < array.length; i++) {
      array[i] = false;
    }
  }

  /**
   * Updates the state with the given Environment.
   */
  public void update(Environment environment) {

    marioMode = environment.getMarioMode();

    byte[][] scene = environment.getLevelSceneObservationZ(1);
    byte[][] enemiesState = environment.getEnemiesObservationZ(1);
    
    // Fill jump height.
    jumpHeight = -1;
    for (int x = MARIO_X; x >= 0 && jumpHeight < 0; x--) {
      jumpHeight = getDistanceToGround(scene, x);
    }
    if (LearningParams.DEBUG) {
      System.out.println("j: " + jumpHeight);
    }
    
    // Fill enemy info.
    boolean hasEnemy = false;
    resetArray(enemies);
    int start = MARIO_X - LearningParams.OBSERVATION_SIZE / 2;
    int end = MARIO_X + LearningParams.OBSERVATION_SIZE / 2 + 1;
    for (int y = start; y < end; y++) {
      for (int x = start; x < end; x++) {
        if (enemiesState[y][x] == Sprite.KIND_GOOMBA ||
            enemiesState[y][x] == Sprite.KIND_SPIKY) {
          enemies[getRegion(x, y)] = true;
          hasEnemy = true;
        }
      }
    }
    if (LearningParams.DEBUG && hasEnemy) {
      System.out.println("e: " + printArray(enemies));
    }
    
    // Fill obstacle info.
    boolean hasObstacle = false;
    resetArray(obstacles);
    for (int y = 0; y < obstacles.length; y++) {
      if (isObstacle(scene, MARIO_X + 1, MARIO_Y - y)) {
        obstacles[y] = true;
        hasObstacle = true;
      }
    }
    if (LearningParams.DEBUG && hasObstacle) {
      System.out.println("o: " + printArray(obstacles));
    }

    // Fill gap info.
    resetArray(gaps);
    for (int i = 0; i < gaps.length; i++) {
      gaps[i] = getDistanceToGround(scene, MARIO_X + i) < 0;
    }
  }
}
