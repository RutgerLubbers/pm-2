kafka-webview
kafdrop



sudo kafka-avro-console-consumer --bootstrap-server localhost:19092 --topic datagram --property schema.registry.url="http://localhost:19091" --from-beginning

sudo kafka-avro-console-consumer --bootstrap-server localhost:19092 --topic electricity-actual --property schema.registry.url="http://localhost:19091" --from-beginning
