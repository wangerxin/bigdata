# coding=utf-8
from kafka import KafkaConsumer


consumer = KafkaConsumer('my_topic', group_id='group2', bootstrap_servers=['localhost:9092'])
for msg in consumer:
    print(msg)
