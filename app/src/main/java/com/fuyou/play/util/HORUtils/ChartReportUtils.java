package com.fuyou.play.util.HORUtils;


/**
 * Created by Administrator on 2017-10-13.
 */

public class ChartReportUtils {

    /*public static int getUserElement(HORChartResult result){
        if (result==null) return 1;
        List<List<String>> data = getElementList(result);
        int tag = 0;
        for (List<String> item:data){
            tag++;
            item.add(tag+"");
        }
        Collections.sort(data, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> list1, List<String> list2) {
                if (list1.size()!=list2.size()){
                    return list1.size() - list2.size();
                } else {
                    if (list1.contains("Asc")||list2.contains("Asc")){
                        if (list1.contains("Asc")){
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (list1.contains("Sun")||list2.contains("Sun")){
                            if (list1.contains("Sun")){
                                return 1;
                            } else {
                                return -1;
                            }
                        } else {
                            if (list1.contains("Moon")||list2.contains("Moon")){
                                if (list1.contains("Moon")){
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                }
                return 0;
            }
        });
        List<String> xList = data.get(3);
        if (xList.contains("4")){
            return 4;
        } else if (xList.contains("3")){
            return 3;
        } else if (xList.contains("2")){
            return 2;
        } else {
            return 1;
        }
    }*/

    /*public static int getUserElement(List<List<String>> temp){
        if (temp==null||temp.size()!=4) return 1;
        List<List<String>> data = new ArrayList<>();
        for (List<String> item:temp){
            List<String> itemList = new ArrayList<>();
            itemList.addAll(item);
            data.add(itemList);
        }
        int tag = 0;
        for (List<String> item:data){
            tag++;
            item.add(tag+"");
        }
        Collections.sort(data, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> list1, List<String> list2) {
                if (list1.size()!=list2.size()){
                    return list1.size() - list2.size();
                } else {
                    if (list1.contains("Asc")||list2.contains("Asc")){
                        if (list1.contains("Asc")){
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (list1.contains("Sun")||list2.contains("Sun")){
                            if (list1.contains("Sun")){
                                return 1;
                            } else {
                                return -1;
                            }
                        } else {
                            if (list1.contains("Moon")||list2.contains("Moon")){
                                if (list1.contains("Moon")){
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                }
                return 0;
            }
        });
        List<String> xList = data.get(3);
        if (xList.contains("4")){
            return 4;
        } else if (xList.contains("3")){
            return 3;
        } else if (xList.contains("2")){
            return 2;
        } else {
            return 1;
        }
    }

    public static List<List<String>> getElementList(HORChartResult result){
        if (result==null) return null;
        List<String> fireList = new ArrayList<>();
        List<String> earthList = new ArrayList<>();
        List<String> windList = new ArrayList<>();
        List<String> waterList = new ArrayList<>();
        String temp = FileUtils.getElementForSign(result.getAscSign().getName());
        if ("Fire".equals(temp)){
            fireList.add("Asc");
        }else if ("Earth".equals(temp)){
            earthList.add("Asc");
        }else if ("Water".equals(temp)){
            waterList.add("Asc");
        }else {
            windList.add("Asc");
        }
        for (int i=0;i<5;i++){
            HORPlanetData item = result.getPlanetDatas().get(i);
            temp = FileUtils.getElementForSign(item.getSign().getName());
            String name = item.getPlanet().getName();
            if ("Fire".equals(temp)){
                fireList.add(name);
            }else if ("Earth".equals(temp)){
                earthList.add(name);
            }else if ("Water".equals(temp)){
                waterList.add(name);
            }else {
                windList.add(name);
            }
        }
        List<List<String>> data = new ArrayList<>();
        data.add(fireList);
        data.add(earthList);
        data.add(windList);
        data.add(waterList);
        return data;
    }

    public static int getUserPersonality(HORChartResult data){
        List<HorosNormalEntity> temp = new ArrayList<>();
        for (int i=0;i<5;i++){
            temp.add(data.getPlanetDatas().get(i).getSign());
        }
        temp.add(data.getAscSign());
        List<PersonalityTypeBean> list = ChartReportUtils.getPersonalityTypeData(temp);
        Collections.sort(list, new Comparator<PersonalityTypeBean>() {
            @Override
            public int compare(PersonalityTypeBean a, PersonalityTypeBean b) {
                List<String> list1 = a.getData();
                List<String> list2 = b.getData();
                if (list1.size()!=list2.size()){
                    return list1.size() - list2.size();
                } else {
                    if (list1.contains("Asc")||list2.contains("Asc")){
                        if (list1.contains("Asc")){
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (list1.contains("Sun")||list2.contains("Sun")){
                            if (list1.contains("Sun")){
                                return 1;
                            } else {
                                return -1;
                            }
                        } else {
                            if (list1.contains("Moon")||list2.contains("Moon")){
                                if (list1.contains("Moon")){
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                }
                return b.getData().size() - a.getData().size();
            }
        });
        return list.get(3).getId()+1;
    }


    public static int getUserPersonality(List<PersonalityTypeBean> list){
        if (list==null||list.size()!=4) return 1;
        Collections.sort(list, new Comparator<PersonalityTypeBean>() {
            @Override
            public int compare(PersonalityTypeBean a, PersonalityTypeBean b) {
                List<String> list1 = a.getData();
                List<String> list2 = b.getData();
                if (list1.size()!=list2.size()){
                    return list1.size() - list2.size();
                } else {
                    if (list1.contains("Asc")||list2.contains("Asc")){
                        if (list1.contains("Asc")){
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (list1.contains("Sun")||list2.contains("Sun")){
                            if (list1.contains("Sun")){
                                return 1;
                            } else {
                                return -1;
                            }
                        } else {
                            if (list1.contains("Moon")||list2.contains("Moon")){
                                if (list1.contains("Moon")){
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        }
                    }
                }
                return b.getData().size() - a.getData().size();
            }
        });
        return list.get(3).getId()+1;
    }

    public static List<PersonalityTypeBean> getPersonalityTypeData(List<HorosNormalEntity> list){
        System.out.println(list.toString());
        List<PersonalityTypeBean> result = getEmptyList();
        if (list==null||list.size()!=6) return result;
        try {
            getAscAndSunPT(result, list.get(0).getId(), 1);
            getMoonPT(result, list.get(1).getId());
            getVenusPT(result, list.get(2).getId());
            getMarsPT(result, list.get(3).getId());
            getMercuryPT(result, list.get(4).getId());
            getAscAndSunPT(result, list.get(5).getId(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    *//**
     *
     * @param list 数据
     * @param id 星座编号
     * @param type 标识 0：ASC  1：SUN
     * @return
     *//*
    public static List<PersonalityTypeBean> getAscAndSunPT(List<PersonalityTypeBean> list, int id, int type){
        List<String> temp = null;
        String str = type==0?"Asc":"Sun";
        switch (id){
            case 6:
            case 8:
            case 9:

                temp = list.get(0).getData();
                temp.add(str);
                list.get(0).setData(temp);
                break;
            case 0:
            case 2:
            case 4:
                temp = list.get(1).getData();
                temp.add(str);
                list.get(1).setData(temp);
                break;
            case 3:
            case 5:
            case 7:
                temp = list.get(2).getData();
                temp.add(str);
                list.get(2).setData(temp);
                break;
            case 1:
            case 10:
            case 11:
                temp = list.get(3).getData();
                temp.add(str);
                list.get(3).setData(temp);
                break;
        }
        return list;
    }
    *//**
     *
     * @param list 数据
     * @param id 星座编号
     * @return
     *//*
    public static List<PersonalityTypeBean> getMarsPT(List<PersonalityTypeBean> list, int id){
        List<String> temp = null;
        String str = "Mars";
        switch (id){
            case 0:
            case 4:
            case 9:
                temp = list.get(0).getData();
                temp.add(str);
                list.get(0).setData(temp);
                break;
            case 6:
            case 7:
            case 8:
                temp = list.get(1).getData();
                temp.add(str);
                list.get(1).setData(temp);
                break;
            case 2:
            case 5:
            case 11:
                temp = list.get(2).getData();
                temp.add(str);
                list.get(2).setData(temp);
                break;
            case 1:
            case 10:
            case 3:
                temp = list.get(3).getData();
                temp.add(str);
                list.get(3).setData(temp);
                break;
        }
        return list;
    }
    *//**
     *
     * @param list 数据
     * @param id 星座编号
     * @return
     *//*
    public static List<PersonalityTypeBean> getMoonPT(List<PersonalityTypeBean> list, int id){
        List<String> temp = null;
        String str = "Moon";
        switch (id){
            case 5:
            case 6:
            case 8:
                temp = list.get(0).getData();
                temp.add(str);
                list.get(0).setData(temp);
                break;
            case 2:
            case 3:
            case 4:
                temp = list.get(1).getData();
                temp.add(str);
                list.get(1).setData(temp);
                break;
            case 0:
            case 7:
            case 9:
                temp = list.get(2).getData();
                temp.add(str);
                list.get(2).setData(temp);
                break;
            case 1:
            case 10:
            case 11:
                temp = list.get(3).getData();
                temp.add(str);
                list.get(3).setData(temp);
                break;
        }
        return list;
    }
    *//**
     *
     * @param list 数据
     * @param id 星座编号
     * @return
     *//*
    public static List<PersonalityTypeBean> getVenusPT(List<PersonalityTypeBean> list, int id){
        List<String> temp = null;
        String str = "Venus";
        switch (id){
            case 0:
            case 1:
            case 11:
                temp = list.get(0).getData();
                temp.add(str);
                list.get(0).setData(temp);
                break;
            case 2:
            case 7:
            case 4:
                temp = list.get(1).getData();
                temp.add(str);
                list.get(1).setData(temp);
                break;
            case 3:
            case 5:
            case 6:
                temp = list.get(2).getData();
                temp.add(str);
                list.get(2).setData(temp);
                break;
            case 8:
            case 10:
            case 9:
                temp = list.get(3).getData();
                temp.add(str);
                list.get(3).setData(temp);
                break;
        }
        return list;
    }
    *//**
     *
     * @param list 数据
     * @param id 星座编号
     * @return
     *//*
    public static List<PersonalityTypeBean> getMercuryPT(List<PersonalityTypeBean> list, int id){
        List<String> temp = null;
        String str = "Mercury";
        switch (id){
            case 0:
            case 2:
            case 9:
                temp = list.get(0).getData();
                temp.add(str);
                list.get(0).setData(temp);
                break;
            case 5:
            case 7:
            case 10:
                temp = list.get(1).getData();
                temp.add(str);
                list.get(1).setData(temp);
                break;
            case 4:
            case 8:
            case 6:
                temp = list.get(2).getData();
                temp.add(str);
                list.get(2).setData(temp);
                break;
            case 1:
            case 3:
            case 11:
                temp = list.get(3).getData();
                temp.add(str);
                list.get(3).setData(temp);
                break;
        }
        return list;
    }

    public static List<PersonalityTypeBean> getEmptyList(){
        List<PersonalityTypeBean> list = new ArrayList<>();
        for (int i=0;i<4;i++){
            List<String> temp = new ArrayList<>();
            PersonalityTypeBean bean = new PersonalityTypeBean();
            bean.setId(i);
            bean.setData(temp);
            list.add(bean);
        }
        return list;
    }*/

}
