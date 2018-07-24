package com.sf.demo.data;

import com.sf.demo.R;
import com.sf.demo.enity.MenuItem;
import com.sf.demo.list.expand.MenuChild;
import com.sf.demo.list.expand.MenuParent;
import com.sf.demo.view.ChartView;
import com.sf.demo.view.highlight.Tab;
import com.sf.demo.view.linechart.IncomeExpendInfo;
import com.sf.demo.window.pop.PopInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sufan on 17/6/17.
 */

public class DemoData {

    public static List<ChartView.Point> getChartPoint(){
        List<ChartView.Point> pointList=new ArrayList<>();
        for(int i=0;i<5;i++){
            ChartView.Point point=new ChartView.Point();
            switch (i){
                case 0:
                    point.setX(1);
                    point.setY(250);
                    break;
                case 1:
                    point.setX(1.5f);
                    point.setY(180);
                    break;
                case 2:
                    point.setX(2.0f);
                    point.setY(70);
                    break;
                case 3:
                    point.setX(4);
                    point.setY(180);
                    break;
                case 4:
                    point.setX(31);
                    point.setY(250);
                    break;
            }
            pointList.add(point);
        }
        return pointList;
    }


    public static List<MenuItem> getMenuList() {
        String[] name = {"面对面付款", "他行收款", "交易日志", "有财卡提现",
                "智能还款", "智能钱包", "定时转账", "一键归集"};

        String[] imgName = {"mdmfk", "thsk", "jyrz", "ycktx",
                "znhk", "znqb", "dszz", "yjgj"};
        List<MenuItem> menuList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            MenuItem item = new MenuItem();
            item.name = name[i];
            item.imgName = imgName[i];
            menuList.add(item);
        }
        return menuList;
    }


    public static List<MenuItem> getMyApplicationList() {
        List<MenuItem> list = new ArrayList<>();

        String[] tags = {"查询", "资金往来", "超级管家", "贷款", "便民生活"};

        String[] name0 = {"账户管理", "交易明细", "交易日志", "综合账单"}; //查询
        String[] name1 = {"转账汇款", "资金归集", "他行收款", "面对面付款",
                "E+账户存取", "有财卡提现"};             //资金往来
        String[] name2 = {"智能还款", "智能钱包", "定时转账", "一键归集"};   //超级管家
        String[] name3 = {"微网贷", "其他贷款"};  //贷款
        String[] name4 = {"手机充值", "生活缴费", "交通罚款", "游戏点卡",
                "掌上商城", "ETC缴费", "智慧校园", "彩票站点缴费",
                "公园门票"};  //便民生活

        String[] img0 = {"zhgl", "jymx", "jyrz", "zhzd"}; //查询
        String[] img1 = {"zzhk", "zjgj", "thsk", "mdmfk",
                "e_zhcq", "ycktx"};            //资金往来
        String[] img2 = {"znhk", "znqb", "dszz", "yjgj"};   //超级管家
        String[] img3 = {"wwd", "qtdk"};  //贷款
        String[] img4 = {"sjcz", "shjf", "jtfk", "yxdk",
                "zssc", "etc_jf", "zhxy", "cpzdjf",
                "gymp"};  //便民生活


        for (int i = 0; i < tags.length; i++) {
            MenuItem titleItem = new MenuItem();
            titleItem.name = tags[i];
            titleItem.isTitle = true;
            list.add(titleItem);
            if (i == 0) {
                for (int j = 0; j < name0.length; j++) {
                    MenuItem item = new MenuItem();
                    item.name = name0[j];
                    item.imgName = img0[j];
                    list.add(item);
                }
            } else if (i == 1) {
                for (int j = 0; j < name1.length; j++) {
                    MenuItem item = new MenuItem();
                    item.name = name1[j];
                    item.imgName = img1[j];
                    list.add(item);
                }

            } else if (i == 2) {
                for (int j = 0; j < name2.length; j++) {
                    MenuItem item = new MenuItem();
                    item.name = name2[j];
                    item.imgName = img2[j];
                    list.add(item);
                }

            } else if (i == 3) {
                for (int j = 0; j < name3.length; j++) {
                    MenuItem item = new MenuItem();
                    item.name = name3[j];
                    item.imgName = img3[j];
                    list.add(item);
                }

            } else {
                for (int j = 0; j < name4.length; j++) {
                    MenuItem item = new MenuItem();
                    item.name = name4[j];
                    item.imgName = img4[j];
                    list.add(item);
                }
            }
        }
        return list;
    }

    public static List<MenuItem> getDefaultMenu() {
        List<MenuItem> list = new ArrayList<>();
        String[] name = {"账户管理", "转账汇款", "手机充值", "生活缴费", "交易明细"};
        String[] img = {"zhgl", "zzhk", "sjcz", "shjf", "jymx"};

        MenuItem titleItem = new MenuItem();
        titleItem.name = "我的应用";
        titleItem.isTitle = true;
        titleItem.isAdd = false;
        list.add(titleItem);
        for (int i = 0; i < name.length; i++) {
            MenuItem item = new MenuItem();
            item.name = name[i];
            item.imgName = img[i];
            item.isAdd = false;
            list.add(item);
        }
        return list;
    }


    public static List<PopInfo> getPopList() {
        String[] names = {"账户管理", "交易明细", "交易日志", "综合账单"};
        String[] imgNames = {"zhgl", "jymx", "jyrz", "zhzd"}; //查询
        List<PopInfo> list = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            PopInfo popInfo = new PopInfo();
            popInfo.name = names[i];
            popInfo.imgName = imgNames[i];
            list.add(popInfo);
        }
        return list;
    }

    public static List<MenuParent> getExpandList() {
        String[] tags = {"查询", "资金往来", "超级管家", "贷款", "便民生活"};

        String[] name0 = {"账户管理", "交易明细", "交易日志", "综合账单"}; //查询
        String[] name1 = {"转账汇款", "资金归集", "他行收款", "面对面付款",
                "E+账户存取", "有财卡提现"};             //资金往来
        String[] name2 = {"智能还款", "智能钱包", "定时转账", "一键归集"};   //超级管家
        String[] name3 = {"微网贷", "其他贷款"};  //贷款
        String[] name4 = {"手机充值", "生活缴费", "交通罚款", "游戏点卡",
                "掌上商城", "ETC缴费", "智慧校园", "彩票站点缴费",
                "公园门票"};  //便民生活

        String[] img0 = {"zhgl", "jymx", "jyrz", "zhzd"}; //查询
        String[] img1 = {"zzhk", "zjgj", "thsk", "mdmfk",
                "e_zhcq", "ycktx"};            //资金往来
        String[] img2 = {"znhk", "znqb", "dszz", "yjgj"};   //超级管家
        String[] img3 = {"wwd", "qtdk"};  //贷款
        String[] img4 = {"sjcz", "shjf", "jtfk", "yxdk",
                "zssc", "etc_jf", "zhxy", "cpzdjf",
                "gymp"};  //便民生活

        List<MenuParent> parentList = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            MenuParent parent = new MenuParent();
            parent.name = tags[i];
            List<MenuChild> childList = new ArrayList<>();
            if (i == 0) {
                for (int j = 0; j < name0.length; j++) {
                    MenuChild child = new MenuChild();
                    child.name = name0[j];
                    child.imgName = img0[j];
                    childList.add(child);
                }
            } else if (i == 1) {
                for (int j = 0; j < name1.length; j++) {
                    MenuChild child = new MenuChild();
                    child.name = name1[j];
                    child.imgName = img1[j];
                    childList.add(child);
                }

            } else if (i == 2) {
                for (int j = 0; j < name2.length; j++) {
                    MenuChild child = new MenuChild();
                    child.name = name2[j];
                    child.imgName = img2[j];
                    childList.add(child);
                }

            } else if (i == 3) {
                for (int j = 0; j < name3.length; j++) {
                    MenuChild child = new MenuChild();
                    child.name = name3[j];
                    child.imgName = img3[j];
                    childList.add(child);
                }

            } else {
                for (int j = 0; j < name4.length; j++) {
                    MenuChild child = new MenuChild();
                    child.name = name4[j];
                    child.imgName = img4[j];
                    childList.add(child);
                }
            }
            parent.list = childList;
            parentList.add(parent);
        }
        return parentList;
    }

    public static List<String> getTextList() {
        String[] ss = {"手势登录", "指纹登录", "声纹人脸登录"};
        return Arrays.asList(ss);
    }

    public static List<IncomeExpendInfo> getIncomeExpendList() {
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月","7月"};
        String[] incomes = {"494.61", "637.96", "1218.03", "1037.84", "1000.90", "600.00","450.45"};
        String[] expends = {"637.96", "1218.03", "494.61", "1037.84", "1000.90", "600.00","450.45"};

        List<IncomeExpendInfo> list=new ArrayList<>();
        for (int i = 0; i < months.length; i++) {
            IncomeExpendInfo info=new IncomeExpendInfo();
            info.month=months[i];
            info.income=incomes[i];
            info.expend=expends[i];
            list.add(info);
        }
        return list;
    }

    public static List<Tab> getTabs(){
        String[] titles=new String[]{"首页","金融","生活","我的"};
        int [] imgIds=new int[]{R.drawable.demo_tab1,R.drawable.demo_tab2,
        R.drawable.demo_tab3,R.drawable.demo_tab4};

        List<Tab> tabs=new ArrayList<>();
        for(int i=0;i<titles.length;i++){
            Tab tab=new Tab();
            tab.title=titles[i];
            tab.imgId=imgIds[i];
            tabs.add(tab);
        }
        return tabs;
    }
}
