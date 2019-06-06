package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.OnImportClick;
import interfaces.OnSubscriberCompleted;
import model.SearchModel;
import reportgarden.toppackapp.R;

public class ListItemAdapter extends BaseAdapter {
    List<SearchModel.Item> listItems;
    ArrayList<String> importedRepository;
    Activity act;
    OnImportClick callback;
   public ListItemAdapter(List<SearchModel.Item> listItems , Activity act, ArrayList<String> importedRepository)
    {
        this.listItems=listItems;
       callback=(OnImportClick) act;
       this.importedRepository=importedRepository;
        this.act=act;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final  SearchModel.Item model=listItems.get(position);
        ViewHolder holder=null;
        if(convertView==null){

            LayoutInflater inflater=act.getLayoutInflater();
            convertView=inflater.inflate(R.layout.list_item, null,true);
            holder=new ViewHolder(convertView);

        }else{
            holder=(ViewHolder)convertView.getTag();

        }
        holder.name.setText(model.getFullName());
        holder.starts.setText("Stars : "+model.getStargazersCount());
        holder.forks.setText("Forks :"+model.getForksCount());
        holder.import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callback.onClick(model.getFullName());
            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_image);
        requestOptions.error(R.drawable.no_image);
        Glide.with(act).load(model.getOwner().getAvatarUrl()).apply(requestOptions).into(holder.imageView);
if(importedRepository.contains(model.getFullName()))
{
    holder.import_btn.setVisibility(View.INVISIBLE);
}else{
    holder.import_btn.setVisibility(View.VISIBLE);
}
        convertView.setTag(holder);
        return convertView;
    }
    public class ViewHolder{
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.stars)
        TextView starts;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.forks)
        TextView forks;
        @BindView(R.id.import_btn)
        Button import_btn;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
