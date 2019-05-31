package nl.hoepsch.pm.dsmr;

/**
 * Interface that allows consuming the serial port data.
 */
public interface SerialDataAcceptor {

    /**
     * Accept the data read from the serial port.
     *
     * @param data The data read.
     */
    void accept(byte[] data);
}
