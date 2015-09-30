package com.miqian.mq.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import com.miqian.mq.R;
import com.miqian.mq.entity.DetailForRegularDeposit;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.ProgressDialogView;
import com.miqian.mq.views.RoundCornerProgressBar;
import com.miqian.mq.views.WFYTitle;

/**
 * Created by sunyong on 9/17/15.
 */
public class DetailsForRegularEarningActivity extends BaseActivity {
  private Dialog mWaitingDialog;

  //tip: 未结息定期赚，未结息定期计划  不显示
  //未结息定期转让中 0
  TextView status_zero;
  //未结息定期已还款 1
  TextView status_one;
  //已结息已还款定期 2
  TextView status_two;
  //已结息已转让定期 3
  TextView status_three;

  //获得数据
  public void obtainData() {
    myUpdateOperation("1", "", 0, 10);
  }

  TextView projects_matching;
  TextView money_amount;//总额
  TextView annualized_return;//年化收益率
  TextView limit;//期限
  String status;//期限的右侧。
  TextView date_start, date_end;
  RoundCornerProgressBar progress;//购买比例
  TextView pay_mode;//还款方式
  TextView fromInvestmentAmount;//待收收益
  TextView totalInvestmentAmount;//总收益
  WFYTitle topBar;

  public void initView() {

    progress = (RoundCornerProgressBar) findViewById(R.id.progress);
    progress.setProgress(50);
    mWaitingDialog = ProgressDialogView.create(this);
    mWaitingDialog.setCanceledOnTouchOutside(false);
    mWaitingDialog.show();

    status_zero = (TextView) findViewById(R.id.status_zero);
    status_one = (TextView) findViewById(R.id.status_one);
    status_two = (TextView) findViewById(R.id.status_two);
    status_three = (TextView) findViewById(R.id.status_three);

    limit = (TextView) findViewById(R.id.limit);

    annualized_return = (TextView) findViewById(R.id.annualized_return);
    date_start = (TextView) findViewById(R.id.date_start);
    date_end = (TextView) findViewById(R.id.date_end);
    progress = (RoundCornerProgressBar) findViewById(R.id.progress);
    projects_matching = (TextView) findViewById(R.id.projects_matching);
    pay_mode = (TextView) findViewById(R.id.pay_mode);
    money_amount = (TextView) findViewById(R.id.money_amount);
    fromInvestmentAmount = (TextView) findViewById(R.id.fromInvestmentAmount);
    totalInvestmentAmount = (TextView) findViewById(R.id.totalInvestmentAmount);

    //colorizePartOfPlainText();
  }

  public int getLayoutId() {
    return R.layout.activity_regular_earn_detail;
  }

  public void initTitle(WFYTitle mTitle) {
    topBar = mTitle;
    mTitle.setTitleText("定期赚详情");
    mTitle.setRightImage(R.drawable.ic_launcher);
    mTitle.setOnRightClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Uihelper.showToast(DetailsForRegularEarningActivity.this, "open the webview");
      }
    });
  }

  /**
   * 提供两种方式选择状态，具体看服务器的返回值类型。
   */
  private void selectStatus(int status, String statusStr) {
    switch (status) {
      case 0:
        status_zero.setVisibility(View.VISIBLE);
        break;

      case 1:
        status_one.setVisibility(View.VISIBLE);
        break;

      case 2:
        status_two.setVisibility(View.VISIBLE);
        break;

      case 3:
        status_three.setVisibility(View.VISIBLE);
        break;
    }

    switch (statusStr) {
      case "0":
        status_zero.setVisibility(View.VISIBLE);
        break;

      case "1":
        status_one.setVisibility(View.VISIBLE);
        break;

      case "2":
        status_two.setVisibility(View.VISIBLE);
        break;

      case "3":
        status_three.setVisibility(View.VISIBLE);
        break;
    }
  }


  /**
   * render UI according to the specified operationType
   *
   * @param operationType 0为获取定期赚和定期计划，1为获取定期赚，2为获取定期计划.   必传
   * @param subjectId 标的编号，如传入则返回改标的相关信息
   * @param pageNo 默认1
   * @param pageSize 默认为10
   */
  private void myUpdateOperation(final String operationType, String subjectId, int pageNo,
      int pageSize) {
    HttpRequest.detailsForRegularEarning(this, new ICallback<DetailForRegularDeposit>() {

      @Override public void onSucceed(DetailForRegularDeposit result) {
        //set up the receive data on UI
        if (operationType.equals("1")) {
          topBar.setTitleText("定期赚详情");
          projects_matching.setText(result.getData().getSubjectName() + "   1");
          money_amount.setText(result.getData().getSubjectTotalPrice());
          limit.setText("项目期限 " + result.getData().getLimit());

          annualized_return.setText("年华收益率：" + result.getData().getYearInterest());//年利率
          date_start.setText(result.getData().getStartTimestamp());
          date_end.setText(result.getData().getEndTimestamp());
          progress.setProgress(Float.valueOf(result.getData().getPurchasePercent()));
          pay_mode.setText(result.getData().getPayMode());
          fromInvestmentAmount.setText(result.getData().getFromInvestmentAmount());

          //totalInvestmentAmount.setText();// todo 总收益 貌似没有这个字段

        } else if (operationType.equals("2")) {
          topBar.setTitleText("定期计划详情");
          projects_matching.setText(result.getData().getSubjectName() + "   2");
          projects_matching.setText(result.getData().getSubjectName() + "   1");
          money_amount.setText(result.getData().getSubjectTotalPrice());
          annualized_return.setText(result.getData().getSubjectTotalPrice());
          limit.setText(result.getData().getLimit());

          annualized_return.setText(result.getData().getYearInterest());//年利率
          date_start.setText(result.getData().getStartTimestamp());
          date_end.setText(result.getData().getEndTimestamp());
          progress.setProgress(Float.valueOf(result.getData().getPurchasePercent()));
          pay_mode.setText(result.getData().getPayMode());
          fromInvestmentAmount.setText(result.getData().getFromInvestmentAmount());

          //totalInvestmentAmount.setText();// todo 总收益 貌似没有这个字段
        }
        // dismiss the dialog
        mWaitingDialog.dismiss();
      }

      @Override public void onFail(String error) {
        Uihelper.showToast(DetailsForRegularEarningActivity.this, error);
        mWaitingDialog.dismiss();
      }
    });
  }
}
