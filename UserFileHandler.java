import java.io.*;
import java.util.*;
import javax.swing.*;

public class UserFileHandler {

    private static final File userFile = new File("users.txt");

    // ----------------- User Handling -----------------
    public static boolean idExists(String id) {
        if (!userFile.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) return true;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error checking ID!");
        }
        return false;
    }

    public static boolean saveUser(String id, String pass, String name, String hall, String dept, String batch, String season, String mobile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write(id + "," + pass + "," + name + "," + hall + "," + dept + "," + batch + "," + season + "," + mobile);
            bw.newLine();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error saving data!");
            return false;
        }
    }

    public static String validateLogin(String id, String pass) {
        if (!userFile.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id) && data[1].equals(pass)) {
                    return data[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserName(String id) {
        if(!userFile.exists()) return "";
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while((line=br.readLine()) != null) {
                String[] d = line.split(",");
                if(d[0].equals(id)) return d[2];
            }
        } catch(Exception e) { e.printStackTrace(); }
        return "";
    }

    public static List<String[]> getAllUsers() {
        List<String[]> list = new ArrayList<>();
        if(!userFile.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while((line = br.readLine()) != null){
                String[] d = line.split(",");
                list.add(new String[]{d[0], d[2]});
            }
        } catch(Exception e){ e.printStackTrace(); }
        return list;
    }

    // ----------------- Password Reset Methods -----------------
    public static boolean validateUserForReset(String id, String mobile) {
        if(!userFile.exists()) return false;
        id = id.trim();
        mobile = mobile.trim();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length < 8) continue; // minimum 8 fields: id, pass, name, hall, dept, batch, season, mobile
                String fileId = data[0].trim();
                String fileMobile = data[7].trim(); // Mobile field at index 7
                if(fileId.equals(id) && fileMobile.equals(mobile)) {
                    return true;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static boolean updatePassword(String id, String newPass) {
        if(!userFile.exists()) return false;
        File tempFile = new File("users_temp.txt");
        boolean updated = false;

        try(BufferedReader br = new BufferedReader(new FileReader(userFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(data[0].equals(id)){
                    data[1] = newPass; // update password only
                    updated = true;
                }
                bw.write(String.join(",", data));
                bw.newLine();
            }

        } catch(Exception e){ e.printStackTrace(); return false; }

        if(updated){
            if(!userFile.delete()) return false;
            if(!tempFile.renameTo(userFile)) return false;
        } else {
            tempFile.delete();
        }
        return updated;
    }

    // ----------------- Habit Handling -----------------
    
    // Save habit for a user on a date (overwrite if same date exists)
    public static void saveHabit(String userId, String date, Map<String,Integer> habitData) {
        File habitFile = new File("habits/" + userId + "_habits.txt");
        habitFile.getParentFile().mkdirs();
        Map<String,String> allLines = new LinkedHashMap<>();
        
        // Load existing lines
        if(habitFile.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(habitFile))) {
                String line;
                while((line = br.readLine()) != null) {
                    String[] parts = line.split(",",2);
                    allLines.put(parts[0], parts.length>1?parts[1]:"");
                }
            } catch(Exception e){ e.printStackTrace(); }
        }

        // Convert habitData to line string
        StringBuilder sb = new StringBuilder();
        for(String habit : habitData.keySet()){
            sb.append(habit).append(":").append(habitData.get(habit)).append(",");
        }
        if(sb.length()>0) sb.setLength(sb.length()-1); // remove last comma
        allLines.put(date, sb.toString()); // overwrite or add

        // Write all lines back
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(habitFile,false))) {
            for(String d : allLines.keySet()){
                bw.write(d + (allLines.get(d).isEmpty() ? "" : "," + allLines.get(d)));
                bw.newLine();
            }
        } catch(Exception e){ e.printStackTrace(); }
    }

    // Load habit for a specific date
    public static Map<String,Integer> loadHabit(String userId, String date) {
        Map<String,Integer> data = new LinkedHashMap<>();
        File habitFile = new File("habits/" + userId + "_habits.txt");
        if(!habitFile.exists()) return data;

        try(BufferedReader br = new BufferedReader(new FileReader(habitFile))) {
            String line;
            while((line=br.readLine()) != null){
                String[] parts = line.split(",",2);
                if(parts[0].equals(date)){
                    if(parts.length>1){
                        String[] kvs = parts[1].split(",");
                        for(String kv : kvs){
                            String[] arr = kv.split(":");
                            if(arr.length==2){
                                data.put(arr[0], Integer.parseInt(arr[1]));
                            }
                        }
                    }
                    break;
                }
            }
        } catch(Exception e){ e.printStackTrace(); }
        return data;
    }
    
    public static void addHabit(String userId, String habitName, int goal) {
        // Example implementation: append habit info to a file or store in a map
        // You should replace this with your actual storage logic
        System.out.println("Habit added for user " + userId + ": " + habitName + " (Goal: " + goal + ")");
    }

    // Load last N days habits with date (missing days filled with 0)
    public static List<Map<String,Object>> readHabitsWithDate(String userId, int lastDays){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Map<String,Integer>> allData = new LinkedHashMap<>();
        File habitFile = new File("habits/" + userId + "_habits.txt");
        if(habitFile.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(habitFile))){
                String line;
                while((line=br.readLine()) != null){
                    String[] parts = line.split(",",2);
                    Map<String,Integer> map = new LinkedHashMap<>();
                    if(parts.length>1){
                        String[] kvs = parts[1].split(",");
                        for(String kv : kvs){
                            String[] arr = kv.split(":");
                            if(arr.length==2) map.put(arr[0], Integer.parseInt(arr[1]));
                        }
                    }
                    allData.put(parts[0], map);
                }
            } catch(Exception e){ e.printStackTrace(); }
        }

        // Generate lastDays dates
        java.time.LocalDate today = java.time.LocalDate.now();
        for(int i=lastDays-1;i>=0;i--){
            java.time.LocalDate d = today.minusDays(i);
            String dateStr = d.toString();
            Map<String,Integer> map = allData.getOrDefault(dateStr,new LinkedHashMap<>());
            Map<String,Object> entry = new LinkedHashMap<>();
            entry.put("date", dateStr);
            entry.put("habits", map);
            list.add(entry);
        }
        return list;
    }

   @SuppressWarnings("unchecked")
   public static Map<String, Map<String, Integer>> loadHabitsWithDate(String uid) {
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        List<Map<String,Object>> list = readHabitsWithDate(uid, 7); // default last 7 days
        for(Map<String,Object> entry : list){
            String date = (String) entry.get("date");
            Map<String,Integer> habits = (Map<String,Integer>) entry.get("habits");
            result.put(date, habits);
        }
        return result;
    }
}