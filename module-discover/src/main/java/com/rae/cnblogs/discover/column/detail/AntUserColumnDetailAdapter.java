package com.rae.cnblogs.discover.column.detail;

import android.content.Context;
import android.support.annotation.Nullable;

import com.antcode.sdk.model.AntArticleInfo;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rae.cnblogs.discover.R;
import com.rae.cnblogs.discover.RaeBaseQuickAdapter;

import java.util.List;

public class AntUserColumnDetailAdapter extends RaeBaseQuickAdapter<AntArticleInfo, BaseViewHolder> {


    public AntUserColumnDetailAdapter(Context context, @Nullable List<AntArticleInfo> data) {
        super(context, R.layout.item_user_column_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AntArticleInfo item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_summary, item.getSummary());
    }
}