### 业务

* 单聊
  * 发送消息：`send [username] [content]`
  * 查看所有用户名：`list [page_no]`

* 群聊
  * 查看所有群聊名：`glist [page_no]`
  * 查看已加入群聊名 `glistm [page_no]`
  * 创建群聊：`gcreate [group name] [m1,m2,m3...]`
  * 加入群聊：`gjoin [group name]`
  * 退出群聊：`gquit [group name]`（群聊人数为0，自动销毁）
  * 发送消息：`gsend [group name] [content]` （自动加入群聊）
  * 查看群成员：`gmembers [group name] [page_no]`

### 涉及技术

* Netty
* Protobuf、Gson
* MySQL、HikariCP、Apache commons-dbutils
* Lombok、Logback
* 空闲检测、心跳维持
* 自定义协议、消息编解码器
* 帧解码器解决粘包，半包（拆包）
* 本地 LRU Cache 缓存群成员信息，加速群消息转发

### 自定义协议

| 魔数（NETTYBABY） | 消息编号 | 填充 | 正文长度 | 正文   |
| ----------------- | -------- | ---- | -------- | ------ |
| 9b                | 1b       | 2b   | 4b       | 不定长 |

* `NETTYBABY` 纯打趣，协议魔数一般占 4 字节
* `填充`是为了使得`header`占 2^n 个字节，也是为了预留空间，方便协议升级（这里没这个打算，所以没有版本号）
* 正文的内容是一个消息对象（`top.glidea.common.message`），采用 Protobuf 序列化 / 反序列化

### TODO

* LRU Cache 并发度优化
* 梳理 Netty、HikariCP 可调优参数
* 敏感词过滤
* 离线消息缓存：单聊，群聊
* 好友系统：添加好友、删除好友、分页列出好友...
* ......
