package io.jenkins.plugins.sample;

import hudson.model.Action;

/**
 * Created by king on 2018/6/7.
 */
public class HelloWorldAction implements Action
{

    private String name;

    public HelloWorldAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Greeting";
    }

    @Override
    public String getUrlName() {
        return "greeting";
    }
}
