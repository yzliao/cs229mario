package edu.stanford.cs229.agents;

import java.util.ArrayList;
import java.util.List;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Abstract representation of the game environment.
 * @author kunyi@stanford.edu
 */
public class MarioState {
  
  private static final int MARIO_X = 9;
  private static final int MARIO_Y = 9;
  
  public final List<Field> fields = new ArrayList<Field>();

  // 0-small, 1-big, 2-fire.
  private Int marioMode = new Int("m", 2);
  private int lastMode = 2;
  
  private Int marioDirection = new Int("Dir", 7);
  private float marioX = 0;
  private float marioY = 0;
  
  private Int stuck = new Int("!!", 1);
  private int stuckCount = 0;
  
  private Int onGround = new Int("g", 1);
  private Int canJump = new Int("j", 1);

  private Int collisionsWithCreatures = new Int("C", 1);
  private int lastCollisionsWithCreatures = 0;
  
  private BitArray enemies = new BitArray("e", 6);
  private int enemiesCount = 0;
  private int lastEnemiesCount = 0;
  
  // Whether enemies are killed in this frame.
  private Int enemiesKilledByStomp = new Int("ks", 1);
  private Int enemiesKilledByFire = new Int("kf", 1);
  private int killsByFire = 0;
  private int killsByStomp = 0;

  // [4 bits] Whether obstacles are in front of Mario.
  //   | 3
  //   | 2 
  // M | 1
  //   | 0
  private BitArray obstacles = new BitArray("o", 3);

  // [2 bits] Whether there are gaps under or in front of Mario.
  //  M |
  // ---|---
  //  0 | 1
  //BitArray gaps = new BitArray(2);
  
  private Int win = new Int("W", 1);
  
  private int stateNumber = 0;
  private Environment environment;
  private byte[][] scene;

  private int dDistance = 0;
  private int dElevation = 0;
  private int lastElevation = 0;
  private int lastDistance = 0;
  
  /**
   * Updates the state with the given Environment.
   */
  public void update(Environment environment) {

    this.environment = environment;
    this.scene = environment.getMergedObservationZZ(1, 1);
    
    // Update distance and elevation.
    int distance = environment.getEvaluationInfo().distancePassedPhys;
    dDistance = distance - lastDistance;
    if (Math.abs(dDistance) <= LearningParams.MIN_MOVE_DISTANCE) {
      dDistance = 0;
    }
    lastDistance = distance;
    
    int elevation = Math.max(0,
        getDistanceToGround(MARIO_X - 1) - getDistanceToGround(MARIO_X));
    dElevation = Math.max(0, elevation - lastElevation);
    lastElevation = elevation;
    
    // *******************************************************************
    // Update state params.
    marioMode.value = environment.getMarioMode();
    
    float[] pos = environment.getMarioFloatPos();
    marioDirection.value = getDirection(pos[0] - marioX, pos[1] - marioY);
    marioX = pos[0];
    marioY = pos[1];
    
    if (dDistance == 0) {
      stuckCount += 1;
    } else {
      stuckCount = 0;
      stuck.value = 0;
    }
    if (stuckCount >= LearningParams.MAX_NUM_STUCK_FRAMES) {
      stuck.value = 1;
    }
    
    collisionsWithCreatures.value =
        environment.getEvaluationInfo().collisionsWithCreatures -
        lastCollisionsWithCreatures;
    lastCollisionsWithCreatures =
        environment.getEvaluationInfo().collisionsWithCreatures;

    // Fill can jump.
    ///*
    canJump.value =
        (!environment.isMarioOnGround() || environment.isMarioAbleToJump())
        ? 1 : 0;
    //*/
    
    onGround.value = environment.isMarioOnGround() ? 1 : 0;
    
    // Fill enemy info.
    ///*
    enemiesCount = 0;
    enemies.reset();
    int start = MARIO_X - LearningParams.OBSERVATION_SIZE / 2;
    int end = MARIO_X + LearningParams.OBSERVATION_SIZE / 2 + 1;
    for (int y = 0; y < scene.length; y++) {
      for (int x = 0; x < scene.length; x++) {
        if (scene[y][x] == Sprite.KIND_GOOMBA ||
            scene[y][x] == Sprite.KIND_SPIKY) {
          if (x >= start && x < end && y >= start && y < end) {
            enemies.value[getRegion(x, y)] = true;
            enemiesCount += 1;
          }
        }
      }
    }
    
    // Fill killed info.
    if (enemiesCount < lastEnemiesCount) {
      enemiesKilledByFire.value = environment.getKillsByFire() - killsByFire;
      enemiesKilledByStomp.value = environment.getKillsByStomp() - killsByStomp;
    }
    lastEnemiesCount = 0;
    killsByFire = environment.getKillsByFire();
    killsByStomp = environment.getKillsByStomp();
    //*/
    
    // Fill obstacle info.
    obstacles.reset();
    for (int y = 0; y < obstacles.value.length; y++) {
      if (isObstacle(MARIO_X + 1, MARIO_Y - y + 1)) {
        obstacles.value[y] = true;
      }
    }
    
    // Fill gap info.
    /*gaps.reset();
    for (int i = 0; i < gaps.length; i++) {
      gaps[i] = getDistanceToGround(scene, MARIO_X + i) < 0;
    }*/
    
    // Win?
    win.value = environment.getMarioStatus() == Mario.STATUS_WIN ? 1 : 0;
    
    this.computeStateNumber();
    
    if (LearningParams.DEBUG) {
      System.out.println(this);
    }
  }
  
  public int calculateReward() {
    int reward =
        (marioMode.value - lastMode) * LearningParams.REWARD_PARAMS.mode +
        stuck.value * LearningParams.REWARD_PARAMS.stuck +
        collisionsWithCreatures.value * LearningParams.REWARD_PARAMS.collision +
        // Reward for making forward and upward progress.
        dDistance * LearningParams.REWARD_PARAMS.distance +
        dElevation * LearningParams.REWARD_PARAMS.elevation +
        // Give reward for enemy kills.
        enemiesKilledByFire.value * LearningParams.REWARD_PARAMS.killedByFire +
        enemiesKilledByStomp.value * LearningParams.REWARD_PARAMS.killedByStomp +
        // Give reward for Princes and winning.
        //princes.value * LearningParams.REWARD_PARAMS.princes +
        win.value * LearningParams.REWARD_PARAMS.win;
    
    reward -= LearningParams.TIME_PENALTY;
    
    if (LearningParams.DEBUG) {
      System.out.println("D: " + dDistance);
      System.out.println("H:" + dElevation);
      System.out.println("Reward = " + reward);
    }
    
    lastMode = marioMode.value;
    
    return reward;
  }
  
  public boolean canJump() {
    return environment.isMarioAbleToJump();
  }
  
  /**
   * Returns a unique number to identify each different state.
   */
  public int getStateNumber() {
    return stateNumber;
  }
  
  private void computeStateNumber() {
    stateNumber = 0;
    int i = 0;
    for (Field field : fields) {
      stateNumber += field.getInt() << i;
      i += field.getNBits();
    }
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Field field : fields) {
      if (first) {
        first = false;
      } else {
        sb.append(" | ");
      }
      sb.append(field.toString());
    }
    return sb.toString();
  }
  
  public static String printStateNumber(int state) {
    StringBuilder sb = new StringBuilder("[]");
    /*int n = 0;
    for (Field field : FIELDS) {
      n += field.getNBits();
    }
    for (Field field : FIELDS) {
      n -= field.getNBits();
      
    }*/
    return sb.toString();
  }

  private int getRegion(int x, int y) {
    if (x < 9) {
      return Region.BEHIND;
    } else if (x == 9) {
      if (y < 9) {
        return Region.ABOVE;
      } else {
        return Region.BELOW;
      }
    } else if (y == 9 || (marioMode.value > 0 && y == 10)) {
      return Region.FRONT;
    } else if (y > 9) {
      return Region.FRONT_ABOVE;
    } else {
      return Region.FRONT_BELOW;
    }
  }
  
  /**
   * Computes the distance from Mario to the ground. This method will return -1
   * if there's no ground below Mario.
   */
  private int getDistanceToGround(int x) {
    for (int y = MARIO_Y + 1; y < scene.length; y++) {
      if (isGround(x, y)) {
        return Math.min(3, y - MARIO_Y - 1);
      }
    }
    return -1;
  }
  
  private boolean isPrinces(int x, int y) {
    return scene[y][x] == GeneralizerLevelScene.PRINCESS;
  }
  
  private boolean isObstacle(int x, int y) {
    switch(scene[y][x]) {
      case GeneralizerLevelScene.BRICK:
      case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
      case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
      case GeneralizerLevelScene.LADDER:
        return true;
    }
    return false;
  }
  
  private boolean isGround(int x, int y) {
    return isObstacle(x, y) || scene[y][x] == GeneralizerLevelScene.BORDER_HILL;
  }
  
  public static class Direction {
    public static final int NONE = 0;
    public static final int UP = 1;
    public static final int UP_RIGHT = 2;
    public static final int RIGHT = 3;
    public static final int DOWN_RIGHT = 4;
    public static final int DOWN = 5;
    public static final int DOWN_LEFT = 6;
    public static final int LEFT = 7;
    public static final int UP_LEFT = 8;
  }
  
  private static final float DIRECTION_THRESHOLD = 0.8f;
  
  private int getDirection(float dx, float dy) {
    if (Math.abs(dx) < DIRECTION_THRESHOLD) {
      dx = 0;
    }
    if (Math.abs(dy) < DIRECTION_THRESHOLD) {
      dy = 0;
    }
    
    if (dx == 0 && dy > 0) {
      return Direction.UP;
    } else if (dx > 0 && dy > 0) {
      return Direction.UP_RIGHT;
    } else if (dx > 0 && dy == 0) {
      return Direction.RIGHT;
    } else if (dx > 0 && dy < 0) {
      return Direction.DOWN_RIGHT;
    } else if (dx == 0 && dy < 0) {
      return Direction.DOWN;
    } else if (dx < 0 && dy < 0) {
      return Direction.DOWN_LEFT;
    } else if (dx < 0 && dy == 0) {
      return Direction.LEFT;
    } else if (dx < 0 && dy > 0) {
      return Direction.UP_LEFT;
    }
    return Direction.NONE;
  }
  
  /**
   * Screen is divided into 5 regions as shown below. For each region, we set
   * whether an enemy is present in that region.
   *  _______________________________
   * |        |       |   s          |
   * |        | Above | Front Above |
   * |        |_______|_____________|
   * | Behind | Mario |   Front     |
   * |        |_______|_____________|
   * |        | Below | Front Below |
   * |________|_______|_____________|
   */
  public static class Region {
    public static final int ABOVE = 0;
    public static final int FRONT_ABOVE = 1;
    public static final int FRONT = 2;
    public static final int FRONT_BELOW = 3;
    public static final int BELOW = 4;
    public static final int BEHIND = 5;
  }
  
  public abstract class Field {
    String name;
    public Field(String name) {
      this.name = name;
      fields.add(this);
    }
    
    @Override
    public String toString() {
      return String.format("%s: %s", name, getValueToString());
    }
    
    public abstract String getValueToString();
    public abstract int getNBits();
    public abstract int getInt();
  }
  
  public class BitArray extends Field {
    boolean[] value;
    
    public BitArray(String name, int n) {
      super(name);
      value = new boolean[n];
    }
    
    @Override
    public int getNBits() {
      return value.length;
    }
    
    @Override
    public int getInt() {
      int decInt = 0;
      for (int i = 0; i < value.length; i++) {
        decInt <<= 1;
        decInt += value[i] ? 1 : 0;
      }
      return decInt;
    }

    @Override
    public String getValueToString() {
      return Utils.printArray(value);
    }
    
    private void reset() {
      for (int i = 0; i < value.length; i++) {
        value[i] = false;
      }
    }
  }
  
  public class Int extends Field {
    int value;
    // Maximum possible value of this integer.
    private final int max;
    
    public Int(String name, int max) {
      super(name);
      this.max = max;
    }
    
    @Override
    public int getNBits() {
      return (int)Math.ceil(Math.log(max + 1) / Math.log(2));
    }
    
    @Override
    public int getInt() {
      value = Math.max(0, Math.min(max, value));
      return value;
    }
    
    @Override
    public String getValueToString() {
      return String.valueOf(value);
    }
  }
  
  public static void main(String[] argv) {
    MarioState state = new MarioState();
    state.marioMode.value = 2;
    state.canJump.value = 1;
    state.onGround.value = 1;
    state.obstacles.value[0] = true;
    state.obstacles.value[1] = true;
    state.obstacles.value[2] = true;
    state.computeStateNumber();
    System.out.println(state.getStateNumber());
  }
}
