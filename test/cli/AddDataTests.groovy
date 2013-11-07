import grails.test.AbstractCliTestCase

class AddDataTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testAddData() {

        execute(["add-data"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
