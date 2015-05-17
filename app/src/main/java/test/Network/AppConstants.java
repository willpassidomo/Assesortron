package test.Network;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import assesortron.assesortronTaskerAPI.AssesortronTaskerAPI;

/**
 * Created by willpassidomo on 3/4/15.
 */
public class AppConstants {
        /**
         * Class instance of the JSON factory.
         */
        public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

        /**
         * Class instance of the HTTP transport.
         */
        public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


        /**
         * Retrieve a Helloworld api service handle to access the API.
         */
        public static AssesortronTaskerAPI getAssServiceHandle() {
            // Use a builder to help formulate the API request.
            AssesortronTaskerAPI.Builder assesortronAPI = new AssesortronTaskerAPI.Builder(AppConstants.HTTP_TRANSPORT,
                    AppConstants.JSON_FACTORY,null);
            return assesortronAPI.build();
        }

}
