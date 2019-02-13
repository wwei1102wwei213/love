package com.yyspbfq.filmplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyspbfq.filmplay.R;
import com.yyspbfq.filmplay.biz.download.DownloadTask;
import com.yyspbfq.filmplay.biz.download.DownloadTaskManager;
import com.yyspbfq.filmplay.db.VideoDownloadBean;
import com.yyspbfq.filmplay.ui.activity.VideoPlayActivity;
import com.yyspbfq.filmplay.utils.BLog;

import java.util.ArrayList;
import java.util.List;

public class DownloadListAdapter extends BaseAdapter{


    private Context context;
    private List<VideoDownloadBean> list;
    private EditModelListener listener;
    private int MODEL = 0;

    public interface EditModelListener {
        void modeChangeListener();
    }

    public void setMODEL(int MODEL) {
        this.MODEL = MODEL;
    }

    public DownloadListAdapter(Context context, List<VideoDownloadBean> list, EditModelListener listener) {
        this.context = context;
        if (list==null) list = new ArrayList<>();
        this.list = list;
        selects = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView==null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_download_list_lv, parent, false);
            vh.time = convertView.findViewById(R.id.tv_video_time);
            vh.title = convertView.findViewById(R.id.tv_name);
            vh.v = convertView;
            vh.check = convertView.findViewById(R.id.v_check);
            vh.iv = convertView.findViewById(R.id.iv);
            vh.space = convertView.findViewById(R.id.v_space);
            vh.pb = convertView.findViewById(R.id.pb_download);
            vh.tv_state = convertView.findViewById(R.id.tv_state);
            vh.tv_progress = convertView.findViewById(R.id.tv_progress);
            vh.v_finish_title = convertView.findViewById(R.id.v_finish_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            final VideoDownloadBean bean = list.get(position);
            Glide.with(context).load(bean.getVideo_thumb()).crossFade().into(vh.iv);
            vh.time.setText(bean.getVideo_time()==null?"":bean.getVideo_time());
            vh.title.setText(bean.getName()==null?"":bean.getName());
            if (bean.getState()==99) {
                vh.tv_state.setText("已缓存");
                vh.tv_state.setTextColor(context.getResources().getColor(R.color.name_text_color));
                vh.tv_progress.setText(bean.getMBSize(bean.getVideo_size())+"/"+bean.getMBSize(bean.getVideo_size()));
                vh.pb.setVisibility(View.GONE);
                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MODEL==0) {
                            VideoPlayActivity.actionStart(context, bean.getVid());
                        } else {
                            if (selects.contains(bean.getVid())) {
                                selects.remove(bean.getVid());
                            } else {
                                selects.add(bean.getVid());
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
            } else {
                vh.pb.setVisibility(View.VISIBLE);
                int status = DownloadTaskManager.getInstance().checkState(bean.getVid());
                if (status == DownloadTask.TASK_STATUS_INIT) {
                    vh.tv_state.setText("正在排队");
                    vh.tv_state.setTextColor(context.getResources().getColor(R.color.name_text_color));
                } else if (status == DownloadTask.TASK_STATUS_WORKING) {
                    vh.tv_state.setTextColor(Color.GREEN);
                    vh.tv_state.setText("下载中");
                } else if (status == DownloadTask.TASK_STATUS_PAUSE) {
                    vh.tv_state.setTextColor(Color.RED);
                    vh.tv_state.setText("已暂停");
                } else if (status == DownloadTask.TASK_STATUS_CANCEL) {
                    vh.tv_state.setText("正在取消");
                    vh.tv_state.setTextColor(context.getResources().getColor(R.color.name_text_color));
                } else {
                    vh.tv_state.setTextColor(context.getResources().getColor(R.color.name_text_color));
                    vh.tv_state.setText("等待开始");
                }
                VideoDownloadBean tempBean = DownloadTaskManager.getInstance().getTagByKey(bean.getVid());
                if (tempBean!=null) {
                    vh.tv_progress.setText(tempBean.getMBSize(tempBean.getCurrent_size())+"/"+tempBean.getMBSize(bean.getVideo_size()));
                    if (tempBean.getVideo_size()==0) {
                        vh.pb.setProgress(0);
                    } else {
                        vh.pb.setProgress((int)(tempBean.getCurrent_size()*100/tempBean.getVideo_size()));
                    }
                } else {
                    if (bean.getVideo_size()==0) {
                        vh.pb.setProgress(0);
                    } else {
                        vh.pb.setProgress((int)(bean.getCurrent_size()*100/bean.getVideo_size()));
                    }
                    vh.tv_progress.setText(bean.getMBSize(bean.getCurrent_size())+"/"+bean.getMBSize(bean.getVideo_size()));
                }
                vh.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MODEL==0) {
                            if (status == DownloadTask.TASK_STATUS_INIT) {

                            } else if (status == DownloadTask.TASK_STATUS_WORKING) {
                                DownloadTaskManager.getInstance().pauseTask(bean.getVid());
                            } else if (status == DownloadTask.TASK_STATUS_PAUSE) {
                                DownloadTaskManager.getInstance().addTask(bean.getVid(), bean.getDownload_url(), bean.getVid(), bean);
                            } else if (status == DownloadTask.TASK_STATUS_CANCEL) {

                            } else {
                                if (status==-1) {
                                    DownloadTaskManager.getInstance().addTask(bean.getVid(), bean.getDownload_url(), bean.getVid(), bean);
                                }
                            }
                        } else {
                            if (selects.contains(bean.getVid())) {
                                selects.remove(bean.getVid());
                            } else {
                                selects.add(bean.getVid());
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
            }
            if (position==finishIndex) {
                vh.v_finish_title.setVisibility(View.VISIBLE);
            } else {
                vh.v_finish_title.setVisibility(View.GONE);
            }
            vh.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (MODEL==0) {
                        if (listener!=null) listener.modeChangeListener();
                    }
                    return true;
                }
            });
            if (MODEL!=0) {
                vh.check.setVisibility(View.VISIBLE);
                if (selects.contains(bean.getVid())) {
                    vh.check.setSelected(true);
                } else {
                    vh.check.setSelected(false);
                }
            } else {
                vh.check.setVisibility(View.GONE);
            }
            if (position==list.size()-1) {
                vh.space.setVisibility(View.VISIBLE);
            } else {
                vh.space.setVisibility(View.GONE);
            }
        } catch (Exception e){
            BLog.e(e);
        }
        return convertView;
    }

    private List<String> selects;
    public void update(int model) {
        this.MODEL = model;
        selects = new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<String> getSelects() {
        return selects;
    }

    public int getMODEL() {
        return MODEL;
    }

    public String getDeleteParams() {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<selects.size();i++) {
            if (i!=0) sb.append(",");
            sb.append(selects.get(i));
        }
        return sb.toString();
    }

    public void selectAll() {
        if (selects.size()==list.size()) {
            selects = new ArrayList<>();
        } else {
            selects = new ArrayList<>();
            for (int i=0;i<list.size();i++) {
                selects.add(list.get(i).getVid());
            }
        }
        notifyDataSetChanged();
    }

    public boolean isAll() {
        return selects.size()==list.size();
    }

    public void deleteSelect() {
        this.list.removeAll(selects);
        notifyDataSetChanged();
    }

    private int finishIndex;
    public void update(List<VideoDownloadBean> list) {
        this.list = list;
        boolean isFind = false;
        for (int i=0;i<list.size();i++) {
            if (list.get(i).getState()==99) {
                isFind = true;
                finishIndex = i;
                break;
            }
        }
        if (!isFind) finishIndex = -1;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView iv;
        TextView time, title;
        View v, check, space, v_finish_title;
        ProgressBar pb;
        TextView tv_state, tv_progress;
    }

}
