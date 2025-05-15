package com.engine.network.states;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class TestNetObject implements INetObject {
  public String name;
  public int num;

  public TestNetObject() {
      this.name = "";
      this.num = 0;
  }

  public TestNetObject(String name, int id) {
      this.name = name;
      this.num = id;
  }

  @Override
  public void deserialize(DataInputStream dataSegments) throws Exception {
    this.name = dataSegments.readUTF();
    this.num = dataSegments.readInt();
  }

  @Override
  public void serialize(DataOutputStream dataSegments) throws Exception {
    dataSegments.writeUTF(this.name);
    dataSegments.writeInt(this.num);
  }

  public String toString() {
    return "TestNetObject{" +
        "name='" + name + '\'' +
        ", num=" + num +
        '}';
  }
}