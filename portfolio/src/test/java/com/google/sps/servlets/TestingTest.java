// A couple trivial tests just to verify that the testing framework works.
package com.google.sps;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
}
