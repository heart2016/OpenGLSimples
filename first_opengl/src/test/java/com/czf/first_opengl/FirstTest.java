package com.czf.first_opengl;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class FirstTest extends TestCase {

    public FirstTest(){
        super("FistTest");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("测试SetUp");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.out.println("测试tearDown");
    }

    @SmallTest
    public void testSomething(){
        System.out.println("测试");
    }

    @SmallTest
    public void testSomethings(){
        System.out.println("测试sssss");
    }
}
