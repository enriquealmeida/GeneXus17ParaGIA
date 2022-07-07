package $Main.AndroidPackageName$;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.artech.base.services.IGxProcedure;

import com.genexus.android.core.activities.StartupActivity;
import com.genexus.android.core.layers.GxObjectFactory;
import com.genexus.android.uitesting.logging.TestWatcher;
import com.genexus.android.uitesting.rules.GenexusActivityTestRule;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
@LargeTest
public class $Main.Name$UITests {
    @Rule
    public GenexusActivityTestRule<StartupActivity> mActivityRule = new GenexusActivityTestRule<>(StartupActivity.class);

    @Rule
    public TestWatcher mTestWatcher = new TestWatcher("$Main.VisualTestingRepositoryURL$");

$Main.GxTest4UITestRelatedObjectInfo:{ testInfo |
    @Test
    public void test$testInfo.Module$$testInfo.Name$() {
        IGxProcedure proc = GxObjectFactory.getProcedure("$testInfo.Package; format="Lower"$","$testInfo.Name; format="Lower"$");
        proc.execute(null);
    \}
}; separator="\n"$
}
