1 readout per second

86.4k per day

31.536M per year

636 bytes per datagram.
- 22G per year

gzip: 21 bytes per datagram
- 0.8G per year

86.4k / 60 = 1440 (one sample per minute? or average per minute?)


# P1 -> Kafka[`p1_raw`]
ok, so push "all" data through kafka. Perhaps have a counter, every n items will be transmitted

# Client on Kafka[`p1_raw`] 
have a listener on the raw stream to split it into gas meter and electricity meter readouts and current usage (elec, gas).
if ts of last readout <> current readout transmit.
this means we have delta for gas

we have actual for electricity, push every item?

DSMR 5 says gas is per 5 minutes, so readout per 5 minutes for gas?

gas + elec meter readout, push every 5 minutes?

--

have listeners on the specific streams for further processing.

read datagram, verify crc, push datagram.

read datagram, get 

1-0:1.8.1(001130.629*kWh)
1-0:1.8.2(000596.802*kWh)
1-0:2.8.1(000073.503*kWh)
1-0:2.8.2(000098.616*kWh)

1-0:1.8.1(001130.629*kWh)
1-0:1.8.2(000597.695*kWh)
1-0:2.8.1(000073.503*kWh)
1-0:2.8.2(000098.616*kWh)


