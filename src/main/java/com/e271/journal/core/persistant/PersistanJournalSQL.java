/*
 * The MIT License
 *
 * Copyright 2015 Eugenio Ochoa.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.e271.journal.core.persistant;

import com.e271.journal.core.JournalEntry;
import com.e271.journal.core.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 *
 * @author Eugenio Ochoa
 */
public class PersistanJournalSQL implements PersistantJournal {
    
    private DataSource datasource;
    
    public PersistanJournalSQL( String url, String username, String password ) {
        PoolProperties p = new PoolProperties();
            p.setUrl(url);
            p.setDriverClassName("com.mysql.jdbc.Driver");
            p.setUsername(username);
            p.setPassword(password);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(20);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
                p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
              "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            
            this.datasource = new DataSource();
            datasource.setPoolProperties(p); 
    }

    @Override
    public void saveEntry(User user, JournalEntry entry) {
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "UPDATE EntryJournal "
                            + "SET title = ?, "
                            + "content = ?, "
                            + "photo = ?) "
                            + "WHERE user_id = ?"
                            + "AND entry_date = ?");
            
            
            prepareStatement.setString(1, entry.getTitle());
            prepareStatement.setString(2, entry.getContent());
            prepareStatement.setBlob(3, 
                    new ByteArrayInputStream(entry.getPhoto()));
            
            prepareStatement.setString(4, user.getId());
            
            prepareStatement.setDate(4, 
                    new java.sql.Date(entry.getDate().getTime().getTime()));
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    }

    @Override
    public void addEntry(User user, JournalEntry entry) {
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "INSERT INTO EntryJournal (entry_date, user_id, title, content, photo) "
                            + "VALUES (?, ?, ?, ?, ?)");
            
            prepareStatement.setDate(1, new java.sql.Date(entry.getDate().getTime().getTime()) );
            prepareStatement.setString(2, user.getId());
            prepareStatement.setString(3, entry.getTitle());
            prepareStatement.setString(4, entry.getContent());
            
            if (entry.getPhoto() != null) {
                prepareStatement.setBlob(5, new ByteArrayInputStream(entry.getPhoto()));
            }else{
                prepareStatement.setBlob(5, (InputStream) null);
            }
            
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    }

    @Override
    public void deleteEntry(User user, Calendar date) {
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "DELETE FROM EntryJournal "
                            + "WHERE user_id = ? AND "
                            + "entry_date = ?");
            
            prepareStatement.setString(1, user.getId());
            prepareStatement.setDate(2, new java.sql.Date(date.getTime().getTime()));
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    }

    @Override
    public JournalEntry getEntry(User user, Calendar date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveUser(User user) {
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "UPDATE Users "
                            + "SET user_name = ?, "
                            + "password = ?, "
                            + "char_count = ? "
                            + "WHERE user_id = ?");
            
            
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getPassword());
            prepareStatement.setLong(3, user.getCharCount());
            prepareStatement.setString(4, user.getId());
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    }

    @Override
    public void addUser(User user) {
        
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "INSERT INTO Users (user_id, user_name, password, char_count) "
                            + "VALUES (?, ?, ?, ?)");
            
            prepareStatement.setString(1, user.getId());
            prepareStatement.setString(2, user.getName());
            prepareStatement.setString(3, user.getPassword());
            prepareStatement.setLong(4, user.getCharCount());
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    }

    @Override
    public void deleteUser(User user) {
        Connection connection = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "DELETE FROM Users WHERE user_id = ?");
            
            prepareStatement.setString(1, user.getId());
            
            prepareStatement.executeUpdate();
            
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
    
    }

    @Override
    public boolean userExists(String userId) {
        return getUser(userId) != null;
    }

    @Override
    public User getUser(String userId) {
        Connection connection = null;
        User _user = null;
        try {
            connection = this.datasource.getConnection();
            
            Statement st = connection.createStatement();
            PreparedStatement prepareStatement = connection.prepareStatement(
                    "SELECT * FROM Users WHERE user_id = ?"
            );
            
            prepareStatement.setString(1, userId);
            
            ResultSet rs = prepareStatement.executeQuery();
            if (rs.first()) {
                _user = new User( rs.getNString("user_id"), 
                    rs.getNString("user_name"), 
                    rs.getNString("password"),
                    rs.getLong("char_count"));
            }
            
            
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersistanJournalSQL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {connection.close();} catch (Exception ignore) {}
        }
        return _user;
    }

    @Override
    public SortedMap<Calendar, JournalEntry> getEntriesFrom(User User, Calendar date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void closePersistant() {
        this.datasource.close();
    }
    
}
