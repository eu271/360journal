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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Eugenio Ochoa
 */
public class JournalEntry implements Comparable<JournalEntry>{
    
    public static final int MAX_LENTGHT = 360;
    
    private Calendar date;
    private String title;
    private String content;
    private byte[] photo;
    
    protected JournalEntry() {}
    
    public JournalEntry(Calendar date, String title, String content, byte[] photo) 
            throws TooManyCharacters {
        
        if (content.length() > JournalEntry.MAX_LENTGHT) {
            throw new TooManyCharacters(content.length());
        }
        
        this.date = date;
        this.title = title;
        this.content = content;
        this.photo = photo;
    }
    
    public JournalEntry(Calendar date, String title, String content) 
            throws TooManyCharacters {
        this(date, title, content, null);
    }
    
    public JournalEntry(String title, String content, byte[] photo) 
            throws TooManyCharacters {
        
        this(null, title, content, photo);
        Calendar _date = new GregorianCalendar();
        this.date = new GregorianCalendar(_date.get(Calendar.YEAR),
                _date.get(Calendar.MONTH),
                _date.get(Calendar.DAY_OF_MONTH));
    }
    
    public JournalEntry(String title, String content) 
            throws TooManyCharacters {
        
        this(title, content, null);
    }
    
    
    public Calendar getDate() {
        return this.date;
    }
    public void setDate(Calendar date) {
        this.date = date;
    }
    
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) throws TooManyCharacters {
        
        if (content.length() > JournalEntry.MAX_LENTGHT) {
            throw new TooManyCharacters(content.length());
        }
        
        this.content = content;
    }
    
    public byte[] getPhoto() {
        return this.photo;
    }
    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    
    
    @Override
    public int compareTo(JournalEntry o) {
        return this.date.compareTo(o.date);
    }
}
