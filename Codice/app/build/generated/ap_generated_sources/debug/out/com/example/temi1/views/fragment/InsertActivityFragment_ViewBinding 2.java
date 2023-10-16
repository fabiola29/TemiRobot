// Generated code from Butter Knife. Do not modify!
package com.example.temi1.views.fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.temi1.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InsertActivityFragment_ViewBinding implements Unbinder {
  private InsertActivityFragment target;

  @UiThread
  public InsertActivityFragment_ViewBinding(InsertActivityFragment target, View source) {
    this.target = target;

    target.recurring = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_recurring, "field 'recurring'", CheckBox.class);
    target.recurringDays = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_recurring_options, "field 'recurringDays'", LinearLayout.class);
    target.mon = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkMon, "field 'mon'", CheckBox.class);
    target.tue = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkTue, "field 'tue'", CheckBox.class);
    target.wed = Utils.findRequiredViewAsType(source, R.id.fragment_ccreateTask_checkWed, "field 'wed'", CheckBox.class);
    target.thu = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkThu, "field 'thu'", CheckBox.class);
    target.fri = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkFri, "field 'fri'", CheckBox.class);
    target.sat = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkSat, "field 'sat'", CheckBox.class);
    target.sun = Utils.findRequiredViewAsType(source, R.id.fragment_createTask_checkSun, "field 'sun'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InsertActivityFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recurring = null;
    target.recurringDays = null;
    target.mon = null;
    target.tue = null;
    target.wed = null;
    target.thu = null;
    target.fri = null;
    target.sat = null;
    target.sun = null;
  }
}
