package com.vnindie.glocoapp.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Doctor extends RealmObject {
  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("avatar_url")
  private String avatarUrl;
  @SerializedName("age")
  private int age;

  public Doctor() {
    // Realm require this
  }

  public Doctor(int id, String name, String avatarUrl, int age) {
    this.id = id;
    this.name = name;
    this.avatarUrl = avatarUrl;
    this.age = age;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}
