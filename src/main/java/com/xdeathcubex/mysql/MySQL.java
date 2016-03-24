package com.xdeathcubex.mysql;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class MySQL {

    static BoneCP connectionPool = null;
    static Connection connection = null;


    public MySQL(String host, String user, String pw, String db) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try {
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + "/" + db + "?autoReconnect=true");
            config.setUsername(user);
            config.setPassword(pw);
            config.setMinConnectionsPerPartition(5);
            config.setMaxConnectionsPerPartition(10);
            config.setPartitionCount(1);
            config.setDisableConnectionTracking(true);
            connectionPool = new BoneCP(config);
            connection = connectionPool.getConnection();
            if (connection != null) {
                System.out.println("MySQL connected successful!");
                Statement st = connection.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS FriendSystem(UUID Varchar(255), Friends Text(9999), Requests Text(9999))");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerProperties(UUID Varchar(255), invites BOOLEAN, notifies BOOLEAN, msgs BOOLEAN, jump BOOLEAN)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            connectionPool.shutdown();
        }
    }

    public static void setUser(String uuid){
        if(connection != null) {
            if (get("Friends", uuid) == null) {
                try {
                    Statement st = connection.createStatement();
                    st.executeUpdate("INSERT INTO FriendSystem VALUES ('" + uuid + "','','')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(String what, String uuid){
        String toget = null;
        if(connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM FriendSystem WHERE UUID = '" + uuid + "'");
                while (rs.next()) {
                    toget = rs.getString(what);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return toget;
    }
    public static void add(String uuid, String changeWhat, String addWhat){
        if(connection != null){
        try {
            String newAdd = MySQL.get(changeWhat, uuid) + addWhat + " ";
            Statement st = connection.createStatement();
            st.executeUpdate("UPDATE `FriendSystem` SET `"+changeWhat+"` = '"+newAdd+"' WHERE UUID = '"+uuid+"'");
        } catch (SQLException e){ e.printStackTrace(); }
        }
    }


    public static void remove(String uuid, String fromWhat, String removeWhat){
        if(connection != null) {
            try {
                String toChange = get(fromWhat, uuid);
                toChange = toChange.replaceAll(removeWhat + " ", "");
                Statement st = connection.createStatement();
                st.executeUpdate("UPDATE `FriendSystem` SET `" + fromWhat + "` = '" + toChange + "' WHERE UUID = '" + uuid + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean is(String what, String who, String fromWho){
          return get(what, fromWho).contains(who);
    }

    public static String get1(String uuid){
        String toget = null;
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM PlayerProperties WHERE UUID = '" + uuid + "'");
                while(rs.next()){
                    toget = rs.getString("UUID");
                }
            }catch (SQLException e){ e.printStackTrace(); }
        } else {
            ProxyServer.getInstance().broadcast(new TextComponent("Moin =D"));
        }
        return toget;
    }
    public static void setupProperties(String uuid){
        if(get1(uuid) == null){
            try{
                Statement st = connection.createStatement();
                st.executeUpdate("INSERT INTO PlayerProperties VALUES ('" + uuid + "','1','1','1','1')");
            } catch (SQLException e){ e.printStackTrace(); }
        }
    }

    public static boolean getProperties(String uuid, String what){
        boolean toget = false;
        if(connection != null){
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM PlayerProperties WHERE UUID = '" + uuid + "'");
                while(rs.next()){
                    toget = rs.getBoolean(what);
                }
            } catch (SQLException e){ e.printStackTrace(); }
        }
        return toget;
    }

    public static void changeProperties(String uuid, String changeWhat){
        try {
            boolean newAddb = !MySQL.getProperties(uuid, changeWhat);
            int newAddi = newAddb ? 1 : 0;
            Statement st = connection.createStatement();
            st.executeUpdate("UPDATE `PlayerProperties` SET `"+changeWhat+"` = '"+newAddi+"' WHERE UUID = '" + uuid + "'");
        } catch (SQLException e){ e.printStackTrace(); }
    }

    public static String getRank(String uuid){
        String toget = null;
        if(connection != null) {
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM RankSystem WHERE UUID = '" + uuid + "'");
                while (rs.next()) {
                    toget = rs.getString("rank");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return toget;
    }
}
