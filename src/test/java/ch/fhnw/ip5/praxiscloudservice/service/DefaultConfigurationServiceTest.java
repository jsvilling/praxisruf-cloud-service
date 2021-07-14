package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.DefaultClientClientConfigurationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultConfigurationServiceTest {

    @Mock
    private ClientConfigurationRepository clientConfigurationRepository;

    @InjectMocks
    private DefaultClientClientConfigurationService configurationService;

}
