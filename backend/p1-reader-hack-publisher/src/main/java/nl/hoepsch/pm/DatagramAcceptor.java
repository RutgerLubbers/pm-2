package nl.hoepsch.pm;

import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;

public interface DatagramAcceptor {
    void accept(DSMR5Datagram datagram);
}
