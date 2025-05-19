package com.engine.network.states;

public enum ControlMode {
  SERVER (1),
  CLIENT (2),
  BOTH (3);

  private final int value;

  public int value() {
    return value;
  }

  public static ControlMode from(int value) {
    for (ControlMode mode : ControlMode.values()) {
      if (mode.value == value) {
        return mode;
      }
    }
    return null;
  }

  ControlMode(int i) {
    this.value = i;
  }
}
