package br.com.greenngoconnect.rippleimpact.api.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class NgoApiConsumerIT {

    @RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            // Se quiser aplicar templating globalmente em todos os stubs, use:
            // .templatingEnabled(true)
            .build();

   // @Test
    void shouldCreateNgo() {
        wm.stubFor(post(urlEqualTo("/v1/ngos"))
                .withHeader("Content-Type", matching("application/json.*"))
                .withRequestBody(matchingJsonPath("$.name"))
                .withRequestBody(matchingJsonPath("$.email"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Location", "http://localhost:{{request.port}}/v1/ngos/{{randomValue type='UUID'}}")
                        .withBody("""
                                {
                                  "id": "{{randomValue type='UUID'}}",
                                  "name": "{{jsonPath request.body '$.name'}}",
                                  "email": "{{jsonPath request.body '$.email'}}",
                                  "status": "ACTIVE"
                                }
                                """)
                        .withTransformers("response-template"))); // <- habilita templating neste stub

        String baseUrl = "http://localhost:" + wm.getPort();
        // faça a chamada HTTP real usando baseUrl...
    }
}