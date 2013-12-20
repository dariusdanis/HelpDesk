package com.helpdesk.ui.statistic;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.Cursor;
import com.googlecode.wickedcharts.highcharts.options.DataLabels;
import com.googlecode.wickedcharts.highcharts.options.Function;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PixelOrPercent;
import com.googlecode.wickedcharts.highcharts.options.PixelOrPercent.Unit;
import com.googlecode.wickedcharts.highcharts.options.PlotOptions;
import com.googlecode.wickedcharts.highcharts.options.PlotOptionsChoice;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.Tooltip;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.color.HighchartsColor;
import com.googlecode.wickedcharts.highcharts.options.color.NullColor;
import com.googlecode.wickedcharts.highcharts.options.color.RadialGradient;
import com.googlecode.wickedcharts.highcharts.options.series.Point;
import com.googlecode.wickedcharts.highcharts.options.series.PointSeries;
import com.googlecode.wickedcharts.wicket6.highcharts.Chart;
import com.helpdesk.domain.service.FacilityService;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.user.HomePage;

public class StatisticsPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private FacilityService facilityService;
	
	@SpringBean
	private RequestService requestService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else if (!director()) {
			setResponsePage(HomePage.class);
			return;
		}
		requestService.getTopThree();
		add(initTopThreeFacilityStatistic("topThreeFacility", requestService.getTopThree()));
	}

	private Component initTopThreeFacilityStatistic(String wicketId, List<Integer> values) {
		Options options = new Options();
		if (values.size() == 6) {
			options.setTitle(new Title("Top Three Servises By Time"));
			
			options.setChartOptions(new ChartOptions()
					.setPlotBackgroundColor(new NullColor())
					.setPlotBorderWidth(null).setPlotShadow(Boolean.FALSE));
			
			options.setTooltip(new Tooltip().setFormatter(new Function()
					.setFunction("return '<b>'+ this.point.name +':</b> ' +' ('+ parseFloat(this.percentage).toFixed(2) +'% )';")));
	
			options.setPlotOptions(new PlotOptionsChoice()
					.setPie(new PlotOptions()
							.setSize(new PixelOrPercent(95, Unit.PERCENT))
							.setAllowPointSelect(Boolean.TRUE)
							.setCursor(Cursor.POINTER)
							.setDataLabels(
									new DataLabels()
											.setEnabled(Boolean.TRUE)
											.setColor(new HexColor("#000000"))
											.setConnectorColor(
													new HexColor("#000000"))
											.setFormatter(
													new Function()
															.setFunction("return '<b>'+ this.point.name +':</b> ' +' ('+ parseFloat(this.percentage).toFixed(2) +'% )';")))));
	
	
			options.addSeries(new PointSeries()
					.setType(SeriesType.PIE)
					.setName("Event type popularity")
					.addPoint(
							new Point(facilityService.getById(values.get(0)).getName(), values.get(1))
									.setColor(new RadialGradient()
											.setCx(0.5)
											.setCy(0.3)
											.setR(0.7)
											.addStop(0, new HighchartsColor(0))
											.addStop(
													1,
													new HighchartsColor(0)
															.brighten(-0.3f))))
					.addPoint(
							new Point(facilityService.getById(values.get(2)).getName(), values.get(3))
									.setColor(new RadialGradient()
											.setCx(0.5)
											.setCy(0.3)
											.setR(0.7)
											.addStop(0, new HighchartsColor(3))
											.addStop(
													1,
													new HighchartsColor(3)
															.brighten(-0.3f))))
					.addPoint(
							new Point(facilityService.getById(values.get(4)).getName(), values.get(5))
									.setSliced(Boolean.TRUE)
									.setSelected(Boolean.TRUE)
									.setColor(
											new RadialGradient()
													.setCx(0.5)
													.setCy(0.3)
													.setR(0.7)
													.addStop(0,
															new HighchartsColor(2))
													.addStop(
															1,
															new HighchartsColor(2)
																	.brighten(-0.3f)))));
		} else {
			options.setTitle(new Title("Not enaught date!"));
		}
		return new Chart(wicketId, options);
	}

}
