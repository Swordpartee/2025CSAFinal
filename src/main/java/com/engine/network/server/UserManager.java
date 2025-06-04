package com.engine.network.server;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import com.engine.network.encryption.Encryption;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class UserManager {
    private final String CONFIG_PATH = System.getenv("APPDATA") + "\\Java Server\\config.json";
    private final SecretKey CONFIG_KEY = new SecretKeySpec(Base64.getDecoder().decode(System.getenv("AES_ECLIPSE_SECRET_KEY")), "AES");

    private String getConfigContent() throws Exception {
        String content = new String(Encryption.decryptAES(Files.readAllBytes(Paths.get(CONFIG_PATH)), CONFIG_KEY));
        if (content.isEmpty()) {
            System.out.println("No users found!");
            content = "{\"users\": {}}"; // Create an empty users object if the file is empty
            Files.write(Paths.get(CONFIG_PATH), Encryption.encryptAES(content.getBytes(), CONFIG_KEY), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        return content;
    }

    /**
     * Get a list of all of the users stored in the server's config file.
     * @param adminPassword
     * @param adminSessionKey
     * @param sorted
     * @return array of all usernames
     * @throws Exception
     */
    public String[] getUsers(String adminPassword, boolean sorted) throws Exception {
        if (authenticateUser("admin", adminPassword)) {
            String content = getConfigContent();
            
            JSONObject json = new JSONObject(content);
            JSONObject users = json.getJSONObject("users");

            @SuppressWarnings("unchecked")
            Iterator<String> keys = sorted ? users.sortedKeys() : users.keys();

            ArrayList<String> userList = new ArrayList<String>();
            while (keys.hasNext()) {
                userList.add(keys.next());
            }

            return userList.toArray(new String[userList.size()]);
        }
        System.out.println("Not Authorized!");
        return new String[0];
    }

    /**
     * Delete a user from the server's config file.
     * @param username
     * @param password
     * @param sessionKey
     * @return true if the user was deleted successfully, false otherwise 
     * @throws Exception
     */
    public boolean deleteUser(String username, String password) throws Exception {
        if (username.equals("admin")) {
            System.out.println("You cannot delete this user!");
        }

        if (authenticateUser("admin", password) || authenticateUser(username, password)) {
            String content = getConfigContent();
            
            JSONObject json = new JSONObject(content);
            JSONObject users = json.getJSONObject("users");

            users.remove(username);
            Files.write(Paths.get(CONFIG_PATH), Encryption.encryptAES(json.toString(4).getBytes(), CONFIG_KEY),
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("User deleted!");
            return true;
        }

        System.out.println("You aren't authorized to do that!");
        return false;
    }

    /** 
     * Make sure a user is authenticated with the server (allow them to log in).
     * @param username
     * @param password
     * @return true if the user is authenticated successfully, false otherwise
     * @throws Exception
     */
    public boolean authenticateUser(String username, String password) throws Exception {
        String content = getConfigContent();

        JSONObject json = new JSONObject(content);
        JSONObject users = json.getJSONObject("users");

        if (!users.has(username)) {
            System.out.println("User doesn't exist!");
            return false;
        }

        String storedHash = users.getString(username);
        Argon2 argon2 = Argon2Factory.create();

        if (argon2.verify(storedHash, password.getBytes())) {
            return true;
        }

        return false;
    }

    /**
     * Add a user to the server's config file.
     * @param username
     * @param password
     * @return true if the user was added successfully, false otherwise
     * @throws Exception
     */
    public boolean addUser(String username, String password) throws Exception {
        try {
            String content = getConfigContent();
            
            JSONObject json = new JSONObject(content);
            JSONObject users = json.getJSONObject("users");

            // Hash the password
            Argon2 argon2 = Argon2Factory.create();
            String hashedPassword = argon2.hash(2, 65536, 1, password.getBytes());

            if (!users.has(username)) {
                users.put(username, hashedPassword);

                Files.write(Paths.get(CONFIG_PATH), Encryption.encryptAES(json.toString(4).getBytes(), CONFIG_KEY), StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("User " + username + " added!");
                return true;
            }
            System.out.println("User already exists!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong trying to add a user!");
            return false;
        }
    }
}
