package com.team2073.robot.dev;

import java.io.*;

public class GraphCSV {

    private PrintWriter pw;
    private static String pathName = System.getProperty("user.home");
    private StringBuilder sb = new StringBuilder();
    private String fileName;
    private String[] valueNames;

    public GraphCSV(String fileName, String... valueNames) {
        this.fileName = fileName;
        this.valueNames = valueNames;
        try {
            initFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void initFile() throws IOException {
        File f = new File(pathName);
        File fCSV = new File(f, '/' + fileName + ".csv");
        System.out.println(f.getAbsolutePath());
        pw = new PrintWriter(new FileOutputStream(fCSV, false));
        StringBuilder sb = new StringBuilder();
        for (String name : valueNames) {
            sb.append(name + ',');
        }
        sb.append('\n');
        pw.write(sb.toString());
        pw.flush();
    }

    public void updateMainFile(double... values) {
        for (double num : values) {
            sb.append(num + ",");
        }
//        System.out.println(sb.toString());
        sb.append('\n');
    }

    public void writeToFile() {
        System.out.println("WRITING TO FILE");
        pw.write(sb.toString());
        pw.flush();
        System.out.println("WROTE");
    }
}
