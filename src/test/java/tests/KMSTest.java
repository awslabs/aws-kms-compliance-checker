import java.io.File;
import java.util.List;

import org.checkerframework.framework.test.CheckerFrameworkPerDirectoryTest;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test runner that uses the Checker Framework's tooling.
 *
 * The test data is located in the "tests/kms" folder (by Checker Framework convention). If this test fails,
 * that means that one or more of the Java files in that directory, when run through the typechecker, did
 * not produce the expected results.
 *
 * When looking at the tests in that folder, lines starting with "// :: error: " are expected errors. The test will
 * fail if they are not present. The rest of those lines are error keys, which are printed by the typechecker. So,
 * for example, "// :: error: argument.type.incompatible" means that an argument type on the following line should
 * be incompatible with a parameter type.
 *
 * To add a new test case, create a Java file in that directory. Use the "// :: error: " syntax to add any expected
 * warnings. All files ending in .java in that directory will automatically be run by this test runner.
 *
 * This test runner depends on the Checker Framework's testing library, which is found in the Brazil package
 * Maven-org-checkerframework_testlib.
 */
public class KMSTest extends CheckerFrameworkPerDirectoryTest {
    public KMSTest(List<File> testFiles) {
        super(
                testFiles,
                com.amazon.checkerframework.compliance.kms.ComplianceChecker.class,
                "kms",
                "-Anomsgtext",
		"-nowarn");
    }

    @Parameters
    public static String[] getTestDirs() {
        return new String[] {"kms"};
    }
}