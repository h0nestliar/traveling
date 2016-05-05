package com.eure.traveling;


import com.eure.traveling.entity.Shot;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class ShotListAdapter extends RealmBaseAdapter<Shot> {

    public ShotListAdapter(Context context, OrderedRealmCollection<Shot> data, boolean automaticUpdate) {
        super(context, data, automaticUpdate);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView playerName = (TextView) view.findViewById(R.id.player_name);
        TextView likesCount = (TextView) view.findViewById(R.id.likes_count);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        title.setText(getItem(position).getTitle());
        playerName.setText(getItem(position).getDesigner().getName());
        likesCount.setText(Integer.toString(getItem(position).getLikesCount()));
        Picasso.with(context).load(getItem(position).getImage().getTeaser()).into(imageView);

        return view;
    }

    public void add(RealmResults<Shot> shots) {
        if (adapterData == null) {
            updateData(shots);
            return;
        }
        adapterData.addAll(shots);
    }
}
