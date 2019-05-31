package nl.hoepsch.pm.dsmr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Adapter that accepts the input read from the serial port and writes it to a piped output stream.
 * This output stream is paired to an input stream, used by the datagram reader.
 */
@Component
public class ToPipeSerialDataAcceptor implements SerialDataAcceptor, InitializingBean {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToPipeSerialDataAcceptor.class);

    /**
     * The pipe to write to.
     */
    private final SerialPortDataPipe pipe;

    /**
     * The datagram reader, that reads from a serial data pipe.
     */
    private final SerialPortDataPipeDatagramReader pipeReader;

    /**
     * The constructor.
     *
     * @param pipe The pipe to write to.
     * @param pipeReader The datagram reader, that reads from a serial data pipe.
     */
    @Autowired
    public ToPipeSerialDataAcceptor(final SerialPortDataPipe pipe, final SerialPortDataPipeDatagramReader pipeReader) {
        this.pipe = pipe;
        this.pipeReader = pipeReader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(final byte[] data) {
        try {
            pipe.write(data);
        } catch (IOException e) {
            LOGGER.error("Got error writing the output stream.", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pipeReader.consume();
    }
}
