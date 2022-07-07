package com.genexus.android.core.externalapi;

import com.genexus.android.core.actions.ApiAction;
import com.genexus.android.core.externalapi.ExternalApi;
import com.genexus.android.core.externalapi.ExternalApiDefinition;
import com.genexus.android.core.externalapi.ExternalApiFactory;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ExternalApiFactoryTest {
    private static class TestEO extends ExternalApi {
        public static final String OBJECT_NAME = "TestEO";

        public TestEO(ApiAction action) {
            super(action);
        }
    }

    private static class TestEOWithFailingConstructor extends ExternalApi {
        public static final String OBJECT_NAME = "TestEOWithFailingConstructor";
        private String mStr;
        private int mLen;

        public TestEOWithFailingConstructor(ApiAction action) {
            super(action);
            mLen = mStr.length();
        }
    }

    private static class TestEOWithoutProperConstructor extends ExternalApi {
        public static final String OBJECT_NAME = "TestEOWithoutProperConstructor";

        public TestEOWithoutProperConstructor(ApiAction action, String extraParameter) {
            super(action);
        }
    }

    @Test
    public void shouldGetInstanceOfExternalApi_whenCorrectDefinitionHasBeenProvided() {
        ExternalApiFactory factory = new ExternalApiFactory();
        factory.addDefinition(new ExternalApiDefinition(TestEO.OBJECT_NAME, TestEO.class));

        ExternalApi externalApi = factory.getInstance(TestEO.OBJECT_NAME, mock(ApiAction.class));

        assertThat(externalApi).isInstanceOf(TestEO.class);
    }

    @Test
    public void shouldThrowNoClassDefFoundError_whenNoDefinitionIsPresent() {
        ExternalApiFactory factory = new ExternalApiFactory();

        try {
            ExternalApi externalApi = factory.getInstance(TestEO.OBJECT_NAME, mock(ApiAction.class));
            fail("getInstance should throw NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            assertThat(e).hasMessageThat().isEqualTo(String.format("No ExternalApiDefinition was found for '%s'.", TestEO.OBJECT_NAME));
        }
    }

    @Test
    public void shouldThrowRuntimeException_whenExceptionIsThrownOnTheConstructor() {
        ExternalApiFactory factory = new ExternalApiFactory();
        factory.addDefinition(new ExternalApiDefinition(TestEOWithFailingConstructor.OBJECT_NAME, TestEOWithFailingConstructor.class));

        try {
            ExternalApi externalApi = factory.getInstance(TestEOWithFailingConstructor.OBJECT_NAME, mock(ApiAction.class));
            fail("getInstance should throw RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().isEqualTo(String.format("Error while instantiating External Api class '%s'.", TestEOWithFailingConstructor.class.getName()));
        }
    }

    @Test
    public void shouldThrowRuntimeException_whenExternalApiDoesNotHaveTheAppropiateConstructor() {
        ExternalApiFactory factory = new ExternalApiFactory();
        factory.addDefinition(new ExternalApiDefinition(TestEOWithoutProperConstructor.OBJECT_NAME, TestEOWithoutProperConstructor.class));

        try {
            ExternalApi externalApi = factory.getInstance(TestEOWithoutProperConstructor.OBJECT_NAME, mock(ApiAction.class));
            fail("getInstance should throw RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e).hasMessageThat().isEqualTo(String.format("External Api class '%s' does not have the appropriate constructor.", TestEOWithoutProperConstructor.class.getName()));
        }
    }
}
