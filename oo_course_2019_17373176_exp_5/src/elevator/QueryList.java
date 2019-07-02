package elevator;

import elevator.Query.Direction;

import java.util.ArrayList;

public class QueryList {

    /*OVERVIEW:请求队列类，管理乘客请求（Query）
    请求队列需要在添加请求时对请求的合法性进行二次判断，所以需要记录楼层的取值范围，及队列中最后一个请求的时间
    对于不满足时间非降序的添加请求，需要抛出一个可辨识的异常UnsortedException
    请求队列提供添加、遍历、清空的方法*/

    private /*@ spec_public @*/ ArrayList<Query> queue;//请求队列
    private /*@ spec_public @*/ int highLevel;//最高楼层
    private  /*@ spec_public @*/ int lowLevel;//最低楼层
    private  /*@ spec_public @*/ double lastTime;//队列中最近一次请求的时间

    public QueryList(int high, int low, double time) {
        this.queue = new ArrayList<Query>();
        this.highLevel = high;
        this.lowLevel = low;
        this.lastTime = time;
    }

    public QueryList(int high, int low) {
        this(high, low, 0);
    }

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
    public boolean appendQuery(Query req) throws UnsortedException {
        if (lastTime > req.getTime()) {
            throw new UnsortedException("Unsorted Data.");
        } else {
            int target = req.getTarget();
            Direction direct = req.getDirection();
            if (target < highLevel && target > lowLevel
                    || target == highLevel && direct != Direction.UP
                    || target == lowLevel && direct != Direction.DOWN) {
                //将新的请求加入请求队列的队尾，所有请求仍然按时间先后顺序排列
                boolean state = queue.add(req);
                if (state == true) {
                    lastTime = req.getTime();
                }
                return state;
            } else {
                return false;
            }
        }
    }

    /*@ public normal_behavior
      @ requires index >=0 && index < queue.size();
      @ assignable queue;
      @ ensures \result == \old(queue.get(index)) && queue.contains(\result)==false;
      @ also
      @ public exceptional_behavior
      @ requires index < 0 || index >= queue.size();
      @ signals_only InvalidRemoveException;
      @*/
    public Query removeQuery(int index) throws InvalidRemoveException {

        //请依据规格补充此处缺少的代码
        if (index < 0 || index >= queue.size()) {
            throw new InvalidRemoveException("Invalid index!");
        } else {
            return queue.remove(index);
        }
    }

    public /*@ pure @*/ int getSize() {
        return queue.size();
    }

    public /*@ pure @*/ Query getQuery(int index) {
        return queue.get(index);
    }

    public void clear() {
        queue.clear();
    }
}
