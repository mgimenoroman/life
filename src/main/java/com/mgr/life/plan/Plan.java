package com.mgr.life.plan;

public class Plan {

    private String name;

    public Plan() {
    }

    public Plan(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Plan setName(String name) {
        this.name = name;
        return this;
    }
}
