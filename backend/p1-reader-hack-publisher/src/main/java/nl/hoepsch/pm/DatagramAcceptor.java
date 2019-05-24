package nl.hoepsch.pm;

import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;

/**
 * Interface to accept newly parsed DSMR datagrams.
 */
public interface DatagramAcceptor {

    /**
     * Handle a new datagram.
     *
     * @param datagram The datagram to handle.
     */
    void accept(DSMR5Datagram datagram);
}
