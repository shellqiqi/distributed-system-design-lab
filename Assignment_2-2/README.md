# 分布式系统设计课程作业
## 单播模型
### 发送者程序变更
- 数据格式同广播模型
- 根据random1=0/1，分别发送给888/999端口进程
- 如果出现random1=2，则要同时发给2个对端
### 接收者程序变更
终止条件：收到15个0或5个1或random1=2

