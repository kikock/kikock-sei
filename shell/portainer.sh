#!/bin/bash
# 安装 portainer
#搜索 docker 管理面板
docker search portainer

docker pull portainer/portainer



#创建Portainer文件夹 映射文件位置 data放置数据,public放置汉化页面
mkdir -p /usr/local/docker/portainer/data /usr/local/docker/portainer/public

#进入页面
cd /usr/local/docker/portainer

#下载汉化文件

wget https://labx.me/dl/4nat/public.zip

#解压汉化文件

unzip public.zip

#启动容器Portainer

docker run -d --restart=always --name portainer -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock -v /usr/local/docker/portainer/data:/data -v /usr/local/docker/portainer/public:/public portainer/portainer:latest

