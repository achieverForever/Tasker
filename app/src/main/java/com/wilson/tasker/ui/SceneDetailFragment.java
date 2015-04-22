package com.wilson.tasker.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wilson.tasker.R;
import com.wilson.tasker.actions.BrightnessAction;
import com.wilson.tasker.actions.WallpaperAction;
import com.wilson.tasker.actions.WifiConnectAction;
import com.wilson.tasker.adapters.ActionListAdapter;
import com.wilson.tasker.adapters.ConditionListAdapter;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.CallerCondition;
import com.wilson.tasker.conditions.ChargerCondition;
import com.wilson.tasker.conditions.OrientationCondition;
import com.wilson.tasker.conditions.SmsCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.events.AfterSceneSavedEvent;
import com.wilson.tasker.events.RefreshSceneListEvent;
import com.wilson.tasker.events.SceneDetailEvent;
import com.wilson.tasker.listeners.OnActionChangedListener;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.manager.DisplayManager;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Action;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.ui.dialogs.AddActionDialog;
import com.wilson.tasker.ui.dialogs.AddConditionDialog;
import com.wilson.tasker.ui.dialogs.AppListDialog;
import com.wilson.tasker.ui.dialogs.EditBatteryLevelConditionDialog;
import com.wilson.tasker.ui.dialogs.EditBrightnessActionDialog;
import com.wilson.tasker.ui.dialogs.EditCallerConditionDialog;
import com.wilson.tasker.ui.dialogs.EditChargerConditionDialog;
import com.wilson.tasker.ui.dialogs.EditOrientationConditionDialog;
import com.wilson.tasker.ui.dialogs.EditSmsConditionDialog;
import com.wilson.tasker.utils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SceneDetailFragment extends Fragment
	implements OnConditionChangedListener, OnActionChangedListener {
	private static final int REQUEST_PICK_IMAGE = 100;

	private EditText edtSceneName;
	private Button btnSave;
	private ListView conditionList;
	private ListView actionList;
	private ConditionListAdapter conditionListAdapter;
	private ActionListAdapter actionListAdapter;
	private Button btnAddCondition;
	private Button btnAddAction;
	private Scene scene;

	/** 保存被删除的Condition，用于后续反注册Manager */
	private List<Condition> removedConditions = new ArrayList<>();

	public static SceneDetailFragment newInstance() {
		return new SceneDetailFragment();
	}

	public SceneDetailFragment() {
		// Required empty constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_scene_detail, container, false);
		setupViews(view);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void setupViews(View root) {
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		edtSceneName = (EditText) root.findViewById(R.id.edt_scene_name);
		conditionList = (ListView) root.findViewById(R.id.lv_conditions);
		actionList = (ListView) root.findViewById(R.id.lv_actions);
		btnSave = (Button) root.findViewById(R.id.btn_save);

		edtSceneName.setText(scene.getDesc());
		edtSceneName.setTypeface(FontManager.getsInstance().loadFont(getActivity(), "fonts/Roboto-Thin.ttf"));
		conditionListAdapter = new ConditionListAdapter(getActivity(), scene.getConditions(), scene);
		actionListAdapter = new ActionListAdapter(getActivity(), scene.getActions(), scene);

		setUpConditionList(inflater);
		setUpActionList(inflater);

		btnSave.setOnClickListener(new OnSaveSceneClickListener());
	}

	private void setUpConditionList(LayoutInflater inflater) {
		View addConditionFooter = inflater.inflate(R.layout.condition_list_footer, conditionList, false);
		conditionList.addFooterView(addConditionFooter);
		conditionList.setAdapter(conditionListAdapter);

		conditionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			                               long id) {
				Condition condition = (Condition) conditionListAdapter.getItem(position);
				showDeleteConditionDialog(condition);
				return true;
			}
		});

		addConditionFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddConditionDialog();
			}
		});

		conditionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Condition condition = (Condition) parent.getAdapter().getItem(position);
				// TODO - 根据不同类型的Condition显示对应的Condition编辑界面
				switch (condition.eventCode) {

					case Event.EVENT_TOP_APP_CHANGED:
						showAppListDialog((TopAppCondition) condition);
						break;

					case Event.EVENT_LOCATION:
						Intent intent = new Intent(getActivity(), BaiduMapActivity.class);
						startActivity(intent);
						break;

					case Event.EVENT_BATTERY_LEVEL:
						showBatteryLevelDialog((BatteryLevelCondition) condition);
						break;

					case Event.EVENT_CALLER:
						showCallerDialog((CallerCondition) condition);
						break;

					case Event.EVENT_SMS:
						showSmsDialog((SmsCondition) condition);
						break;

					case Event.EVENT_CHARGER:
						showChargerDialog((ChargerCondition) condition);
						break;

					case Event.EVENT_ORIENTATION:
						showOrientationDialog((OrientationCondition) condition);
						break;

					default:
						Log.d(Utils.LOG_TAG,
							Event.eventCodeToString(condition.eventCode) + " condition click");
						break;
				}
			}
		});

		setListViewHeightBasedOnChildren(conditionList);
	}

	private void showAppListDialog(TopAppCondition condition) {
		AppListDialog appListDialog = AppListDialog.newInstance();
		appListDialog.setCondition(condition);
		appListDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		appListDialog.show(getFragmentManager(), "app_list");
	}

	private void showBatteryLevelDialog(BatteryLevelCondition condition) {
		EditBatteryLevelConditionDialog batteryLevelDialog
				= EditBatteryLevelConditionDialog.newInstance(
				condition.type, condition.targetValue);
		batteryLevelDialog.setCondition(condition);
		batteryLevelDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		batteryLevelDialog.show(getFragmentManager(), "edit_battery_level_dialog");
	}

	private void showCallerDialog(CallerCondition condition) {
		EditCallerConditionDialog editCallerDialog
				= EditCallerConditionDialog.newInstance((condition.callerNumber));
		editCallerDialog.setCondition(condition);
		editCallerDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		editCallerDialog.show(getFragmentManager(), "edit_caller_dialog");
	}

	private void showSmsDialog(SmsCondition condition) {
		EditSmsConditionDialog editSmsDialog
				= EditSmsConditionDialog.newInstance(condition.msgFromPtn, condition.msgBodyPtn);
		editSmsDialog.setCondition(condition);
		editSmsDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		editSmsDialog.show(getFragmentManager(), "edit_sms_dialog");
	}

	private void showChargerDialog(ChargerCondition condition) {
		EditChargerConditionDialog editChargerDialog
				= EditChargerConditionDialog.newInstance(condition.isCharging);
		editChargerDialog.setCondition(condition);
		editChargerDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		editChargerDialog.show(getFragmentManager(), "edit_charger_dialog");
	}

	private void showOrientationDialog(OrientationCondition condition) {
		EditOrientationConditionDialog editChargerDialog
				= EditOrientationConditionDialog.newInstance(condition.targetOrientation);
		editChargerDialog.setCondition(condition);
		editChargerDialog.setOnConditionChangedListener(SceneDetailFragment.this);
		editChargerDialog.show(getFragmentManager(), "edit_charger_dialog");
	}

	private void showDeleteConditionDialog(final Condition condition) {
		new MaterialDialog.Builder(getActivity())
				.content("Are your sure you want to delete this condition?")
				.positiveText("Yes")
				.negativeText("No")
				.callback(new MaterialDialog.Callback() {
					@Override
					public void onPositive(MaterialDialog materialDialog) {
						removedConditions.add(condition);
						conditionListAdapter.handleConditionChanged(condition, null);
						setListViewHeightBasedOnChildren(conditionList);
					}

					@Override
					public void onNegative(MaterialDialog materialDialog) {
						materialDialog.dismiss();
					}
				})
				.build()
				.show();

	}

	private void showAddConditionDialog() {
		AddConditionDialog dialog = AddConditionDialog.newInstance();
		dialog.setOnConditionChangedListener(SceneDetailFragment.this);
		dialog.show(getFragmentManager(), "condition_list");
	}

	private void setUpActionList(LayoutInflater inflater) {
		View addActionFooter = inflater.inflate(R.layout.action_list_footer, actionList, false);
		actionList.addFooterView(addActionFooter);
		actionList.setAdapter(actionListAdapter);

		actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Action action = (Action) actionListAdapter.getItem(position);
				switch (action.actionType) {
					case Action.TYPE_BRIGHTNESS:
						showBrightnessDialog((BrightnessAction) action);
						break;

					case Action.TYPE_WALL_PAPER:
						showWallpaperDialog((WallpaperAction) action);
						break;

					case Action.TYPE_WIFI_CONNECT:
						showWifiConnectDialog((WifiConnectAction) action);
						break;
				}
			}
		});

		actionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			                               long id) {
				Action action = (Action) actionListAdapter.getItem(position);
				showDeleteActionDialog(action);
				return true;
			}
		});

		addActionFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddActionDialog();
			}
		});

		setListViewHeightBasedOnChildren(actionList);
	}

	private void showBrightnessDialog(BrightnessAction action) {
		EditBrightnessActionDialog dialog
				= EditBrightnessActionDialog.newInstance(action.brightness);
		dialog.setAction(action);
		dialog.setOnActionChangedListener(this);
		dialog.show(getFragmentManager(), "edit_brightness_dialog");
	}

	private void showWallpaperDialog(WallpaperAction action) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_PICK_IMAGE);
	}

	private void showWifiConnectDialog(WifiConnectAction action) {

	}

	private void showDeleteActionDialog(final Action action) {
		new MaterialDialog.Builder(getActivity())
				.content("Are your sure you want to delete this action?")
				.positiveText("Yes")
				.negativeText("No")
				.callback(new MaterialDialog.Callback() {
					@Override
					public void onPositive(MaterialDialog materialDialog) {
						actionListAdapter.handleActionChanged(action, null);
						setListViewHeightBasedOnChildren(actionList);
					}

					@Override
					public void onNegative(MaterialDialog materialDialog) {
						materialDialog.dismiss();
					}
				})
				.build()
				.show();
	}

	private void showAddActionDialog() {
		AddActionDialog dialog = AddActionDialog.newInstance();
		dialog.setOnActionChangedListener(SceneDetailFragment.this);
		dialog.show(getFragmentManager(), "action_list");
	}

	public void onEvent(SceneDetailEvent event) {
		Log.d(Utils.LOG_TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");
		scene = event.scene;
		// Scene处于编辑状态时不允许被调度执行
		scene.setState(Scene.STATE_DISABLED);
	}

	@Override
	public void onConditionChanged(Condition previous, Condition current) {
		conditionListAdapter.handleConditionChanged(previous, current);
		setListViewHeightBasedOnChildren(conditionList);
	}

	@Override
	public void onActionChanged(Action previous, Action current) {
		actionListAdapter.handleActionChanged(previous, current);
		setListViewHeightBasedOnChildren(actionList);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	private class OnSaveSceneClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			scene.setDesc(edtSceneName.getText().toString());
			scene.setState(Scene.STATE_ENABLED);

			EventBus.getDefault().postSticky(new RefreshSceneListEvent());
			EventBus.getDefault().post(new AfterSceneSavedEvent(removedConditions, scene.getConditions()));
			getActivity().finish();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
			if (data == null) {
				Log.e(Utils.LOG_TAG, "No image data returned");
				return;
			}
			// TODO - 这里比较挫，如何将返回的Uri赋值给WallpaperAction
			for (Action action : scene.getActions()) {
				if (action.actionType == Action.TYPE_WALL_PAPER) {
					((WallpaperAction) action).imageUri = data.getData();
				}
			}
		}
	}
}
