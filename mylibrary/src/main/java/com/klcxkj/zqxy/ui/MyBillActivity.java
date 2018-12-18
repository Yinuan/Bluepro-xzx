package com.klcxkj.zqxy.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.adapter.BillAdapter;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.databean.BillInfo;
import com.klcxkj.zqxy.databean.UserInfo;
import com.klcxkj.zqxy.response.PublicArrayData;
import com.klcxkj.zqxy.utils.GlobalTools;
import com.klcxkj.zqxy.utils.StatusBarUtil;

import net.android.tools.afinal.FinalHttp;
import net.android.tools.afinal.http.AjaxCallBack;
import net.android.tools.afinal.http.AjaxParams;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MyBillActivity extends BaseActivity {

	private LinearLayout all_bill_layout, consume_bill_layout,
			recharge_bill_layout;
	private TextView all_bill_txt, consume_bill_txt, recharge_bill_txt;
	private PullToRefreshListView mPullRefreshListView;

	private BillAdapter billAdapter;
	private ArrayList<BillInfo> allbillInfos = new ArrayList<BillInfo>();
	
	private ArrayList<BillInfo> consumebillInfos = new ArrayList<BillInfo>();
	
	private ArrayList<BillInfo> rechargebillInfos = new ArrayList<BillInfo>();

	private UserInfo mUserInfo;
	private SharedPreferences sp;

	private int current_tab = 1;

	private int all_bill_page_no=1,consume_bill_page_no=1,recharge_bill_page_no=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mybill);
		StatusBarUtil.setColor(this,getResources().getColor(R.color.base_color),0);
		sp = getSharedPreferences("adminInfo", Context.MODE_PRIVATE);
		mUserInfo = Common.getUserInfo(sp);

		initView();

	}

	private void initView() {
		showMenu("我的账单");
		all_bill_layout = (LinearLayout) findViewById(R.id.all_bill_layout);
		consume_bill_layout = (LinearLayout) findViewById(R.id.consume_bill_layout);
		recharge_bill_layout = (LinearLayout) findViewById(R.id.recharge_bill_layout);

		all_bill_txt = (TextView) findViewById(R.id.all_bill_txt);
		consume_bill_txt = (TextView) findViewById(R.id.consume_bill_txt);
		recharge_bill_txt = (TextView) findViewById(R.id.recharge_bill_txt);

		all_bill_layout.setOnClickListener(onClickListener);
		consume_bill_layout.setOnClickListener(onClickListener);
		recharge_bill_layout.setOnClickListener(onClickListener);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(MyBillActivity.this, System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
						if (current_tab == 1) {
							all_bill_page_no = 1;
							switchtab(current_tab, all_bill_page_no,true,true);
						}else if (current_tab == 2) {
							consume_bill_page_no = 1;
							switchtab(current_tab, consume_bill_page_no,true,true);
						}else if (current_tab == 3) {
							recharge_bill_page_no = 1;
							switchtab(current_tab, recharge_bill_page_no,true,true);
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(MyBillActivity.this, System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);


						refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

						if (current_tab == 1) {
							all_bill_page_no = all_bill_page_no+1;
							switchtab(current_tab, all_bill_page_no,true,false);
						}else if (current_tab == 2) {
							consume_bill_page_no = consume_bill_page_no +1;
							switchtab(current_tab, consume_bill_page_no,true,false);
						}else if (current_tab == 3) {
							recharge_bill_page_no = recharge_bill_page_no+1;
							switchtab(current_tab, recharge_bill_page_no,true,false);
						}

					}

				});

		billAdapter = new BillAdapter(MyBillActivity.this, allbillInfos);

		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(billAdapter);

		switchtab(current_tab,all_bill_page_no,true,true);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = v.getId();
			if (i == R.id.all_bill_layout) {
				switchtab(1, all_bill_page_no, false, false);

			} else if (i == R.id.consume_bill_layout) {
				switchtab(2, consume_bill_page_no, false, false);


			} else if (i == R.id.recharge_bill_layout) {
				switchtab(3, recharge_bill_page_no, false, false);


			} else {
			}

		}
	};

	private void switchtab(int tab,int page_no, boolean fresh,boolean init) {
		current_tab = tab;
		switch (tab) {
		case 1:
			all_bill_layout.setSelected(true);
			consume_bill_layout.setSelected(false);
			recharge_bill_layout.setSelected(false);

			all_bill_txt.setSelected(true);
			consume_bill_txt.setSelected(false);
			recharge_bill_txt.setSelected(false);

			getMyBill(mUserInfo, 0,all_bill_page_no,fresh,init);
			break;
		case 2:
			all_bill_layout.setSelected(false);
			consume_bill_layout.setSelected(true);
			recharge_bill_layout.setSelected(false);

			all_bill_txt.setSelected(false);
			consume_bill_txt.setSelected(true);
			recharge_bill_txt.setSelected(false);
			getMyBill(mUserInfo, 2,consume_bill_page_no,fresh,init);
			break;
		case 3:
			all_bill_layout.setSelected(false);
			consume_bill_layout.setSelected(false);
			recharge_bill_layout.setSelected(true);

			all_bill_txt.setSelected(false);
			consume_bill_txt.setSelected(false);
			recharge_bill_txt.setSelected(true);

			getMyBill(mUserInfo, 1,recharge_bill_page_no,fresh,init);
			break;

		default:
			break;
		}
	}

	private void getMyBill(UserInfo mInfo, final int type, int page_no, boolean fresh, final boolean init) {
		
		
		if (!fresh) {
			if (type == 0) {
				// 全部
				if (allbillInfos.size() > 0) {
					billAdapter.changeData(allbillInfos);
					if (mPullRefreshListView != null) {
						mPullRefreshListView.onRefreshComplete();
					}
					return;
				}

			}else if (type == 1) {
				//充值
				if (rechargebillInfos.size() > 0) {
					billAdapter.changeData(rechargebillInfos);
					if (mPullRefreshListView != null) {
						mPullRefreshListView.onRefreshComplete();
					}
					return;
				}
			}else if (type == 2) {
				//消费
				if (consumebillInfos.size() > 0) {
					billAdapter.changeData(consumebillInfos);
					if (mPullRefreshListView != null) {
						mPullRefreshListView.onRefreshComplete();
					}
					return;
				}
			}
		}

		if (Common.isNetWorkConnected(MyBillActivity.this)) {

			if (TextUtils.isEmpty(mInfo.TelPhone + "")) {
				Common.showToast(MyBillActivity.this, R.string.phonenum_null, Gravity.CENTER);
				if (mPullRefreshListView != null) {
					mPullRefreshListView.onRefreshComplete();
				}
				return;
			}


			//startProgressDialog(MyBillActivity.this);

			AjaxParams ajaxParams = new AjaxParams();
			ajaxParams.put("TelPhone", mInfo.TelPhone + "");
			ajaxParams.put("PrjID", mInfo.PrjID + "");
			ajaxParams.put("GroupID", "2");
			ajaxParams.put("BeginDT", "2017-01-01 12:00");
			ajaxParams.put("EndDT", Common.timeconvertHHmm(System.currentTimeMillis()));
			ajaxParams.put("CurNum", ""+ page_no);
			ajaxParams.put("RecTypeID", "" + type);
			ajaxParams.put("loginCode", mInfo.TelPhone+","+mInfo.loginCode);
			ajaxParams.put("phoneSystem", "Android");
			ajaxParams.put("version", MyApp.versionCode);

			loadingDialogProgress = GlobalTools.getInstance().showDailog(MyBillActivity.this,"加载.");
			new FinalHttp().get(Common.BASE_URL + "personBillList", ajaxParams,
					new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);

							if (loadingDialogProgress !=null){
								loadingDialogProgress.dismiss();
							}
							String result = t.toString();
							if (mPullRefreshListView != null) {
								mPullRefreshListView.onRefreshComplete();
							}

							PublicArrayData publicArrayData = new Gson().fromJson(result, PublicArrayData.class);
							if (publicArrayData.error_code.equals("0")) {

								Type listType = new TypeToken<ArrayList<BillInfo>>() {
								}.getType();

								if (type == 0) {
									// 全部
									ArrayList<BillInfo> billInfos =  new Gson().fromJson(publicArrayData.data, listType);
									
									if (init) {
										allbillInfos.clear();
									}

									allbillInfos.addAll(billInfos);
									billAdapter.changeData(allbillInfos);
								}else if (type == 1) {
									//充值
									ArrayList<BillInfo> billInfos =  new Gson().fromJson(publicArrayData.data, listType);
									if (init) {
										rechargebillInfos.clear();
									}
									rechargebillInfos.addAll(billInfos);
									billAdapter.changeData(rechargebillInfos);
								}else if (type == 2) {
									//消费
									ArrayList<BillInfo> billInfos =  new Gson().fromJson(
											publicArrayData.data, listType);
									if (init) {
										consumebillInfos.clear();
									}
									consumebillInfos.addAll(billInfos);
									billAdapter.changeData(consumebillInfos);
								}
							}else if (publicArrayData.error_code.equals("7")){
								Common.logout2(MyBillActivity.this, sp,dialogBuilder,publicArrayData.message);
							}

						}

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							if (mPullRefreshListView != null) {
								mPullRefreshListView.onRefreshComplete();
							}
							if (loadingDialogProgress !=null){
								loadingDialogProgress.dismiss();
							}

						}
					});

		} else {
			if (mPullRefreshListView != null) {
				mPullRefreshListView.onRefreshComplete();
			}
			Common.showNoNetworkDailog(dialogBuilder, MyBillActivity.this);
		}

	}

}
