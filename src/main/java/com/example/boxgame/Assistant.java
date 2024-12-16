package com.example.boxgame;

import java.io.*;

public class Assistant {
    String inputFilename = "exe/map.txt";
    String outputFilename = "exe/output.txt";
    public void solve(String data){
        //Write data to file
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(inputFilename));
            bw.write(data);
            bw.newLine();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Start exe
        String[] cmd = {"cmd", "/c", "call", "exe\\solver_dfs.exe",">",outputFilename};
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Read data from file
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(outputFilename));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
