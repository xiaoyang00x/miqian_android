package com.miqian.mq.views;

/**
 * 手势锁九宫格中每个点
 * Created by wangduo on 15/12/11.
 */
class GestureLockPoint {

    public final static int STATE_NOR = 0; // 普通状态
    public final static int STATE_PRESS = 1; // 按下状态
    public final static int STATE_ERROR = 2; // 错误状态

    public int pointX, pointY;
    public int state;
    public int index;

    public GestureLockPoint() {
    }

    public GestureLockPoint(int pointX, int pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    /**
     * 计算两点之间的距离
     *
     * @param pointA
     * @param pointB
     * @return
     */
    public static int distance(GestureLockPoint pointA, GestureLockPoint pointB) {
        return (int) Math.sqrt((pointA.pointX - pointB.pointX) * (pointA.pointX - pointB.pointX) + (pointA.pointY - pointB.pointY) * (pointA.pointY - pointB.pointY));
    }

    public static float degree(GestureLockPoint a, GestureLockPoint b) {
        float degree;
        if (a.pointY == b.pointY) { // 0度、180度
            if (a.pointX > b.pointX) {
                degree = 180;
            } else {
                degree = 0;
            }
        } else if (a.pointX == b.pointX) { // 90度、270度
            if (a.pointY > b.pointY) {
                degree = 270;
            } else {
                degree = 90;
            }
        } else {
            degree = getAngle(a, b);
        }
        return degree;
    }

    private static float getAngle(GestureLockPoint a, GestureLockPoint b) {
        //两点的x、y值
        int x = b.pointX - a.pointX;
        int y = b.pointY - a.pointY;
        double hypotenuse = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        //斜边长度
        double cos = x / hypotenuse;
        double radian = Math.acos(cos);
        //求出弧度
        float angle = (float) (180 / (Math.PI / radian));
        //用弧度算出角度
        if (b.pointY < a.pointY) {
            angle = 360 - angle;
        }
        return angle;
    }

}
