// A couple trivial tests just to verify that the testing framework works.
package com.google.sps;

import java.util.List;
import com.google.sps.servlets.JournalServlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;

import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class TestingTest {

  @Test
  public void testAddition() {
    Assert.assertEquals(2 + 2, 4);
  }

  @Test
  public void testMockito() {
    // Taken from
    // https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#1

    // mock creation
    List mockedList = Mockito.mock(List.class);

    //using mock object
    mockedList.add("one");
    mockedList.clear();

    //verification
    Mockito.verify(mockedList).add("one");
    Mockito.verify(mockedList).clear();
  }

  
  
  @Test
  public void testEmail() {

    Assert.assertEquals("chiamaka@google.com", JournalServlet.setEmail());
  }

  @Test
  public void everyEntityHasSalienceScore() {
    
    List<String> test = new ArrayList();
    test.add("string");
    test.add("0.0");
    test.add("string");
    test.add("string");

    List<String> expected = new ArrayList();
    expected.add("Unequal amount of subject, type, and salience");

    Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }

  @Test
  public void catchUnequalAttributes() {
    
    List<String> test = new ArrayList();
    test.add("string");
    test.add("0.0");
    test.add("string");

    List<String> expected = new ArrayList();
    expected.add("Unequal amount of subject, type, and salience");

    Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }
  
  /**
  @Test
  public void filterIfCommon() {
    
    List test = Mockito.mock(List.class);
    test.add("string");
    test.add("0.3");
    test.add("COMMON");
    test.add("Chiamaka");
    test.add("0.7");
    test.add("PROPER");

    List expected = Mockito.mock(List.class);
    expected.add("Chiamaka");
    test.add("0.7");
    test.add("PROPER");
    
    Assert.assertEquals(expected, JournalServlet.getSubjects(test));
  }
  */


  
  
}
