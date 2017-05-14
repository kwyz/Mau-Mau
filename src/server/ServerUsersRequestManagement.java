package server;

import java.io.*;

public class ServerUsersRequestManagement {

    public boolean acces;
    BufferedReader reader;
    PrintWriter prnwriter;
    FileWriter filewrt;
    String sCurrentLineFromFile = null;
    public boolean startGame = false;
    public boolean gameState = false;
    private final String[] userInGame = new String[6];
    private boolean endGame = false;
    private boolean gameWasStarted = false;

    ServerUsersRequestManagement() {
    }

    public boolean DetectTypeOfQuery(String query) throws IOException {
        if (query.contains("Username")) {
            String getDataArray[] = query.split("-");
            String tempString = getDataArray[1];
            acces = checkUsername(tempString);
        }
        return acces;
    }
    

    /* Loby methods start*/
    protected void disconnectPlayerFromGame(String username) {
        for (int i = 0; i < 6; i++) {
            if (userInGame[i].equals(username)) {
                userInGame[i] = null;
                i = 5;
            }
        }
    }

    protected void addUserToGame(String username) {
        for (int i = 0; i < userInGame.length; i++) {
            if (userInGame[i] == null) {
                userInGame[i] = username;
                break;
            }
        }
    }

    /* show this array in client*/

    protected String[] getUserInGame() {
        return userInGame;
    }

    /*Loby methods end*/
     /* Check if game can be started */
    protected boolean gameStart(int readyCount, int playersInGame) {
        if (playersInGame >= 2 && playersInGame < 7) {
            startGame = readyCount == playersInGame;
            if(startGame == true){
            	gameWasStarted = true;
            }
        }
        return startGame;
    }

    public boolean checkIfGameIsEnd(int usersInGame) {
        if (usersInGame < 2 && gameWasStarted == true) {
            gameState = true;
            gameWasStarted = false;
        } else {
            gameState = false;
        }
        return gameState;
    }

    /*End of check*/

    protected boolean endGame(int readyCount) {
        endGame = readyCount < 2;
        return endGame;
    }
   

    protected boolean DisconectPlayer(String userToDisconect) throws FileNotFoundException, IOException {
        String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
        reader = new BufferedReader(new FileReader(pathname + "UserDataBase.txt"));
        File tempFile = new File(pathname + "UserDataBaseTemp.txt");
        tempFile.createNewFile();
        
        while ((sCurrentLineFromFile = reader.readLine()) != null) {
            acces = false;
            if (sCurrentLineFromFile.equals(userToDisconect)) {
                acces = false;
            } else {
                prnwriter = new PrintWriter(filewrt = new FileWriter(pathname + "UserDataBaseTemp.txt", true));
                prnwriter.println(sCurrentLineFromFile);
                prnwriter.flush();
                filewrt.flush();
            }

        }
        reader.close();
        reader = new BufferedReader(new FileReader(pathname + "UserDataBaseTemp.txt"));
        File f = new File(pathname + "UserDataBase.txt");
        f.delete();
        File FileTemp = new File(pathname + "UserDataBase.txt");
        FileTemp.createNewFile();
        while ((sCurrentLineFromFile = reader.readLine()) != null) {
            prnwriter = new PrintWriter(filewrt = new FileWriter(pathname + "UserDataBase.txt", true));
            prnwriter.println(sCurrentLineFromFile);
            prnwriter.flush();
            filewrt.flush();
        }
        tempFile.delete();
        reader.close();
        return acces;
    }

    protected boolean checkUsername(String userName) throws FileNotFoundException, IOException {
        String pathname = ClassLoader.getSystemClassLoader().getResource(".").getPath();
        reader = new BufferedReader(new FileReader(pathname + "UserDataBase.txt"));
        while ((sCurrentLineFromFile = reader.readLine()) != null) {
            if (sCurrentLineFromFile.equals(userName)) {
                reader.close();
                acces = false;
                break;
            } else {
                acces = true;
            }
        }
        if (acces == true) {
            prnwriter = new PrintWriter(filewrt = new FileWriter(pathname + "UserDataBase.txt", true));
            prnwriter.println(userName);
            prnwriter.flush();
            filewrt.flush();
            reader.close();
        }
        return acces;
    }

}
