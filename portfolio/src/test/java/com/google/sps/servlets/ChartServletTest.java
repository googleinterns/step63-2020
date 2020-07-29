/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sps.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import junit.framework.TestCase;
import com.google.gson.Gson;
import java.util.ArrayList;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import org.junit.After;
import org.junit.Before;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ChartServletTest extends TestCase {

    private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    public void testAddProperties() throws ServletException, IOException {
        // This is NOT a mock, we want to test an ACTUAL Chart servlet.
        ChartServlet servlet = new ChartServlet();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String name = "Q1";
        String value = "1";

        // Construct the mock request
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("value")).thenReturn(value);

        // Make the other arguments
        String currentDate = "2020/07/22";
        String userNickname = "test@example.com";

        // Make the call to addProperties
        Entity output = servlet.addProperties(currentDate, request, userNickname);

        // Build the expected props in the output Entity
        ArrayList<String> testList = new ArrayList<>();
        testList.add(currentDate);
        testList.add(value);
        testList.add(name);
        Gson g = new Gson();
        String testProps = g.toJson(testList);

        // Assert that the properties of the output look like what we expect.
        assertEquals(output.getProperty("input"), testProps);
        assertEquals(output.getProperty("User"), userNickname);

        /* Old stuff below here */

        //doReturn(test).when(servlet).addProperties(currentDate,request,userNickname) 1st Option
        //when(servlet.addProperties(currentDate, request, userNickname)).thenReturn(test); //2nd Option
        //when(servlet.sayHello("jason")).thenReturn("Hello jason");

        /* When I do doReturn(), the error I get is:

        Unfinished stubbing detected here:
        -> at com.google.sps.servlets.ChartServletTest.testDoPostSucceed(ChartServletTest.java:76)

        E.g. thenReturn() may be missing.
        Examples of correct stubbing:
            when(mock.isOk()).thenReturn(true);
            when(mock.isOk()).thenThrow(exception);
            doThrow(exception).when(mock).someVoidMethod();
        Hints:
        1. missing thenReturn()
        2. you are trying to stub a final method, you naughty developer!
        3: you are stubbing the behaviour of another mock inside before 'thenReturn' instruction if completed */

        /* When I do when().thenReturn(), the error I get is:

        org.mockito.exceptions.misusing.WrongTypeOfReturnValue:
        Entity cannot be returned by getParameter()
        getParameter() should return String
        ***
        If you're unsure why you're getting above error read on.
        Due to the nature of the syntax above problem might occur because:
        1. This exception *might* occur in wrongly written multi-threaded tests.
        Please refer to Mockito FAQ on limitations of concurrency testing.
        2. A spy is stubbed using when(spy.foo()).then() syntax. It is safer to stub spies -
        - with doReturn|Throw() family of methods. More in javadocs for Mockito.spy() method.



        The only time I use .getParameter is when getting the value and name from html to put into an arraylist in ChartServlet,
        which does return a string.*/
    }
}