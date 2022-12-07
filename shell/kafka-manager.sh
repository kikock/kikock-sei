		#!/bin/bash
		# kafka管理平台
		docker run -d \
		    --name kafka-manager \
		    -p 9001:9000 \
		    -e ZK_HOSTS="192.168.66.88:2181" \
		    sheepkiller/kafka-manager 

