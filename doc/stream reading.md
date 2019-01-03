Reader from smart meter publishes data every `n` seconds.

Data contains:
  ts electricity
      elec values
  ts gas
      gas values
      
The raw stream may be split into two streams, gas and electricity.

    
#### Readers store their offset
A reader must store the offset in the stream it reads.

#### Delta Readers
Some readers read a value and store the value.
Some other readers take the value and compute a difference (delta) based on a previous value. For instance the GAS YTD reader, takes
the first known value of the current year and computes the difference between the consumed value and the old value (from jan 1st).

#### What happens when `ts` differs from `last_ts`, so that a new period starts?

Consumers collect data for a certain period. If the `ts` is outside the current period, then:
* the current period must be stored;
* a new period must be started.

The 

#### Storage of values
1. Values for each consumer are stored after a configurable time has lapsed.

1. Values for each consumer are stored after the consumer's "period" rolls over. That is, a new 
period has been started.

#### What happens when ts differs for more than, say, a few minutes?
In case of just storing the value, just store the value. However, the display of this value may cause trouble.

In case of a delta reader, ignore the value. Possibly start a new period.
