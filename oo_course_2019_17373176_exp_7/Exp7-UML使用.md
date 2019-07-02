# Exp7-UML使用  
## 类
- Taxi出租车  
- TaxiOrdinary extends Taxi普通出租  
- TaxiSpecial extends Taxi特殊出租  
- Passenger乘客  
- Position乘客位置  
- Request乘客请求
- RequestNow extends Request立即请求  
- RequestOrder extends Request预约请求  

  
## 出租车
- 四种状态：SERVE, RECIEVE, WAIT, STOP
- WAIT可获得请求派单，变成RECIEVE
- 一旦接到乘客，变为SERVE
- 只能从WAIT变为STOP
  
## 乘客
- 一个乘客可发出多个叫车请求，关联1对n
- 请求包含乘客，出发位置，目的地位置， 预约还包括出发时间
- type为是否预约
  
## 位置
二维坐标

## 请求
- 乘客信息，出发位置，目的地位置
- 预约请求包括出发时间