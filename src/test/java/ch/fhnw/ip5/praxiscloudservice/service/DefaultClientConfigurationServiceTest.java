package ch.fhnw.ip5.praxiscloudservice.service;

import ch.fhnw.ip5.praxiscloudservice.persistence.ClientConfigurationRepository;
import ch.fhnw.ip5.praxiscloudservice.persistence.ClientRepository;
import ch.fhnw.ip5.praxiscloudservice.service.configuration.DefaultClientClientConfigurationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultClientConfigurationServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientConfigurationRepository clientConfigurationRepository;

    @InjectMocks
    private DefaultClientClientConfigurationService clientConfigurationService;



}
