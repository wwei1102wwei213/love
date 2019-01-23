package com.yyspbfq.filmplay.biz;

import android.os.Handler;

public class ParseCityBiz implements Runnable{

    public static final int MSG_LOAD_SUCCESS = 0x0001;
    public static final int MSG_LOAD_FAILED = 0x0002;

    private Handler handler;

    public ParseCityBiz(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        /*try {
            String cityJson = SystemUtils.getAssetsJson(BaseApplication.getInstance(), "area.json");
            List<ProvinceBean> provinceBeen = GsonUtil.toList(cityJson, ProvinceBean.class);
            //添加省份数据
            //注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
            //PickerView会通过getPickerViewText方法获取字符串显示出来。
            List<List<String>> optionsCitys = new ArrayList<>();
            List<List<List<String>>> optionAreas = new ArrayList<>();
            for (int i = 0; i < provinceBeen.size(); i++) {//遍历省份
                List<String> provinceCitys = new ArrayList<>();//该省的城市列表（第二级）
                List<List<String>> provinceAreas = new ArrayList<>();//该省的所有地区列表（第三极）
                List<CityBean> iCitys = provinceBeen.get(i).getCitys();
                for (int c = 0; c < iCitys.size(); c++) {//遍历该省份的所有城市
                    CityBean cityBean = iCitys.get(c);
                    provinceCitys.add(cityBean.getArea_name());//添加城市
                    List<String> cityAreas = new ArrayList<>();//该城市的所有地区列表
                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (cityBean.getArea_name() == null
                            || cityBean.getAreas().size() == 0) {
                        cityAreas.add("");
                    } else {
                        List<AreaBean> cAreas = cityBean.getAreas();
                        for (int d = 0; d < cAreas.size(); d++) {//该城市对应地区所有数据
                            String areaName = cAreas.get(d).getArea_name();
                            cityAreas.add(areaName);//添加该城市所有地区数据
                        }
                    }
                    provinceAreas.add(cityAreas);//添加该省所有地区数据
                }
                //添加城市数据
                optionsCitys.add(provinceCitys);
                //添加地区数据
                optionAreas.add(provinceAreas);
            }
            if (handler!=null) {
                CityDataBean bean = new CityDataBean();
                bean.setOptionProvince(provinceBeen);
                bean.setOptionsCitys(optionsCitys);
                bean.setOptionAreas(optionAreas);
                Message msg = new Message();
                msg.what = MSG_LOAD_SUCCESS;
                msg.obj = bean;
                handler.sendMessage(msg);
            }
        } catch (Exception e){
            BLog.e(e);
            if (handler!=null) {
                handler.sendEmptyMessage(MSG_LOAD_FAILED);
            }
        }*/
    }
}
