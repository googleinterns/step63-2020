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
import org.junit.Assert;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
    }

    public void testGetQuestionNum(){
        ChartServlet servlet = new ChartServlet();
        ArrayList<String> properties = new ArrayList<>();
        DatastoreService datastore = Mockito.mock(DatastoreService.class);
        properties.add("2020-08-06");
        properties.add("1");
        properties.add("Q2");
        Gson g = new Gson();
        Entity ent = new Entity("input");
        ent.setProperty("input", g.toJson(properties));

        String[] expected = {"[\"2020-08-06\"", "\"1\"", "\"Q2\"]"};

        Assert.assertArrayEquals(servlet.getOldProperties(ent), expected);
    }

}
