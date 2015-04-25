package com.wilson.tasker.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SCENE_ACTIVITY.
 */
public class SceneActivity {

    private Long id;
    private java.util.Date time;
    private String sceneName;
    private Integer actionType;

    public SceneActivity() {
    }

    public SceneActivity(Long id) {
        this.id = id;
    }

    public SceneActivity(Long id, java.util.Date time, String sceneName, Integer actionType) {
        this.id = id;
        this.time = time;
        this.sceneName = sceneName;
        this.actionType = actionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getTime() {
        return time;
    }

    public void setTime(java.util.Date time) {
        this.time = time;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

}