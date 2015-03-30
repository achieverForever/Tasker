package com.wilson.tasker.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.wilson.tasker.R;
import com.wilson.tasker.adapters.ActionListAdapter;
import com.wilson.tasker.adapters.ConditionListAdapter;
import com.wilson.tasker.conditions.BatteryLevelCondition;
import com.wilson.tasker.conditions.TopAppCondition;
import com.wilson.tasker.events.SceneDetailEvent;
import com.wilson.tasker.listeners.OnConditionChangedListener;
import com.wilson.tasker.manager.FontManager;
import com.wilson.tasker.model.Condition;
import com.wilson.tasker.model.Event;
import com.wilson.tasker.model.Scene;
import com.wilson.tasker.ui.dialogs.AddConditionDialog;
import com.wilson.tasker.ui.dialogs.AppListDialog;
import com.wilson.tasker.ui.dialogs.EditBatteryLevelConditionDialog;
import com.wilson.tasker.utils.Utils;

import de.greenrobot.event.EventBus;

public class SceneDetailFragment extends Fragment implements OnConditionChangedListener {

	private EditText edtSceneName;
	private Button btnSave;
	private ListView conditionList;
	private ListView actionList;
	private ConditionListAdapter conditionListAdapter;
	private ActionListAdapter actionListAdapter;
	private Button btnAddCondition;
	private Button btnAddAction;
	private Scene scene;

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
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	private void setUpConditionList(LayoutInflater inflater) {
		View conditionListFooter = inflater.inflate(R.layout.condition_list_footer, conditionList, false);
		conditionList.addFooterView(conditionListFooter);
		conditionList.setAdapter(conditionListAdapter);
		conditionList.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		conditionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Condition condition = (Condition) parent.getAdapter().getItem(position);
				// TODO - 根据不同类型的Condition显示对应的UI
				switch (condition.eventCode) {
					case Event.EVENT_TOP_APP_CHANGED:
						AppListDialog appListDialog = AppListDialog.newInstance();
						appListDialog.setCondition((TopAppCondition) condition);
						appListDialog.setOnConditionChangedListener(SceneDetailFragment.this);
						appListDialog.show(getFragmentManager(), "app_list");
						break;
					case Event.EVENT_LOCATION:
						Intent intent = new Intent(getActivity(), BaiduMapActivity.class);
						startActivity(intent);
					case Event.EVENT_BATTERY_LEVEL:
						EditBatteryLevelConditionDialog batteryLevelDialog
							= EditBatteryLevelConditionDialog.newInstance();
						batteryLevelDialog.setCondition((BatteryLevelCondition) condition);
						batteryLevelDialog.setOnConditionChangedListener(SceneDetailFragment.this);
						batteryLevelDialog.show(getFragmentManager(), "edit_battery_level_dialog");
						break;
					default:
						Log.d(Utils.LOG_TAG, Event.eventCodeToString(condition.eventCode) + " condition click");
						break;
				}
			}
		});
		conditionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final Condition condition = (Condition) conditionListAdapter.getItem(position);
				new AlertDialog.Builder(getActivity())
						.setMessage("Are you sure you want to delete this item?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								conditionListAdapter.handleConditionChanged(condition, null);
								setListViewHeightBasedOnChildren(conditionList);
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.show();
				return true;
			}
		});
		conditionListFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddConditionDialog dialog = AddConditionDialog.newInstance();
				dialog.setOnConditionChangedListener(SceneDetailFragment.this);
				dialog.show(getFragmentManager(), "condition_list");
			}
		});
		setListViewHeightBasedOnChildren(conditionList);
	}

	private void setUpActionList(LayoutInflater inflater) {
		View actionListFooter = inflater.inflate(R.layout.action_list_footer, conditionList, false);
		actionList.addFooterView(actionListFooter);
		actionList.setAdapter(actionListAdapter);

		actionList.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		actionListFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		setListViewHeightBasedOnChildren(actionList);
	}

	public void onEvent(SceneDetailEvent event) {
		Log.d(Utils.LOG_TAG, "onEvent [" + Event.eventCodeToString(event.eventCode) + "]");
		scene = event.scene;
	}

	@Override
	public void onConditionChanged(Condition previous, Condition current) {
		conditionListAdapter.handleConditionChanged(previous, current);
		setListViewHeightBasedOnChildren(conditionList);
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
}
