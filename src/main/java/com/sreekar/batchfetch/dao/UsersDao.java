package com.sreekar.batchfetch.dao;

import com.sreekar.batchfetch.models.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDao {

    Logger Log = Logger.getLogger(UsersDao.class.getName());

    private final Connection con;

    public UsersDao(Connection con) {
        this.con = con;
    }

    public List<User> getUsers(int start, int end) throws SQLException {
        return evaluateQuery(String.format("select * from Users limit %d,%d;", start, end));
    }

    public int getUsersCount() throws SQLException {
        ResultSet rs =  con.createStatement()
                .executeQuery("select count(*) as users from Users");
        rs.next();
        return rs.getInt(1);

    }

    private List<User> evaluateQuery(String sql) throws SQLException {
        List<User> result = new LinkedList<>();
        Statement stmt = con.createStatement();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            User user = new User();
            user.setId(Integer.parseInt(rs.getString(1)));
            user.setUsername(rs.getString(2));
            user.setEmail(rs.getString(3));
            user.setAddress(rs.getString(4));
            result.add(user);
        }

        return result;
    }

    public void getUsers() throws SQLException {
        evaluateQuery("select * from Users;");
    }
}
