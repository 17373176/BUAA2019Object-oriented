##task1QueryList类的removeQuery( )方法  

	public Query removeQuery(int index) throws InvalidRemoveException {

	    //请依据规格补充此处缺少的代码
    	if (index < 0 || index >= queue.size()) {
    	    throw new InvalidRemoveException("Invalid index!");
        } else {
            return queue.remove(index);
		}
    }  
  
##task2QueryList类的appendQuery( )方法  

	/*@ public normal_behavior
      @ requires req!=null && req.queryTime >= lastTime;
      @ assignable queue;
      @ ensures (req.targetFloor == lowLevel) ==> \result == (req.queryDirection != Direction.DOWN);
      @ ensures (req.targetFloor == highLevel) ==> \result ==(req.queryDirection != Direction.UP);
      @ ensures (req.targetFloor > highLevel || req.targetFloor < lowLevel) ==> (\result == false);

      // 此处请依据代码补充此处缺少的规格
      // 注意描述能够将新的请求加入请求队列的队尾，保证原有的按时间先后排列的顺序
      @ ensures \old(queue.size()) +1 == queue.size();
      @ ensures contains(req);
      @ ensures (\forall Query e; e != req; contains(e) <==> \old(contains(e)));
      @ ensures (state == queue.add(req) && state == true && lastTime == req.queryTime) ==> (\result == true);

      @ also
      @ public exceptional_behavior
      @ require lastTime > req.queryTime;
      @ assignable \nothing;
      @ signals_only UnsortedException;
      @*/


##task3Elevator类的moveDown( )方法  

	/*@
      // 此处请依据moveDown()实现代码，撰写规格，写法可学习参考moveUp()已给出的示例
      @ assignable curStatus;
      @ ensures \result == ((\old(curStatus.targetFloor - 1) >= lowLevel);
      @ ensures (\old(curStatus.targetFloor - 1) >= lowLevel ==>
      @          curStatus.targetFloor == \old(targetFloor) - 1 &&
      @          curStatus.queryTime == \old(queryTime) + moveTime &&
      @          curStatus.queryDirection == Query.Direction.DOWN;
      @*/



##task4Elevator类的pickupQuery( )方法  

     public void pickupQuery(Query req) {
        // 此处请依据给定的规格，把该方法的代码补充完整
        if (req == null) {
            return;
        }
        //curHandleQ是否为空
        if (emptyQuery() && curStatus.getTime() < req.getTime()) {
            curStatus = new Query(getCurFloor(), req.getTime(), getCurDirect());
        }
        curHandleQuery.add(curStatus);
    }