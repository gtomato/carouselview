package com.gt.android.demo.carouselviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gt.android.ui.widget.CarouselView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author  sunny-chung
 */

public class ConfigRecyclerAdapter extends RecyclerView.Adapter<ConfigRecyclerAdapter.ViewHolder> {

    Class mClass;
    List<Method> mSetterMethods = new ArrayList<>();
    final Object mLock = new Object();

    Map<String, Number> mValues = new HashMap<>(); // key = beanName; value = actual value (boolean stores as Byte 1/0)

    public ConfigRecyclerAdapter(Class<? extends CarouselView.ViewTransformer> aClass) {
        setClass(aClass);
    }

    public void setClass(Class<? extends CarouselView.ViewTransformer> aClass) {
        mClass = aClass;

        synchronized (mLock) {
            if (mClass != null) {
                mSetterMethods = CarouselParameters.getSetterMethods(aClass);
                mValues = CarouselParameters.getDefaultTransformerParameters(aClass, mSetterMethods);
            } else {
                mSetterMethods = new ArrayList<>();
                mValues = new HashMap<>();
            }
        }
    }

    public Map<String, Number> getSelectedValues() {
        return mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.config_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        Method method;
        synchronized (mLock) {
            method = mSetterMethods.get(position);
        }

        final String beanName = CarouselParameters.getBeanName(method.getName());

        /**
         * if cell changed, reconfigure the whole cell;
         * otherwise, just update the result display text
         */
        boolean isCellChanged = vh.getOldPosition() != vh.getPosition();

        if (isCellChanged) {

            String parameterName = CarouselParameters.getParameterName(method.getName());
            vh.lblTitle.setText(parameterName);

            vh.sekParameter.setVisibility(View.GONE);
            vh.swhParameter.setVisibility(View.GONE);

        }

        Class parameterType = method.getParameterTypes()[0];
        if (Float.class == parameterType
                || float.class == parameterType
                || Double.class == parameterType
                || double.class == parameterType) {

            vh.sekParameter.setVisibility(View.VISIBLE);

            final CarouselParameters.RangeInfo<Double> info = CarouselParameters.getParameterRangeInfoDouble(method);
            vh.sekParameter.setMax(info.numIntervals + 1);

            if (isCellChanged) {

                Number lastValue = mValues.get(beanName);

                int chosenPosition = 0;
                if (lastValue != null) {
                    chosenPosition = (int) Math.round((lastValue.doubleValue() - info.from) / info.interval + 1);
                }
                vh.sekParameter.setProgress(chosenPosition);

                vh.sekParameter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            if (progress != 0) {
                                double value = info.from + (vh.sekParameter.getProgress() - 1) * info.interval;
                                mValues.put(beanName, value);
                            } else {
                                mValues.remove(beanName);
                            }
                            notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }

            Number value = mValues.get(beanName);
            if (value == null) {
                vh.lblResult.setText("OFF");
            } else {
                vh.lblResult.setText(String.format("%.1f", value.doubleValue()));
            }

        } else if (Integer.class == parameterType || int.class == parameterType) {

            vh.sekParameter.setVisibility(View.VISIBLE);

            final CarouselParameters.RangeInfo<Integer> info = CarouselParameters.getParameterRangeInfoInt(method);
            vh.sekParameter.setMax(info.numIntervals + 1);

            if (isCellChanged) {

                Number lastValue = mValues.get(beanName);

                int chosenPosition = 0;
                if (lastValue != null) {
                    chosenPosition = (int) Math.round((lastValue.doubleValue() - info.from) / info.interval + 1);
                }
                vh.sekParameter.setProgress(chosenPosition);

                vh.sekParameter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) notifyItemChanged(position);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }

            switch (vh.sekParameter.getProgress()) {
                case 0: {
                    vh.lblResult.setText("OFF");
                } break;

                default: {
                    int value = info.from + (vh.sekParameter.getProgress() - 1) * info.interval;
                    vh.lblResult.setText("" + value);
                } break;
            }

        } else if (Boolean.class == parameterType || boolean.class == parameterType) {

            vh.swhParameter.setVisibility(View.VISIBLE);

            if (isCellChanged) {

                vh.swhParameter.setOnCheckedChangeListener(null);

                Number lastValue = mValues.get(beanName);
                boolean newChecked = lastValue != null && lastValue.byteValue() != 0;
                vh.swhParameter.setChecked(newChecked);

                vh.swhParameter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mValues.put(beanName, isChecked ? (byte) 1 : (byte) 0);
                        notifyItemChanged(position);
                    }
                });

            }

            vh.lblResult.setText(vh.swhParameter.isChecked() ? "ON" : "OFF");

        } else {

            vh.lblResult.setText("Unsupported Type - " + parameterType);

        }
    }

    @Override
    public int getItemCount() {
        synchronized (mLock) {
            return mSetterMethods.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.lblTitle) TextView lblTitle;
        @InjectView(R.id.sekParameter) SeekBar sekParameter;
        @InjectView(R.id.swhParameter) Switch swhParameter;
        @InjectView(R.id.lblResult) TextView lblResult;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
