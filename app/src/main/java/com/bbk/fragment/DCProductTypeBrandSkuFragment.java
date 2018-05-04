package com.bbk.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SelectedValue.SelectedValueType;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.R;
import com.bbk.adapter.DCServiceCountGridAdapter;
import com.bbk.util.ColorUtil;
import com.bbk.view.DCGridView;

public class DCProductTypeBrandSkuFragment extends BaseViewPagerFragment
		implements OnClickListener, OnItemClickListener {

	private View mView;
	private JSONObject dataJo;

	private TextView serviceCountTv;
	private PieChartView serviceCountChart;
	private PieChartData serviceCountChartAdapter;
	private List<SliceValue> serviceCountChartData;
	private DCGridView serviceCountGrid;
	private DCServiceCountGridAdapter serviceCountGridAdapter;
	private List<Map<String, Object>> serviceCountGridData;

	private PieChartView lowChart;
	private PieChartData lowChartAdapter;
	private List<SliceValue> lowChartData;

	private PieChartView aveChart;
	private PieChartData aveChartAdapter;
	private List<SliceValue> aveChartData;

	private PieChartView highChart;
	private PieChartData highChartAdapter;
	private List<SliceValue> highChartData;

	private LineChartView domainLineChart;
	private LineChartData domainLineChartAdapter;
	private List<Line> domainLineChartData;
	private TableLayout popularityTable;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String dataJoStr = getArguments().getString("dataJo");
		try {
			dataJo = new JSONObject(dataJoStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (null == mView && dataJo != null) {
			mView = inflater.inflate(
					R.layout.fragment_dc_producttype_brand_sku, null);
			initView();
			initData();
		}
		return mView;
	}

	public void initView() {
		serviceCountTv = (TextView) mView.findViewById(R.id.tv_domain_count);

		serviceCountChart = (PieChartView) mView
				.findViewById(R.id.domain_count_chart);
		serviceCountChart.setValueSelectionEnabled(true);
		serviceCountChartData = new ArrayList<SliceValue>();
		serviceCountChartAdapter = new PieChartData(serviceCountChartData);
		serviceCountChartAdapter.setHasLabelsOnlyForSelected(true);
		serviceCountChartAdapter.setHasLabelsOutside(false);
		serviceCountChartAdapter.setHasCenterCircle(true);
		serviceCountChartAdapter.setSlicesSpacing(0);
		serviceCountChartAdapter.setCenterCircleScale(0.35F);
		serviceCountChartAdapter.setValueLabelBackgroundEnabled(false);

		serviceCountGrid = (DCGridView) mView
				.findViewById(R.id.gridview_domain_count);
//		serviceCountGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽点击效果
		serviceCountGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SelectedValue selctValue = new SelectedValue(position, position, SelectedValueType.NONE);
				serviceCountChart.selectValue(selctValue);
			}
		});
		serviceCountGridData = new ArrayList<>();
		serviceCountGridAdapter = new DCServiceCountGridAdapter(getActivity(),
				serviceCountGridData);
		serviceCountGrid.setAdapter(serviceCountGridAdapter);

		lowChart = (PieChartView) mView.findViewById(R.id.low_chart);
		lowChartData = new ArrayList<SliceValue>();
		lowChartAdapter = new PieChartData(lowChartData);
		lowChartAdapter.setHasLabelsOutside(false);
		lowChartAdapter.setHasCenterCircle(true);
		lowChartAdapter.setSlicesSpacing(0);
		lowChartAdapter.setCenterCircleScale(0.8F);

		aveChart = (PieChartView) mView.findViewById(R.id.ave_chart);
		aveChartData = new ArrayList<SliceValue>();
		aveChartAdapter = new PieChartData(aveChartData);
		aveChartAdapter.setHasLabelsOutside(false);
		aveChartAdapter.setHasCenterCircle(true);
		aveChartAdapter.setSlicesSpacing(0);
		aveChartAdapter.setCenterCircleScale(0.8F);

		highChart = (PieChartView) mView.findViewById(R.id.high_chart);
		highChartData = new ArrayList<SliceValue>();
		highChartAdapter = new PieChartData(highChartData);
		highChartAdapter.setHasLabelsOutside(false);
		highChartAdapter.setHasCenterCircle(true);
		highChartAdapter.setSlicesSpacing(0);
		highChartAdapter.setCenterCircleScale(0.8F);

		domainLineChart = (LineChartView) mView
				.findViewById(R.id.domain_line_chart);
		domainLineChartData = new ArrayList<>();
		domainLineChartAdapter = new LineChartData(domainLineChartData);

		popularityTable = (TableLayout) mView
				.findViewById(R.id.table_popularity);
	}

	public void initData() {
		try {
			JSONArray serviceCountJa = dataJo.getJSONObject("sku")
					.getJSONArray("fenbu");
			List<String> names = new ArrayList<>();
			for (int i = 0; i < serviceCountJa.length(); i++) {
				JSONObject jo = serviceCountJa.getJSONObject(i);
				String domain = jo.getString("domain");
				names.add(domain);
			}
			int serviceCount = 0;
			Map<String, Integer> colorMap = ColorUtil.getColors(names
					.toArray(new String[] {}));
			for (int i = 0; i < serviceCountJa.length(); i++) {
				JSONObject jo = serviceCountJa.getJSONObject(i);
				String domain = jo.getString("domain");
				int value = jo.getInt("value1");
				serviceCount += value;
				int color = colorMap.get(domain);
				SliceValue sliceValue = new SliceValue(value, color);
				sliceValue.setLabel("" + jo.getInt("value1"));
				serviceCountChartData.add(sliceValue);
				Map<String, Object> map = new HashMap<>();
				map.put("domain", domain);
				map.put("color", color);
				serviceCountGridData.add(map);
			}
			serviceCountTv.setText("" + serviceCount);
			serviceCountChart.setPieChartData(serviceCountChartAdapter);
			serviceCountGridAdapter.notifyDataSetChanged();

			float low = (float) dataJo.getJSONObject("sku").getDouble("low");
			SliceValue lowSliceValue = new SliceValue(low, Color.RED);
			lowChartData.add(lowSliceValue);
			lowChartAdapter.setCenterText1("$" + low + "元");
			lowChartAdapter.setCenterText1FontSize(16);
			lowChart.setPieChartData(lowChartAdapter);

			float avg = (float) dataJo.getJSONObject("sku").getDouble("ave");
			SliceValue avgSliceValue = new SliceValue(avg, Color.YELLOW);
			aveChartData.add(avgSliceValue);
			aveChartAdapter.setCenterText1("$" + avg + "元");
			aveChartAdapter.setCenterText1FontSize(16);
			aveChart.setPieChartData(aveChartAdapter);

			float high = (float) dataJo.getJSONObject("sku").getDouble("high");
			SliceValue highSliceValue = new SliceValue(high, Color.BLUE);
			highChartData.add(highSliceValue);
			highChartAdapter.setCenterText1("$" + high + "元");
			highChartAdapter.setCenterText1FontSize(16);
			highChart.setPieChartData(highChartAdapter);

			JSONArray fenbuJa = dataJo.getJSONObject("sku").getJSONArray(
					"fenbu");
			List<PointValue> values = new ArrayList<PointValue>();
			List<AxisValue> axiss = new ArrayList<>();
			for (int j = 0; j < fenbuJa.length(); j++) {
				JSONObject jo = fenbuJa.getJSONObject(j);
				values.add(new PointValue(j,(float) jo.getDouble("value2")));
				axiss.add(new AxisValue(j).setLabel(jo.getString("domain")));
			}
			Line line = new Line(values);
			line.setColor(ChartUtils.COLORS[0]);
			line.setFilled(true);
			line.setHasLines(true);
			line.setHasPoints(true);
			line.setPointColor(ChartUtils.COLORS[(0) % ChartUtils.COLORS.length]);
			domainLineChartData.add(line);
			Axis axisX = new Axis(axiss);
			axisX.setHasSeparationLine(true);
			axisX.setHasLines(true);
			axisX.setTextSize(8);
			Axis axisY = new Axis();
			axisY.setHasLines(true);
			domainLineChartAdapter.setAxisXBottom(axisX);
			domainLineChartAdapter.setAxisYLeft(axisY);
			domainLineChart.setLineChartData(domainLineChartAdapter);

			JSONArray serviceJa = dataJo.getJSONObject("sku").getJSONArray(
					"dianpuList");
			for (int i = 0; i < serviceJa.length(); i++) {
				JSONObject jo = serviceJa.getJSONObject(i);
				String service = jo.getString("service");
				String ranking = String.valueOf(i + 1);
				String domain = jo.getString("domain");
				String popularity = jo.getString("rise_i");
				View view = inflate(R.layout.table_item_dc_service_popularity);
				((TextView) view.findViewById(R.id.tv_service)).setText(service);
				((TextView) view.findViewById(R.id.tv_ranking)).setText(ranking);
				((TextView) view.findViewById(R.id.tv_domain)).setText(domain);
				((TextView) view.findViewById(R.id.tv_popularity)).setText(popularity);
				popularityTable.addView(view);
			}
		} catch (Exception e) {
		}

	}

	public View inflate(int id) {
		return getActivity().getLayoutInflater().inflate(id, null, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) parent
				.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), DetailsMainActivity22.class);
		intent.putExtra("groupRowKey", (String) map.get("groupRowKey"));
		startActivity(intent);
	}


	@Override
	protected void loadLazyData() {

	}

}
