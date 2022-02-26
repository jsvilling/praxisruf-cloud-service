package ch.fhnw.ip6.praxisruf;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * This test verifies that the application context is initialized successfully.
 * Any and all test for domain logic is in the responsibility of the relevant modules.
 */
@Disabled
@SpringBootTest
class PraxisCloudServiceApplicationTest {

    @Autowired
    ApplicationContext context;

    @Test
    @Disabled
    void testApplicationStartup() {
        Assertions.assertThat(context).isNotNull();
    }

}
