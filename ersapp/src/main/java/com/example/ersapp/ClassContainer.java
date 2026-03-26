package com.example.piec;

import java.util.*;

public class ClassContainer
{
    private Map<String, ClassEmployee>  employeeGroups;

    public ClassContainer(Map<String, ClassEmployee> employeeGroups)
    {
        if(employeeGroups == null)
        {
            this.employeeGroups = new HashMap<>();
        }

        this.employeeGroups = employeeGroups;
    }

    public void addClass(String employeeGroupName, double employeeGroupCapacity)
    {
        employeeGroups.put(employeeGroupName, new ClassEmployee(employeeGroupName, (int)employeeGroupCapacity, new ArrayList<Employee>()));
    }

    public void removeClass(String employeeGroupName)
    {
        employeeGroups.remove(employeeGroupName);
    }

    public List<String> findEmpty() {
        List<String> emptyGroups = new ArrayList<>();
        for (Map.Entry<String, ClassEmployee> entry : employeeGroups.entrySet())
        {
            if (entry.getValue().isEmpty())
            {
                emptyGroups.add(entry.getKey());
            }
        }
        return emptyGroups;
    }

    public void summary()
    {
        for (Map.Entry<String, ClassEmployee> entry : employeeGroups.entrySet())
        {
            String groupName = entry.getKey();
            double fillPercent = entry.getValue().getFillPercentage();
            System.out.println("Group: " + groupName + "\nFill percentage: " + fillPercent + "%");
        }
    }
}
