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

        // Create a RegistrationRequestModel object with the desired username and password for registration.
        AuthenticationRequestModel requestModel = AuthenticationRequestModel
                .username(PropertiesReader.getProperty("newUserEmail"))
                .password(PropertiesReader.getProperty("newUserPassword"));

        // Create a RequestBody object containing the registration data in JSON format.
        RequestBody requestBody = RequestBody
                .create(TestConfig.gson.toJson(requestModel),
                        TestConfig.JSON);

        // Build the request to register a new user.
        Request request = new Request.Builder()
                .url(PropertiesReader.getProperty("registrationUrl"))
                .post(requestBody)
                .build();

        // Execute the HTTP request.
        Response response = TestConfig.client.newCall(request).execute();
        System.out.println("Response code: "+response.code());

        // Check if the response is successful (status code 2xx).
        if (response.isSuccessful()) {
            // Deserialize the response body to get the token.
            AuthenticationResponseModel responseModel =
                    TestConfig.gson.fromJson(response.body().string(),
                            AuthenticationResponseModel.class);

            // Write the token to properties file or store it as needed.
            PropertiesWriter.writeProperties("tokenRegistration", responseModel.getToken());

            // Assert that the response contains a token.
            Assert.assertTrue(response.isSuccessful());
        } else {
            System.out.println("Error: " + response.code());
        }

    }

}
