package com.example.ersapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class ApiClient {

    public void sendCompleteData(List<ClassEmployee> groups) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"groups\": [\n");

        for (int i = 0; i < groups.size(); i++) {
            ClassEmployee group = groups.get(i);
            jsonBuilder.append("    {\n");
            /*
            if (group.getId() != null) {
                jsonBuilder.append("      \"id\": ").append(group.getId()).append(",\n");
            }
            */
            jsonBuilder.append("      \"employeeGroupName\": \"").append(escapeJson(group.getEmployeeGroupName())).append("\",\n");
            jsonBuilder.append("      \"maxEmployeeNumber\": ").append(group.getMaxEmployeeNumber()).append(",\n");
            jsonBuilder.append("      \"fillPercentage\": ").append(group.getFillPercentage()).append(",\n");

            // Dodajemy pracowników
            jsonBuilder.append("      \"employees\": [\n");
            List<Employee> employees = group.getEmployeeList();
            for (int j = 0; j < employees.size(); j++) {
                Employee emp = employees.get(j);
                jsonBuilder.append("        {\n");
                //jsonBuilder.append("          \"id\": ").append(emp.getId()).append(",\n");
                jsonBuilder.append("          \"firstName\": \"").append(escapeJson(emp.getFirstName())).append("\",\n");
                jsonBuilder.append("          \"lastName\": \"").append(escapeJson(emp.getLastName())).append("\",\n");
                jsonBuilder.append("          \"condition\": \"").append(emp.getCondition()).append("\",\n");
                jsonBuilder.append("          \"birthYear\": ").append(emp.getBirthYear()).append(",\n");
                jsonBuilder.append("          \"salary\": ").append(emp.getSalary()).append("\n");
                jsonBuilder.append("        }");
                if (j < employees.size() - 1) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("      ],\n");

            // Dodajemy oceny
            jsonBuilder.append("      \"rates\": [\n");
            List<Rate> rates = group.getRates();
            if (rates != null) {
                for (int k = 0; k < rates.size(); k++) {
                    Rate rate = rates.get(k);
                    jsonBuilder.append("        {\n");
                    jsonBuilder.append("          \"grade\": ").append(rate.getGrade()).append(",\n");
                    jsonBuilder.append("          \"date\": \"").append(rate.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\",\n");
                    jsonBuilder.append("          \"comment\": \"").append(escapeJson(rate.getComment() != null ? rate.getComment() : "")).append("\",\n");
                    jsonBuilder.append("          \"groupId\": ").append(group.getId()).append("\n");  // bez przecinka!
                    jsonBuilder.append("        }");
                    if (k < rates.size() - 1) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
            }
            jsonBuilder.append("      ],\n");

            // Dodajemy średnią ocen
            if (rates != null && !rates.isEmpty()) {
                jsonBuilder.append("      \"averageGrade\": ").append(group.getAverageGrade()).append("\n");
            } else {
                jsonBuilder.append("      \"averageGrade\": null\n");
            }

            jsonBuilder.append("    }");
            if (i < groups.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("  ]\n");
        jsonBuilder.append("}");

        String json = jsonBuilder.toString();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/data/complete"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    System.out.println("Complete data sent successfully!");
                    System.out.println("Server response: " + response);
                })
                .exceptionally(e -> {
                    System.err.println("Error sending complete data: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                });
    }

    // Pomocnicza metoda do escapowania znaków w JSON
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Metoda do wysyłania tylko pracowników
    public void sendEmployees(List<Employee> employees) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"employees\": [\n");

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            jsonBuilder.append("    {\n");
            jsonBuilder.append("      \"id\": ").append(emp.getId()).append(",\n");
            jsonBuilder.append("      \"firstName\": \"").append(escapeJson(emp.getFirstName())).append("\",\n");
            jsonBuilder.append("      \"lastName\": \"").append(escapeJson(emp.getLastName())).append("\",\n");
            jsonBuilder.append("      \"condition\": \"").append(emp.getCondition()).append("\",\n");
            jsonBuilder.append("      \"birthYear\": ").append(emp.getBirthYear()).append(",\n");
            jsonBuilder.append("      \"salary\": ").append(emp.getSalary()).append(",\n");
            jsonBuilder.append("      \"groupId\": ").append(emp.getGroup() != null ? emp.getGroup().getId() : "null").append("\n");
            jsonBuilder.append("    }");
            if (i < employees.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("  ]\n");
        jsonBuilder.append("}");

        sendJsonData(jsonBuilder.toString(), "http://localhost:8080/api/employees/bulk", "Employees data");
    }

    // Metoda do wysyłania tylko grup
    public void sendGroups(List<ClassEmployee> groups) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"groups\": [\n");

        for (int i = 0; i < groups.size(); i++) {
            ClassEmployee group = groups.get(i);
            jsonBuilder.append("    {\n");
            jsonBuilder.append("      \"id\": ").append(group.getId()).append(",\n");
            jsonBuilder.append("      \"employeeGroupName\": \"").append(escapeJson(group.getEmployeeGroupName())).append("\",\n");
            jsonBuilder.append("      \"maxEmployeeNumber\": ").append(group.getMaxEmployeeNumber()).append(",\n");
            jsonBuilder.append("      \"currentEmployeeCount\": ").append(group.getEmployeeNumber()).append(",\n");
            jsonBuilder.append("      \"fillPercentage\": ").append(group.getFillPercentage()).append("\n");
            jsonBuilder.append("    }");
            if (i < groups.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("  ]\n");
        jsonBuilder.append("}");

        sendJsonData(jsonBuilder.toString(), "http://localhost:8080/api/groups/bulk", "Groups data");
    }

    // Metoda do wysyłania tylko ocen
    public void sendRates(List<Rate> rates) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");
        jsonBuilder.append("  \"rates\": [\n");

        for (int i = 0; i < rates.size(); i++) {
            Rate rate = rates.get(i);
            jsonBuilder.append("    {\n");
            jsonBuilder.append("      \"id\": ").append(rate.getId()).append(",\n");
            jsonBuilder.append("      \"grade\": ").append(rate.getGrade()).append(",\n");
            jsonBuilder.append("      \"date\": \"").append(rate.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\",\n");
            jsonBuilder.append("      \"comment\": \"").append(escapeJson(rate.getComment() != null ? rate.getComment() : "")).append("\",\n");
            jsonBuilder.append("      \"groupId\": ").append(rate.getGroup() != null ? rate.getGroup().getId() : "null").append(",\n");
            jsonBuilder.append("      \"groupName\": \"").append(escapeJson(rate.getGroupName())).append("\"\n");
            jsonBuilder.append("    }");
            if (i < rates.size() - 1) {
                jsonBuilder.append(",");
            }
            jsonBuilder.append("\n");
        }

        jsonBuilder.append("  ]\n");
        jsonBuilder.append("}");

        sendJsonData(jsonBuilder.toString(), "http://localhost:8080/api/rates/bulk", "Rates data");
    }

    // Wspólna metoda do wysyłania danych JSON
    private void sendJsonData(String json, String url, String dataType) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    System.out.println(dataType + " sent successfully!");
                    System.out.println("Server response: " + response);
                })
                .exceptionally(e -> {
                    System.err.println("Error sending " + dataType.toLowerCase() + ": " + e.getMessage());
                    e.printStackTrace();
                    return null;
                });
    }
}