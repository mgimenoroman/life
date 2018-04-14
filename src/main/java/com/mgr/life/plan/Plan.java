package com.mgr.life.plan;

public class Plan {

    private String name;

    public Plan() {
    }

    Plan(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public Plan setName(String name) {
        this.name = name;
        return this;
    }
}
