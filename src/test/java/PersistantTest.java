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

import com.e271.journal.core.Journal;
import com.e271.journal.core.User;
import com.e271.journal.core.exceptions.TooManyCharacters;
import com.e271.journal.core.exceptions.UserNotCreated;
import com.e271.journal.core.persistant.PersistanJournalSQL;
import com.e271.journal.core.persistant.PersistantJournal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import jdk.nashorn.internal.objects.NativeArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Eugenio Ochoa
 */
public class PersistantTest {
    
    private static final String URL = "jdbc:mysql://localhost:3306/360journal?zeroDateTimeBehavior=convertToNull";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "#656869#.Cvsgck";
    
    private static Journal j;
    
    private static List<User> users;
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    public PersistantTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws UserNotCreated {
        users = new ArrayList<>();
        
        users.add( new User("pepe1", "Josefino 1", "qwerty") );
        users.add( new User("pepe2", "Josefina", "qwerty") );
        
        j = new Journal(
            new PersistanJournalSQL(URL, USERNAME, PASSWORD));
        
        for(User _user : users) {
            String id = _user.getId();
            String name = _user.getName();
            String password = _user.getPassword();
            
            j.addUser(id, name, password);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        
        for(User _user : users) {
            String id = _user.getId();
            
            j.deleteUser(id);
        }
        j.closeJournal();
    }
    
    @Before
    public void setUp() throws UserNotCreated {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void userExists() {
        
        boolean exists = j.userExists("pepe1");
        assertTrue(exists);
        
        exists = j.userExists("pepe99");
        assertFalse(exists);
    }
    
    @Test
    public void getUser() {
        
        User _user = j.getUser("pepe1");
        
        assertTrue(_user.getId().equals(users.get(0).getId()));
        assertTrue(_user.testPassword(users.get(0).getPassword()));
    }

    @Test
    public void validateUser() {
        
        boolean isValid = j.validateUser("pepe1", users.get(0).getPassword());
        assertTrue(isValid);
        
        isValid = j.validateUser("pepe1", "qwertu");
        assertFalse(isValid);
    }
    
    @Test
    public void addUser() throws Exception {
        String id = users.get(1).getId();
        String name = users.get(1).getName();
        String password = users.get(1).getPassword();
        
        boolean canAdd = j.canAdd(id);
        
        assertFalse(canAdd);
        exception.expect(UserNotCreated.class);
        j.addUser(id, name, password);
        
    }
    
    @Test
    public void addEntry() throws TooManyCharacters {
        Calendar _date = new GregorianCalendar();
        Calendar date = new GregorianCalendar(_date.get(Calendar.YEAR),
                _date.get(Calendar.MONTH),
                _date.get(Calendar.DAY_OF_MONTH));
        
        j.addEntry("pepe1", "New Journal Entry.", "360 Words possible.", null);
        
        j.deleteEntry("pepe1", date);
        
        exception.expect(TooManyCharacters.class);
        String moreThan360 = new String(new char[361]).replace('\0', ' ');
        j.addEntry("pepe1", "New Journal Entry.", moreThan360, null);
    }
}
