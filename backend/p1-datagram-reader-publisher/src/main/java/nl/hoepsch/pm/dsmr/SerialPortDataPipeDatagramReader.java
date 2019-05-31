package nl.hoepsch.pm.dsmr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A reader of a {@link SerialPortDataPipe}, that passes the data to the {@link DatagramReader}.
 */
@Component
public class SerialPortDataPipeDatagramReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortDataPipeDatagramReader.class);

    /**
     * The pipe to read from.
     */
    private final SerialPortDataPipe pipe;

    /**
     * The datagram reader that reads from the pipe.
     */
    private final DatagramReader reader;

    /**
     * The constructor.
     *
     * @param pipe   The pipe to read from.
     * @param reader The datagram reader that reads from the pipe.
     */
    @Autowired
    public SerialPortDataPipeDatagramReader(final SerialPortDataPipe pipe, final DatagramReader reader) {
        this.pipe = pipe;
        this.reader = reader;
    }

    /**
     * Start consuming data from the pipe.
     */
    @Async
    public void consume() {
        LOGGER.debug("Start reading the pipe.");
        try {
            reader.consume(pipe.getInputStream(), datagram -> LOGGER.debug("Got datagram"));
        } catch (IOException e) {
            LOGGER.error("Got exception.", e);
        }
    }
}
