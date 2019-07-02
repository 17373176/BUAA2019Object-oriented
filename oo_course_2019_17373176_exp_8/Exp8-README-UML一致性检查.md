# UML不一致性检查
  
## 类图和顺序图
1. 类图没有处理illegaVistorException的操作
2. rid属性信息GUI中没有关联
3. GUI中没有door opened的接收操作


## 状态图自身,类图与状态图
### 图3 open-close
1. locker.unlock()不应该有参数  
2. locker.lock()不应该没有参数  
3. locker没有getLockld()这个操作

### 图4 register-unregister
1. guests.size()<10与==9存在共同条件，会同时满足
2. NotAvaliableTokens状态需要添加自身到自身的转移，即Register(client)[guests.size()>=10]

## 顺序图和状态图
### 图3 open-close
1. 状态图应当存在一开始门就是开着的状态，即从开始状态到open状态的转移，通过isOpen()操作
2. 顺序图中没有close状态的操作
3. 状态图中应当有顺序图中illegallVisitorException的转移

