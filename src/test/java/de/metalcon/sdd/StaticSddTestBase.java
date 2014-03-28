package de.metalcon.sdd;

import static de.metalcon.sdd.DynamicSddTestBase.closeSdd;
import static de.metalcon.sdd.DynamicSddTestBase.createSdd;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class StaticSddTestBase {

    protected static Sdd sdd;

    @BeforeClass
    public static void setUpStatic() throws IOException {
        sdd = createSdd();
    }

    @AfterClass
    public static void tearDownStatic() throws IOException {
        closeSdd(sdd);
    }

}
