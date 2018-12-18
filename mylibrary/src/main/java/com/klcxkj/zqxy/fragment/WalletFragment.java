package com.klcxkj.zqxy.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.adapter.CurrentPackageAdapter;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.databean.PackageData;
import com.klcxkj.zqxy.databean.PayResult;
import com.klcxkj.zqxy.databean.Recghangebean;
import com.klcxkj.zqxy.databean.UserInfo;
import com.klcxkj.zqxy.response.PublicGetData;
import com.klcxkj.zqxy.response.PublicGivenMoneyData;
import com.klcxkj.zqxy.ui.MainUserActivity;
import com.klcxkj.zqxy.ui.MyInfoActivity;
import com.klcxkj.zqxy.ui.SearchBratheDeviceActivity;
import com.klcxkj.zqxy.utils.GlobalTools;
import com.klcxkj.zqxy.widget.Effectstype;
import com.klcxkj.zqxy.widget.LoadingDialogProgress;
import com.klcxkj.zqxy.widget.MyGridView;

import net.android.tools.afinal.FinalHttp;
import net.android.tools.afinal.http.AjaxCallBack;
import net.android.tools.afinal.http.AjaxParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class WalletFragment extends BaseFragment {

	private TextView cash_account_txt;
	private TextView give_account_txt;
	private MyGridView package_grid;
	private TextView package_item_tips;
	private Button recharge_btn;

	private ArrayList<PackageData> currentPackageArrayList = new ArrayList<PackageData>();
	private CurrentPackageAdapter currentPackageAdapter;
	private SharedPreferences sp;
	private UserInfo mUserInfo;
	public static boolean tg =false;
	private LoadingDialogProgress loadingDialogProgress;

	private int monney;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = getActivity().getSharedPreferences("adminInfo", Context.MODE_PRIVATE);



		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void msgEvent(String msg){
		if (msg.equals("wx_pay_success")){
			updateUserInfo(mUserInfo);
		}
	}
	private void initPackage() {
		currentPackageArrayList.clear();

		//根据项目获取充值金额
		getMonneyByPro();

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_mywallet, container, false);
		mUserInfo = Common.getUserInfo(sp);
		initPackage();
		initView(view);
		initData();


		return view;
	}

	private void initData() {
		if (mUserInfo != null) {
			cash_account_txt.setText(Common.getShowMonty(mUserInfo.AccMoney, ""));
			give_account_txt.setText(Common.getShowMonty(mUserInfo.GivenAccMoney, ""));
		}
	}

	private void initView(View view) {
		RelativeLayout title_layout = (RelativeLayout) view.findViewById(R.id.title_layout);
		if (tg){
			title_layout.setVisibility(View.GONE);
		}else {
			title_layout.setVisibility(View.VISIBLE);
		}
		cash_account_txt = (TextView) view.findViewById(R.id.cash_account_txt);
		give_account_txt = (TextView) view.findViewById(R.id.give_account_txt);
		package_grid = (MyGridView) view.findViewById(R.id.package_grid);
		package_item_tips = (TextView) view.findViewById(R.id.package_item_tips);
		recharge_btn = (Button) view.findViewById(R.id.recharge_btn);
		recharge_btn.setEnabled(false);
		package_grid = (MyGridView) view.findViewById(R.id.package_grid);
		package_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				if (currentPackageAdapter != null) {
					PackageData packageData = currentPackageAdapter.getItem(position);
					currentPackageAdapter.setSelectedData(packageData, true);

					if (packageData.package_id != -1) {
						getZengsong(packageData.package_account);
					}

				}
			}
		});

		recharge_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean is_bind_account = Common.isBindAccount(sp);
				if (is_bind_account) {
					recharge_btn.setEnabled(false);
					ArrayList<PackageData> packageDatas = new ArrayList<PackageData>();
					if (currentPackageAdapter.getCheckMap() ==null){
						recharge_btn.setEnabled(true);
						return;
					}
					Map<PackageData, Boolean> checkMap = currentPackageAdapter.getCheckMap();
					Iterator iter = checkMap.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();

						boolean val = (Boolean) entry.getValue();
						if (val) {
							PackageData packageData = (PackageData) entry.getKey();
							packageDatas.add(packageData);
						}
					}

					if (packageDatas.size() == 0) {
						Common.showToast(getActivity(), R.string.no_select_package, Gravity.CENTER);
						recharge_btn.setEnabled(true);
						return;
					}
					final PackageData packageData = packageDatas.get(0);

					if (packageData == null) {
						Common.showToast(getActivity(), R.string.no_select_package, Gravity.CENTER);
						recharge_btn.setEnabled(true);
						return;
					}

					if (packageData.package_id == -1) {
						showRechargeAccount();
					} else {
						monney =packageData.package_account;

					}
				} else {
					showBindDialog();
				}

			}
		});
	}

	private void showBindDialog() {
		dialogBuilder.withTitle(getString(R.string.tips))
				.withMessage(getString(R.string.bind_tips2))
				.withEffect(Effectstype.Fadein).isCancelable(false)
				.withButton1Text(getString(R.string.cancel))
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();

					}
				}).withButton2Text(getString(R.string.sure))
				.setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
						Intent intent = new Intent();
						intent.setClass(getActivity(), SearchBratheDeviceActivity.class);
						intent.putExtra("capture_type", 4);
						startActivity(intent);

					}
				}).show();
	}

	private void showSaveDialog1() {
		dialogBuilder.withTitle(getString(R.string.tips))
				.withMessage(getString(R.string.save_idcard))
				.withEffect(Effectstype.Fadein).isCancelable(false)
				.withButton1Text(getString(R.string.cancel))
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();

					}
				}).withButton2Text(getString(R.string.sure))
				.setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent();
						intent.setClass(getActivity(), MyInfoActivity.class);
						intent.putExtra("is_admin", false);
						startActivity(intent);

						MainUserActivity activity = (MainUserActivity) getActivity();
						activity.setCurrentTab(2);
						dialogBuilder.dismiss();

					}
				}).show();
	}

	private void showSaveDialog2(String msg) {
		dialogBuilder.withTitle(getString(R.string.tips)).withMessage(msg)
				.withEffect(Effectstype.Fadein).isCancelable(false)
				.withButton1Text(getString(R.string.cancel))
				.withButton2Text(getString(R.string.i_known))
				.setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();

					}
				}).show();
	}

	private void getCurrentPackage() {
		if (currentPackageArrayList == null || currentPackageArrayList.size() == 0) {
			initPackage();
		}

		if (currentPackageAdapter == null) {
			currentPackageAdapter = new CurrentPackageAdapter(getActivity(), currentPackageArrayList);
			package_grid.setAdapter(currentPackageAdapter);
			currentPackageAdapter.setSelectedData(currentPackageArrayList.get(0), true);

		} else {
			currentPackageAdapter.changeData(currentPackageArrayList);
			package_grid.setAdapter(currentPackageAdapter);
			currentPackageAdapter.setSelectedData(currentPackageArrayList.get(0), true);
		}
		getZengsong(currentPackageArrayList.get(0).package_account);
	}

	public void showRechargeAccount() {

		Dialog dialog = new Dialog(getActivity(), R.style.dialog_untran);
		// 通过布局填充器获login_layout
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.recharge_account_layout, null);
		// 获取两个文本编辑框（密码这里不做登陆实现，仅演示）
		final EditText account_edit = (EditText) view.findViewById(R.id.account_edit);

		TextView cancel_txt = (TextView) view.findViewById(R.id.cancel_txt);
		TextView sure_txt = (TextView) view.findViewById(R.id.sure_txt);

		dialog.setContentView(view);
		dialog.show();

		account_edit.requestFocus();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				showInputMethod(account_edit, getActivity());
			}
		}, 100);

		cancel_txt.setTag(dialog);
		sure_txt.setTag(dialog);
		cancel_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog mAlertDialog = (Dialog) v.getTag();
				if (mAlertDialog != null) {
					recharge_btn.setEnabled(true);
					mAlertDialog.dismiss();
				}

			}
		});

		sure_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog mAlertDialog = (Dialog) v.getTag();
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();

					String accountString = account_edit.getText().toString();
					try {
						int account = Integer.valueOf(accountString);
						if (account >= 300 && account <= 1000) {
							//getAliPayInfo(mUserInfo, account);
							monney =account;
							getZengsong(account);

						} else {
							recharge_btn.setEnabled(true);
							Common.showToast(getActivity(), R.string.insert_recharge_tip2, Gravity.CENTER);
						}

					} catch (Exception e) {
						recharge_btn.setEnabled(true);
						Common.showToast(getActivity(), R.string.insert_recharge_tip2, Gravity.CENTER);
					}

				}

			}
		});

	}

	public static void showInputMethod(View view, Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}



	private static final int SDK_PAY_FLAG = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				recharge_btn.setEnabled(true);
				PayResult payResult = (PayResult) msg.obj;

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Common.showToast(getActivity(), R.string.zhifubao_pay_seccess, Gravity.BOTTOM);

					updateUserInfo(mUserInfo);

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Common.showToast(getActivity(), R.string.zhifubao_pay_process, Gravity.CENTER);

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Common.showToast(getActivity(), R.string.zhifubao_pay_failed, Gravity.CENTER);

					}
				}
				break;
			}
			default:
				break;
			}
		}

	};

	private void updateUserInfo(final UserInfo mInfo) {

		if (Common.isNetWorkConnected(getActivity())) {

			if (TextUtils.isEmpty(mInfo.TelPhone + "")) {
				Common.showToast(getActivity(), R.string.phonenum_null, Gravity.CENTER);
				return;
			}


			AjaxParams ajaxParams = new AjaxParams();
			ajaxParams.put("TelPhone", mInfo.TelPhone + "");
			ajaxParams.put("PrjID", mInfo.PrjID + "");
			ajaxParams.put("WXID", "0");
			ajaxParams.put("loginCode", mInfo.TelPhone + "," + mInfo.loginCode);
			ajaxParams.put("phoneSystem", "Android");
			ajaxParams.put("version", MyApp.versionCode);

			ajaxParams.put("isOpUser","0");

			new FinalHttp().get(Common.BASE_URL + "accountInfo", ajaxParams,
					new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);
							String result = t.toString();
							PublicGetData publicGetData = new Gson().fromJson(
									result, PublicGetData.class);
							if (publicGetData.error_code.equals("0")) {

								UserInfo info = new Gson().fromJson(publicGetData.data, UserInfo.class);
								info.loginCode = mInfo.loginCode;
								if (info != null) {
									Editor editor = sp.edit();
									editor.putString(Common.USER_PHONE_NUM, info.TelPhone + "");
									editor.putString(Common.USER_INFO, new Gson().toJson(info));
									editor.putInt(Common.ACCOUNT_IS_USER, info.GroupID);
									editor.commit();
									mUserInfo = info;
									initData();
									EventBus.getDefault().postSticky("monney_is_change");
								}

							} else if (publicGetData.error_code.equals("7")) {
								Common.logout2(getActivity(), sp, dialogBuilder,publicGetData.message);
							}

						}

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
						}
					});

		} else {
			Common.showNoNetworkDailog(dialogBuilder, getActivity());
		}

	}

	private void getZengsong(final int account) {

		if (Common.isNetWorkConnected(getActivity())) {
			AjaxParams ajaxParams = new AjaxParams();

			ajaxParams.put("PrjID", mUserInfo.PrjID + "");

			ajaxParams.put("SaveMoney", "" + account);
			ajaxParams.put("loginCode", mUserInfo.TelPhone + "," + mUserInfo.loginCode);
			ajaxParams.put("phoneSystem", "Android");
			ajaxParams.put("version", MyApp.versionCode);

			new FinalHttp().get(Common.BASE_URL + "getGivenMoney", ajaxParams,
					new AjaxCallBack<Object>() {

						@Override
						public void onSuccess(Object t) {
							super.onSuccess(t);
							String result = t.toString();
							PublicGivenMoneyData publicGivenMoneyData = new Gson().fromJson(result, PublicGivenMoneyData.class);
							if (publicGivenMoneyData.error_code.equals("0")) {
								if (publicGivenMoneyData.GivenMoney > 0) {
									String str = "活动说明：充 <font color='#FF0000'>"
											+ account
											+ "元</small></font> 送 <font color='#FF0000'>"
											+ publicGivenMoneyData.GivenMoney
											+ "元</small></font>";
									package_item_tips.setText(Html.fromHtml(str));
									package_item_tips.setVisibility(View.VISIBLE);
								} else {
									package_item_tips.setVisibility(View.GONE);
								}

							} else if (publicGivenMoneyData.error_code.equals("7")) {
								Common.logout(getActivity(), sp, dialogBuilder);
							}

						}

						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
						}
					});

		} else {
			Common.showNoNetworkDailog(dialogBuilder, getActivity());
		}

	}

	/**
	 * 根据项目获取充值金额
	 */
	private void getMonneyByPro() {
		if (Common.isNetWorkConnected(getActivity())){
			if (mUserInfo.PrjID==0){
				showBindDialog();
			}else {
				loadingDialogProgress = GlobalTools.getInstance().showDailog(getActivity(),"加载..");
				AjaxParams params =new AjaxParams();
				params.put("PrjID",""+mUserInfo.PrjID);
				params.put("loginCode",mUserInfo.TelPhone+","+mUserInfo.loginCode);
				params.put("phoneSystem", "Android");
				params.put("version", MyApp.versionCode);
				new FinalHttp().get(Common.BASE_URL + "czquery", params, new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object result) {
						super.onSuccess(result);
						if (loadingDialogProgress !=null){
							loadingDialogProgress.dismiss();
						}
						Recghangebean rebean =new Gson().fromJson(result.toString(),Recghangebean.class);
						if (rebean.getError_code().equals("0")) {

							if (rebean.getData() !=null && rebean.getData().size()>0){
								for (int i = 0; i <rebean.getData().size() ; i++) {
									//
									PackageData pai =new PackageData();
									pai.package_id=i;
									pai.package_account= Integer.parseInt(rebean.getData().get(i).getCzvalue());
									currentPackageArrayList.add(pai);
								}
								PackageData packageData6 = new PackageData();
								packageData6.package_id = -1;
								packageData6.package_account = -1;
								currentPackageArrayList.add(packageData6);
								if (currentPackageAdapter == null) {
									currentPackageAdapter = new CurrentPackageAdapter(getActivity(), currentPackageArrayList);
									package_grid.setAdapter(currentPackageAdapter);
									currentPackageAdapter.setSelectedData(currentPackageArrayList.get(0), true);

								} else {
									currentPackageAdapter.changeData(currentPackageArrayList);
									package_grid.setAdapter(currentPackageAdapter);
									currentPackageAdapter.setSelectedData(currentPackageArrayList.get(0), true);
								}
								getZengsong(currentPackageArrayList.get(0).package_account);
								recharge_btn.setEnabled(true);
							}
						}else {
							//未获取到充值金额信息
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						if (loadingDialogProgress !=null){
							loadingDialogProgress.dismiss();
						}
					}
				});
			}
		}else {
			Common.showNoNetworkDailog(dialogBuilder, getActivity());
			if (loadingDialogProgress !=null){
				loadingDialogProgress.dismiss();
			}
		}


	}



	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}
}
