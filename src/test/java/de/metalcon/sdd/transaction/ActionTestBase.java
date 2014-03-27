package de.metalcon.sdd.transaction;

import org.junit.Before;

import de.metalcon.sdd.StaticSddTestBase;
import de.metalcon.sdd.WriteTransaction;

public abstract class ActionTestBase extends StaticSddTestBase {

    protected WriteTransaction tx;

    @Before
    public void setUp() {
        tx = sdd.createWriteTransaction();
    }

}
