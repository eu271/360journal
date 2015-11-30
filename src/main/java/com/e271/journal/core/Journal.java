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
package com.e271.journal.core;

import com.e271.journal.core.exceptions.TooManyCharacters;
import com.e271.journal.core.exceptions.UserNotCreated;
import com.e271.journal.core.persistant.PersistantJournal;
import java.util.Calendar;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 *
 * @author Eugenio Ochoa
 */
public class Journal {
    
    private static final long ENTRY_MAX_LENGTH = 360;
    
    private PersistantJournal journal;
    
    public Journal(PersistantJournal journal) {
        this.journal = journal;
    }
    
    public User getUser(String userId) {
        return journal.getUser(userId);
    }
    
    public boolean validateUser(String userId, String password) {
        if (journal.userExists(userId)) {
            User _user = journal.getUser(userId);
            return _user.testPassword(password);
        }
        return false;
    }
    
    public boolean userExists(String userId) {
        return journal.userExists(userId);
    }
    
    public boolean canAdd(String userId) {
        return ! journal.userExists(userId);
    }
    public void addUser(String userId, String name, String password) 
            throws UserNotCreated {
        
        if (journal.userExists(userId)) {
          throw new UserNotCreated(userId); 
        }
        
        journal.addUser(new User(userId, name, password));
    }
    
    public void deleteUser(String userId) {
        journal.deleteUser(journal.getUser(userId));
    }
    
    public void addEntry(String userId, String title, String content, byte[] photo) 
            throws TooManyCharacters {
        
        if( content.length() > ENTRY_MAX_LENGTH )
            throw new TooManyCharacters(content.length());
        
        User _user = journal.getUser(userId);
        
        _user.setCharCount(_user.getCharCount() + content.length());
        
        journal.saveUser(_user);
        journal.addEntry(_user, 
                new JournalEntry(title, content, photo));
    }
    public JournalEntry getEntry(String userId, Calendar date) {
        
        return journal.getEntry(journal.getUser(userId), date);
    }
    public SortedMap<Calendar, JournalEntry> getEntrysFrom(String userId, Calendar date){ 
        
        return journal.getEntriesFrom(journal.getUser(userId), date);
    }
    
    public void deleteEntry(String userId, Calendar date) {
        
        journal.deleteEntry(journal.getUser(userId), date);
    }
    
    
    public void closeJournal() {
        this.journal.closePersistant();
    }

    
}
