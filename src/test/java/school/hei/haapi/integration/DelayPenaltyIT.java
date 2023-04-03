package school.hei.haapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.bouncycastle.est.LimitedSource;
import school.hei.haapi.SentryConf;
import school.hei.haapi.endpoint.rest.api.PayingApi;
import school.hei.haapi.endpoint.rest.client.ApiClient;
import school.hei.haapi.endpoint.rest.client.ApiException;
import school.hei.haapi.endpoint.rest.model.DelayPenalty;
import school.hei.haapi.endpoint.rest.model.Fee;
import school.hei.haapi.endpoint.rest.security.cognito.CognitoComponent;
import school.hei.haapi.integration.conf.TestUtils;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static school.hei.haapi.integration.conf.TestUtils.FEE7_ID;
import static school.hei.haapi.integration.conf.TestUtils.FEE8_ID;
import static school.hei.haapi.integration.conf.TestUtils.MANAGER1_TOKEN;
import static school.hei.haapi.integration.conf.TestUtils.STUDENT1_ID;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = FeeIT.ContextInitializer.class)
@AutoConfigureMockMvc
public class DelayPenaltyIT {
  @MockBean
  private SentryConf sentryConf;
  @MockBean
  private CognitoComponent cognitoComponentMock;

  private static ApiClient anApiClient(String token) {
    return TestUtils.anApiClient(token, FeeIT.ContextInitializer.SERVER_PORT);
  }

  static Fee fee1() {
    Fee fee = new Fee();
    fee.setId(FEE7_ID);
    fee.setStudentId(STUDENT1_ID);
    fee.setStatus(Fee.StatusEnum.UNPAID);
    fee.setType(Fee.TypeEnum.TUITION);
    fee.setTotalAmount(100000);
    fee.setRemainingAmount(100000);
    fee.setComment("Comment");
    fee.setUpdatedAt(Instant.parse("2023-02-08T08:30:24Z"));
    fee.creationDatetime(Instant.parse("2021-11-08T08:25:24.00Z"));
    fee.setDueDatetime(Instant.parse("2023-04-03T08:30:24.00Z"));
    return fee;
  }
  static Fee fee2() {
    Fee fee = new Fee();
    fee.setId(FEE8_ID);
    fee.setStudentId(STUDENT1_ID);
    fee.setStatus(Fee.StatusEnum.LATE);
    fee.setType(Fee.TypeEnum.TUITION);
    fee.setTotalAmount(200000);
    fee.setRemainingAmount(200000);
    fee.setComment("Comment");
    fee.setUpdatedAt(Instant.parse("2023-02-08T08:30:24Z"));
    fee.creationDatetime(Instant.parse("2021-11-08T08:25:24.00Z"));
    fee.setDueDatetime(Instant.parse("2023-04-01T08:30:24.00Z"));
    return fee;
  }

  @Test
  public void update_ok() throws ApiException {
    ApiClient manager1Client = anApiClient(MANAGER1_TOKEN);
    List<Fee> beforeChanges = List.of(fee1(), fee2());
    PayingApi api = new PayingApi(manager1Client);
    List<Fee> actualFees = api.getStudentFees(STUDENT1_ID, 1, 5, "LATE");


    DelayPenalty actual = api.getDelayPenalty();
    assertEquals(3, actual.getGraceDelay());
    assertTrue(actualFees.containsAll(beforeChanges));
  }

}
