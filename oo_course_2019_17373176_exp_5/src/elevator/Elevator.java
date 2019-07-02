package elevator;

import java.util.ArrayList;

public class Elevator implements SchedulableCarrier {
    /*
    OVERVIEW: 电梯类，模拟电梯实体，能够响应用户请求并改变电梯状态。
    该类自己记录和管理电梯运动过程中的状态变化，
    包括当前停靠的楼层，停靠、开关门之后的时间，当前运动方向。
    */

    private /*@ spec_public @*/ int highLevel;//最高楼层
    private /*@ spec_public @*/ int lowLevel;//最低楼层
    private /*@ spec_public @*/ Query curStatus;//电梯当前状态（记录停靠楼层、开关门之后时间、当前运动方向）
    private /*@ spec_public @*/ArrayList<Query> curHandleQuery;//当前捎带处理队列

    public Elevator(int high, int low) {
        this.highLevel = high;
        this.lowLevel = low;
        this.curStatus = new Query(1, 0.0);
        this.curHandleQuery = new ArrayList<Query>();
    }

    /*@ assignable curStatus;
      @ ensures result == ((\old(curStatus.targetFloor + 1) <= highLevel);
      @ ensures (\old(curStatus.targetFloor + 1) <= highLevel ==>
      @          curStatus.targetFloor == \old(targetFloor) + 1 &&
      @          curStatus.queryTime == \old(queryTime) + moveTime &&
      @          curStatus.queryDirection == Query.Direction.UP;
      @*/
    public boolean moveUp() {
        if (getCurFloor() + 1 > highLevel) {
            return false;
        } else {
            curStatus = new Query(getCurFloor() + 1,
                    getCurTime() + moveTime, Query.Direction.UP);
            return true;
        }
    }

    /*@
      // 此处请依据moveDown()实现代码，撰写规格，写法可学习参考moveUp()已给出的示例
      @ assignable curStatus;
      @ ensures \result == ((\old(curStatus.targetFloor - 1) >= lowLevel);
      @ ensures (\old(curStatus.targetFloor - 1) >= lowLevel ==>
      @          curStatus.targetFloor == \old(targetFloor) - 1 &&
      @          curStatus.queryTime == \old(queryTime) + moveTime &&
      @          curStatus.queryDirection == Query.Direction.DOWN;
      @*/
    public boolean moveDown() {
        if (getCurFloor() - 1 < lowLevel) {
            return false;
        } else {
            curStatus = new Query(getCurFloor() - 1,
                    getCurTime() + moveTime, Query.Direction.DOWN);
            return true;
        }
    }

    public boolean callOpenAndClose() {
        curStatus = new Query(getCurFloor(), getCurTime() + callTime);
        return true;
    }

    public String toString() {
        return "(" + this.getCurFloor() + "," + this.getCurDirect() + "," + this.getCurQuery().getTarget() + ")";
    }

    public /*@ pure @*/ int getCurFloor() {
        return curStatus.getTarget();
    }

    public /*@ pure @*/ double getCurTime() {
        return curStatus.getTime();
    }

    public /*@ pure @*/ Query.Direction  getCurDirect() {
        return curStatus.getDirection();
    }

    public Query /*@ pure @*/  getCurQuery() {
        if (emptyQuery()) {
            return null;
        }
        else {
            return curHandleQuery.get(0);
        }
    }

    public boolean  /*@ pure @*/  emptyQuery() {
        return curHandleQuery.isEmpty();
    }

    public boolean checkFinishedQuery() {
        int cnt = 0;
        for (int i = 1; i < curHandleQuery.size(); ++i) {
            Query pickedQuery = curHandleQuery.get(i);
            if (pickedQuery.getTarget() == getCurFloor()) {
                System.out.printf("(%d, %s, %.1f)\t(%s)\n", getCurFloor(), "STAY", getCurTime(), pickedQuery.toString());
                curHandleQuery.remove(i);
                ++cnt;
            }
        }
        return cnt > 0;
    }

    //OVERVIEW: 将已判断为可捎带的请求加入当前捎带处理队列
    /*@ requires req != null;
      @ assignable curHandleQuery,curStatus;
      @ ensures curHandleQuery.size() == (\old(curHandleQuery.size()) + 1) && curHandleQuery.contains(req);
      @ ensures \old(curHandleQuery.isEmpty()) && (\old(curStatus.queryTime) < req.queryTime) ==>
      @         curStatus.queryTime ==  req.queryTime
      @         && curStatus.targetFloor == \old(targetFloor)
      @         && curStatus.queryDirection == \old(queryDirection);
      @*/
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

    //根据响应的请求逐层更新电梯状态

    public String updateStatus(Query req) throws Exception {
        //执行主请求，逐层更新电梯状态
        int directDelta = (int)Math.signum(req.getTarget() - getCurFloor());
        String curDirect;
        switch (directDelta) {
            case -1 : {
                curDirect = "DOWN";
                moveDown();
                break;
            }
            case 0 : {
                curDirect = "STAY";
                break;
            }
            case 1 : {
                curDirect = "UP";
                moveUp();
                break;
            }
            default : throw new Exception("Invalid Status.");
        }
        return curDirect;
    }

    //电梯响应当前捎带处理队列
    public void moveForQuery() throws Exception {
        final String status;
        if (emptyQuery()) {
            return;
        }
        Query req = getCurQuery();
        boolean hasFinishedQuery = false;

        //开始主请求工作之前，查询当前捎带队列中是否有已完成请求，若有，全部剔除，然后开关门一次
        hasFinishedQuery = checkFinishedQuery();
        if (hasFinishedQuery) {
            callOpenAndClose();
            return;
        }

        //执行主请求，逐层更新电梯状态
        status = updateStatus(req);

        // 每更新完一次电梯状态查询当前捎带队列中是否有已完成请求，若有，全部剔除，并开关门一次
        hasFinishedQuery = checkFinishedQuery();

        //主请求完成
        if (req.getTarget() == getCurFloor()) {
            hasFinishedQuery = true;
            System.out.printf("(%d, %s, %.1f)\n", getCurFloor(), status, getCurTime());
            curHandleQuery.remove(0);
        }
        //主请求完成，查询当前捎带队列中是否有已完成请求，若有，全部剔除，然后开关门一次
        if (hasFinishedQuery) {
            callOpenAndClose();
        }
    }
}