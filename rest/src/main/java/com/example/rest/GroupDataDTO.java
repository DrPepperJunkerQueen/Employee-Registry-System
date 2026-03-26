package com.example.rest;

import com.example.rest.ClassEmployee;
import java.util.List;

public class GroupDataDTO {

    private List<ClassEmployee> groups;

    public List<ClassEmployee> getGroups() {
        return groups;
    }

    public void setGroups(List<ClassEmployee> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "GroupDataDTO{" +
                "groups=" + groups +
                '}';
    }
}
