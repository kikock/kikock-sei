		#!/bin/bash
		#  kafka 搭建脚本
		
		#搜索 kafka 
		docker search kafka
		
		# 官方镜像
		docker pull wurstmeister/kafka:2.12-2.3.0

		
		
		# --name	容器名
		# -p	端口映射
		# -v	容器卷映射
		# --restart=always	设置随docker自启动
		# -d	后台运行
		
		docker run -d \
		    --restart=always \
		    --name kafka \
		    --privileged=true \
		    --env KAFKA_ADVERTISED_HOST_NAME=localhost \
		    -e KAFKA_ZOOKEEPER_CONNECT=192.168.66.88:2181 \
		    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.66.88:9092 \
		    -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
		    -e KAFKA_HEAP_OPTS="-Xmx256M -Xms128M" \
		    -p 9092:9092 \
		    wurstmeister/kafka:2.12-2.3.0
		    

		
		
	

