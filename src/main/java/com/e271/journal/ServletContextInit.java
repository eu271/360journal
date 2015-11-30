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
package com.e271.journal;

import com.e271.journal.core.Journal;
import com.e271.journal.core.User;
import com.e271.journal.core.persistant.PersistanJournalSQL;
import com.e271.journal.core.persistant.PersistantJournal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Eugenio Ochoa
 */
public class ServletContextInit implements ServletContextListener {
    
    private static final String URL = "jdbc:mysql://localhost:3306/360journal?zeroDateTimeBehavior=convertToNull";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "#656869#.Cvsgck";
    
    private ServletContext context;
    private Journal j;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        this.j = new Journal(
                new PersistanJournalSQL(URL, USERNAME, PASSWORD));
        
        User _user = new User("pepe", "pepe", "pepe");
        
        System.out.println("Initializaicing server configuration.");
        this.context = sce.getServletContext();

        this.context.setAttribute("Journal", j);
        
        try {
            j.addUser("Jose", "Jose pepe", "asdasd");
        } catch (Exception ex) {
            Logger.getLogger(ServletContextInit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        j.closeJournal();
    }
    
}
