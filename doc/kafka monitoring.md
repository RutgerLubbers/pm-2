kafka-webview
kafdrop

Snappy Compression!

kafka-topics --zookeeper localhost:2181 --list



kafka-topics --zookeeper localhost:2181 --topic datagram --delete
kafka-topics --zookeeper localhost:2181 --topic datagram --replication-factor 1 --partitions 1 --create

kafka-topics --zookeeper localhost:2181 --topic electricity-actual --delete
kafka-topics --zookeeper localhost:2181 --topic electricity-actual --replication-factor 1 --partitions 1 --create

kafka-topics --zookeeper localhost:2181 --topic electricity-meter --delete
kafka-topics --zookeeper localhost:2181 --topic electricity-meter --replication-factor 1 --partitions 1 --create

kafka-topics --zookeeper localhost:2181 --topic electricity-meter-per-day --delete
kafka-topics --zookeeper localhost:2181 --topic electricity-meter-per-day --replication-factor 1 --partitions 1 --create

kafka-topics --zookeeper localhost:2181 --delete --topic p1-reader-stream-vw-electricity-meter-per-day-repartition
kafka-topics --zookeeper localhost:2181 --delete --topic p1-reader-stream-vw-electricity-meter-per-day-changelog

kafka-topics --zookeeper localhost:2181 --delete --topic  p1-reader-stream-KSTREAM-AGGREGATE-STATE-STORE-0000000006-changelog
kafka-topics --zookeeper localhost:2181 --delete --topic  p1-reader-stream-KSTREAM-AGGREGATE-STATE-STORE-0000000006-repartition



sudo kafka-avro-console-consumer --bootstrap-server localhost:19092 --topic datagram --property schema.registry.url="http://localhost:19091" --from-beginning

sudo kafka-avro-console-consumer --bootstrap-server localhost:19092 --topic electricity-actual --property schema.registry.url="http://localhost:19091" --from-beginning
