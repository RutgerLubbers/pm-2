#!/usr/bin/python

import sys

import serial

# cu /dev/ttyUSB0 -s 115200 --parity=none -E q

# Set COM port config
ser = serial.Serial()
ser.baudrate = 115200
# ser.bytesize = serial.SEVENBITS
# ser.parity = serial.PARITY_EVEN
ser.stopbits = serial.STOPBITS_ONE
ser.xonxoff = 0
ser.rtscts = 0
ser.timeout = 20
ser.port = "/dev/ttyUSB0"

# Open COM port
try:
    ser.open()
except:
    sys.exit("Fout bij het openen van %s. Aaaaarch." % ser.name)

# Initialize
i = 0
datagram_started = False
must_read = True
datagram = ""
while must_read:
    p1_line = ''
    # Read 1 line van de seriele poort
    try:
        p1_raw = ser.readline()
    except:
        sys.exit("Seriele poort %s kan niet gelezen worden. Aaaaaaaaarch." % ser.name)
    p1_str = str(p1_raw)
    p1_line = p1_str.strip()
    if p1_str.startswith("/"):
        datagram = ""
        datagram_started = True
        lines_read = 0

    if datagram_started:
        datagram += p1_line
        datagram += "\n"
        must_read = not p1_line.startswith("!")


print(datagram)

# Close port and show status
try:
    ser.close()
except:
    sys.exit("Oops %s. Programma afgebroken. Kon de seriele poort niet sluiten." % ser.name)
