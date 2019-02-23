package com.fuyou.play.view.activity;

import com.fuyou.play.view.BaseActivity;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class UserBirthInfoActivity extends BaseActivity /*implements View.OnClickListener, ChooseTimeInterface*/{

    //User_Birth_Info
    //0为默认 1为编辑
    private int type;
    private String born_id;
    private boolean isChanged = false;

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent();
        setContentView(R.layout.activity_user_birth_info);
        setTitleAndBack(getString(R.string.user_birth_info_title));
        type = getIntent().getIntExtra("User_Birth_Info",0);
        if (type==1){
            born_id = getIntent().getStringExtra("User_Born_Id");
        }
        initView();
    }

    private TextView tv_birth_day,tv_birth_time,tv_birth_place;
    private View v_check,v_save;
    private void initView(){
        tv_birth_day = (TextView) findViewById(R.id.tv_birth_day);
        tv_birth_time = (TextView) findViewById(R.id.tv_birth_time);
        tv_birth_place = (TextView) findViewById(R.id.tv_birth_place);
        v_check = findViewById(R.id.v_check);
        v_save = findViewById(R.id.tv_save);
        if (type==0){
            findViewById(R.id.iv_back).setVisibility(View.INVISIBLE);
        }
        tv_birth_day.setOnClickListener(this);
        tv_birth_time.setOnClickListener(this);
        tv_birth_place.setOnClickListener(this);
        v_check.setOnClickListener(this);
        v_save.setOnClickListener(this);

        CircleImageView civ = (CircleImageView) findViewById(R.id.civ);
        Glide.with(this).load(UserDataUtil.getAvatar(this)).into(civ);

        setUserView();
    }

    private void setUserView(){
        if (type == 1 || type == 2){
            tv_birth_day.setText(ConvertDataUtils.getUserBornTime(this));
            tv_birth_time.setText(ConvertDataUtils.getUserLocalTime(this));
            tv_birth_place.setText(UserDataUtil.getCity(this)+","+UserDataUtil.getCountry(this));
            if ("1".equals(UserDataUtil.getDayLight(this))){
                v_check.setSelected(true);
            } else {
                v_check.setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_birth_day:
                toSelectBirthday();
                break;
            case R.id.tv_birth_time:
                toSelectBirthTime();
                break;
            case R.id.tv_birth_place:
                startActivityForResult(new Intent(this,ChooseCountryActivity.class), 125);
                break;
            case R.id.v_check:
                toCheckDaylight();
                break;
            case R.id.tv_save:
                toSave();
                break;
        }
    }

    private ChooseTimeFragment timeFragment;
    //选择日期
    private void toSelectBirthday(){
        try {
            if (timeFragment == null){
                timeFragment = ChooseTimeFragment.getInstance(this);
            }
            timeFragment.setDefault(tv_birth_day.getText().toString());
            timeFragment.show(this.getFragmentManager(),ChooseTimeFragment.type);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"chooseTime");
        }
    }

    private ChooseHHmmFragment hmmFragment;
    //选择时分
    private void toSelectBirthTime(){
        try {
            if (hmmFragment == null){
                hmmFragment = ChooseHHmmFragment.getInstance(this);
            }
            hmmFragment.setDefault(tv_birth_time.getText().toString());
            hmmFragment.show(this.getFragmentManager(),ChooseHHmmFragment.type);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e,"chooseHHmm");
        }
    }

    @Override
    public void timeCallBack(String time, int flag) {
        if (flag==1){
            tv_birth_day.setText(TextUtils.isEmpty(time)?"":time);
        } else if (flag==2){
            tv_birth_time.setText(TextUtils.isEmpty(time)?"":time);
        }
        isChanged = true;
    }

    private void toCheckDaylight(){
        if (v_check.isSelected()){
            v_check.setSelected(false);
        } else {
            v_check.setSelected(true);
        }
        isChanged = true;
    }

    private void toSave(){
        String birthday = tv_birth_day.getText().toString();
        if (TextUtils.isEmpty(birthday)){
            showToast("Birth Date is required.");
            return;
        }
        String birthTime = tv_birth_time.getText().toString();
        if (TextUtils.isEmpty(birthTime)){
            showToast("Birth Time is required.");
            return;
        }
        String birthPlace = tv_birth_place.getText().toString();
        if (TextUtils.isEmpty(birthPlace)){
            showToast("Birth Place is required.");
            return;
        }
        if (type==1&&!isChanged){
            showToast("no change");
            return;
        }
        v_save.setClickable(false);
        initData();
    }

    private void initData(){
        if (type==0||type==2){
            Factory.getHttpRespBiz(this, Const.N_AddBirthInfo, null).post();
        } else if (type==1){
            Factory.getHttpRespBiz(this, Const.N_UserModifyBornInfo, null).post();
        }
    }

    @Override
    public Map getParamInfo(int flag, Object obj) {
        Map<String, String> map = new HashMap<>();
        if (flag == Const.N_AddBirthInfo){
            String time = tv_birth_time.getText().toString();
            int hhmm = TimeUtils.getMillForHHmm(time);
            LogCustom.show("hhmm:"+hhmm);
            String date = tv_birth_day.getText().toString();
            long bornDaySec = TimeUtils.formatTimeForStrAndZoneOffset(date);
            LogCustom.show("bornDaySec:"+bornDaySec);
            long bornTime = bornDaySec + hhmm;
            map.put("born_time",bornTime+"");
            String where = tv_birth_place.getText().toString().trim();
            where = where.replace(", ", ",");
            map.put("birth_place", where);
            map.put("day_light", v_check.isSelected()?"1":"0");
            map.put("born_name", UserDataUtil.getUserName(this));
            map.put("is_me", "0");
        } else if (flag == Const.N_UserModifyBornInfo){
            String time = tv_birth_time.getText().toString();
            int hhmm = TimeUtils.getMillForHHmm(time);
            LogCustom.show("hhmm:"+hhmm);
            String date = tv_birth_day.getText().toString();
            long bornDaySec = TimeUtils.formatTimeForStrAndZoneOffset(date);
            LogCustom.show("bornDaySec:"+bornDaySec);
            long bornTime = bornDaySec + hhmm;
            map.put("born_time",bornTime+"");
            String where = tv_birth_place.getText().toString().trim();
            where = where.replace(", ", ",");
            map.put("birth_place", where);
            map.put("day_light", v_check.isSelected()?"1":"0");
            map.put("is_me", "0");
            map.put("born_id", born_id);
        } else if (flag == Const.N_AstrolabeModify){
            HORChartResult result = HORDataManger.getInstance().getUserHORData(this);
            List<HORPlanetData> planetDataList = result.getPlanetDatas();
            List<UpdateAstrolabeEntity> temp = new ArrayList<>();
            for (HORPlanetData item:planetDataList){
                UpdateAstrolabeEntity entity = new UpdateAstrolabeEntity();
                entity.setIndex(item.getPlanetIdx());
                entity.setSign(item.getSign().getId());
                entity.setOffsetAngle(item.getOffsetAngle());
                entity.setHouse(item.getHouse());
                temp.add(entity);
            }
            map.put("planet", temp.toString());
            map.put("asc_sign", result.getAscSign().getId()+"");
            map.put("element", ChartReportUtils.getUserElement(result)+"");
            map.put("personality", ChartReportUtils.getUserPersonality(result)+"");
            map.put("planetary", "3");
        }
        return map;
    }

    @Override
    public byte[] getPostParams(int flag, Object obj) {
        return new byte[0];
    }

    @Override
    public void toActivity(Object response, int flag, Object obj) {
        if (flag == Const.N_AddBirthInfo){
            UserBirthInfoBean bean = (UserBirthInfoBean) response;
            if (bean.getStatus()==200){
                UserDataUtil.setIsHaveCreateBirthInfo(this, "1");
                UserDataUtil.saveUserData(this, bean.getData(), 2);
                SubHelper.getInstance().setSubMainData(null);
                HORDataManger.getInstance().clearUserHORData();
                Factory.getHttpRespBiz(this, Const.N_AstrolabeModify, null).post();
                if (type==0){
                    if(Const.TDFlag) {
                        Factory.TDCustomEvent(UserBirthInfoActivity.this, "Save_birthInfo_SignUp");
                    }
                    startActivity(MainActivity.class);
                } else if (type==2){
                    setResult(Const.INTENT_RESULT_EDIT_PROFILE);
                }
                finish();
            } else {
                showBaseError(bean);
            }
        } else if(flag == Const.N_UserModifyBornInfo){
            UserBirthInfoBean bean = (UserBirthInfoBean) response;
            if (bean.getStatus()==200){
                UserDataUtil.saveUserData(this, bean.getData(), 2);
                SubHelper.getInstance().setSubMainData(null);
                HORDataManger.getInstance().clearUserHORData();
                Factory.getHttpRespBiz(this, Const.N_AstrolabeModify, null).post();
                toFinish();
            } else {
                showBaseError(bean);
            }
        } else if (flag == Const.N_AstrolabeModify){

        }
    }

    @Override
    public void showLoading(int flag, Object obj) {
        if (flag == Const.N_AstrolabeModify){

        } else {
            v_save.setClickable(false);
            showWaitDialog("Saving...");
        }
    }

    @Override
    public void hideLoading(int flag, Object obj) {
        if (flag == Const.N_AstrolabeModify){

        } else {
            v_save.setClickable(true);
            closeWaitDialog();
        }
    }

    @Override
    public void showError(int flag, Object obj) {
        if (flag == Const.N_AstrolabeModify){

        } else {
            v_save.setClickable(true);
            closeWaitDialog();
        }
    }

    private void toFinish(){
        setResult(Const.INTENT_RESULT_EDIT_PROFILE);
        finish();
    }

    private ProgressDialog waitDialog;
    private boolean isRun = false;
    private void showWaitDialog(String message) {
        closeWaitDialog();
        if (waitDialog==null){
            waitDialog = new ProgressDialog(this);
        }
        waitDialog.setMessage(message);
        waitDialog.show();
        isRun = true;
    }

    private void closeWaitDialog(){
        if (waitDialog!=null){
            waitDialog.dismiss();
        }
        isRun = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogCustom.show("resultCode:"+requestCode);
        if (resultCode==Const.INTENT_RESULT_PLACE_DATA){
            if (data!=null){
                PlaceDataBean bean = (PlaceDataBean) data.getSerializableExtra("Place_Result_Data");
                if (bean!=null){
                    LogCustom.show("resultCode:"+bean.toString());
                    tv_birth_place.setText(bean.getCity()+", "+bean.getCountry());
                    isChanged = true;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (type==0){
            if (!isRun){
                startActivity(MainActivity.class);
                finish();
            }
        } else {
            super.onBackPressed();
        }

    }*/
}
