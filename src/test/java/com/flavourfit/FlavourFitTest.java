package com.flavourfit;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlavourFitTest {

    @Test
    void contextLoads() {
    }

    @Test
    void connectionTest(){
        FlavourFit obj = new FlavourFit();
        Assert.assertEquals(true, obj.connection());
    }

}
