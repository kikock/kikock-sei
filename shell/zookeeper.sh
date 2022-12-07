	
	#!/bin/bash
	#  zookeeper 搭建脚本
	
	#搜索 zookeeper 
	docker search zookeeper
	
	# 官方镜像
	docker pull zookeeper:latest
	
	
	#创建nexus3映射文件夹
	mkdir -p /usr/local/docker/zookeeper/data/
	
	# 增加权限
	chown -R 777 /usr/local/docker/zookeeper/data/
	
	
	# --name	容器名
	# -p	端口映射
	# -v	容器卷映射
	# --restart=always	设置随docker自启动
	# -d	后台运行
	# --appendonly	开启持久化
	# --privileged=true	使容器内的root拥有真正root权限
	# --requirepass root	设置redis-cli的密码root
	
	docker run -d \
	    --restart=always \
	    --name zookeeper \
	    --privileged=true \
	    -p 2181:2181 \
	    -v /usr/local/docker/zookeeper/data:/opt/zookeeper-3.4.13/data \
	    wurstmeister/zookeeper
	
