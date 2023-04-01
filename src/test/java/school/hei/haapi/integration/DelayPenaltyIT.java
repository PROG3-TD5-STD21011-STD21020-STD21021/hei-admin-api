package school.hei.haapi.integration;

import io.swagger.annotations.Api;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import school.hei.haapi.SentryConf;
import school.hei.haapi.endpoint.rest.api.PayingApi;
import school.hei.haapi.endpoint.rest.client.ApiClient;
import school.hei.haapi.endpoint.rest.client.ApiException;
import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.endpoint.rest.model.DelayPenalty;
import school.hei.haapi.endpoint.rest.security.cognito.CognitoComponent;
import school.hei.haapi.integration.conf.AbstractContextInitializer;
import school.hei.haapi.integration.conf.TestUtils;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.haapi.integration.conf.TestUtils.MANAGER1_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.anAvailableRandomPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = DelayPenaltyIT.ContextInitializer.class)
@AutoConfigureMockMvc
public class DelayPenaltyIT {

    @MockBean
    private SentryConf sentryConf;
    @MockBean
    private CognitoComponent cognitoComponentMock;

    private static ApiClient anApiClient(String token) {
        return TestUtils.anApiClient(token, FeeIT.ContextInitializer.SERVER_PORT);
    }

    private static DelayPenalty penalty1(){
        DelayPenalty delayPenalty = new DelayPenalty();
        delayPenalty.setGraceDelay(5);
        delayPenalty.setId("penality1_id");
        delayPenalty.setApplicabilityDelayAfterGrace(5);
        delayPenalty.setInterestTimerate(DelayPenalty.InterestTimerateEnum.DAILY);
        delayPenalty.setInterestPercent(2);

        return delayPenalty;
    }

    private static DelayPenalty penaltyUpdated(){
        penalty1().setGraceDelay(7);
        penalty1().setInterestPercent(5);
        penalty1().setApplicabilityDelayAfterGrace(10);

        return penalty1();
    }

    private static CreateDelayPenaltyChange penalty_to_update(){
        CreateDelayPenaltyChange penaltyToUpdate = new CreateDelayPenaltyChange();
        penaltyToUpdate.setGraceDelay(7);
        penaltyToUpdate.setInterestPercent(5);
        penaltyToUpdate.setApplicabilityDelayAfterGrace(10);
        penaltyToUpdate.setInterestTimerate(CreateDelayPenaltyChange.InterestTimerateEnum.DAILY);

        return penaltyToUpdate;

    }


    @Test
    void manager_read_ok() throws ApiException {
        ApiClient managerClient = anApiClient(MANAGER1_TOKEN);
        PayingApi api = new PayingApi(managerClient);

        DelayPenalty delayPenalty = api.getDelayPenalty();

        assertEquals(penalty1(), delayPenalty);
    }

    @Test
    void manager_update_ok() throws ApiException {
        ApiClient managerClient = anApiClient(MANAGER1_TOKEN);
        PayingApi api = new PayingApi(managerClient);

        DelayPenalty delayPenalty = api.createDelayPenaltyChange(penalty_to_update());
        assertEquals(delayPenalty, penaltyUpdated() );
    }

    static class ContextInitializer extends AbstractContextInitializer {
        public static final int SERVER_PORT = anAvailableRandomPort();

        @Override
        public int getServerPort() {
            return SERVER_PORT;
        }
    }
}
