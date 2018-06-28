package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityAlarmEditBinding;
import com.moemoe.lalala.event.AlarmEvent;
import com.moemoe.lalala.greendao.gen.AlarmClockEntityDao;
import com.moemoe.lalala.model.entity.AlarmClockEntity;
import com.moemoe.lalala.model.entity.DeskMateEntity;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.PhoneAlarmEditV2Fragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.TreeMap;

public class AlarmEditActivity extends BaseActivity {

    private ActivityAlarmEditBinding binding;
    /**
     * 闹钟实例
     */
    private AlarmClockEntity mAlarmClock;
    /**
     * 周一按钮状态，默认未选中
     */
    private Boolean isMondayChecked = false;

    /**
     * 周二按钮状态，默认未选中
     */
    private Boolean isTuesdayChecked = false;

    /**
     * 周三按钮状态，默认未选中
     */
    private Boolean isWednesdayChecked = false;

    /**
     * 周四按钮状态，默认未选中
     */
    private Boolean isThursdayChecked = false;

    /**
     * 周五按钮状态，默认未选中
     */
    private Boolean isFridayChecked = false;

    /**
     * 周六按钮状态，默认未选中
     */
    private Boolean isSaturdayChecked = false;

    /**
     * 周日按钮状态，默认未选中
     */
    private Boolean isSundayChecked = false;
    /**
     * 保存重复描述信息String
     */
    private StringBuilder mRepeatStr;
    /**
     * 按键值顺序存放重复描述信息
     */
    private TreeMap<Integer, String> mMap;

    private BottomMenuFragment mBottomMenuFragment;

    private ArrayList<DeskMateEntity> deskMateEntities;

    private boolean isUpdate;
    private ImageView mIvList;
    private TextView mTvTitle;
    private ImageView mIvBack;

    public static void newInstance(Context context, AlarmClockEntity entity) {
        Intent intent = new Intent(context, AlarmEditActivity.class);
        intent.putExtra("alarm", entity);
        context.startActivity(intent);
    }

    @Override
    protected void initComponent() {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_edit);
        binding.setPresenter(new Presenter());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mTvTitle.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
        mIvList = findViewById(R.id.iv_menu_list);
        mIvList.setImageResource(R.drawable.btn_alarm_save);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mAlarmClock = getIntent().getParcelableExtra("alarm");
            isUpdate = true;
        } else {
            isUpdate = false;
        }
        if (mAlarmClock == null) {
            mAlarmClock = new AlarmClockEntity();
            mAlarmClock.setId(-1);
            mAlarmClock.setOnOff(true); // 闹钟默认开启
            mAlarmClock.setRepeat("只响一次");
            mAlarmClock.setWeeks(null);
            binding.tvName.setText("小莲");
            mAlarmClock.setRoleName("小莲");
            mAlarmClock.setRoleId("len");
            binding.tvType.setText("按时休息");
            mAlarmClock.setRingName("按时休息");
            mAlarmClock.setRingUrl(R.raw.vc_alerm_len_sleep_1);
            Calendar calendar = Calendar.getInstance();
            mAlarmClock.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            // 保存闹钟实例的分钟
            mAlarmClock.setMinute(calendar.get(Calendar.MINUTE));
            binding.tvTime.setText(StringUtils.formatTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        } else {

            binding.tvDelete.setVisibility(View.VISIBLE);
            binding.tvDelete.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    EventBus.getDefault().post(new AlarmEvent(mAlarmClock, 2));
                    finish();
                }
            });
            binding.tvName.setText(mAlarmClock.getRoleName());
            binding.tvType.setText(mAlarmClock.getRingName());
            binding.etMark.setText(mAlarmClock.getTag());
            binding.tvTime.setText(StringUtils.formatTime(mAlarmClock.getHour(), mAlarmClock.getMinute()));
            if (mAlarmClock.getWeeks() == null) {
                binding.ivRepeat.setSelected(true);
            } else {
                binding.ivRepeat.setSelected(false);
                if (mAlarmClock.getWeeks().contains("2")) {
                    binding.tvWeek1.setSelected(true);
                    isMondayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("3")) {
                    binding.tvWeek2.setSelected(true);
                    isTuesdayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("4")) {
                    binding.tvWeek3.setSelected(true);
                    isWednesdayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("5")) {
                    binding.tvWeek4.setSelected(true);
                    isThursdayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("6")) {
                    binding.tvWeek5.setSelected(true);
                    isFridayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("7")) {
                    binding.tvWeek6.setSelected(true);
                    isSaturdayChecked = true;
                }
                if (mAlarmClock.getWeeks().contains("1")) {
                    binding.tvWeek7.setSelected(true);
                    isSundayChecked = true;
                }
            }
        }
        mRepeatStr = new StringBuilder();
        mMap = new TreeMap<>();
        binding.etMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = binding.etMark.getText();
                int len = editable.length();
                if (len > 7) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    String newStr = str.substring(0, 7);
                    binding.etMark.setText(newStr);
                    editable = binding.etMark.getText();
                    int newLen = editable.length();
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    Selection.setSelection(editable, selEndIndex);
                }
                if (!TextUtils.isEmpty(editable)) {
                    mAlarmClock.setTag(editable.toString());
                } else {
                    mAlarmClock.setTag("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("定时提醒");
        mIvList.setVisibility(View.VISIBLE);
        mIvList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                sendAlarmEvent(isUpdate);
                finish();
            }
        });
    }

    public void sendAlarmEvent(boolean isUpdate) {
        AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
        if (mAlarmClock.getId() == -1) {
            AlarmClockEntity entity = dao.queryBuilder().orderDesc(AlarmClockEntityDao.Properties.Id).limit(1).unique();
            long id;
            if (entity == null) {
                id = 0;
            } else {
                id = entity.getId();
            }
            mAlarmClock.setId(id + 1);
        }
        dao.insertOrReplace(mAlarmClock);
        EventBus.getDefault().post(new AlarmEvent(mAlarmClock, isUpdate ? 3 : 1));
    }

    @Override
    protected void initData() {

    }

    /**
     * 角色选择
     */
    private void showRole() {
        mBottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        deskMateEntities = PreferenceUtils.getAuthorInfo().getDeskMateEntities();
        int count = 0;
        for (DeskMateEntity entity : deskMateEntities) {
            MenuItem item = new MenuItem(count, entity.getRoleName());
            items.add(item);
            count++;
        }
        mBottomMenuFragment.setMenuItems(items);
        mBottomMenuFragment.setShowTop(false);
        mBottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        mBottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                binding.tvName.setText(deskMateEntities.get(itemId).getRoleName());
                mAlarmClock.setRoleName(deskMateEntities.get(itemId).getRoleName());
                mAlarmClock.setRoleId(deskMateEntities.get(itemId).getRoleOf());
            }
        });
        mBottomMenuFragment.show(getSupportFragmentManager(), "Alarm");
    }

    /**
     * 铃声type
     */
    private void showType() {
        mBottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(1, "按时休息");
        items.add(item);
        item = new MenuItem(2, "起床提醒");
        items.add(item);
        item = new MenuItem(3, "其他事宜");
        items.add(item);
        mBottomMenuFragment.setMenuItems(items);
        mBottomMenuFragment.setShowTop(false);
        mBottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        mBottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 1) {
                    binding.tvType.setText("按时休息");
                    mAlarmClock.setRingName("按时休息");
                    switch (mAlarmClock.getRoleId()) {
                        case "mei":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_mei_sleep_1);
                            break;
                        case "sari":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_sari_sleep_1);
                            break;
                        default:
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_len_sleep_1);
                            break;
                    }
                }
                if (itemId == 2) {
                    binding.tvType.setText("起床提醒");
                    mAlarmClock.setRingName("起床提醒");
                    switch (mAlarmClock.getRoleId()) {
                        case "mei":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_mei_wakeup_1);
                            break;
                        case "sari":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_sari_wakeup_1);
                            break;
                        default:
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_len_wakeup_1);
                            break;
                    }
                }
                if (itemId == 3) {
                    binding.tvType.setText("其他事宜");
                    mAlarmClock.setRingName("其他事宜");
                    switch (mAlarmClock.getRoleId()) {
                        case "mei":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_mei_remind_1);
                            break;
                        case "sari":
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_sari_remind_1);
                            break;
                        default:
                            mAlarmClock.setRingUrl(R.raw.vc_alerm_len_remind_1);
                            break;
                    }
                }
            }
        });
        mBottomMenuFragment.show(getSupportFragmentManager(), "Alarm");
    }


    private void setRepeatDescribe() {
        if (isMondayChecked & isTuesdayChecked & isWednesdayChecked
                & isThursdayChecked & isFridayChecked & isSaturdayChecked
                & isSundayChecked) {
            mAlarmClock.setRepeat("每天");
            // 响铃周期
            mAlarmClock.setWeeks("2,3,4,5,6,7,1");
            // 周一到周五全部选中
        } else if (!isMondayChecked & !isTuesdayChecked & !isWednesdayChecked
                & !isThursdayChecked & !isFridayChecked & !isSaturdayChecked
                & !isSundayChecked) {
            mAlarmClock.setRepeat("只响一次");
            mAlarmClock.setWeeks(null);
        } else {
            mRepeatStr.setLength(0);
            mRepeatStr.append("周");
            Collection<String> col = mMap.values();
            for (String aCol : col) {
                mRepeatStr.append(aCol).append("、");
            }
            // 去掉最后一个"、"
            mRepeatStr.setLength(mRepeatStr.length() - 1);
            mAlarmClock.setRepeat(mRepeatStr.toString());

            mRepeatStr.setLength(0);
            if (isMondayChecked) {
                mRepeatStr.append("2,");
            }
            if (isTuesdayChecked) {
                mRepeatStr.append("3,");
            }
            if (isWednesdayChecked) {
                mRepeatStr.append("4,");
            }
            if (isThursdayChecked) {
                mRepeatStr.append("5,");
            }
            if (isFridayChecked) {
                mRepeatStr.append("6,");
            }
            if (isSaturdayChecked) {
                mRepeatStr.append("7,");
            }
            if (isSundayChecked) {
                mRepeatStr.append("1,");
            }
            mAlarmClock.setWeeks(mRepeatStr.toString());
        }
    }


    public class Presenter {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_name:
                    showRole();
                    break;
                case R.id.tv_type:
                    showType();
                    break;
                case R.id.tv_time:

                    final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                    alertDialogUtil.createTimepickerDialog(AlarmEditActivity.this, mAlarmClock.getHour(), mAlarmClock.getMinute());
                    alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                        @Override
                        public void CancelOnClick() {
                            alertDialogUtil.dismissDialog();
                        }

                        @Override
                        public void ConfirmOnClick() {
                            mAlarmClock.setHour(alertDialogUtil.getHour());
                            // 保存闹钟实例的分钟
                            mAlarmClock.setMinute(alertDialogUtil.getMinute());
                            binding.tvTime.setText(StringUtils.formatTime(alertDialogUtil
                                    .getHour(), alertDialogUtil.getMinute()));
                            alertDialogUtil.dismissDialog();
                        }
                    });
                    alertDialogUtil.showDialog();
                    break;
                case R.id.tv_week_1:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek1.setSelected(!binding.tvWeek1.isSelected());
                        isMondayChecked = !isMondayChecked;
                        if (isMondayChecked) {
                            mMap.put(1, "一");
                        } else {
                            mMap.remove(1);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_2:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek2.setSelected(!binding.tvWeek2.isSelected());
                        isTuesdayChecked = !isTuesdayChecked;
                        if (isTuesdayChecked) {
                            mMap.put(2, "二");
                        } else {
                            mMap.remove(2);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_3:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek3.setSelected(!binding.tvWeek3.isSelected());
                        isWednesdayChecked = !isWednesdayChecked;
                        if (isWednesdayChecked) {
                            mMap.put(3, "三");
                        } else {
                            mMap.remove(3);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_4:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek4.setSelected(!binding.tvWeek4.isSelected());
                        isThursdayChecked = !isThursdayChecked;
                        if (isThursdayChecked) {
                            mMap.put(4, "四");
                        } else {
                            mMap.remove(4);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_5:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek5.setSelected(!binding.tvWeek5.isSelected());
                        isFridayChecked = !isFridayChecked;
                        if (isFridayChecked) {
                            mMap.put(5, "五");
                        } else {
                            mMap.remove(5);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_6:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek6.setSelected(!binding.tvWeek6.isSelected());
                        isSaturdayChecked = !isSaturdayChecked;
                        if (isSaturdayChecked) {
                            mMap.put(6, "六");
                        } else {
                            mMap.remove(6);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.tv_week_7:
                    if (binding.ivRepeat.isSelected()) {
                        binding.tvWeek7.setSelected(!binding.tvWeek7.isSelected());
                        isSundayChecked = !isSundayChecked;
                        if (isSundayChecked) {
                            mMap.put(7, "日");
                        } else {
                            mMap.remove(7);
                        }
                        setRepeatDescribe();
                    }
                    break;
                case R.id.iv_repeat:
                    binding.ivRepeat.setSelected(!binding.ivRepeat.isSelected());
                    if (!binding.ivRepeat.isSelected()) {
                        mMap.clear();
                        isMondayChecked = false;
                        isTuesdayChecked = false;
                        isWednesdayChecked = false;
                        isThursdayChecked = false;
                        isFridayChecked = false;
                        isSaturdayChecked = false;
                        isSundayChecked = false;
                        binding.tvWeek1.setSelected(false);
                        binding.tvWeek2.setSelected(false);
                        binding.tvWeek3.setSelected(false);
                        binding.tvWeek4.setSelected(false);
                        binding.tvWeek5.setSelected(false);
                        binding.tvWeek6.setSelected(false);
                        binding.tvWeek7.setSelected(false);
                        setRepeatDescribe();
                    }
                    break;
            }
        }
    }
}
