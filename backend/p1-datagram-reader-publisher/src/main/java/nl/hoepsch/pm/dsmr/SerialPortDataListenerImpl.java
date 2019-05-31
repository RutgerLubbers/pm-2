package nl.hoepsch.pm.dsmr;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A serial port data listener, that writes the data read to the configured SerialDataAcceptors.
 */
public class SerialPortDataListenerImpl implements SerialPortDataListener {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialPortDataListenerImpl.class);

    /**
     * The, never null, set of data acceptors.
     */
    private final List<SerialDataAcceptor> acceptors;

    /**
     * The constructor.
     */
    public SerialPortDataListenerImpl(final List<SerialDataAcceptor> acceptors) {
        this.acceptors = requireNonNull(acceptors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialEvent(final SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        }
        LOGGER.trace("Received data available event.");
        readData(event.getSerialPort());
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private void readData(final SerialPort serialPort) {
        int available = serialPort.bytesAvailable();
        while (available != 0) {
            final byte[] newData = new byte[serialPort.bytesAvailable()];
            serialPort.readBytes(newData, newData.length);

            for (final SerialDataAcceptor acceptor : acceptors) {
                acceptor.accept(newData);
            }

            available = serialPort.bytesAvailable();
        }
    }



}
