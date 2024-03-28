import helpers.PropertiesReader;
import helpers.PropertiesWriter;
import helpers.TestConfig;
import models.AuthenticationRequestModel;
import models.AuthenticationResponseModel;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegistrationTest {

    @Test
    public void registrationPositive() throws IOException {

        // Create a RegistrationRequestModel object with the username and password for registration.
        AuthenticationRequestModel requestModel = AuthenticationRequestModel
                .username(PropertiesReader.getProperty("newUserEmail"))
                .password(PropertiesReader.getProperty("newUserPassword"));

        RequestBody requestBody = RequestBody
                .create(TestConfig.gson.toJson(requestModel),
                        TestConfig.JSON);

        // Build the request to register a new user.
        Request request = new Request.Builder()
                .url(PropertiesReader.getProperty("registrationUrl"))
                .post(requestBody)
                .build();

        Response response = TestConfig.client.newCall(request).execute();
        System.out.println("Response code: "+response.code());

        // Check if the response is successful
        if (response.isSuccessful()) {
            AuthenticationResponseModel responseModel =
                    TestConfig.gson.fromJson(response.body().string(),
                            AuthenticationResponseModel.class);

            // Write the token to properties file
            PropertiesWriter.writeProperties("tokenRegistration", responseModel.getToken());

            // Assert that the response contains a token.
            Assert.assertTrue(response.isSuccessful());
        } else {
            System.out.println("Error: " + response.code());
        }

    }

}
