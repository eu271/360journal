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
 * LIABILITY, WUsersEntryJournalEntryJournalHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
 * Author:  Eugenio Ochoa
 * Created: Nov 26, 2015
 */


DROP TABLE IF EXISTS Users, EntryJournal CASCADE;

CREATE TABLE Users (
    user_id CHAR(25),
        CONSTRAINT PK_Users PRIMARY KEY(user_id),
    user_name VARCHAR(30),
    password CHAR(102),
    
    char_count INTEGER
);

CREATE TABLE EntryJournal (
    entry_date DATE,
    user_id CHAR(25),
        CONSTRAINT FK_UsersEntryJournal FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE,
        CONSTRAINT PK_EntryJournal PRIMARY KEY (entry_date, user_id),
    title VARCHAR(30),
    content VARCHAR(360),
    photo BLOB
);