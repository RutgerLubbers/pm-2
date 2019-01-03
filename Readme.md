https://dzone.com/articles/kafka-producer-in-java
https://dzone.com/articles/writing-a-kafka-consumer-in-java

http://cloudurable.com/blog/kafka-tutorial-kafka-from-command-line/index.html

https://www.javaworld.com/article/3066873/big-data/big-data-messaging-with-kafka-part-2.html?page=2

Commit on a specific point:
https://stackoverflow.com/a/37852206

http://domoticx.com/p1-poort-slimme-meter-hardware/

# ODROID
Install `cu` via `apt install cu`, then read with `cu -l /dev/ttyUSB0 -s 115200 --parity=none -E q`


`apt install python-pip` and
`pip install pyserial`

# Reader from P1
The reader from the P1 port pushes each data event to Kafka. The data is split into a few groups:
- gas meter reading
  - timestamp
  - usage [m3]
- electricity usage
  - timestamp
  - usage [kW]
  - supply [kW]
- electricity meter reading
  - timestamp
  - high
    - usage [kWh]
    - supply [kWh]
  - low
    - usage [kWh]
    - supply [kWh]

The 'meter readings' are the totals for the meter from beginning of time (according to the meter's time that is ;-). So it will never be zero, and successive values will increase in values.
The electricity usage is the only metric that has a value for currant usage (or supply). So, it's values can become zero and will fluctuate.

For each of these metrics a separate topic is created:
- gasMeterTopic;
- electricityMeterTopic; and
- electricityUsageTopic.

The sampling period for the different metrics may (and will) differ. That's why the timestamp is present in the metrics.
Consumers must take the timestamp into account when processing the data stream.

[Perhaps there should be a cleaned topic for each of the metrics?]

Output must be:
- daily graph electricity usage / supply
- daily graph for gas usage

- daily usage / supply of electricity (number) [high]
- daily usage / supply of electricity (number) [low]
- daily usage of gas (number)

- ytd usage / supply of electricity (number) [high]
- ytd usage / supply of electricity (number) [low]
- ytd usage of gas (number)

- graph for current month
  - gas
  - electricity usage / supply [high]
  - electricity usage / supply [low]
- graph for current year
  - gas
  - electricity usage / supply [high]
  - electricity usage / supply [low]
- average graph per month
  - gas
  - electricity usage / supply [high]
  - electricity usage / supply [low]
- average graph per day ?
  - gas
  - electricity usage / supply [high]
  - electricity usage / supply [low]

The consumers will commit their offset (last read record) after their period has ended. So, for the daily usage of gas, when an event arrives for a new day, 
the last event of last day is committed. Upon a commit the aggregated result is stored (in json / mongo / ...).

For consumes that take longer than this, intermediate results are stored and can be continued upon. The interval when to commit can be configured per consumer. (For instance, commitInterval: 6h)



## Number of messages
With a measurement every 15 seconds, there are 5760 measurements a day. And this, for each topic the same number of messages.
Per year this is 2 102 400 messages. 

### Start ZooKeeper & Kafka
```bash
$ cd /opt/kafka/1.0.0
$ ./bin/zookeeper-server-start.sh config/zookeeper.properties

$ cd /opt/kafka/1.0.0
$ ./bin/kafka-server-start.sh config/server.properties
```

Note, data is stored under `/opt/kafka/1.0.0/data`
### Create a topic
```bash
$ cd /opt/kafka/1.0.0
$ ./bin/kafka-topics.sh --create --replication-factor 1 --partitions 1 --topic gasTopic --zookeeper localhost:2181
```
