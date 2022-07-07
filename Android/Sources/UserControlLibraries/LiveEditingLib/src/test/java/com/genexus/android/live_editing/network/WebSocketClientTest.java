package com.genexus.android.live_editing.network;
//
//import com.genexus.live_editing.serializers.ParsingModule;
//import com.google.gson.Gson;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnit;
//import org.mockito.junit.MockitoRule;
//
//import okhttp3.OkHttpClient;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//
//public class WebSocketClientTest {
//    @Rule
//    public MockitoRule rule = MockitoJUnit.rule();
//    @Mock
//    private INetworkEventsListener listener;
//    private Gson gson;
//    private OkHttpClient okHttpClient;
//
//    @Before
//    public void setUp() {
//        ParsingModule parsingModule = new ParsingModule();
//        gson = parsingModule.providesGson();
//
//        NetworkModule networkModule = new NetworkModule(listener);
//        okHttpClient = networkModule.providesOkHttpClient();
//    }
//
//    @Test
//    public void shouldConnectSuccessfully_whenEndpointEstablishesConnectionSuccessfully() {
//        INetworkClient client = new WebSocketClient(okHttpClient, gson, listener);
//
//        //ConnectionStringInfo connInfo = ConnectionStringInfo.parse();
//        //client.connect(connInfo, Collections.emptyList());
//
//        verify(listener).onConnectionEstablished(any());
//    }
//
//    @Test
//    public void shouldFailConnection_whenEndpointDoesNotRespond() {
//
//        verify(listener).onMaxAttemptsReached();
//
//    }
//}
