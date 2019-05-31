package nl.hoepsch.pm.dsmr;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.fazecast.jSerialComm.SerialPort.NO_PARITY;
import static com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT;

/**
 * Datagram reader to read datagrams from a (USB) serial port connected to the P1 port of a smart meter.
 */
@Component
public class SerialPortDatagramReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortDatagramReader.class);

    /**
     * The datagram reader.
     */
    private final DatagramReader reader;

    /**
     * The serial port to read.
     */
    @Value("${serial-port}")
    private String port;

    /**
     * The constructor.
     *
     * @param reader The datagram reader.
     */
    @Autowired
    public SerialPortDatagramReader(final DatagramReader reader) {
        this.reader = reader;
    }

    /**
     * Read the P1 port of the smart meter.
     *
     * @throws InterruptedException In case the program is stopped.
     */
    @Async
    public void consume() throws InterruptedException {
        LOGGER.info("Opening '{}'.", port);
        final SerialPort comPort = SerialPort.getCommPort(port);
        configurePort(comPort);

        consume(comPort);
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private void consume(final SerialPort comPort) throws InterruptedException {
        while (true) {
            comPort.openPort();

            if (comPort.isOpen()) {
                try {
                    reader.consume(comPort.getInputStream(), datagram -> System.out.println(datagram.getRaw()));
                } catch (IOException e) {
                    LOGGER.error("Got a read exception:", e);
                }
            }
            Thread.sleep(1500);
        }
    }

    private void configurePort(final SerialPort comPort) {
        comPort.setBaudRate(115_200);
        comPort.setNumStopBits(ONE_STOP_BIT);
        comPort.setParity(NO_PARITY);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1_500, 0);
    }
}
