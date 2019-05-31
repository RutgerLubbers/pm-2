package nl.hoepsch.pm.dsmr;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fazecast.jSerialComm.SerialPort.NO_PARITY;
import static com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT;

/**
 * Datagram reader to read data from a (USB) serial port connected to the P1 port of a smart meter.
 */
@Component
public class SerialPortConfigurator implements InitializingBean {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortConfigurator.class);

    /**
     * The serial data acceptors.
     */
    private final List<SerialDataAcceptor> dataAcceptors;

    /**
     * The serial port to read.
     */
    @Value("${serial-port}")
    private String port;

    /**
     * The constructor.
     *
     * @param dataAcceptors The serial data acceptors.
     */
    @Autowired
    public SerialPortConfigurator(final List<SerialDataAcceptor> dataAcceptors) {
        this.dataAcceptors = dataAcceptors;
    }

    /**
     * Read the P1 port of the smart meter.
     */
    private SerialPort createPort() {
        LOGGER.info("Opening '{}'.", port);
        return SerialPort.getCommPort(port);
    }

    private void configurePort(final SerialPort serialPort) {
        serialPort.setBaudRate(115_200);
        serialPort.setNumStopBits(ONE_STOP_BIT);
        serialPort.setParity(NO_PARITY);

        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1_500, 0);

        LOGGER.info("Registering '{}' data listeners.", dataAcceptors.size());
        serialPort.addDataListener(new SerialPortDataListenerImpl(dataAcceptors));
    }

    private void open(final SerialPort serialPort) {
        serialPort.openPort();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        final SerialPort serialPort = createPort();
        configurePort(serialPort);
        open(serialPort);
    }
}
