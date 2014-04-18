package de.metalcon.sdd;

import static de.metalcon.sdd.DynamicSddTestBase.closeSdd;
import static de.metalcon.sdd.DynamicSddTestBase.createSdd;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

public class MemberSddTestBase {

    protected Sdd sdd;

    @Before
    public void setUp() throws IOException {
        sdd = createSdd();
    }

    @After
    public void tearDown() throws IOException {
        closeSdd(sdd);
    }

}
